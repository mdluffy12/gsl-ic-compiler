package Test;

import java.io.File;

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

public class GraphBuilder implements Visitor {

	private final GraphViz gv;
	private String dotSource;

	public GraphBuilder() {
		this.gv = new GraphViz();
	}

	@Override
	public Object visit(Program program) {
		gv.addln("Program");
		for (ICClass icClass : program.getClasses())
			icClass.accept(this);
		return null;
	}

	@Override
	public Object visit(ICClass icClass) {
		StringBuffer output = new StringBuffer();

		String classNode = null;

		if (icClass.hasSuperClass()) {
			// gv.addln("Program -> " + icClass.getSuperClassName());
			classNode = "\"CLASS " + icClass.getName() + " : "
					+ icClass.getSuperClassName() + "\"";
			// gv.addln(icClass.getName() + "->" + icClass.getSuperClassName());
		} else {
			classNode = "\"CLASS " + icClass.getName() + "\"";
		}

		gv.addln("Program -> " + classNode);

		for (Field field : icClass.getFields()) {
			String FieldNode = "\"FIELD " + field.getName() + "\"";
			gv.addln(classNode + " -> " + FieldNode);
			field.accept(this);
		}

		for (Method method : icClass.getMethods()) {
			String MethodNode = "\"METHOD " + method.getName() + "\"";
			gv.addln(classNode + " -> " + MethodNode);
			method.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(Field field) {
		field.getType().accept(this);
		return null;
	}

	@Override
	public Object visit(VirtualMethod method) {
		method.getType().accept(this);
		for (Formal formal : method.getFormals())
			formal.accept(this);
		for (Statement statement : method.getStatements()) {
			String MethodNode = "\"METHOD " + method.getName() + "\"";
			gv.addln(MethodNode + " -> StatementBlock");
			statement.accept(this);
		}
		return null;
	}

	@Override
	public Object visit(StaticMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LibraryMethod method) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Formal formal) {
		// output.append("Parameter: " + formal.getName());
		// formal.getType().accept(this);
		return null;
	}

	@Override
	public Object visit(PrimitiveType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(UserType type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Assignment assignment) {
		gv.addln("StatementBlock -> Assignment");
		assignment.getVariable().accept(this);
		assignment.getAssignment().accept(this);
		return null;
	}

	@Override
	public Object visit(CallStatement callStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Return returnStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(If ifStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(While whileStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Break breakStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(Continue continueStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(LocalVariable localVariable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VariableLocation location) {
		gv.addln("Assignment -> \"ID " + location.getName() + "\"");
		// location.getLocation().accept(this);

		return null;
	}

	@Override
	public Object visit(ArrayLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(StaticCall call) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(VirtualCall call) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(This thisExpression) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(NewClass newClass) {
		// TODO Auto-generated method stub
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
		StringBuffer output = new StringBuffer();
		gv.addln("Assignment -> " + binaryOp.getOperator().getDescription());
		binaryOp.getFirstOperand().accept(this);
		binaryOp.getSecondOperand().accept(this);
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
	public Object visit(Literal literal) {

		// literal.getType().getDescription() + ": "
		// + literal.getType().toFormattedString(literal.getValue()));
		return null;
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		expressionBlock.getExpression().accept(this);
		return null;
	}

	public GraphViz getGrapViz() {
		return gv;
	}

	public void initStart() {
		gv.addln(gv.start_graph());
	}

	public void initEnd() {
		gv.addln(gv.end_graph());
	}

	public void Print() {
		this.dotSource = gv.getDotSource();
		System.out.println(dotSource);
	}

	public void CreateImg(String fileName) {
		String type = "png";
		File out = new File("C:\\files\\" + fileName + "." + type);
		gv.writeGraphToFile(gv.getGraph(dotSource, type), out);
	}

	public void Build(Program root) {
		root.accept(this);
	}

}
