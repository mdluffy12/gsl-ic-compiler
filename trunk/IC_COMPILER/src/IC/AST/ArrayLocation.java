package IC.AST;

import IC.Parser.SemanticError;

/**
 * Array reference AST node.
 * 
 * @author Tovi Almozlino
 */
public class ArrayLocation extends Location {

	private final Expression array;

	private final Expression index;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new array reference node.
	 * 
	 * @param array
	 *            Expression representing an array.
	 * @param index
	 *            Expression representing a numeric index.
	 */
	public ArrayLocation(Expression array, Expression index) {
		super(array.getLine());
		this.array = array;
		this.index = index;
	}

	public Expression getArray() {
		return array;
	}

	public Expression getIndex() {
		return index;
	}
}