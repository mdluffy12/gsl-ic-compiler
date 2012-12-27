package IC.AST;

import IC.Parser.SemanticError;

/**
 * Continue statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class Continue extends Statement {

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a continue statement node.
	 * 
	 * @param line
	 *            Line number of continue statement.
	 */
	public Continue(int line) {
		super(line);
	}

	@Override
	public String toString() {
		return "continue;";
	}

}
