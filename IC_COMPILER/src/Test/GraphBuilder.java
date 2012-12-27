package Test;

import java.io.File;

import IC.AST.ASTNode;
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

public class GraphBuilder implements Visitor {

	private final GraphViz gv;
	private String dotSource;

	// counters are used to give unique names to nodes of the same type
	protected int cnt = 0; // TODO might need to make it long for large programs
	String extraInfo = ""; // used to print extra info when needed

	public GraphBuilder() {
		this.gv = new GraphViz();
	}

	@Override
	public Object visit(Program program) {
		buildSubtree(program, "Program",
				program.getClasses().toArray(new ASTNode[0]));
		return null;
	}

	// the comments here represent the workflow of all the methods
	@Override
	public Object visit(ICClass icClass) {
		// create label
		String classLabel = "CLASS " + icClass.getName();

		if (icClass.hasSuperClass())
			classLabel = classLabel + " : " + icClass.getSuperClassName();

		// build a node from the label and add the edges from it to the fom
		// nodes (after building those nodes recursively)
		String classNode = buildSubtree(icClass, classLabel, icClass
				.getFields().toArray(new ASTNode[0]));
		addEdges(classNode, "", icClass.getMethods().toArray(new ASTNode[0])); // addEdges
																				// is
																				// not
		// normally
		// called but here we have
		// two lists of children
		// nodes

		// return the name of the UNIQUE node (so that we can refer to it to add
		// edges that point to it)
		return classNode;
	}

	@Override
	public Object visit(Field field) {
		return buildSubtree(field, "FIELD " + field.getName(), field.getType());
	}

	@Override
	public Object visit(VirtualMethod method) {
		String methodNode = buildSubtree(method,
				"Virtual Method " + method.getName());

		addEdges(methodNode, "Return type: ", method.getType());

		addEdges(methodNode, "", method.getFormals().toArray(new ASTNode[0]));

		addEdges(methodNode, "STATEMENT: ",
				method.getStatements().toArray(new ASTNode[0]));

		return methodNode;
	}

	@Override
	public Object visit(StaticMethod method) {
		String methodNode = buildSubtree(method,
				"Static Method " + method.getName(), method.getType());

		addEdges(methodNode, "", method.getFormals().toArray(new ASTNode[0]));
		addEdges(methodNode, "STATEMENT: ",
				method.getStatements().toArray(new ASTNode[0]));

		return methodNode;
	}

	@Override
	public Object visit(LibraryMethod method) {
		String methodNode = buildSubtree(method,
				"Library Method " + method.getName(), method.getType());

		addEdges(methodNode, "", method.getFormals().toArray(new ASTNode[0]));

		return methodNode;
	}

	@Override
	public Object visit(Formal formal) {
		return buildSubtree(formal, "Parameter: " + formal.getType() + " "
				+ formal.getName());
	}

	@Override
	public Object visit(PrimitiveType type) {
		return buildSubtree(type, type.toString());
	}

	@Override
	public Object visit(UserType type) {

		return buildSubtree(type, type.toString());
	}

	@Override
	public Object visit(Assignment assignment) {
		return buildSubtree(assignment, "Assignment statement",
				assignment.getVariable(), assignment.getAssignment());
	}

	@Override
	public Object visit(CallStatement callStatement) {
		return buildSubtree(callStatement, "Call statement",
				callStatement.getCall());
	}

	@Override
	public Object visit(Return returnStatement) {
		if (returnStatement.hasValue())
			return buildSubtree(returnStatement, "Return statement",
					returnStatement.getValue());

		return buildSubtree(returnStatement, "Return statement");
	}

	@Override
	public Object visit(If ifStatement) {
		String ifNode = buildSubtree(ifStatement, "If statement",
				ifStatement.getCondition(), ifStatement.getOperation());

		if (ifStatement.hasElse())
			addEdges(ifNode, "ELSE: ", ifStatement.getElseOperation());

		return ifNode;
	}

	@Override
	public Object visit(While whileStatement) {
		// this code replaces the code below, TODO remove comments later
		return buildSubtree(whileStatement, "While statement",
				whileStatement.getCondition(), whileStatement.getOperation());

		/*
		 * String whileNode = "while" + cnt++; gv.addln(whileNode +
		 * " [label=\"While statement\"]");
		 * 
		 * gv.addln(whileNode + " -> " +
		 * whileStatement.getCondition().accept(this)); gv.addln(whileNode +
		 * " -> " + whileStatement.getOperation().accept(this));
		 * 
		 * return whileNode;
		 */
	}

	@Override
	public Object visit(Break breakStatement) {
		return buildSubtree(breakStatement, "Break statement");
	}

	@Override
	public Object visit(Continue continueStatement) {
		return buildSubtree(continueStatement, "Continue statement");
	}

	@Override
	public Object visit(StatementsBlock statementsBlock) {

		/*
		 * String sbNode = "stateBlock" + cnt++; gv.addln(sbNode +
		 * " [label=\"Statement Block\"]");
		 * 
		 * for (Statement statement : statementsBlock.getStatements())
		 * gv.addln(sbNode + " -> " + statement.accept(this));
		 * 
		 * return sbNode;
		 */

		// above code is replaced by: TODO remove comment

		return buildSubtree(statementsBlock, "Statement Block", statementsBlock
				.getStatements().toArray(new ASTNode[0]));
	}

	@Override
	public Object visit(LocalVariable localVariable) {
		String varLabel = "Local variable declaration:\\n"
				+ localVariable.getType() + " " + localVariable.getName();

		if (localVariable.hasInitValue())
			return buildSubtree(localVariable, varLabel,
					localVariable.getInitValue());

		return buildSubtree(localVariable, varLabel);
	}

	@Override
	public Object visit(VariableLocation varLoc) {
		String varLabel = "Reference to variable: " + varLoc.getName();

		if (varLoc.isExternal()) {
			// extraInfo = "LOCATION: ";
			return buildSubtree(varLoc, varLabel, varLoc.getLocation());
		}

		return buildSubtree(varLoc, varLabel);
	}

	@Override
	public Object visit(ArrayLocation location) {
		return buildSubtree(location, "Reference to array",
				location.getArray(), location.getIndex());
	}

	@Override
	public Object visit(StaticCall call) {
		String callLabel = "Call to static method: " + call.getName()
				+ " in class: " + call.getClassName();

		return buildSubtree(call, callLabel,
				call.getArguments().toArray(new ASTNode[0]));
	}

	@Override
	public Object visit(VirtualCall call) {
		String callLabel = "Call to virtual method: " + call.getName();

		String callNode = buildSubtree(call, callLabel, call.getArguments()
				.toArray(new ASTNode[0]));

		if (call.isExternal())
			addEdges(callNode, "", call.getLocation());

		return callNode;
	}

	@Override
	public Object visit(This thisExpression) {
		return buildSubtree(thisExpression, "This statement");
	}

	@Override
	public Object visit(NewClass newClass) {
		return buildSubtree(newClass,
				"Instantiation of class: " + newClass.getName());
	}

	@Override
	public Object visit(NewArray newArray) {
		return buildSubtree(newArray, "Array allocation", newArray.getType(),
				newArray.getSize());
	}

	@Override
	public Object visit(Length length) {
		return buildSubtree(length, "Reference to array length in...",
				length.getArray());
	}

	@Override
	public Object visit(MathBinaryOp binaryOp) {
		String opLabel = "Math binary op: "
				+ binaryOp.getOperator().getDescription();

		return buildSubtree(binaryOp, opLabel, binaryOp.getFirstOperand(),
				binaryOp.getSecondOperand());
	}

	@Override
	public Object visit(LogicalBinaryOp binaryOp) {
		String opLabel = "Logical binary op: "
				+ binaryOp.getOperator().getDescription();

		return buildSubtree(binaryOp, opLabel, binaryOp.getFirstOperand(),
				binaryOp.getSecondOperand());
	}

	@Override
	public Object visit(MathUnaryOp unaryOp) {
		String opLabel = "Math unary op: "
				+ unaryOp.getOperator().getDescription();

		return buildSubtree(unaryOp, opLabel, unaryOp.getOperand());
	}

	@Override
	public Object visit(LogicalUnaryOp unaryOp) {
		String opLabel = "Logical unary op: "
				+ unaryOp.getOperator().getDescription();

		return buildSubtree(unaryOp, opLabel, unaryOp.getOperand());
	}

	@Override
	public Object visit(Literal literal) {
		return buildSubtree(literal, literal.getType().getDescription()
				+ ": "
				+ literal.getType().toFormattedString(literal.getValue())
						.replaceAll("[\"]", "")); // remove quotation marks to
													// avoid problems with DOT
													// lang
	}

	@Override
	public Object visit(ExpressionBlock expressionBlock) {
		return buildSubtree(expressionBlock, "Parenthesized expression",
				expressionBlock.getExpression());
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

	public void CreateImg(String Directory, String fileName) {
		String type = "png";
		GraphViz.setTmpDir(Directory);
		File out = new File(Directory + fileName + "_tree" + "." + type);
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}

	public void Build(Program root) {
		try {
			root.accept(this);
		} catch (SemanticError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ==================== Utility Methods ====================

	/**
	 * Creates a subtree rooted at the node whose label and children are
	 * provided.
	 * 
	 * To achieve that, a node with the given label is created and edges are
	 * added from it to each of the children ASTNodes (which get built in the
	 * process, creating the rest of the subtree).
	 * 
	 * "Object" is used to avoid having to cast everywhere.
	 * 
	 * @return The unique name (NOT label) of the subtree root
	 */
	protected String buildSubtree(ASTNode root, String label,
			ASTNode... children) {
		// String node = label + cnt++; //old version, might need it to make
		// testing easier
		String node = "" + cnt++; // create unique name

		String lineLabel = "";
		if (root.getLine() > 0) {
			lineLabel = "\\n Line: " + root.getLine();
		}

		gv.addln(node + " [label=\"" + extraInfo + " " + label + lineLabel
				+ "\"]");
		extraInfo = "";

		if (children != null)
			addEdges(node, "", children);

		return node;
	}

	protected void addEdges(String node, String extraLabel, ASTNode... children) {
		for (ASTNode child : children) {
			extraInfo = extraLabel;
			try {
				gv.addln(node + " -> " + child.accept(this));
			} catch (SemanticError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
