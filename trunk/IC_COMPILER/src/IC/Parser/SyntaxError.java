/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC.Parser;

public class SyntaxError extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7592428716163194091L;
	private final String err_msg;
	private final int error_line;
	private final Token token;

	public SyntaxError() {
		this.err_msg = "";
		this.error_line = -1;
		this.token = null;
	}

	/**
	 * SyntaxError constructor
	 * 
	 * builds a Syntax error out of an error message
	 * 
	 * @param err_msg
	 *            error message to be returned when called getMessage()
	 */
	public SyntaxError(String err_msg) {
		this.err_msg = err_msg;
		this.error_line = -1;
		this.token = null;
	}

	/**
	 * SyntaxError constructor
	 * 
	 * builds a Syntax error out of an error message
	 * 
	 * @param err_msg
	 *            error message to be returned when called getMessage()
	 * @param error_line
	 *            the line in the IC file where the error has occured
	 */
	public SyntaxError(String err_msg, int error_line) {
		this.err_msg = err_msg;
		this.error_line = error_line;
		this.token = null;
	}

	/**
	 * SyntaxError constructor
	 * 
	 * builds a Syntax error out of an error message
	 * 
	 * @param err_msg
	 *            error message to be returned when called getMessage()
	 * @param error_line
	 *            the line in the IC file where the error has occured
	 */
	public SyntaxError(String err_msg, Token token) {
		this.err_msg = err_msg;
		this.error_line = token.getLine();
		this.token = token;
	}

	/**
	 * SyntaxError constructor
	 * 
	 * builds a Syntax error out of an error message
	 * 
	 * @param err_msg
	 *            error message to be returned when called getMessage()
	 * @param error_line
	 *            the line in the IC file where the error has occured
	 */
	public SyntaxError(Token token) {
		this.err_msg = "";
		this.error_line = token.getLine();
		this.token = token;
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
		if (this.error_line >= 0) {
			sb.append(this.error_line + ": Syntax error: ");

			if (token != null) {
				sb.append("unexpected " + token.getName());
			}
		}
		return sb.append(this.err_msg).toString();
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

}
