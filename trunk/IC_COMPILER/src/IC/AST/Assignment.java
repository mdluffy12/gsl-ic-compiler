package IC.AST;

import IC.Parser.SemanticError;

/**
 * Assignment statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class Assignment extends Statement {

	private final Location variable;

	private final Expression assignment;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new assignment statement node.
	 * 
	 * @param variable
	 *            Variable to assign a value to.
	 * @param assignment
	 *            Value to assign.
	 */
	public Assignment(Location variable, Expression assignment) {
		super(variable.getLine());
		this.variable = variable;
		this.assignment = assignment;
	}

	public Location getVariable() {
		return variable;
	}

	public Expression getAssignment() {
		return assignment;
	}

}
