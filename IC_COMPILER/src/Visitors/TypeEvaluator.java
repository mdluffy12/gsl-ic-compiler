/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

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
import SymbolTable.Symbol;
import SymbolTable.SymbolTable;
import Types.ArrayType;
import Types.ClassType;
import Types.IntType;
import Types.MethodType;
import Types.Type;
import Types.TypeAdapter;
import Types.TypeTable;
import Types.UndefinedClassException;

/**
 * TypeEvaluator used to evaluate expression types and performs some partial
 * type checking which are relevant to the evaluation
 * 
 * @author Roni
 */
public class TypeEvaluator implements Types.ITypeEvaluator, Visitor {

	private final ISymbolTableOperations symTableOps;

	public TypeEvaluator(ISymbolTableOperations symTableOps) {
		this.symTableOps = symTableOps;
	}

	@Override
	public Object visit(VariableLocation location) throws SemanticError {
		ISymbolTable environment = null;

		if (location.getLocation() == null)
			environment = symTableOps.findSymbolTable(location);
		else {
			Type locationType = this.evaluateAndCheckExpressionType(location
					.getLocation());
			if (!(locationType instanceof ClassType)) {
				throw new SemanticError(
						"cannot invoke virtual call on a non-class type "
								+ location.getName(), location);
			}
			environment = symTableOps.findClassEnvironment(locationType
					.getName());
		}

		if (environment == null) {
			throw new SemanticError("Undefined environment", location);
		}
		Symbol resSym = environment.lookup(location.getName(), location);

		if (resSym == null) {
			throw new SemanticError(location.getName() + " cannot be resolved",
					location);
		}

		return resSym.getIdType();
	}

	@Override
	public Object visit(ArrayLocation location) throws SemanticError {
		Type arrayType = this.evaluateAndCheckExpressionType(location
				.getArray());
		Type indexType = this.evaluateAndCheckExpressionType(location
				.getIndex());

		if (!(arrayType instanceof ArrayType)) {
			throw new SemanticError(
					"the type of the expression must be an array type but it resolved to "
							+ arrayType.toString(), location);
		}
		if (!(indexType instanceof IntType)) {
			throw new SemanticError(
					"the type of the array index must be an int but it is resolved to "
							+ indexType.toString(), location);
		}
		return ((ArrayType) arrayType).getElemType();
	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {

		String callClassName = call.getClassName();
		SymbolTable classEnvironment = (SymbolTable) symTableOps
				.findClassEnvironment(callClassName);

		if (classEnvironment == null) {
			throw new SemanticError(callClassName
					+ " cannot be resolved as class", call);
		}
		return handleCallVisit(classEnvironment, call, false);
	}

	@Override
	public Object visit(VirtualCall call) throws SemanticError {
		SymbolTable environment = null;

		Expression callLocation = call.getLocation();
		if (callLocation == null)
			environment = (SymbolTable) symTableOps.findSymbolTable(call);
		else {
			Type locationType = this
					.evaluateAndCheckExpressionType(callLocation);
			if (!(locationType instanceof ClassType)) {
				throw new SemanticError("cannot invoke virtual method "
						+ call.getName() + " on type "
						+ locationType.toString(), call);
			}

			environment = (SymbolTable) symTableOps
					.findClassEnvironment(locationType.getName());

			if (environment == null)
				throw new SemanticError(
						"Unknown in environment : in virtualcall");
		}

		return handleCallVisit(environment, call, true);
	}

	private Object handleCallVisit(SymbolTable environment, Call call,
			boolean isVirtualCall) throws SemanticError {

		String calledFunctionName = call.getName();

		Symbol classParent = environment.getClassParent();

		if (classParent == null) {
			// something is wrong
			return null;
		}

		String className = classParent.getIdName();

		Symbol methodSymbol = environment.lookup(calledFunctionName, call);

		// in case method not found
		if (methodSymbol == null) {
			throw new SemanticError("the method " + calledFunctionName
					+ " is undefined for the type " + className, call);
		}

		if (isVirtualCall) {
			VirtualCall vCall = (VirtualCall) call;
			if (!methodSymbol.isVirtualMethod() && vCall.isExternal()) {
				throw new SemanticError(
						"method "
								+ methodSymbol.getIdName()
								+ " cannot be resolved as a virtual method for the type "
								+ className, call);
			}

			// get method parent of the virtual call's scope
			Symbol methodParent = vCall.getEnclosingScope().getMethodParent();

			/*
			 * in case call is not external (location is null) , and it is
			 * declared within a non-virtual method while the function call is
			 * actually declared as virtual, throw exception
			 */
			if (methodParent != null && methodSymbol.isVirtualMethod()
					&& !methodParent.isVirtualMethod() && !vCall.isExternal()) {
				throw new SemanticError(
						"cannot make a static reference to the non-static method "
								+ methodSymbol.getIdName()
								+ " from the static method "
								+ methodParent.getIdName(), call);
			}

			Symbol localMethodSymbol = null;
			ISymbolTable classSymbolTable = null;

			if (methodParent != null) {

				// find class in which the method of which the call is defined
				Symbol classSymbol = methodParent.getIdNode()
						.getEnclosingScope().getClassParent();

				// get class scope
				classSymbolTable = symTableOps.findClassEnvironment(classSymbol
						.getIdName());

				// look for method in current class scope
				localMethodSymbol = classSymbolTable.localLookup(vCall
						.getName());
			}

			/*
			 * in case call is not external (location is null) , and it is
			 * referring a static method which is not locally defined (in the
			 * class and not in any other super class), throw exception
			 */
			if (localMethodSymbol == null && methodSymbol != null
					&& methodSymbol.isStaticMethod() && !vCall.isExternal()
					&& classSymbolTable != null) {
				throw new SemanticError("the method "
						+ methodSymbol.getIdName()
						+ " is undefined for the type "
						+ classSymbolTable.getId(), call);
			}

		} else if (methodSymbol != null) {

			/*
			 * check case that the static call is referring a static method
			 * which is not locally defined
			 */

			Symbol localMethodSymbol = environment
					.localLookup(calledFunctionName);

			if (localMethodSymbol == null && methodSymbol.isStaticMethod()) {
				throw new SemanticError("the method " + calledFunctionName
						+ " is undefined for the type " + className, call);
			}

		}

		Type type = methodSymbol.getIdType();

		if (!(type instanceof MethodType))
			throw new SemanticError("cannot resolve" + type.toString()
					+ " as a method type", call);

		MethodType methodType = (MethodType) type;
		Type[] paramTypes = methodType.getParamTypes();
		List<Expression> methodArgs = call.getArguments();

		if (paramTypes.length != methodArgs.size()) {
			throw new SemanticError("the method " + calledFunctionName
					+ " in the type " + className + " is not applicable for "
					+ methodArgs.size() + " argument(s) ", call);
		}

		Type paramType;

		for (int i = 0; i < paramTypes.length; i++) {
			type = this.evaluateAndCheckExpressionType(methodArgs.get(i));
			paramType = paramTypes[i];

			if (!type.subTypeOf(paramType)) {
				throw new SemanticError("the method " + calledFunctionName
						+ " in the type " + className
						+ " is not applicable for type " + type.toString()
						+ " as the type of the #" + (i + 1) + " argument", call);
			}

		}

		return methodType.getReturnType();
	}

	@Override
	public Object visit(This thisExpression) throws SemanticError {
		ISymbolTable scope = thisExpression.getEnclosingScope();
		if (scope == null) {
			throw new RuntimeException("No scope found for ASTNode");
		} else {
			Symbol methodSym = scope.getMethodParent();
			if (methodSym == null) {
				throw new RuntimeException(
						"No method parent for 'this' expression");
			} else {
				if (methodSym.isStaticMethod())
					throw new SemanticError(
							"cannot use this in a static context",
							thisExpression);
			}
		}
		String thisClassName = symTableOps.findClassName(thisExpression);
		return TypeTable.classType(thisClassName);
	}

	@Override
	public Object visit(NewClass newClass) throws SemanticError {
		try {
			return TypeTable.classType(newClass.getName());
		} catch (UndefinedClassException e) {
			throw new SemanticError(e.getClassname()
					+ " cannot be resolved to a type", newClass);
		}
	}

	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		Type type = this.evaluateAndCheckExpressionType(newArray.getSize());
		if (!(type instanceof IntType)) {
			throw new SemanticError(
					"the type of the array index must be an int but it is resolved to "
							+ type.toString(), newArray);
		}
		try {
			return TypeTable
					.arrayType(TypeAdapter.adaptType(newArray.getType()));
		} catch (UndefinedClassException e) {
			throw new SemanticError(e.getClassname()
					+ " cannot be resolved to a type", newArray);
		}
	}

	@Override
	public Object visit(Length length) throws SemanticError {
		Type arrType = this.evaluateAndCheckExpressionType(length.getArray());
		if (!(arrType instanceof ArrayType))
			throw new SemanticError("the type " + arrType
					+ " does not have a field length", length);
		return TypeTable.intType;
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {

		String opRep = binaryOp.getOperator().getOperatorString();

		Type firstOperandType = this.evaluateAndCheckExpressionType(binaryOp
				.getFirstOperand());
		Type secondOperandType = this.evaluateAndCheckExpressionType(binaryOp
				.getSecondOperand());
		BinaryOps op = binaryOp.getOperator();

		if (op != BinaryOps.PLUS && op != BinaryOps.MINUS
				&& op != BinaryOps.DIVIDE && op != BinaryOps.MULTIPLY
				&& op != BinaryOps.MOD) {
			throw new SemanticError("the operator " + opRep
					+ " is undefined for the argument types "
					+ firstOperandType.toString() + " and "
					+ secondOperandType.toString(), binaryOp);
		}

		if (firstOperandType.equals(TypeTable.stringType)
				&& secondOperandType.equals(TypeTable.stringType)) {
			if (op == BinaryOps.PLUS)
				return TypeTable.stringType;
			else
				throw new SemanticError("the operator " + opRep
						+ " is undefined for strings (expected plus)", binaryOp);

		} else {
			if (firstOperandType != TypeTable.intType
					|| secondOperandType != TypeTable.intType) {
				throw new SemanticError("the operator " + opRep
						+ " is undefined for the argument types "
						+ firstOperandType.toString() + " and "
						+ secondOperandType.toString(), binaryOp);
			}
			return TypeTable.intType;
		}
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {

		String opRep = binaryOp.getOperator().getOperatorString();

		Type firstOperandType = this.evaluateAndCheckExpressionType(binaryOp
				.getFirstOperand());
		Type secondOperandType = this.evaluateAndCheckExpressionType(binaryOp
				.getSecondOperand());
		BinaryOps op = binaryOp.getOperator();

		if (op == BinaryOps.PLUS && op == BinaryOps.MINUS
				&& op == BinaryOps.DIVIDE && op == BinaryOps.MULTIPLY
				&& op == BinaryOps.MOD) {
			throw new SemanticError("the operator " + opRep
					+ " is undefined for the argument types "
					+ firstOperandType.toString() + " and "
					+ secondOperandType.toString(), binaryOp);
		}

		if (op == BinaryOps.EQUAL || op == BinaryOps.NEQUAL) {
			if (firstOperandType.subTypeOf(secondOperandType)
					|| secondOperandType.subTypeOf(firstOperandType))
				return TypeTable.boolType;
			else
				throw new SemanticError("incompatible operand types "
						+ firstOperandType.toString() + " and "
						+ secondOperandType.toString() + " for operator "
						+ opRep, binaryOp);

		} else if (op == BinaryOps.GT || op == BinaryOps.GTE
				|| op == BinaryOps.LT || op == BinaryOps.LTE) {
			if (firstOperandType.equals(TypeTable.intType)
					&& secondOperandType.equals(TypeTable.intType))
				return TypeTable.boolType;
			else
				throw new SemanticError("the operator " + opRep
						+ " is undefined for the argument types "
						+ firstOperandType.toString() + " and "
						+ secondOperandType.toString(), binaryOp);

		} else if (op == BinaryOps.LAND || op == BinaryOps.LOR) {
			if (firstOperandType.equals(TypeTable.boolType)
					&& secondOperandType.equals(TypeTable.boolType))
				return TypeTable.boolType;
			else
				throw new SemanticError("the operator " + opRep
						+ " is undefined for the argument types "
						+ firstOperandType.toString() + " and "
						+ secondOperandType.toString(), binaryOp);
		} else
			throw new SemanticError("unexpected operator " + opRep, binaryOp);
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {

		String opRep = unaryOp.getOperator().getOperatorString();

		Type operandType = this.evaluateAndCheckExpressionType(unaryOp
				.getOperand());
		UnaryOps op = unaryOp.getOperator();

		if (op != UnaryOps.UMINUS)
			throw new SemanticError("unexpected operator " + opRep, unaryOp);
		else if (!operandType.equals(TypeTable.intType))
			throw new SemanticError("the operator " + opRep
					+ " is undefined for the argument type "
					+ operandType.toString() + " (expected int)", unaryOp);
		else
			return TypeTable.intType;
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {

		String opRep = unaryOp.getOperator().getOperatorString();

		Type operandType = this.evaluateAndCheckExpressionType(unaryOp
				.getOperand());
		UnaryOps op = unaryOp.getOperator();

		if (op != UnaryOps.LNEG)
			throw new SemanticError("unexpected operator " + opRep, unaryOp);
		else if (!operandType.equals(TypeTable.boolType))
			throw new SemanticError("the operator " + opRep
					+ " is undefined for the argument type "
					+ operandType.toString() + " (expected boolean)", unaryOp);
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
			throw new SemanticError("unexpected literal "
					+ literal.getType().toString(), literal);
		}
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		return this.evaluateAndCheckExpressionType(expressionBlock
				.getExpression());
	}

	@Override
	public Type evaluateAndCheckExpressionType(Expression exp)
			throws SemanticError {
		return (Type) exp.accept(this);
	}

	/*
	 * -------------------------------------------------------------------
	 * ------- passive AST nodes (no actions when accept invoked) --------
	 * -------------------------------------------------------------------
	 */

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

}
