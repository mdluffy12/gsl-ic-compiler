/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC.Parser;

public class sym {
	

  /* ---- operators (100) ---- */
	
  public static final int  ASSIGN   = 100;  // '=' 
  public static final int  GT       = 101;  // '>'
  public static final int  LT       = 102;	// '<'
  public static final int  LNEG     = 103; 	// '!'
  public static final int  EQUAL    = 104;  // '=='
  public static final int  LTE      = 105;	// '<='
  public static final int  GTE      = 106;  // '>='
  public static final int  NEQUAL   = 107;	// '!='
  public static final int  LAND     = 108; 	// '&&'
  public static final int  LOR      = 109;  // '||'
  public static final int  PLUS     = 110; 	// '+'
  public static final int  MINUS    = 111;	// '-'
  public static final int  MULTIPLY = 112;	// '*'
  public static final int  DIVIDE   = 113;  // '/' 
  public static final int  MOD      = 114;	// '%'

  
  /* ---- separators (200) ---- */
 
  public static final int  LP       = 200; 	// '('
  public static final int  RP       = 201;  // ')'
  public static final int  LCBR     = 202;  // '{'
  public static final int  RCBR     = 203; 	// '}'
  public static final int  LB       = 204;	// '['
  public static final int  RB       = 205;  // ']'
  public static final int  SEMI     = 206;  // ';'
  public static final int  COMMA    = 207;  // ','
  public static final int  DOT      = 208;  // '.'


  /* ---- IC keywords (300) ---- */
  
  public static final int  CLASS    = 300;  // 'class'
  public static final int  CLASS_ID = 301;  // 'class identifier'
  public static final int  EXTENDS  = 302; 	// 'extends'
  public static final int  STATIC   = 303;	// 'static'
  public static final int  VOID     = 304; 	// 'void'
  public static final int  IF       = 305; 	// 'if'
  public static final int  ELSE     = 306; 	// 'else'
  public static final int  WHILE    = 307;	// 'while'
  public static final int  CONTINUE = 308;  // 'continue'
  public static final int  BREAK    = 309; 	// 'break'
  public static final int  BOOLEAN  = 310;  // 'boolean'
  public static final int  INT      = 311;  // 'int'
  public static final int  INTEGER  = 312;	// 'integer'
  public static final int  LENGTH   = 313;  // 'length'
  public static final int  NEW      = 314; 	// 'new'
  public static final int  RETURN   = 315;	// 'return'
  public static final int  THIS     = 316;	// 'this'
  public static final int  STRING   = 317;  // 'string(keyword)'
  public static final int  QUOTE    = 318; 	// 'quoted string'
  public static final int  ID       = 319;	// 'identifier
   
  
  /* boolean literals (400) */
  
  public static final int  FALSE    = 401;	// 'false'
  public static final int  TRUE     = 402;  // 'true'
  
  
  /* ---- null literal (500) ---- */
  
  public static final int  NULL     = 500;  // 'null'
  
  /* ---- end of file (0) ---- */
  public static final int  EOF     = 0;  // 'EOF'
  
  
  /**
	 * returns a string representation of the symbol value 
	 * 
	 * 
	 * 
	 * @param symVal
	 *            
	 */
  public static String getSymbolRep(int symVal){
	
	  switch(symVal)
	  {
	  /* ---- operators (100) ---- */
	  case 100: return "ASSIGN";
	  case 101: return "GT";
	  case 102: return "LT";
	  case 103: return "LNEG";
	  case 104: return "EQUAL";
	  case 105: return "LTE";
	  case 106: return "GTE";
	  case 107: return "NEQUAL";
	  case 108: return "LAND";
	  case 109: return "LOR";
	  case 110: return "PLUS";
	  case 111: return "MINUS";
	  case 112: return "MULTIPLY";
	  case 113: return "DIVIDE";
	  case 114: return "MOD";

	  
	  /* ---- separators (200) ---- */
	 
	  case 200: return "LP";
	  case 201: return "RP";
	  case 202: return "LCBR";
	  case 203: return "RCBR";
	  case 204: return "LB";
	  case 205: return "RB";
	  case 206: return "SEMI";
	  case 207: return "COMMA";
	  case 208: return "DOT";


	  /* ---- IC keywords (300) ---- */
	  
	  case 300: return "CLASS";
	  case 301: return "CLASS_ID";
	  case 302: return "EXTENDS";
	  case 303: return "STATIC";
	  case 304: return "VOID";
	  case 305: return "IF";
	  case 306: return "ELSE";
	  case 307: return "WHILE";
	  case 308: return "CONTINUE";
	  case 309: return "BREAK";
	  case 310: return "BOOLEAN";
	  case 311: return "INT";
	  case 312: return "INTEGER";
	  case 313: return "LENGTH";
	  case 314: return "NEW";
	  case 315: return "RETURN";
	  case 316: return "THIS";
	  case 317: return "STRING";
	  case 318: return "QUOTE";
	  case 319: return "ID";
	  
	  /* boolean literals (400) */
	  
	  case 401: return "FALSE";
	  case 402: return "TRUE";
	  
	  
	  /* ---- null literal (500) ---- */
	  
	  case 500: return "NULL";
	  
	  /* ---- end of file (0) ---- */
	  
	  case 0: return "EOF";
	  
	  default: return "ERROR INVALID SYMBOL NUMBER!!!! >=[";
	  }
	  
  }
}
