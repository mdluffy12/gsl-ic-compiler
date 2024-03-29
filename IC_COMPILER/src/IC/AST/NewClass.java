package IC.AST;

import IC.Parser.SemanticError;

/**
 * Class instance creation AST node.
 * 
 * @author Tovi Almozlino
 */
public class NewClass extends New {

	private final String name;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new class instance creation expression node.
	 * 
	 * @param line
	 *            Line number of expression.
	 * @param name
	 *            Name of class.
	 */
	public NewClass(int line, String name) {
		super(line);
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
