package IC.AST;

import IC.Parser.SemanticError;

/**
 * Method call statement AST node.
 * 
 * @author Tovi Almozlino
 */
public class CallStatement extends Statement {

	private final Call call;

	@Override
	public Object accept(Visitor visitor) throws SemanticError {
		return visitor.visit(this);
	}

	/**
	 * Constructs a new method call statement node.
	 * 
	 * @param call
	 *            Method call expression.
	 */
	public CallStatement(Call call) {
		super(call.getLine());
		this.call = call;
	}

	public Call getCall() {
		return call;
	}

}
