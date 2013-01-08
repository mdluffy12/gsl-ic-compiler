/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 �   
 */

package Visitors;

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
import Types.ITypeEvaluator;
import Types.Type;

public class TypeChecker implements Visitor {
	
	private final ITypeEvaluator evaluator;	
	
	public TypeChecker(ITypeEvaluator evaluator) {
		super();
		this.evaluator = evaluator;
	}

	@Override
	public Object visit(Program program) throws SemanticError {
		
		for (ICClass icClass : program.getClasses()) {
			icClass.accept(this);
		}
		return null;
	}

 
	@Override
	public Object visit(ICClass icClass) throws SemanticError {
		for(Method method : icClass.getMethods())
			method.accept(this);
		return null;
	}

	@Override 
	public Object visit(Field field) throws SemanticError {
		return null;
	}

	@Override
	public Object visit(VirtualMethod method) throws SemanticError {
		handleMethod(method);
		return null;
	}

	@Override
	public Object visit(StaticMethod method) throws SemanticError {
		handleMethod(method);
		return null;
	}


	@Override
	public Object visit(LibraryMethod method) throws SemanticError {
		handleMethod(method);
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
		Types.Type  providedType = evaluator.evaluateAndCheckExpressionType(assignment.getAssignment());
		Types.Type  expectedType = evaluator.evaluateAndCheckExpressionType(assignment.getVariable());
		
		if (!providedType.subTypeOf(expectedType))
			throw new SemanticError("Expected "+expectedType.getName()+" in assignment. Provided type: "+ providedType.getName());
			
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement) throws SemanticError {
		evaluator.evaluateAndCheckExpressionType(callStatement.getCall());
		return null;
	}

	@Override
	public Object visit(Return returnStatement) throws SemanticError {
		//The type checking for the return value is done in the method check already!
		return null;
	}

	@Override 
	public Object visit(If ifStatement) throws SemanticError {
		checkCondition(ifStatement.getCondition());
		
		ifStatement.getOperation().accept(this);
		
		if (ifStatement.hasElse())
			ifStatement.getElseOperation().accept(this);
			
		return null;
	}

	@Override 
	public Object visit(While whileStatement) throws SemanticError {
		checkCondition(whileStatement.getCondition());		
		whileStatement.getOperation().accept(this);

		return null;
	}

	@Override
	public Object visit(Break breakStatement) throws SemanticError {
		//  Break semantic check (that it is in a loop) is not a type check, therefore it is handled elsewhere
		return null;
	}

	@Override 
	public Object visit(Continue continueStatement) throws SemanticError {
		//  Continue semantic check (that it is in a loop) is not a type check, therefore it is handled elsewhere
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) throws SemanticError {
		for(Statement s : statementsBlock.getStatements())
			s.accept(this);
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable) throws SemanticError {
		
		//the check only needs to be done if the variable is being initialized with a value
		if (localVariable.hasInitValue()){
			Types.Type providedType = evaluator.evaluateAndCheckExpressionType(localVariable.getInitValue());
			
			//find type using symbol table
			ISymbolTable varScope = localVariable.getEnclosingScope();
			Type expectedType = varScope.lookup(localVariable.getName(), localVariable).getIdType(); 	//Technically can return 
																						//null but in this case that is impossible
			
			if (!providedType.subTypeOf(expectedType))
				throw new SemanticError("Expected "+providedType.getName()+" in initialization. Provided type: "+ expectedType.getName());
		}
			
		return null;
	}

	@Override 
	public Object visit(VariableLocation location) throws SemanticError {
		// VariableLocation is an expression therefore handled by TypeEvaluator
		return null;
	}

	@Override 
	public Object visit(ArrayLocation location) throws SemanticError {
		// ArrayLocation is an expression therefore handled by TypeEvaluator
		return null;
	}

	@Override
	public Object visit(StaticCall call) throws SemanticError {
		// StaticCall is an expression therefore handled by TypeEvaluator
		return null;
	}

	@Override
	 
	public Object visit(VirtualCall call) throws SemanticError {
		// VirtualCall is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(This thisExpression) throws SemanticError {
		// This is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(NewClass newClass) throws SemanticError {
		// NewClass is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(NewArray newArray) throws SemanticError {
		// NewArray is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(Length length) throws SemanticError {
		// Length is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(MathBinaryOp binaryOp) throws SemanticError {
		// MathBinaryOp is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(LogicalBinaryOp binaryOp) throws SemanticError {
		// LogicalBinaryOp is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(MathUnaryOp unaryOp) throws SemanticError {
		// MathUnaryOp is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(LogicalUnaryOp unaryOp) throws SemanticError {
		// LogicalUnaryOp is an expression therefore handled by TypeEvaluator
		return null;
	}

	 
	@Override
	public Object visit(Literal literal) throws SemanticError {
		// Literal is an expression therefore handled by TypeEvaluator
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) throws SemanticError {
		evaluator.evaluateAndCheckExpressionType(expressionBlock.getExpression());
		return null;
	}
	
	//checks that a condition is boolean
	private void checkCondition(Expression cond) throws SemanticError{
		Types.Type condType = evaluator.evaluateAndCheckExpressionType(cond);
		
		if (!condType.equals(Types.TypeTable.boolType))
			throw new SemanticError("Expected a boolean type for condition.");
	}
	
	private void handleMethod(Method m) throws SemanticError{
		Type methodReturnType = Types.TypeAdapter.adaptType(m.getType()); //TODO Tzvika says: use type table but make sure that you don't accidentally return something from the table which has the same name but is not the method 
		
		boolean hasReturn = false;
		
		for (Statement s : m.getStatements()){
			if (s instanceof Return){
				Type actualReturnType = evaluator.evaluateAndCheckExpressionType(((Return) s).getValue());
				
				if (!methodReturnType.subTypeOf(actualReturnType))
					throw new SemanticError("Unexpected return type: Expected "+methodReturnType.getName()+", provided type: "+ actualReturnType.getName());
			}
			
			else
				s.accept(this);
		}
		
		//if the method doesn't return when needed, throw exception
		//if (!(m instanceof LibraryMethod) && (methodReturnType != Types.TypeTable.voidType) && !hasReturn)
		//	throw new SemanticError("Method doesn't return");
	}
	
}