package IC.AST;

import IC.Parser.SemanticError;

/**
 * AST node for expression in parentheses.
 * 
 * @author Tovi Almozlino
 */
public class ExpressionBlock extends Expression {

	private final Expression expression;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new expression in parentheses node.
	 * 
	 * @param expression
	 *            The expression.
	 */
	public ExpressionBlock(Expression expression) {
		super(expression.getLine());
		this.expression = expression;
	}

	public Expression getExpression() {
		return expression;
	}

}
