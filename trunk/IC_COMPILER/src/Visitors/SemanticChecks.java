/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Visitors;

import java.util.LinkedList;
import java.util.List;

import IC.LiteralTypes;
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
import SymbolTable.ISymbolTable;
import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import Types.MethodType;
import Types.TypeAdapter;
import Types.TypeTable;

/**
 * SemanticChecks is responsible for all the semantic checks that could not be
 * performed while constructing the table (for efficiency or technical reasons)
 * 
 * @authors Micha,Roni,Grisha
 */

public class SemanticChecks implements Visitor {

	/**
	 * counts the depth of while at runtime
	 */
	private int loopCount = 0;
	private final VarInitChecker varInitChecker;

	public SemanticChecks() {
		varInitChecker = new VarInitChecker();
	}

	@Override
	public Object visit(Program program) throws SemanticError {
		for (ICClass icClass : program.getClasses())
			icClass.accept(this);
		return null;
	}

	@Override
	public Object visit(ICClass icClass) throws SemanticError {
		for (Field field : icClass.getFields()) {
			field.accept(this);
		}
		for (Method method : icClass.getMethods()) {
			loopCount = 0;
			method.accept(this);

			method.accept(varInitChecker);
		}
		return null;
	}

	@Override
	public Object visit(Field field) throws SemanticError {

		SymbolTable fieldClass = field.getEnclosingScope();

		if (fieldClass == null || !fieldClass.isClassTable()) {
			// something is wrong
			return null;
		}

		SymbolTable classSymboTableParent = fieldClass.getParentSymbolTable();

		if (classSymboTableParent != null
				&& classSymboTableParent.isGlobalTable()) {
			// symbol table directly inherits global table
			return null;
		}

		// look for field decelerations with same name in super classes
		Symbol fieldSymbol = classSymboTableParent.lookup(field.getName());

		/*
		 * throw error in case we found a field with the same name in the any
		 * super class in the inheritance hierarchy
		 */
		if (fieldSymbol != null && fieldSymbol.isField()) {
			throw new SemanticError(
					"field "
							+ field.getName()
							+ " appears to be hidden, all of the newly defined fields in a subclass must have different names than those in the superclasses",
					field);
		}

		return null;
	}

	@Override
	public Object visit(VirtualMethod method) throws SemanticError {
		handleMethod(method, true);
		return null;
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
		handleMethod(method, false);
		return null;
	}

	private void handleMethod(Method method, boolean isVirtual)
			throws SemanticError {

		MethodType methodType = (MethodType) TypeAdapter.adaptType(method);

		// check if method legally overrides superclass method
		ISymbolTable methodSymTable = method.getEnclosingScope();

		if (methodSymTable == null)
			throw new RuntimeException("Unconnected ASTNode to scope");

		SymbolTable classSymTable = methodSymTable.getParentSymbolTable();
		if (classSymTable == null)
			throw new RuntimeException("Method super-scope is null");

		ISymbolTable superClassSymTable = classSymTable.getParentSymbolTable();
		if (superClassSymTable != null && superClassSymTable.isClassTable()) {
			Symbol superMethodSym = superClassSymTable.lookup(method.getName(),
					method);

			// in case method name is not found in parent, disregard override
			// check
			if (superMethodSym != null) {

				// check illegal override
				if (!methodType.equals(superMethodSym.getIdType())) {
					throw new SemanticError("method " + method.getName()
							+ " in type " + classSymTable.getId()
							+ " is overridden illegally (type mismatch)",
							method);
				}
				if ((isVirtual && superMethodSym.isStaticMethod())
						|| (!isVirtual && superMethodSym.isVirtualMethod())) {
					throw new SemanticError(
							"method "
									+ method.getName()
									+ " in type "
									+ classSymTable.getId()
									+ " is overridden illegally (virtual/static mismatch)",
							method);
				}
			}
		}

		// iterate all statements
		for (Statement stmt : method.getStatements())
			stmt.accept(this);

		Types.Type methodRetType = methodType.getReturnType();

		// Check return paths
		if (!methodRetType.equals(TypeTable.voidType)
				&& !this.hasReturnOnControlPaths(method.getStatements()))
			throw new SemanticError("The method must return a result of type "
					+ methodType.getReturnType(), method);
	}

	@Override
	public Object visit(If ifStatement) throws SemanticError {
		ifStatement.getOperation().accept(this);
		if (ifStatement.hasElse())
			ifStatement.getElseOperation().accept(this);
		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {

		this.loopCount++;

		Statement whileOperation = whileStatement.getOperation();
		whileStatement.getOperation().accept(this);

		this.loopCount--;

		return null;
	}

	@Override
	public Object visit(Break breakStatement) throws SemanticError {
		if (this.loopCount < 1)
			throw new SemanticError("break cannot be used outside of a loop",
					breakStatement);
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) throws SemanticError {
		if (this.loopCount < 1)
			throw new SemanticError(
					"continue cannot be used outside of a loop",
					continueStatement);
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) throws SemanticError {

		for (Statement stmt : statementsBlock.getStatements()) {
			stmt.accept(this);
		}

		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {

		expressionBlock.getExpression().accept(this);

		return null;
	}

	public boolean hasReturnOnControlPaths(Statement stmt) throws SemanticError {
		List<Statement> stmtList = new LinkedList<Statement>();
		stmtList.add(stmt);
		return hasReturnOnControlPaths(stmtList);
	}

	/**
	 * check if every control path has a return value
	 * 
	 * @throws SemanticError
	 */

	public boolean hasReturnOnControlPaths(List<Statement> statements)
			throws SemanticError {

		// initialize return value
		boolean found = false;

		for (Statement stmt : statements) {

			if (stmt instanceof Return) {

				// in case we found a return statement, set found to true
				found = true;
			}

			if (stmt instanceof StatementsBlock) {

				// in case we found a statement block, iterate all statements
				found |= hasReturnOnControlPaths(((StatementsBlock) stmt)
						.getStatements());
			}

			if (stmt instanceof If) {

				/*
				 * in case we found an if statement, iterate recursively the
				 * 'then' statement and the 'else' statement
				 */
				If stmtAsIf = (If) stmt;

				if (stmtAsIf.getCondition() instanceof Literal) {
					Literal cond = (Literal) stmtAsIf.getCondition();
					if (cond.getType() == LiteralTypes.TRUE)
						found |= hasReturnOnControlPaths(stmtAsIf
								.getOperation());
					else if (cond.getType() == LiteralTypes.FALSE) {
						if (stmtAsIf.hasElse())
							found |= hasReturnOnControlPaths(((If) stmt)
									.getElseOperation());
					} else
						throw new SemanticError(
								"Unexpected literal in if statement", stmt);
				}

				found |= (hasReturnOnControlPaths(stmtAsIf.getOperation())

				&& (stmtAsIf.hasElse() ? hasReturnOnControlPaths(stmtAsIf
						.getElseOperation()) : false));
			}
		}

		return found;

	}

	@Override
	public Object visit(VariableLocation varLocation) throws SemanticError {

		SymbolTable varScope = varLocation.getEnclosingScope();
		
		if(varLocation.isExternal()){
			// checking only external variable location here
			return null;
		}
		 
		if (varScope == null) {
			// should'nt happen
			return null;
		}

		Symbol varSymbol = varScope.lookup(varLocation.getName(), varLocation);

		if (varSymbol == null) {
			// already checked, should'nt happen
			return null;
		}

		Symbol methodSymbol = varScope.getMethodParent();

		// if variable location is defined within a static method
		if (!methodSymbol.isVirtualMethod()) {

			/*
			 * check if variable location is referring to a field, and since all
			 * fields are non-static, than we need to throw exception
			 */

			if (varSymbol.isField()) {
				throw new SemanticError(
						"cannot make a static reference to the non-static field "
								+ varSymbol.getIdName(), varLocation);
			}

		}

		return null;
	}
	
	@Override
	public Object visit(LocalVariable localVariable) throws SemanticError {
		if(localVariable.hasInitValue()){
		  localVariable.getInitValue().accept(this);
		}
		return null;
	}

	private Object handleCall(Call call) throws SemanticError {
		for(Expression arg: call.getArguments()){
			arg.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {
		return handleCall(call);
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		return handleCall(call);
	}

	
	@Override
	public Object visit(ArrayLocation location) throws SemanticError {
		location.getArray().accept(this);
		location.getIndex().accept(this);
		return null;
	}
	
	@Override
	public Object visit(Length length) throws SemanticError {
		length.getArray().accept(this);
		return null;
	}
	
	@Override
	public Object visit(Assignment assignment) throws SemanticError {
		assignment.getAssignment().accept(this);
		assignment.getVariable().accept(this);
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement) throws SemanticError {
		callStatement.getCall().accept(this);
		return null;
	}

	@Override
	public Object visit(Return returnStatement) throws SemanticError {
		if(returnStatement.hasValue()){
			returnStatement.getValue().accept(this);
		}
		return null;
	}
	
	
	/*
	 * -------------------------------------------------------------------
	 * ------- passive AST nodes (no actions when accept invoked) --------
	 * -------------------------------------------------------------------
	 */

	@Override
	public Object visit(This thisExpression) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(NewClass newClass) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		binaryOp.getFirstOperand().accept(this);
		binaryOp.getSecondOperand().accept(this);
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		binaryOp.getFirstOperand().accept(this);
		binaryOp.getSecondOperand().accept(this);
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		unaryOp.getOperand().accept(this);
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		unaryOp.getOperand().accept(this);
		return null;
	}

	@Override
	public Object visit(Literal literal) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(LibraryMethod method) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(Formal formal) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(UserType type) throws SemanticError {
		return null;
	}

}