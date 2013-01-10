/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Visitors;

import java.io.File;

import IC.AST.ASTNode;
import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.BinaryOp;
import IC.AST.Break;
import IC.AST.Call;
import IC.AST.CallStatement;
import IC.AST.Continue;
import IC.AST.Expression;
import IC.AST.ExpressionBlock;
import IC.AST.Field;
import IC.AST.Formal;
import IC.AST.ICClass;
import IC.AST.If;
import IC.AST.Length;
import IC.AST.LibraryMethod;
import IC.AST.Literal;
import IC.AST.LocalVariable;
import IC.AST.Location;
import IC.AST.LogicalBinaryOp;
import IC.AST.LogicalUnaryOp;
import IC.AST.MathBinaryOp;
import IC.AST.MathUnaryOp;
import IC.AST.Method;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.Return;
import IC.AST.Statement;
import IC.AST.StatementsBlock;
import IC.AST.StaticCall;
import IC.AST.StaticMethod;
import IC.AST.This;
import IC.AST.UnaryOp;
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import IC.Parser.SemanticError;
import SymbolTable.ClassSymbol;
import SymbolTable.Symbol;
import SymbolTable.Symbol.SymbolKind;
import SymbolTable.SymbolTable;
import SymbolTable.SymbolTable.TableType;

/**
 * SymTableConstructor constructs an hierarchy of symbol tables where the
 * hierarchy root is the global symbol table which consists of all the program
 * classes. In addition the symbol table constructor performs some basic
 * semantic checks in order to maintain the correctness of the symbol table
 * 
 * ----------- semantic checks while constructing -----------
 * 
 * 1) class is defined only once
 * 
 * 2) class extends an already defined class
 * 
 * 3) class does not extends itself
 * 
 * 4) program consists of exactly one main method, and the main method is not
 * defined in the Library class
 * 
 * 5) no field-method name collisions (method overloading is not allowed)
 * 
 * 6) Parameters and variables are defined only once
 * 
 * @author Micha
 */
public class SymTableConstructor implements Visitor {

	/*
	 * -------------------------------------------------------------------
	 * --------------------------- data members --------------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * IC file path
	 */
	private String file_name;

	/**
	 * counts the number of legal main function decelerations
	 */
	private int mainCounter;

	/**
	 * holds kind of method to update in class table (static, virtual or
	 * library). This is just for inner purposes.
	 */
	private SymbolKind methodKind;

	/*
	 * -------------------------------------------------------------------
	 * --------------------------- Constructors --------------------------
	 * -------------------------------------------------------------------
	 */

	public SymTableConstructor(String file_name) {
		File file = new File(file_name);
		this.setFile_name(file.getName());
		SymTableUtils.initUtils();
	}

	/*
	 * -------------------------------------------------------------------
	 * ------------------------ member functions -------------------------
	 * -------------------------------------------------------------------
	 */

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	private SymbolKind getMethodKind() {
		return methodKind;
	}

	private void setMethodKind(SymbolKind methodKind) {
		this.methodKind = methodKind;
	}

	/*
	 * -------------------------------------------------------------------
	 * --------------------- error handling functions --------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * handles invoked error while constructing the symbol table
	 */
	public void HandleError(String err_msg, ASTNode node) throws SemanticError {
		if (node != null) {
			throw new SemanticError(err_msg, node);
		} else {
			throw new SemanticError(err_msg);
		}
	}

	/*
	 * -------------------------------------------------------------------
	 * ------------------------ visitor functions ------------------------
	 * -------------------------------------------------------------------
	 */

	/**
	 * Build global symbol table
	 */
	@Override
	public Object visit(Program program) throws SemanticError {

		/*
		 * TODO remove when done
		 * 
		 * 1) A program must have exactly ONE method main (this can be done
		 * after iterating ALL program classes)
		 * 
		 * 2) Classes can only extend previously defined classes. In other
		 * words, a class CANNOT extend itself or another class defined LATER in
		 * the program.
		 * 
		 * 3) Class names, fields, and methods can be used before the program
		 * declares them. However, the program must eventually declare them.
		 * 
		 * 4) if A extends B, then B symbol table should be parent of A symbol
		 * table? in another words, do we need to support field/method
		 * inheritance
		 */

		Symbol super_class_symbol = null;
		ClassSymbol classSymbol = null;

		// create global symbol table
		SymbolTable global_table = new SymbolTable(file_name, TableType._global);

		// iterate all classes and add them to global symbol table
		for (ICClass icClass : program.getClasses()) {

			// resolve class type
			Types.Type classType = SymTableUtils.getNodeType(icClass);
			
			// check if class is defined twice
			if (SymTableUtils.isClassDefined(icClass, global_table)) {

				String err_msg = "class " + icClass.getName()
						+ " is already defined";
				HandleError(err_msg, icClass);

			}

			// visit class and get class symbol table
			SymbolTable classSymTable = (SymbolTable) icClass.accept(this);

			if (icClass.hasSuperClass()) {
				// in case super class exists

				// get name of ic_class's super class if exists
				String super_class_name = icClass.getSuperClassName();

				// check if class extends itself
				if (icClass.getName().equals(super_class_name)) {

					String err_msg = "cycle detected: the type "
							+ icClass.getName() + " cannot extend itself";
					HandleError(err_msg, icClass);
				}

				// lookup super class in global table
				super_class_symbol = global_table.localLookup(super_class_name);

				if (super_class_symbol == null) {
					// throw error if super class was not found in global table
					// because it cannot be defined later in the program

					String err_msg = "super class " + super_class_name
							+ " is undefined";
					HandleError(err_msg, icClass);
				}

			}

			classSymbol = new ClassSymbol(icClass.getName(), SymbolKind._class,
					classType, icClass, classSymTable);

			// add class symbol to global table
			global_table.addSymbol(classSymbol);

			// set class scope as the global table
			icClass.setEnclosingScope(global_table);

			if (icClass.hasSuperClass()) {

				// set class symbol table parent to be the class parent symbol
				// table in case it inherits a class
				classSymTable
				.setParentSymbolTable(((ClassSymbol) super_class_symbol)
						.getClassTable());

			} else {
				// set class symbol table parent to be the global parent
				classSymTable.setParentSymbolTable(global_table);

			}

		}

		// check if has main method
		if (mainCounter == 0) {
			String err_msg = "a program must have exactly one method main";
			HandleError(err_msg, null);
		}

		// set static root for symbol table
		SymbolTable.setRoot(global_table);

		return global_table;
	}

	/**
	 * Build class symbol table
	 */
	@Override
	public Object visit(ICClass icClass) throws SemanticError {

		/*
		 * TODO remove when done
		 * 
		 * 1) class cannot extend itself (check here or in types??) or another
		 * class defined later in the program
		 * 
		 * 2) class cannot have multiple methods with the same name, EVEN if the
		 * methods have different number of types of arguments, or different
		 * return types (NO method overloading allowed!)
		 * 
		 * 3) Hidden fields are not permitted.
		 * 
		 * 4) All of the newly defined fields in a subclass must have different
		 * names than those in the super classes.
		 * 
		 * 5) methods CAN be overridden in subclasses
		 */

		int mainCounterDiff = 0;

		// create class symbol table
		SymbolTable class_symbol_table = new SymbolTable(icClass.getName(),
				TableType._class);

		// check duplicate fields, duplicate methods, or duplicate combination
		// between any field and method
		SymTableUtils.CheckFieldMethodCollisions(icClass);

		// iterate fields
		for (Field field : icClass.getFields()) {

			// set field scope as it's class scope
			field.setEnclosingScope(class_symbol_table);

			// visit field
			field.accept(this);

			// resolve field type
			Types.Type fieldType = SymTableUtils.getNodeType(field.getType());

			// add field to class table
			class_symbol_table.addSymbol(new Symbol(field.getName(),
					SymbolKind._field, fieldType, field));

		}

		// iterate methods and add them to class symbol table
		for (Method method : icClass.getMethods()) {

			// resolve method type
			Types.Type methodType = SymTableUtils.getNodeType(method);
			
			// set method scope as its class's scope
			method.setEnclosingScope(class_symbol_table);

			// store current main counter
			mainCounterDiff = mainCounter;

			// get method symbol table
			SymbolTable method_symbol_table = (SymbolTable) method.accept(this);

			// check if at least one main method is defined in library class
			if (icClass.isLibrary() && mainCounterDiff < mainCounter) {
				String err_msg = "a program must not have any method main in Library class";
				HandleError(err_msg, icClass);
			}

			// add class symbol table as parent for current method symbol table
			method_symbol_table.setParentSymbolTable(class_symbol_table);
			
			
			// add method symbol to class symbol table
			class_symbol_table.addSymbol(new Symbol(method.getName(),
					getMethodKind(), methodType, method));

		}

		return class_symbol_table;
	}

	/**
	 * returns method symbol table
	 */
	private SymbolTable HandleMethod(Method method) throws SemanticError {

		// create method symbol table
		SymbolTable method_symbol_table = new SymbolTable(method.getName(),
				TableType._method);

		// set method, TODO check if needed
		method.setEnclosingScope(method_symbol_table);

		// check single main
		if (SymTableUtils.isMainMethod(method, getMethodKind())) {
			mainCounter++;
			if (mainCounter >= 2) {
				String err_msg = "a program must have exactly one method main";
				HandleError(err_msg, method);
			}
		}

		// iterate all method parameters
		for (Formal param : method.getFormals()) {

			// scan for parameter in local scope
			if (SymTableUtils.isParameterDefinedInScope(param,
					method_symbol_table)) {
				String err_msg = "duplicated parameter " + param.getName()
						+ " in method "
						+ method.getName();
				HandleError(err_msg, method);
			}

			// resolve parameter type
			Types.Type paramType = SymTableUtils.getNodeType(param.getType());

			// add parameter to method symbol table
			method_symbol_table.addSymbol(new Symbol(param.getName(),
					SymbolKind._param, paramType, param));

			// set parameter scope as its method's scope
			param.setEnclosingScope(method_symbol_table);

		}

		// iterate all method statements
		for (Statement statement : method.getStatements()) {
			// set statement scope as its method's scope
			statement.setEnclosingScope(method_symbol_table);

			// visit statement
			Object stmtRet = statement.accept(this);

			if (stmtRet != null && ((SymbolTable) stmtRet).isBlockTable()) {
				// in case statement is StatementsBlock add method table as it's
				// parent symbol table
				SymbolTable stmt_block_symbol_table = (SymbolTable) stmtRet;
				stmt_block_symbol_table
				.setParentSymbolTable(method_symbol_table);
			}
		}

		return method_symbol_table;
	}

	@Override
	public Object visit(VirtualMethod virtual_method) throws SemanticError {
		setMethodKind(SymbolKind._virtual_method);
		return HandleMethod(virtual_method);

	}

	@Override
	public Object visit(StaticMethod static_method) throws SemanticError {
		setMethodKind(SymbolKind._static_method);
		return HandleMethod(static_method);
	}

	@Override
	public Object visit(LibraryMethod library_method) throws SemanticError {
		setMethodKind(SymbolKind._library_method);
		return HandleMethod(library_method);
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) throws SemanticError {
 
		// create block id
		String blockID = "statement block";

		// create block symbol table
		SymbolTable block_symbol_table = new SymbolTable(blockID,
				TableType._block);

		// iterate all block statements
		for (Statement statement : statementsBlock.getStatements()) {

			// set statement scope to be block symbol table scope
			statement.setEnclosingScope(block_symbol_table);

			Object stmtSymTable = statement.accept(this);
			if (stmtSymTable != null) {

				// in case of nested blocks, set the nested block parent to be
				// the current block symbol table
				((SymbolTable) stmtSymTable)
				.setParentSymbolTable(block_symbol_table);
			}
		}
		return block_symbol_table;
	}

	@Override
	public Object visit(LocalVariable localVariable) throws SemanticError {

		// get variable scope
		SymbolTable localVarScope = localVariable.getEnclosingScope();

		if (SymTableUtils
				.isVariableDefinedInScope(localVariable, localVarScope)) {
			String err_msg = "duplicated local variable " + localVariable.getName();
			HandleError(err_msg, localVariable);
			return null;
		}

		// resolve parameter type
		Types.Type localVarType = SymTableUtils.getNodeType(localVariable
				.getType());

		// add local variable symbol to variable scope table
		localVarScope.addSymbol(new Symbol(localVariable.getName(),
				SymbolKind._variable, localVarType, localVariable));

		// check if variable has init value (r-value)
		if (localVariable.hasInitValue()) {

			// get r-value
			Expression rValue = localVariable.getInitValue();

			// define r-value enclosing scope the same as the l-value
			rValue.setEnclosingScope(localVariable.getEnclosingScope());

			// visit r-value
			rValue.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(Assignment assignment) throws SemanticError {

		// get assignment l-value
		Location rVal = assignment.getVariable();

		// set l-value scope the same as assignments scope
		rVal.setEnclosingScope(assignment.getEnclosingScope());

		// visit l-value
		rVal.accept(this);

		// set r-value scope the same as assignments scope
		Expression lVal = assignment.getAssignment();

		// set r-value scope the same as assignments scope
		lVal.setEnclosingScope(assignment.getEnclosingScope());

		// visit r-value
		lVal.accept(this);

		return null;

	}

	@Override
	public Object visit(CallStatement callStatement) throws SemanticError {

		// get call from call statement
		Call call = callStatement.getCall();

		if (call == null) {
			return null;
		}

		// set calls scope the same callStatement scope
		call.setEnclosingScope(callStatement.getEnclosingScope());

		// visit call
		call.accept(this);

		return null;
	}

	@Override
	public Object visit(Return returnStatement) throws SemanticError {

		// check if return statement has value (not just return; statement)
		if (returnStatement.hasValue()) {

			// get returned expression value
			Expression returnedExpr = returnStatement.getValue();

			// set returned expressions scope the same as returnStatement's
			// scope
			returnedExpr.setEnclosingScope(returnStatement.getEnclosingScope());

			// visit returned expression
			return returnedExpr.accept(this);
		}

		return null;
	}

	@Override
	public Object visit(VariableLocation location) throws SemanticError {

		// get variable location
		Expression varLocation = location.getLocation();

		if (varLocation != null) {

			// set variable expression locations scope same as locations
			varLocation.setEnclosingScope(location.getEnclosingScope());

			// visit expression location
			varLocation.accept(this);
		}

		return null;

	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {

		// get call scope
		SymbolTable callScope = call.getEnclosingScope();

		// add all call arguments to call scope
		for (Expression expr : call.getArguments()) {

			// set arguments scope the same as calls
			expr.setEnclosingScope(callScope);

			// visit argument
			expr.accept(this);
		}

		return null;
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {

		// get location from call
		Expression location = call.getLocation();

		// get call scope
		SymbolTable callScope = call.getEnclosingScope();

		if (location != null) {

			// set locations scope the same as calls
			location.setEnclosingScope(callScope);

			// visit location
			location.accept(this);
		}

		// add all call arguments to call scope
		for (Expression expr : call.getArguments()) {

			// set arguments scope the same as calls
			expr.setEnclosingScope(callScope);

			// visit argument
			expr.accept(this);
		}

		return null;
	}

	@Override
	public Object visit(If ifStatement) throws SemanticError {

		// get if scope
		SymbolTable ifScope = ifStatement.getEnclosingScope();

		// handle if condition
		Expression ifCondition = ifStatement.getCondition();
		ifCondition.setEnclosingScope(ifScope);
		ifCondition.accept(this);

		// handle if operation
		Statement ifOperation = ifStatement.getOperation();
		ifOperation.setEnclosingScope(ifScope);
		Object ifOpVal = ifOperation.accept(this);

		// only in case if operation is a block statement
		if (ifOpVal != null && ((SymbolTable) ifOpVal).isBlockTable()) {
			SymbolTable ifBlockSymbolTable = (SymbolTable) ifOpVal;
			ifBlockSymbolTable.setParentSymbolTable(ifScope);

		}

		// handle else statement (if exists)
		if (ifStatement.hasElse()) {

			// handle else operation
			Statement elseOperation = ifStatement.getElseOperation();
			elseOperation.setEnclosingScope(ifScope);
			Object elseOpVal = elseOperation.accept(this);

			// only in case else operation is a block statement
			if (elseOpVal != null && ((SymbolTable) elseOpVal).isBlockTable()) {
				SymbolTable elseBlockSymbolTable = (SymbolTable) elseOpVal;
				elseBlockSymbolTable.setParentSymbolTable(ifScope);

			}

		}

		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {

		// get while scope
		SymbolTable whileScope = whileStatement.getEnclosingScope();

		// handle while condition
		Expression whileCondition = whileStatement.getCondition();
		whileCondition.setEnclosingScope(whileScope);
		whileCondition.accept(this);

		// handle while operation
		Statement whileOperation = whileStatement.getOperation();
		whileOperation.setEnclosingScope(whileScope);
		Object whileOpVal = whileOperation.accept(this);

		// only in case while operation is a block statement
		if (whileOpVal != null && ((SymbolTable) whileOpVal).isBlockTable()) {
			SymbolTable whileBlockSymbolTable = (SymbolTable) whileOpVal;
			whileBlockSymbolTable.setParentSymbolTable(whileScope);

			// indicates that this block symbol tables is in a loop
			whileBlockSymbolTable.setLoop();
		}
		return null;
	}

	@Override
	public Object visit(ArrayLocation arrLocation) throws SemanticError {

		// handle array expression (left side)
		Expression arrayExpression = arrLocation.getArray();
		arrayExpression.setEnclosingScope(arrLocation.getEnclosingScope());
		arrayExpression.accept(this);

		// handle array index (value in brackets)
		Expression arrayIndex = arrLocation.getIndex();
		arrayIndex.setEnclosingScope(arrLocation.getEnclosingScope());
		arrayIndex.accept(this);

		return null;
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {

		// get array size
		Expression exp = newArray.getSize();

		// set expression scope
		exp.setEnclosingScope(newArray.getEnclosingScope());

		// visit expression
		exp.accept(this);

		return null;

	}

	@Override
	public Object visit(Length length) throws SemanticError {

		// get arr from "arr.length"
		Expression exp = length.getArray();

		// set expression scope
		exp.setEnclosingScope(length.getEnclosingScope());

		// visit expression
		exp.accept(this);

		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		
		Expression expression = expressionBlock.getExpression();
		
		expression.setEnclosingScope(expressionBlock.getEnclosingScope());
      
        return expression.accept(this);
	}
	
	private Object HandleBinaryOp(BinaryOp binaryOp) throws SemanticError {

		// handle first operand
		Expression firstExpr = binaryOp.getFirstOperand();
		firstExpr.setEnclosingScope(binaryOp.getEnclosingScope());
		firstExpr.accept(this);

		// handle second operand
		Expression secondExpr = binaryOp.getSecondOperand();
		secondExpr.setEnclosingScope(binaryOp.getEnclosingScope());
		secondExpr.accept(this);

		return null;

	}

	private Object HandleUnaryOp(UnaryOp unaryOp) throws SemanticError {

		// get operand
		Expression exp = unaryOp.getOperand();
		exp.setEnclosingScope(unaryOp.getEnclosingScope());
		exp.accept(this);

		return null;

	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		return HandleBinaryOp(binaryOp);
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		return HandleBinaryOp(binaryOp);
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		return HandleUnaryOp(unaryOp);
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		return HandleUnaryOp(unaryOp);
	}

	/*
	 * -------------------------------------------------------------------
	 * ------- passive AST nodes (no actions when accept invoked) --------
	 * -------------------------------------------------------------------
	 */

	@Override
	public Object visit(Field field) {
		return null;
	}

	@Override
	public Object visit(This thisExpression) {
		return null;
	}

	@Override
	public Object visit(NewClass newClass) {
		return null;
	}

	@Override
	public Object visit(Formal formal) {
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) {
		return null;
	}

	@Override
	public Object visit(UserType type) {
		return null;
	}

	@Override
	public Object visit(Break breakStatement) {
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) {
		return null;
	}

	@Override
	public Object visit(Literal literal) {
		return null;
	}



}
