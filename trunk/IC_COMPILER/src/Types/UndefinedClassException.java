/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

import IC.Parser.SemanticError;


public class UndefinedClassException extends SemanticError {

	public UndefinedClassException(String err_msg) {
		super(err_msg);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5718669462964046157L;
	//This exception is thrown when a class is asked for from the TypeTable without being defined
}
