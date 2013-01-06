package Visitors;

import java.util.List;

import IC.BinaryOps;
import IC.UnaryOps;
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
import SymbolTable.ISymbolTable;
import SymbolTable.ISymbolTableOperations;
import Types.ArrayType;
import Types.ClassType;
import Types.IntType;
import Types.MethodType;
import Types.Type;
import Types.TypeAdapter;
import Types.TypeTable;

/**
 * TypeEvaluator used to evaluate expression types and performs some partial
 * type checking which are relevant to the evaluation
 * 
 * @author Roni the KING
 */
public class TypeEvaluator implements Types.ITypeEvaluator, Visitor {

	private final ISymbolTableOperations symTableOps;

	public TypeEvaluator(ISymbolTableOperations symTableOps) {
		this.symTableOps = symTableOps;
	}

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
		return null;
	}

	@Override
	public Object visit(VirtualMethod method) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
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

	@Override
	public Object visit(Assignment assignment) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(Return returnStatement) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(If ifStatement) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(While whileStatement) throws SemanticError {
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
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(VariableLocation location) throws SemanticError {
		ISymbolTable environment = null;

		if (location.getLocation() == null)
			environment = symTableOps.findSymbolTable(location);
		else {
			Type locationType = this.evaluateExpressionType(location
					.getLocation());
			if (!(locationType instanceof ClassType))
				throw new SemanticError(
						"Expected class type before virtual call");

			environment = symTableOps.findClassEnvironment(locationType
					.getName());
			// TODO for Roni : check if environment != null
		}

		return environment.lookup(location.getName());
	}

	@Override
	public Object visit(ArrayLocation location) throws SemanticError {
		Type arrayType = this.evaluateExpressionType(location.getArray());
		Type indexType = this.evaluateExpressionType(location.getIndex());

		if (!(arrayType instanceof ArrayType))
			throw new SemanticError("Expected an array type");
		if (!(indexType instanceof IntType))
			throw new SemanticError("Expected an integer for index");

		return ((ArrayType) arrayType).getElemType();
	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {
		ISymbolTable classEnvironment = symTableOps.findClassEnvironment(call
				.getClassName());
		// TODO for Roni : check if classEnvironment != null
		return handleCallVisit(classEnvironment, call);
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		ISymbolTable environment = null;

		if (call.getLocation() == null)
			environment = symTableOps.findSymbolTable(call);
		else {
			Type locationType = this.evaluateExpressionType(call.getLocation());
			if (!(locationType instanceof ClassType))
				throw new SemanticError(
						"Expected class type before virtual call");

			environment = symTableOps.findClassEnvironment(locationType
					.getName());
			// TODO for Roni : check if environment != null
		}

		return handleCallVisit(environment, call);
	}

	private Object handleCallVisit(ISymbolTable environment, Call call)
			throws SemanticError {
		Type type = environment.lookup(call.getName()).getIdType();

		if (!(type instanceof MethodType))
			throw new SemanticError("Expected method type");

		MethodType methodType = (MethodType) type;
		Type[] paramTypes = methodType.getParamTypes();
		List<Expression> methodArgs = call.getArguments();

		if (paramTypes.length != methodArgs.size())
			throw new SemanticError(
					"Unexpected number of arguments in static call");

		for (int i = 0; i < paramTypes.length; i++)
			if (!paramTypes[i].equals(this.evaluateExpressionType(methodArgs
					.get(i))))
				throw new SemanticError(
						"Unexpected argument type in static call. Arg number "
								+ i);

		return methodType.getReturnType();
	}

	@Override
	public Object visit(This thisExpression) throws SemanticError {
		String thisClassName = symTableOps.findClassName(thisExpression);
		return TypeTable.classType(thisClassName);
	}

	@Override
	public Object visit(NewClass newClass) throws SemanticError {
		return TypeTable.classType(newClass.getName());
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		if (!(this.evaluateExpressionType(newArray.getSize()) instanceof IntType))
			throw new SemanticError("Expected integer for array size");
		return TypeTable.arrayType(TypeAdapter.adaptType(newArray.getType()));
	}

	@Override
	public Object visit(Length length) throws SemanticError {
		return TypeTable.intType;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		Type firstOperandType = this.evaluateExpressionType(binaryOp
				.getFirstOperand());
		Type secondOperandType = this.evaluateExpressionType(binaryOp
				.getSecondOperand());
		BinaryOps op = binaryOp.getOperator();

		if (op != BinaryOps.PLUS && op != BinaryOps.MINUS
				&& op != BinaryOps.DIVIDE && op != BinaryOps.MULTIPLY
				&& op != BinaryOps.MOD)
			throw new SemanticError("Expected math operation");

		if (firstOperandType.equals(TypeTable.stringType)
				&& secondOperandType.equals(TypeTable.stringType)) {
			if (op == BinaryOps.PLUS)
				return TypeTable.stringType;
			else
				throw new SemanticError(
						"Expected a plus operation between two strings");
		} else {
			if (firstOperandType != TypeTable.intType
					|| secondOperandType != TypeTable.intType)
				throw new SemanticError("Expected integers in math binary op");
			return TypeTable.intType;
		}
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		Type firstOperandType = this.evaluateExpressionType(binaryOp
				.getFirstOperand());
		Type secondOperandType = this.evaluateExpressionType(binaryOp
				.getSecondOperand());
		BinaryOps op = binaryOp.getOperator();

		if (op == BinaryOps.PLUS && op == BinaryOps.MINUS
				&& op == BinaryOps.DIVIDE && op == BinaryOps.MULTIPLY
				&& op == BinaryOps.MOD)
			throw new SemanticError("Expected logical operation");

		if (op == BinaryOps.EQUAL || op == BinaryOps.NEQUAL) {
			if (firstOperandType.subTypeOf(secondOperandType)
					|| secondOperandType.subTypeOf(firstOperandType))
				return TypeTable.boolType;
			else
				throw new SemanticError(
						"Expected inheriting types in equality operation");
		} else if (op == BinaryOps.GT || op == BinaryOps.GTE
				|| op == BinaryOps.LT || op == BinaryOps.LTE) {
			if (firstOperandType.equals(TypeTable.intType)
					&& secondOperandType.equals(TypeTable.intType))
				return TypeTable.boolType;
			else
				throw new SemanticError(
						"Expected int types in inequality operation");
		} else if (op == BinaryOps.LAND || op == BinaryOps.LOR) {
			if (firstOperandType.equals(TypeTable.boolType)
					&& secondOperandType.equals(TypeTable.boolType))
				return TypeTable.boolType;
			else
				throw new SemanticError(
						"Expected bool types in and/or operation");
		} else
			throw new SemanticError("Unexpected binary operation");
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		Type operandType = this.evaluateExpressionType(unaryOp.getOperand());
		UnaryOps op = unaryOp.getOperator();

		if (op != UnaryOps.UMINUS)
			throw new SemanticError("Unexpected unary op");
		else if (!operandType.equals(TypeTable.intType))
			throw new SemanticError("Expected int type in unary minus op");
		else
			return TypeTable.intType;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		Type operandType = this.evaluateExpressionType(unaryOp.getOperand());
		UnaryOps op = unaryOp.getOperator();

		if (op != UnaryOps.LNEG)
			throw new SemanticError("Unexpected unary op");
		else if (!operandType.equals(TypeTable.boolType))
			throw new SemanticError("Expected bool type in unary negation op");
		else
			return TypeTable.boolType;
	}

	@Override
	public Object visit(Literal literal) throws SemanticError {
		switch (literal.getType()) {
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
		return (Type) exp.accept(this);
	}

}
