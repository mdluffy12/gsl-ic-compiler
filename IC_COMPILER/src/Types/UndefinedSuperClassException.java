/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Types;

import IC.Parser.SemanticError;

public class UndefinedSuperClassException extends SemanticError {

	public UndefinedSuperClassException(String err_msg) {
		super(err_msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7776250715027337240L;
	//This exception is thrown when a class is defined before it's super class (or its super class isn't defined at all)
}
