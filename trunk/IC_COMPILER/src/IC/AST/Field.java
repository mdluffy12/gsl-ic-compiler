package IC.AST;

import IC.Parser.SemanticError;

/**
 * Class field AST node.
 * 
 * @author Tovi Almozlino
 */
public class Field extends ASTNode {

	private final Type type;

	private final String name;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new field node.
	 * 
	 * @param type
	 *            Data type of field.
	 * @param name
	 *            Name of field.
	 */
	public Field(Type type, String name) {
		super(type.getLine());
		this.type = type;
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "<type = " + getType() + ",name = " + getName() + ">";
	}

}
