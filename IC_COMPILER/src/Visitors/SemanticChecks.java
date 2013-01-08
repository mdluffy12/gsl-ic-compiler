/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Visitors;

import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
import IC.AST.CallStatement;
import IC.AST.Continue;
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
import Types.TypeAdapter;

/**
 * SemanticChecks is responsible for all the semantic checks that could not be
 * performed while constructing the table (for efficiency or technical reasons)
 * 
 * @authors Micha,Roni,Grisha (although Grisha is sitting home doing nothing
 *          right now)
 */

public class SemanticChecks implements Visitor {

	private int loopCount = 0;
	
	@Override
	public Object visit(Program program) throws SemanticError {
		// TODO Auto-generated method stub
		for(ICClass icClass : program.getClasses())
			icClass.accept(this);
		return null;
	}

	@Override
	public Object visit(ICClass icClass) throws SemanticError {
		for(Field field : icClass.getFields())
			field.accept(this);
		for(Method method : icClass.getMethods())
			method.accept(this);
		return null;
	}

	@Override
	public Object visit(Field field) throws SemanticError {
		// TODO Auto-generated method stub
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
	
	private void handleMethod(Method method, boolean isVirtual) throws SemanticError {
		//check if method legally overrides superclass method
		ISymbolTable methodSymTable = method.getEnclosingScope();
		if(methodSymTable==null)
			throw new RuntimeException("Unconnected ASTNode to scope");
		
		ISymbolTable classSymTable = methodSymTable.getParentSymbolTable();
		if(classSymTable == null)
			throw new RuntimeException("Method super-scope is null");
		
		ISymbolTable superClassSymTable = classSymTable.getParentSymbolTable();
		if(superClassSymTable!=null && superClassSymTable.isClassTable())
		{
			Symbol superMethodSym = superClassSymTable.lookup(method.getName(), method);
			if(!TypeAdapter.adaptType(method).equals(superMethodSym.getIdType()))
				throw new SemanticError("Incompatible overriding function");
			if((isVirtual && superMethodSym.isStaticMethod()) || (!isVirtual && superMethodSym.isVirtualMethod()))
				throw new SemanticError("Incompatible overriding function"); 
		}
		
		for(Statement stmt : method.getStatements())
			stmt.accept(this);
	}

	@Override
	public Object visit(LibraryMethod method) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Formal formal) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(UserType type) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Assignment assignment) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Return returnStatement) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(If ifStatement) throws SemanticError {
		// TODO Auto-generated method stub
		ifStatement.getOperation().accept(this);
		if(ifStatement.hasElse())
			ifStatement.getElseOperation().accept(this);
		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {
		// TODO Auto-generated method stub
		this.loopCount++;
		
		Statement whileOperation = whileStatement.getOperation();
		whileStatement.getOperation().accept(this);
		
		this.loopCount--;
		return null;
	}

	@Override
	public Object visit(Break breakStatement) throws SemanticError {
		// TODO Auto-generated method stub
		if(this.loopCount<1)
			throw new SemanticError("Break statement must be in loop");
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) throws SemanticError {
		// TODO Auto-generated method stub
		if(this.loopCount<1)
			throw new SemanticError("Continue statement must be in loop");
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VariableLocation location) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ArrayLocation location) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(This thisExpression) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewClass newClass) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Length length) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Literal literal) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

//	protected SymTableUtils symTableUtils;
//
//	protected TypeEvaluator typeEvaluator;
//
//	private ASTNode locationContext = null;
//
//	private final Map<String, ASTNode> initializedVars;
//	private Map<Statement, Map<String, ASTNode>> IfinitVars;
//
//	public SemanticChecks() {
//		this.symTableUtils = new SymTableUtils();
//		this.initializedVars = new HashMap<String, ASTNode>();
//		this.typeEvaluator = new TypeEvaluator(symTableUtils);
//	}
//
//	/**
//	 * handles invoked error while constructing the symbol table
//	 */
//	public void HandleError(String err_msg, ASTNode node) throws SemanticError {
//		if (node != null) {
//			throw new SemanticError(err_msg, node);
//		} else {
//			throw new SemanticError(err_msg);
//		}
//	}
//
//	@Override
//	public Object visit(Program program) throws SemanticError {
//		for (ICClass icClass : program.getClasses()) {
//			icClass.accept(this);
//		}
//
//		return null;
//	}
//
//	@Override
//	public Object visit(ICClass icClass) throws SemanticError {
//
//		// just an example of findSymbolTable usage
//
//		for (Method m : icClass.getMethods()) {
//			m.accept(this);
//		}
//
//		return null;
//	}
//
//	/**
//	 * check if every control path has a return value
//	 */
//	public boolean hasReturnOnControlPaths(Statement stmt) {
//		List<Statement> stmtList = new LinkedList<Statement>();
//		stmtList.add(stmt);
//		return hasReturnOnControlPaths(stmtList);
//	}
//
//	/**
//	 * check if every control path has a return value
//	 */
//
//	public boolean hasReturnOnControlPaths(List<Statement> statements) {
//
//		// initialize return value
//		boolean found = false;
//
//		for (Statement stmt : statements) {
//
//			if (stmt instanceof Return) {
//
//				// in case we found a return statement, set found to true
//				found = true;
//			}
//
//			if (stmt instanceof StatementsBlock) {
//
//				// in case we found a statement block, iterate all statements
//				found |= hasReturnOnControlPaths(((StatementsBlock) stmt)
//						.getStatements());
//			}
//
//			if (stmt instanceof If) {
//
//				/*
//				 * in case we found an if statement, iterate recursively the
//				 * 'then' statement and the 'else' statement
//				 */
//				found |= (hasReturnOnControlPaths(((If) stmt).getOperation())
//
//				&& hasReturnOnControlPaths(((If) stmt).getElseOperation()));
//			}
//		}
//
//		return found;
//
//	}
//
//	private Object HandleMethod(Method method) throws SemanticError {
//
//		if (!method.getName().equals("main")) {
//			boolean found = hasReturnOnControlPaths(method.getStatements());
//			// System.out.println(found);
//		}
//
//		// System.out.println(methodSymbolTable);
//
//		initializedVars.clear();
//		IfinitVars = new HashMap<Statement, Map<String, ASTNode>>();
//
//		for (Statement stmt : method.getStatements()) {
//			stmt.accept(this);
//		}
//
//		return method;
//	}
//
//	@Override
//	public Object visit(If ifStatement) throws SemanticError {
//		ifStatement.getCondition().accept(this);
//		Map<String, ASTNode> initVarsBackup = new HashMap<String, ASTNode>();
//		Map<String, ASTNode> thenInitVars = new HashMap<String, ASTNode>();
//		Map<String, ASTNode> elseInitVars = new HashMap<String, ASTNode>();
//		initVarsBackup.putAll(this.initializedVars);
//
//		ifStatement.getOperation().accept(this);
//
//		for (String varName : initializedVars.keySet()) {
//			if (!initVarsBackup.containsKey(varName)) {
//				thenInitVars.put(varName, initializedVars.get(varName));
//			}
//		}
//
//		IfinitVars.put(ifStatement.getOperation(), thenInitVars);
//
//		// restore all the original initialized variables
//		initializedVars.clear();
//		initializedVars.putAll(initVarsBackup);
//
//		if (ifStatement.hasElse()) {
//
//			ifStatement.getElseOperation().accept(this);
//
//			for (String varName : initializedVars.keySet()) {
//				if (!initVarsBackup.containsKey(varName)) {
//					elseInitVars.put(varName, initializedVars.get(varName));
//				}
//			}
//
//			IfinitVars.put(ifStatement.getElseOperation(), elseInitVars);
//
//			// restore all the original initialized variables
//			initializedVars.clear();
//			initializedVars.putAll(initVarsBackup);
//
//		}
//
//		List<String> newInitList = new ArrayList<String>();
//		for (Map<String, ASTNode> ifInits : IfinitVars.values()) {
//			newInitList.addAll(ifInits.keySet());
//		}
//
//		for (String newInitName : newInitList) {
//			if (getRepetitions(newInitName, newInitList) == IfinitVars.size()) {
//				initializedVars.put(newInitName, getNodeFromInits(newInitName));
//			}
//		}
//
//		return null;
//	}
//
//	private ASTNode getNodeFromInits(String newInitName) {
//
//		for (Map<String, ASTNode> ifInits : IfinitVars.values()) {
//			if (ifInits.containsKey(newInitName)) {
//				return ifInits.get(newInitName);
//			}
//		}
//
//		return null;
//	}
//
//	public int getRepetitions(String s, List<String> vals) {
//
//		int counter = 0;
//		for (String str : vals) {
//			if (s.equals(str)) {
//				counter++;
//			}
//		}
//
//		return counter;
//	}
//
//	public boolean legalInitOfVar(Statement stmt, VariableLocation varLocation) {
//
//		// return false in case statement is declared after variable location
//		if (stmt == null || varLocation == null
//				|| stmt.getLine() > varLocation.getLine()) {
//			return false;
//		}
//
//		// get variable name
//		String varName = varLocation.getName();
//
//		/*
//		 * in case statement is assignment, check if the l-value variable
//		 * location name equals to our variable location name
//		 */
//		if (stmt instanceof Assignment) {
//			Assignment ass = (Assignment) stmt;
//
//			if (ass.getVariable() instanceof VariableLocation
//					&& ((VariableLocation) ass.getVariable()).getName().equals(
//							varName)) {
//				return true;
//			}
//		}
//
//		/*
//		 * in case statement is a LocalVariable, check if the names are equal,
//		 * and the variable has an initial value
//		 */
//		if (stmt instanceof LocalVariable
//				&& ((LocalVariable) stmt).getName().equals(varName)
//				&& ((LocalVariable) stmt).hasInitValue()) {
//			return true;
//		}
//
//		return false;
//
//	}
//
//	public boolean hasInitOnControlPaths(Statement statement,
//			VariableLocation varLocation) {
//		List<Statement> statements = new ArrayList<Statement>();
//		statements.add(statement);
//		return hasInitOnControlPaths(statements, varLocation);
//	}
//
//	public boolean hasInitOnControlPaths(List<Statement> statements,
//			VariableLocation varLocation) {
//
//		// initialize return value
//		boolean initialized = false;
//
//		for (Statement stmt : statements) {
//
//			if (legalInitOfVar(stmt, varLocation)) {
//
//				// in case we found an initialization, set initialized to true
//				initialized = true;
//			}
//
//			if (stmt instanceof StatementsBlock) {
//
//				// in case we found a statement block, iterate all statements
//				initialized |= hasInitOnControlPaths(
//						((StatementsBlock) stmt).getStatements(), varLocation);
//			}
//
//			if (stmt instanceof If) {
//
//				/*
//				 * in case we found an if statement, iterate recursively the
//				 * 'then' statement and the 'else' statement
//				 */
//				initialized |= (hasInitOnControlPaths(
//						((If) stmt).getOperation(), varLocation)
//
//				&& hasInitOnControlPaths(((If) stmt).getElseOperation(),
//						varLocation));
//			}
//
//			if (stmt instanceof While) {
//
//				initialized |= hasInitOnControlPaths(
//						((While) stmt).getOperation(), varLocation);
//			}
//		}
//
//		return initialized;
//
//	}
//
//	@Override
//	public Object visit(While whileStatement) throws SemanticError {
//		whileStatement.getCondition().accept(this);
//
//		Map<String, ASTNode> initVarsBackup = new HashMap<String, ASTNode>();
//		initVarsBackup.putAll(this.initializedVars);
//
//		whileStatement.getOperation().accept(this);
//
//		this.initializedVars.clear();
//		this.initializedVars.putAll(initVarsBackup);
//
//		return null;
//	}
//
//	@Override
//	public Object visit(StatementsBlock statementsBlock) throws SemanticError {
//
//		for (Statement stmt : statementsBlock.getStatements()) {
//			stmt.accept(this);
//		}
//
//		return null;
//	}
//
//	@Override
//	public Object visit(LocalVariable localVariable) throws SemanticError {
//
//		if (localVariable.hasInitValue()) {
//
//			initializedVars.put(localVariable.getName(), localVariable);
//
//			localVariable.getInitValue().accept(this);
//		}
//		return null;
//	}
//
//	@Override
//	public Object visit(VariableLocation location) throws SemanticError {
//
// 
//		// get variable location
//		Expression varLocation = location.getLocation();
//
//		if (varLocation != null) {
//
//			// visit expression location
//			varLocation.accept(this);
//		}
//
//		String variableName = location.getName();
//
//		ASTNode locationContext = getLocationContext();
//
//		SymbolTable varLocationSymbolTable = location.getEnclosingScope();
//
//		Symbol varSymbol = varLocationSymbolTable
//				.lookup(variableName, location);
//
//		if (varSymbol == null) {
//
//			String err_msg = variableName + " cannot be resolved to a variable";
//			HandleError(err_msg, location);
//		}
//
//		boolean isAssigned = isAssignedBeforUse(location);
//		boolean isAssignment = locationContext instanceof Assignment;
//
//		Symbol methodParent = varLocationSymbolTable.getMethodParent();
//
//		if (varSymbol.isField() || varSymbol.isParameter()) {
//			return null;
//		}
//
//		if (!hasInitOnControlPaths(
//				((Method) methodParent.getIdNode()).getStatements(), location)) {
//
//			if (!(isAssignment && ((VariableLocation) ((Assignment) locationContext)
//					.getVariable()).getName().equals(variableName))) {
//				String err_msg = "The local variable " + variableName
//						+ " may not have been initialized";
//				// HandleError(err_msg, location);
//			}
//
//		}
//
//		/*
//		 * 
//		 * if (!isAssigned && !isAssignment) {
//		 * 
//		 * String err_msg = "The local variable " + variableName +
//		 * " may not have been initialized"; HandleError(err_msg, location); }
//		 */
//
//		if (isAssignment) {
//
//			initializedVars.put(location.getName(), location);
//		}
//
//		return null;
//	}
//
//	private boolean isAssignedBeforUse(VariableLocation var) {
//
//		ASTNode node = initializedVars.get(var.getName());
//
//		return (node != null) && (node.getLine() <= var.getLine());
//	}
//
//	@Override
//	public Object visit(Assignment assignment) throws SemanticError {
//		/*
//		 * <<<<<<< .mine
//		 * 
//		 * Location location = assignment.getVariable();
//		 * 
//		 * setLocationContext(assignment);
//		 * 
//		 * Expression expr = assignment.getAssignment();
//		 * 
//		 * expr.accept(this);
//		 * 
//		 * location.accept(this);
//		 * 
//		 * return null; ======= Location loc = assignment.getVariable();
//		 * Expression assgn = assignment.getAssignment();
//		 * 
//		 * Types.Type locType = SymTableUtils.getNodeType(loc); Types.Type
//		 * assgnType = TTTT.visit(assgn); if(!locType.subTypeOf(assgnType))
//		 * return false; else return true; >>>>>>> .r18
//		 */
//
//		Location location = assignment.getVariable();
//
//		Expression expr = assignment.getAssignment();
//
//		Type type = (Type) expr.accept(typeEvaluator);
//		//System.out.println(type.toString());
//
//		location.accept(this);
//
//		expr.accept(this);
//
//		return null;
//	}
//
//	@Override
//	public Object visit(ArrayLocation location) throws SemanticError {
//
//		Expression expr = location.getArray();
//
//		if (expr instanceof VariableLocation) {
//			setLocationContext(location);
//		}
//
//		location.getArray().accept(this);
//
//		location.getIndex().accept(this);
//
//		return null;
//	}
//
//	@Override
//	public Object visit(StaticCall call) throws SemanticError {
//		setLocationContext(call);
//
//		for (Expression expr : call.getArguments()) {
//			expr.accept(this);
//		}
//
//		return null;
//	}
//
//	@Override
//	public Object visit(VirtualCall call) throws SemanticError {
//
//		setLocationContext(call);
//
//		
//		Type type = (Type) call.accept(typeEvaluator);
//		System.out.println(type.toString());
//		
//		
//		for (Expression expr : call.getArguments()) {
// 
//			expr.accept(this);
//
//		}
//
//		if (call.isExternal()) {
//
//			Expression callLocation = call.getLocation();
//			callLocation.accept(this);
//		}
//
//		return null;
//	}
//
//	@Override
//	public Object visit(This thisExpression) throws SemanticError {
//
//		// for Roni
//		String className = symTableUtils.findClassName(thisExpression);
//		System.out.println(className);
//
//		return null;
//	}
//
//	@Override
//	public Object visit(NewClass newClass) throws SemanticError {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object visit(NewArray newArray) throws SemanticError {
//		return newArray.getSize().accept(this);
//	}
//
//	@Override
//	public Object visit(Length length) throws SemanticError {
//		return length.getArray().accept(this);
//	}
//
//	@Override
//	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
//		binaryOp.getFirstOperand().accept(this);
//		binaryOp.getSecondOperand().accept(this);
//		return null;
//	}
//
//	@Override
//	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
//		binaryOp.getFirstOperand().accept(this);
//		binaryOp.getSecondOperand().accept(this);
//		return null;
//	}
//
//	@Override
//	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
//		unaryOp.getOperand().accept(this);
//		return null;
//	}
//
//	@Override
//	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
//		unaryOp.getOperand().accept(this);
//		return null;
//	}
//
//	@Override
//	public Object visit(Literal literal) throws SemanticError {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
//		expressionBlock.getExpression().accept(this);
//		return null;
//	}
//
//	public ASTNode getLocationContext() {
//		return locationContext;
//	}
//
//	public void setLocationContext(ASTNode locationContext) {
//		this.locationContext = locationContext;
//	}
//
//	@Override
//	public Object visit(VirtualMethod method) throws SemanticError {
//
//		return HandleMethod(method);
//	}
//
//	@Override
//	public Object visit(StaticMethod method) throws SemanticError {
//		return HandleMethod(method);
//	}
//
//	@Override
//	public Object visit(LibraryMethod method) throws SemanticError {
//		return HandleMethod(method);
//	}
//
//	@Override
//	public Object visit(Formal formal) throws SemanticError {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object visit(PrimitiveType type) throws SemanticError {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object visit(UserType type) throws SemanticError {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object visit(Break breakStatement) throws SemanticError {
//
//		return null;
//	}
//
//	@Override
//	public Object visit(Continue continueStatement) throws SemanticError {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object visit(Field field) throws SemanticError {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object visit(CallStatement callStatement) throws SemanticError {
//
//		callStatement.getCall().accept(this);
//
//		return null;
//	}
//
//	@Override
//	public Object visit(Return returnStatement) throws SemanticError {
//		returnStatement.getValue().accept(this);
//		return null;
//	}

}