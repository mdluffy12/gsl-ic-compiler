package IC.AST;

import IC.Parser.SemanticError;

/**
 * Array length expression AST node.
 * 
 * @author Tovi Almozlino
 */
public class Length extends Expression {

	private final Expression array;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new array length expression node.
	 * 
	 * @param array
	 *            Expression representing an array.
	 */
	public Length(Expression array) {
		super(array.getLine());
		this.array = array;
	}

	public Expression getArray() {
		return array;
	}

}
