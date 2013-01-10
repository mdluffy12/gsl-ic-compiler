package Visitors;

import java.util.HashSet;
import java.util.Set;

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
import SymbolTable.Symbol;

public class VarsInExpression implements Visitor {
	
	/**
	 * Created By Micha Sherman,Tzvika Geft and Roni Lichtman 
	 * Compilation course, University of Tel Aviv 2012 ©   
	 * @throws SemanticError 
	 */
	
	public Set<Symbol> findVarsInExpression(Expression e) throws SemanticError
	{
		return (Set<Symbol>)e.accept(this);
	}
	
	@Override
	public Object visit(Program program) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ICClass icClass) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Field field) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualMethod method) throws SemanticError {
		// TODO Auto-generated method stub
		handleMethod(method);
		return null;
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
		// TODO Auto-generated method stub
		handleMethod(method);
		return null;
	}

	private void handleMethod(Method method) {
		// TODO Auto-generated method stub
		
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
		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Break breakStatement) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) throws SemanticError {
		// TODO Auto-generated method stub
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
		Set<Symbol> result = new HashSet<Symbol>();
		Symbol varSym = location.getEnclosingScope().lookup(location.getName(), location);
		if(varSym.isVariable())
			result.add(varSym);
		return result;
	}

	@Override
	public Object visit(ArrayLocation location) throws SemanticError {
		Set<Symbol> result = new HashSet<Symbol>();
		result.addAll(((Set<Symbol>)location.getArray().accept(this)));
		result.addAll(((Set<Symbol>)location.getIndex().accept(this)));
		return result;
	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {
		return handleCall(call);
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		return handleCall(call);
	}
	
	private Object handleCall(Call call) throws SemanticError
	{
		Set<Symbol> result = new HashSet<Symbol>();
		for(Expression e : call.getArguments())
			result.addAll(((Set<Symbol>)e.accept(this)));
		return result;
	}

	@Override
	public Object visit(This thisExpression) throws SemanticError {
		return new HashSet<Symbol>();
	}

	@Override
	public Object visit(NewClass newClass) throws SemanticError {
		return new HashSet<Symbol>();
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		
		return newArray.getSize().accept(this);
	}

	@Override
	public Object visit(Length length) throws SemanticError {
		return new HashSet<Symbol>();
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		return handleBinaryOp(binaryOp);
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		return handleBinaryOp(binaryOp);
	}
	
	private Object handleBinaryOp(BinaryOp binaryOp) throws SemanticError {
		Set<Symbol> result = new HashSet<Symbol>();
		result.addAll(((Set<Symbol>)binaryOp.getFirstOperand().accept(this)));
		result.addAll(((Set<Symbol>)binaryOp.getSecondOperand().accept(this)));
		return result;
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		return handleUnaryOp(unaryOp);
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		return handleUnaryOp(unaryOp);
	}
	
	private Object handleUnaryOp(UnaryOp unaryOp) throws SemanticError {
		return unaryOp.getOperand().accept(this);
	}

	@Override
	public Object visit(Literal literal) throws SemanticError {
		return new HashSet<Symbol>();
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		return expressionBlock.getExpression().accept(this);
	}

	
	
}
