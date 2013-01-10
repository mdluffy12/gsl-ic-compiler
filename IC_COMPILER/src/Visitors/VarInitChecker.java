package Visitors;

import java.util.HashSet;
import java.util.Set;

import IC.AST.ArrayLocation;
import IC.AST.Assignment;
import IC.AST.Break;
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

public class VarInitChecker implements Visitor {

	private Set<Symbol> initedVars = new HashSet<Symbol>();
	private final VarsInExpression varsInExp = new VarsInExpression();
 
	@Override
	public Object visit(Program program) throws SemanticError {
		// No need for implement
		return null;
	}

	@Override
	public Object visit(ICClass icClass) throws SemanticError {
		// No need for implement
		return null;
	}

	@Override
	public Object visit(Field field) throws SemanticError {
		// No need for implement
		return null;
	}

	@Override
	public Object visit(VirtualMethod method) throws SemanticError {
		handleMethod(method);
		return null;
	}

	private void handleMethod(Method method) throws SemanticError {
		
		initedVars = new HashSet<Symbol>();
		for(Statement stmt : method.getStatements()){
			stmt.accept(this);
		}
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
		handleMethod(method);
		return null;
	}

	@Override
	public Object visit(LibraryMethod method) throws SemanticError {
		// No need for implement
		return null;
	}

	@Override
	public Object visit(Formal formal) throws SemanticError {
		// No need for implement
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) throws SemanticError {
		// No need for implement
		return null;
	}

	@Override
	public Object visit(UserType type) throws SemanticError {
		// No need for implement
		return null;
	}

	@Override
	public Object visit(Assignment assignment) throws SemanticError {
		
		checkIfExpVarsInited(assignment.getAssignment());
		
		Location varLocation  = assignment.getVariable();
		
		if(varLocation instanceof VariableLocation)
		{
			Symbol varSym = varLocation.getEnclosingScope().lookup(((VariableLocation)varLocation).getName(), varLocation);
			if(varSym.isVariable())
				this.initedVars.add(varSym);
		}
		
		else if(varLocation instanceof ArrayLocation)
		{
			checkIfExpVarsInited(varLocation);
		}
		return null;
	}
	
	private void checkIfExpVarsInited(Expression e) throws SemanticError
	{
		Set<Symbol> vars = (Set<Symbol>)e.accept(this.varsInExp);
		for(Symbol var : vars)
			if(!this.initedVars.contains(var))
				throw new SemanticError("the local variable " + var.getIdName() + " may not have been initialized", e);
	}

	@Override
	public Object visit(CallStatement callStatement) throws SemanticError {
		checkIfExpVarsInited(callStatement.getCall());
		return null;
	}

	@Override
	public Object visit(Return returnStatement) throws SemanticError {
		if(returnStatement.hasValue())
			checkIfExpVarsInited(returnStatement.getValue());
		return null;
	}

	@Override
	public Object visit(If ifStatement) throws SemanticError {
		
		checkIfExpVarsInited(ifStatement.getCondition());
		
		Set<Symbol> beforeIfInited = new HashSet<Symbol>();
		
		beforeIfInited.addAll(this.initedVars);
		
		ifStatement.getOperation().accept(this);
		
		
		if(ifStatement.hasElse())
		{
			Set<Symbol> beforeElseInited = new HashSet<Symbol>();
			beforeElseInited.addAll(this.initedVars);
			
			this.initedVars = beforeIfInited;
			
			ifStatement.getElseOperation().accept(this);
			
			Set<Symbol> afterElseInited = new HashSet<Symbol>();
			
			for(Symbol sym : beforeElseInited)
				if(this.initedVars.contains(sym))
					afterElseInited.add(sym);
			
			this.initedVars = afterElseInited;
		}
		
		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {
		checkIfExpVarsInited(whileStatement.getCondition());
		
		Set<Symbol> initedVarsBeforeWhile = new HashSet<Symbol>();
		initedVarsBeforeWhile.addAll(this.initedVars);
		whileStatement.getOperation().accept(this);
		
		this.initedVars = initedVarsBeforeWhile;
		return null;
	}

	@Override
	public Object visit(Break breakStatement) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) throws SemanticError {
		for(Statement statement : statementsBlock.getStatements())
			statement.accept(this);
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable) throws SemanticError {
		if(localVariable.hasInitValue())
		{
			checkIfExpVarsInited(localVariable.getInitValue());
			this.initedVars.add(localVariable.getEnclosingScope().lookup(localVariable.getName(), localVariable));
		}
		return null;
	}

	@Override
	public Object visit(VariableLocation location) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(ArrayLocation location) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(This thisExpression) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(NewClass newClass) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(Length length) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(Literal literal) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		// NO NEED TO IMPLEMENT SINCE EXPRESSIONS ARE HANDLED IN "checkIfExpVarsInited"!!!!!
		return null;
	}

	
	
}
