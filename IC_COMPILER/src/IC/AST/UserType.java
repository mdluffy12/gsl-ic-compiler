package IC.AST;

import IC.Parser.SemanticError;

/**
 * User-defined data type AST node.
 * 
 * @author Tovi Almozlino
 */
public class UserType extends Type {

	private final String name;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new user-defined data type node.
	 * 
	 * @param line
	 *            Line number of type declaration.
	 * @param name
	 *            Name of data type.
	 */
	public UserType(int line, String name) {
		super(line);
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return getName();
	}

}
