package IC.AST;

import IC.UnaryOps;
import IC.Parser.SemanticError;

/**
 * Logical unary operation AST node.
 * 
 * @author Tovi Almozlino
 */
public class LogicalUnaryOp extends UnaryOp {

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new logical unary operation node.
	 * 
	 * @param operator
	 *            The operator.
	 * @param operand
	 *            The operand.
	 */
	public LogicalUnaryOp(UnaryOps operator, Expression operand) {
		super(operator, operand);
	}

}
