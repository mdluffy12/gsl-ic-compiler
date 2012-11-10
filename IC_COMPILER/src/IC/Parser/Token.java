/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC.Parser;

import java_cup.runtime.Symbol;

public class Token extends Symbol {

	// Token data members
	private Object value;
	private int line;

	/**
	 * Token Constructor
	 * 
	 * @param id
	 *            id value of the token, must be defined in sym
	 * @param line
	 *            current line of token in the IC file
	 * @param value
	 *            token representation (in our case, a string rep. of the token) // maybe needs to be Object to handle int values too
	 */
	public Token(int id, int line, Object value) {
		super(id, null);
		this.value = value;
		this.line = line + 1;
	}
	
	public Token(int id, int line) {
		super(id, null);
		this.line = line + 1;
				
	}

	/**
	 * override of Token toString function
	 * 
	 * returns the string representation of the token: LINE: ID(VALUE)
	 * 
	 */
	public String toString() {
		if (this.sym == IC.Parser.sym.EOF)
			return this.line + ": EOF";
		else
		{
			String endStr = "";
			if(this.sym == IC.Parser.sym.ID || this.sym == IC.Parser.sym.CLASS_ID || this.sym == IC.Parser.sym.INTEGER)
				endStr = "(" + this.value + ")";
			else if(this.sym == IC.Parser.sym.QUOTE)
				endStr = "(\"" + this.value + "\")";
		    return this.line + ": " + IC.Parser.sym.getSymbolRep(this.sym) + endStr;
		}
	}
	
	public boolean isEOF(){
		return super.sym == IC.Parser.sym.EOF;
	}

}