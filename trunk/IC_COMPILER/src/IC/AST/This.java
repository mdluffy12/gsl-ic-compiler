package IC.AST;

import IC.Parser.SemanticError;

/**
 * 'This' expression AST node.
 * 
 * @author Tovi Almozlino
 */
public class This extends Expression {

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a 'this' expression node.
	 * 
	 * @param line
	 *            Line number of 'this' expression.
	 */
	public This(int line) {
		super(line);
	}

}
