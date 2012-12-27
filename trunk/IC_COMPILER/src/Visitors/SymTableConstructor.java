package Visitors;

import IC.AST.ArrayLocation;
import IC.AST.Assignment;
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
import IC.AST.UserType;
import IC.AST.VariableLocation;
import IC.AST.VirtualCall;
import IC.AST.VirtualMethod;
import IC.AST.Visitor;
import IC.AST.While;
import IC.Parser.SemanticError;
import SymbolTable.Symbol;
import SymbolTable.Symbol.SymbolKind;
import SymbolTable.SymbolTable;
import SymbolTable.SymbolTable.TableType;

/**
 * TODO add info
 * 
 * @author micha
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
		this.setFile_name(file_name);
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
		SymbolTable superSymbolTable = null;

		// create global symbol table
		SymbolTable global_table = new SymbolTable(file_name, TableType._global);

		// iterate all classes and add them to global symbol table
		for (ICClass icClass : program.getClasses()) {

			// check if class is defined twice
			if (SymTableUtils.isClassDefined(icClass, global_table)) {
				// TODO throw exception "class already defined"
				System.out.println("The class " + icClass.getName()
						+ " is already defined");
				return null;
			}

			if (icClass.hasSuperClass()) {
				// in case super class exists

				// get name of ic_class's super class if exists
				String super_class_name = icClass.getSuperClassName();

				// check if class extends itself
				if (icClass.getName().equals(super_class_name)) {
					// TODO throw exception "class can not extend itself"
					System.out.println("Cycle detected: the type "
							+ icClass.getName() + " cannot extend itself");
					return null;
				}

				// lookup super class in global table
				super_class_symbol = global_table.localLookup(super_class_name);

				if (super_class_symbol == null) {
					// throw error if super class was not found in global table
					// because it cannot be defined later in the program

					// TODO throw exception ("super class undefined",

					System.out.println("class " + super_class_name
							+ " is undefined");
					return null;
				}

				// get super symbol table in case super class is in global table
				superSymbolTable = super_class_symbol.getSymbolTable();

			} else {
				superSymbolTable = global_table;
			}

			// visit class and get class symbol table
			SymbolTable classSymTable = (SymbolTable) icClass.accept(this);

			icClass.setEnclosingScope(superSymbolTable);

			// add superSymbolTable (or global_table if class does not extend
			// another class) as
			// a parent to the current class table
			classSymTable.setParentSymbolTable(superSymbolTable);

			// add class to global table
			// TODO change symbol type
			global_table.addSymbol(new Symbol(icClass.getName(),
					SymbolKind._class, null, icClass));

		}

		// check if has main method
		if (mainCounter == 0) {
			// TODO throw exception "no main method"
			System.out.println("no main method");
			return null;
		}

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

		// create class symbol table
		SymbolTable class_symbol_table = new SymbolTable(icClass.getName(),
				TableType._class);

		// check duplicate fields, duplicate methods, or duplicate combination
		// between any field and method
		SymTableUtils.CheckFieldMethodCollisions(icClass.getFields(),
				icClass.getMethods());

		// iterate fields
		for (Field field : icClass.getFields()) {

			// set field scope as it's class scope
			field.setEnclosingScope(class_symbol_table);

			// visit field
			field.accept(this);

			// add field to class table
			class_symbol_table.addSymbol(new Symbol(field.getName(),
					SymbolKind._field, field.getType(), field));

		}

		// iterate methods and add them to class symbol table
		for (Method method : icClass.getMethods()) {

			// set method scope as its class's scope
			method.setEnclosingScope(class_symbol_table);

			// get method symbol table
			SymbolTable method_symbol_table = (SymbolTable) method.accept(this);

			// add class symbol table as parent for current method symbol table
			method_symbol_table.setParentSymbolTable(class_symbol_table);

			// add method symbol to class symbol table
			class_symbol_table.addSymbol(new Symbol(method.getName(),
					getMethodKind(), method.getType(), method));

			// add method to class table
			// TODO change symbol type
			class_symbol_table.addSymbol(new Symbol(method.getName(),
					getMethodKind(), null, method));

		}

		return class_symbol_table;
	}

	private SymbolTable HandleMethod(Method method) throws SemanticError {
		// create method table
		SymbolTable methodlTable = new SymbolTable(method.getName(),
				TableType._method);

		// set method, TODO check if needed
		method.setEnclosingScope(methodlTable);

		// check single main
		if (SymTableUtils.isMainMethod(method, getMethodKind())) {
			mainCounter++;
			if (mainCounter >= 2) {
				// TODO throw exception

				System.out.println("can not define more then one main");
				return null;
			}
		}

		// iterate all method parameters
		for (Formal param : method.getFormals()) {

			// scan for parameter in local scope
			if (SymTableUtils.isParameterDefinedInScope(param, methodlTable)) {
				// TODO throw exception

				System.out.println("parameter is already declared");
				return null;
			}

			// TODO handle type
			Symbol param_symbol = new Symbol(param.getName(),
					SymbolKind._param, null, param);

			// add parameter to method symbol table
			methodlTable.addSymbol(param_symbol);

			// set parameter scope as its method's scope
			param.setEnclosingScope(methodlTable);

		}

		// iterate all method statements
		for (Statement stmt : method.getStatements()) {
			// set statement scope as its method's scope
			stmt.setEnclosingScope(methodlTable);

			// visit stmt
			Object stmtRet = stmt.accept(this);

			if (stmtRet != null && stmt instanceof StatementsBlock) {
				// in case stmt is StatementsBlock add method table as its
				// parent
				SymbolTable stmt_block_symbol_table = (SymbolTable) stmtRet;
				stmt_block_symbol_table.setParentSymbolTable(methodlTable);
			}
		}

		return methodlTable;
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

		// TODO : change block table name later
		// build block symbol table
		SymbolTable blockSymbolTable = new SymbolTable("block",
				TableType._block);

		// iterate all block statements
		for (Statement statement : statementsBlock.getStatements()) {

			// set statement scope to be block symbol table scope
			statement.setEnclosingScope(blockSymbolTable);

			Object stmtSymTable = statement.accept(this);
			if (stmtSymTable != null) {

				// in case of nested blocks, set the nested block parent to be
				// the current block symbol table
				((SymbolTable) stmtSymTable)
						.setParentSymbolTable(blockSymbolTable);
			}
		}
		return blockSymbolTable;
	}

	@Override
	public Object visit(LocalVariable localVariable) throws SemanticError {

		SymbolTable localVarScope = localVariable.getEnclosingScope();

		if (SymTableUtils
				.isVariableDefinedInScope(localVariable, localVarScope)) {
			// "Duplicate local variable"
			// TODO throw exception

			System.out.println("variable is already declared");
			return null;
		}

		// TODO handle type
		localVarScope.addSymbol(new Symbol(localVariable.getName(),
				SymbolKind._variable, null, localVariable));

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

			// set returned expressions scopse the same returnStatement scope
			returnedExpr.setEnclosingScope(returnStatement.getEnclosingScope());

			// visit returned expression
			return returnedExpr.accept(this);
		}

		return null;
	}

	/* -------- > continue from here */

	@Override
	public Object visit(If ifStatement) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {
		// TODO Auto-generated method stub
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
	public Object visit(ArrayLocation location) throws SemanticError {
		// TODO Auto-generated method stub
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
	public Object visit(NewArray newArray) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Length length) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		// TODO Auto-generated method stub
		return null;
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
