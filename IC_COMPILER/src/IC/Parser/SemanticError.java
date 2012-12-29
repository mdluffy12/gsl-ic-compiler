/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC.Parser;

import IC.AST.ASTNode;

public class SemanticError extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7592428716163194091L;
	private final String err_msg;
	private final int error_line;
	private final ASTNode ast_node;

	/**
	 * SyntaxError constructor
	 * 
	 * builds a Syntax error out of an error message
	 * 
	 * @param err_msg
	 *            error message to be returned when called getMessage()
	 * @param ast_node
	 *            ASTnode representing the current node where error occured
	 */
	public SemanticError(String err_msg, ASTNode ast_node) {
		this.err_msg = err_msg;
		this.error_line = ast_node.getLine();
		this.ast_node = ast_node;
	}

	public SemanticError(String err_msg) {
		this.err_msg = err_msg;
		this.error_line = 0;
		this.ast_node = null;
	}

	/**
	 * override of getMessage function of Exception
	 * 
	 * returns the string representation of the Syntax Error
	 * 
	 */
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();

		if (this.error_line > 0) {
			sb.append(this.error_line + ": Semantic error: ");

		}

		if (this.err_msg.length() > 0)
			sb.append(this.err_msg);

		return sb.toString();
	}

	/**
	 * override of toString function of Exception
	 * 
	 * returns the string representation of the Syntax Error
	 * 
	 */
	@Override
	public String toString() {
		return this.getMessage();
	}

	public ASTNode getToken() {
		return this.ast_node;
	}

}
