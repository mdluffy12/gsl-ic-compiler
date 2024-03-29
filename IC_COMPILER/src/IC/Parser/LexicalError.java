/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 �   
 */
package IC.Parser;

public class LexicalError extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7592428716163194091L;

	// LexicalError data members
	private final String err_msg;
	private final int error_line;
	private String err_text;

	public LexicalError() {
		this.err_msg = "Unexpected Lexical Error";
		this.error_line = -1;
		this.setErr_text(null);
	}

	/**
	 * LexicalError constructor
	 * 
	 * builds a lexical error out of an error message
	 * 
	 * @param err_msg
	 *            error message to be returned when called getMessage()
	 */
	public LexicalError(String err_msg) {
		this.err_msg = err_msg;
		this.error_line = -1;
		this.setErr_text(null);
	}

	/**
	 * LexicalError constructor
	 * 
	 * builds a lexical error out of an error message
	 * 
	 * @param err_msg
	 *            error message to be returned when called getMessage()
	 * @param error_line
	 *            the line in the IC file where the error has occured
	 */
	public LexicalError(String err_msg, int error_line) {
		this.err_msg = err_msg;
		this.error_line = error_line + 1;
		this.setErr_text(null);
	}

	/**
	 * LexicalError constructor
	 * 
	 * builds a lexical error out of an error message
	 * 
	 * @param err_msg
	 *            error message to be returned when called getMessage()
	 * @param error_line
	 *            the line in the IC file where the error has occured
	 * @param err_text
	 *            additional text to be used in the error msg
	 */
	public LexicalError(String err_msg, int error_line, String err_text) {
		this.err_msg = err_msg;
		this.error_line = error_line + 1;
		this.setErr_text(err_text);
	}

	/**
	 * override of getMessage function of Exception
	 * 
	 * returns the string representation of the Lexical Error
	 * 
	 */
	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();
		if (this.error_line >= 0) {
			sb.append(this.error_line + ": Lexical error: ");
		}
		return sb.append(this.err_msg).toString();
	}

	/**
	 * override of toString function of Exception
	 * 
	 * returns the string representation of the Lexical Error
	 * 
	 */
	@Override
	public String toString() {
		return this.getMessage();
	}

	public String getErr_text() {
		return err_text;
	}

	public void setErr_text(String err_text) {
		this.err_text = err_text;
	}
}
