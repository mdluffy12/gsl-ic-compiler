package Visitors;

import IC.BinaryOps;
import IC.UnaryOps;
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
import IC.AST.LogicalBinaryOp;
import IC.AST.LogicalUnaryOp;
import IC.AST.MathBinaryOp;
import IC.AST.MathUnaryOp;
import IC.AST.NewArray;
import IC.AST.NewClass;
import IC.AST.PrimitiveType;
import IC.AST.Program;
import IC.AST.Return;
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
import Types.Type;
import Types.TypeAdapter;
import Types.TypeTable;

public class TypeEvaluator implements Types.ITypeEvaluator, Visitor {

	@Override
	public Object visit(Program program) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(ICClass icClass) throws SemanticError {
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
		return null;
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
		// TODO Auto-generated method stub
		return null;
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
		return TypeTable.classType(newClass.getName());
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		return TypeTable.arrayType(TypeAdapter.adaptType(newArray.getType()));
	}

	@Override
	public Object visit(Length length) throws SemanticError {
		return TypeTable.intType;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		Type firstOperandType = this.evaluateExpressionType(binaryOp.getFirstOperand());
		Type secondOperandType = this.evaluateExpressionType(binaryOp.getSecondOperand());
		BinaryOps op = binaryOp.getOperator();
		
		if(op!=BinaryOps.PLUS && op!=BinaryOps.MINUS && op!=BinaryOps.DIVIDE && op!=BinaryOps.MULTIPLY && op!=BinaryOps.MOD)
			throw new SemanticError("Expected math operation");
		
		if(firstOperandType.equals(TypeTable.stringType) && secondOperandType.equals(TypeTable.stringType))
		{
			if(op==BinaryOps.PLUS)
				return TypeTable.stringType;
			else
				throw new SemanticError("Expected a plus operation between two strings");
		}
		else
		{
			if(firstOperandType != TypeTable.intType || secondOperandType != TypeTable.intType)
				throw new SemanticError("Expected integers in math binary op");
			return TypeTable.intType;
		}
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		Type firstOperandType = this.evaluateExpressionType(binaryOp.getFirstOperand());
		Type secondOperandType = this.evaluateExpressionType(binaryOp.getSecondOperand());
		BinaryOps op = binaryOp.getOperator();
		
		if(op==BinaryOps.PLUS && op==BinaryOps.MINUS && op==BinaryOps.DIVIDE && op==BinaryOps.MULTIPLY && op==BinaryOps.MOD)
			throw new SemanticError("Expected logical operation");
		
		if(op==BinaryOps.EQUAL || op==BinaryOps.NEQUAL)
		{
			if(firstOperandType.subTypeOf(secondOperandType) || secondOperandType.subTypeOf(firstOperandType))
				return TypeTable.boolType;
			else
				throw new SemanticError("Expected inheriting types in equality operation");
		}
		else if(op==BinaryOps.GT || op==BinaryOps.GTE || op==BinaryOps.LT || op==BinaryOps.LTE)
		{
			if(firstOperandType.equals(TypeTable.intType) && secondOperandType.equals(TypeTable.intType))
				return TypeTable.boolType;
			else
				throw new SemanticError("Expected int types in inequality operation");
		}
		else if(op==BinaryOps.LAND || op==BinaryOps.LOR)
		{
			if(firstOperandType.equals(TypeTable.boolType) && secondOperandType.equals(TypeTable.boolType))
				return TypeTable.boolType;
			else
				throw new SemanticError("Expected bool types in and/or operation");
		}
		else
			throw new SemanticError("Unexpected binary operation");
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		Type operandType = this.evaluateExpressionType(unaryOp.getOperand());
		UnaryOps op = unaryOp.getOperator();
		
		if(op!=UnaryOps.UMINUS)
			throw new SemanticError("Unexpected unary op");
		else if(!operandType.equals(TypeTable.intType))
			throw new SemanticError("Expected int type in unary minus op");
		else
			return TypeTable.intType;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		Type operandType = this.evaluateExpressionType(unaryOp.getOperand());
		UnaryOps op = unaryOp.getOperator();
		
		if(op!=UnaryOps.LNEG)
			throw new SemanticError("Unexpected unary op");
		else if(!operandType.equals(TypeTable.boolType))
			throw new SemanticError("Expected bool type in unary negation op");
		else
			return TypeTable.boolType;
	}

	@Override
	public Object visit(Literal literal) throws SemanticError {
		switch(literal.getType())
		{
		case INTEGER:
			return TypeTable.intType;
		case STRING:
			return TypeTable.stringType;
		case TRUE:
			return TypeTable.boolType;
		case FALSE:
			return TypeTable.boolType;
		case NULL:
			return TypeTable.nullType;
		default:
			throw new SemanticError("Unexpected literal type!");
		}
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		return this.evaluateExpressionType(expressionBlock.getExpression());
	}

	@Override
	public Type evaluateExpressionType(Expression exp) throws SemanticError {
		return (Type)exp.accept(this);
	}

}
