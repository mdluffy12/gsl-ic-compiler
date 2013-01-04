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
import SymbolTable.SymbolTable;

public class SemanticChecks implements Visitor {

	private static SymTableUtils symTableUtils;

	public SemanticChecks() {
		symTableUtils = new SymTableUtils();
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

		// just an example of findSymbolTable usage
		SymbolTable classScope = symTableUtils.findSymbolTable(icClass);

		if (classScope.lookup(icClass.getName()) == null) {
			System.out.println("something is wrong");
		}

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
		Location loc = assignment.getVariable();
		Expression assgn = assignment.getAssignment();
		
		Types.Type locType = SymTableUtils.getNodeType(loc);
		Types.Type assgnType = visit(assgn);
		if(!locType.subTypeOf(assgnType))
			return false;
		else
			return true;
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

}
