package IC.AST;

/**
 * Method parameter AST node.
 * 
 * @author Tovi Almozlino
 */
public class Formal extends ASTNode {

	private final Type type;

	private final String name;

	@Override
	public Object accept(Visitor visitor) {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new parameter node.
	 * 
	 * @param type
	 *            Data type of parameter.
	 * @param name
	 *            Name of parameter.
	 */
	public Formal(Type type, String name) {
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
		return "<type = " + getType().getName() + ",ID = " + getName() + ">";
	}

}
