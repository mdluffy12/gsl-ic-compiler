/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.Parser;

import java_cup.runtime.Symbol;

public class Token extends Symbol {

	// Token data members
	private Object value;
	private final int line;

	/**
	 * Token Constructor
	 * 
	 * @param id
	 *            id value of the token, must be defined in sym
	 * @param line
	 *            current line of token in the IC file
	 * @param value
	 *            token representation (in our case, a string rep. of the token)
	 *            // maybe needs to be Object to handle int values too
	 */
	public Token(int id, int line, Object value) {
		super(id, line + 1, 0, value);
		this.value = value;
		this.line = line + 1;

	}

	public Token(int id, int line) {
		super(id, line + 1, 0);
		this.line = line + 1;

	}

	/**
	 * override of Token toString function
	 * 
	 * returns the string representation of the token: LINE: ID(VALUE)
	 * 
	 */
	@Override
	public String toString() {
		if (this.isEOF())
			return this.line + ": EOF";
		else {
			String endStr = "";
			if (this.sym == IC.Parser.sym.ID
					|| this.sym == IC.Parser.sym.CLASS_ID
					|| this.sym == IC.Parser.sym.INTEGER)
				endStr = "(" + this.value + ")";
			else if (this.sym == IC.Parser.sym.QUOTE)
				endStr = "(\"" + this.value + "\")";
			return this.line + ": " + getSymbolRep(this.sym) + endStr;
		}
	}

	/**
	 * return true iff the token represents EOF
	 */
	public boolean isEOF() {
		return super.sym == IC.Parser.sym.EOF;
	}

	/**
	 * return token name ( '=' for example will return "ASSIGN")
	 */
	public String getName() {
		return getSymbolRep(this.sym);
	}

	/**
	 * Get symbol string representation from symbol integer value
	 * 
	 * 0 is reserved for EOF by CUP, 1 is reserved for error by CUP and is not
	 * handled in this function
	 * 
	 */
	public static String getSymbolRep(int symVal) {

		switch (symVal) {
		/* ---- operators (100) ---- */
		case 2:
			return "ASSIGN";
		case 3:
			return "GT";
		case 4:
			return "LT";
		case 5:
			return "LNEG";
		case 6:
			return "EQUAL";
		case 7:
			return "LTE";
		case 8:
			return "GTE";
		case 9:
			return "NEQUAL";
		case 10:
			return "LAND";
		case 11:
			return "LOR";
		case 12:
			return "PLUS";
		case 13:
			return "MINUS";
		case 14:
			return "UMINUS";
		case 15:
			return "MULTIPLY";
		case 16:
			return "DIVIDE";
		case 17:
			return "MOD";

			/* ---- separators (200) ---- */

		case 18:
			return "LP";
		case 19:
			return "RP";
		case 20:
			return "LCBR";
		case 21:
			return "RCBR";
		case 22:
			return "LB";
		case 23:
			return "RB";
		case 24:
			return "SEMI";
		case 25:
			return "COMMA";
		case 26:
			return "DOT";

			/* ---- IC keywords (300) ---- */

		case 27:
			return "CLASS";
		case 47:
			return "CLASS_ID";
		case 28:
			return "EXTENDS";
		case 29:
			return "STATIC";
		case 30:
			return "VOID";
		case 31:
			return "IF";
		case 32:
			return "ELSE";
		case 33:
			return "WHILE";
		case 34:
			return "CONTINUE";
		case 35:
			return "BREAK";
		case 36:
			return "BOOLEAN";
		case 37:
			return "INT";
		case 49:
			return "INTEGER";
		case 38:
			return "LENGTH";
		case 39:
			return "NEW";
		case 40:
			return "RETURN";
		case 41:
			return "THIS";
		case 42:
			return "STRING";
		case 48:
			return "QUOTE";
		case 46:
			return "ID";

			/* boolean literals (400) */

		case 43:
			return "FALSE";
		case 44:
			return "TRUE";

			/* ---- null literal (500) ---- */

		case 45:
			return "NULL";

			/* ---- end of file (0) ---- */

		case 0:
			return "EOF";

		default:
			return "error : invalid symbol number";
		}

	}

	public int getLine() {
		return this.line;
	}

}