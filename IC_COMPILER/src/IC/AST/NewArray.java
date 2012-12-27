package IC.AST;

import IC.Parser.SemanticError;

/**
 * Array creation AST node.
 * 
 * @author Tovi Almozlino
 */
public class NewArray extends New {

	private final Type type;

	private final Expression size;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new array creation expression node.
	 * 
	 * @param type
	 *            Data type of new array.
	 * @param size
	 *            Size of new array.
	 */
	public NewArray(Type type, Expression size) {
		super(type.getLine());
		this.type = type;
		this.size = size;
	}

	public Type getType() {
		return type;
	}

	public Expression getSize() {
		return size;
	}

}
