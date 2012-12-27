/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */


package IC.Parser;



%%

%class Lexer               // Tells JFlex to give the generated class the name Lexer 
%public                    // Makes the generated class public
%function next_token       // name the scanning method as "next_token"
%type Token				   // makes the scanning method to be declared as returning values of type 'Token'
%line					   // Turns line counting on and stores the value in yyline
//%scanerror LexicalError  // throw an instance 'LexicalError' in case of an internal error
%yylexthrow{
LexicalError
%yylexthrow}               // add throws LexicalError to next_token function

//%cupsym Parser           // Customizes the name of the CUP generated class/interface containing the names of
                           // terminal tokens
%cup                       // enables the CUP compatibility mode


%{
  StringBuilder qstring = new StringBuilder();
  int zeroCounter = 1;
  
  	/* check if a string rep. of an integer is a valid integer value (-2^31 < x < 2^31 - 1)*/
	public boolean isValidInteger(String n_str){
		 try{
             long n = Long.parseLong(n_str);
             if(n < Integer.MIN_VALUE | n > Integer.MAX_VALUE)
                return false;        
           }catch(java.lang.NumberFormatException e){
                return false;}
		 
		 return true;
	}
	
%}

%{
	public int getLineNumber() { return yyline+1; }
%}


/* points to remember --- 
3) Keywords and identifiers must be separated by white space or a token that is neither
a keyword or an identifier.
4) both // and /* */ comments type are supported
5) Integer literals may start with an optional negation sign -, followed a sequence of digits.
Non-zero numbers should not have leading zeroes!
6)Integers have 32-bit signed values between -2^31 and 2^31 - 1.
7) String literals are sequences of characters enclosed in double quotes.
8) String characters
can be: 1) printable ASCII characters (ASCII codes between decimal 32 and 126) other
than quote " and backslash \, and 2) the escape sequences \" to denote quote, \\ to denote
backslash, \t to denote tab, and \n for newline. No other characters or character sequences
can occur in a string. Unclosed strings are lexical errors.
9) Boolean literals must be one of the keywords true or false. The only literal for heap
references is null.
*/



/* ---- main character classes ---- */
UPPER_CASE      = [A-Z]
LOWER_CASE      = [a-z]
DIGIT           = [0-9]
ALPHA           = {UPPER_CASE} | {LOWER_CASE} | [_]
ALPHA_NUMERIC   = {ALPHA} | {DIGIT}
IDENT           = {LOWER_CASE}({ALPHA_NUMERIC})*
CLASS_IDENT     = {UPPER_CASE}({ALPHA_NUMERIC})*

/* ---- Literals ---- */
IntegerLiteral  = [1-9]({DIGIT})* 

/* ---- ignored characters ----- */
LineTerminator  = \r|\n|\r\n                  // both linux and windows representatives of new line
WhiteSpace      = {LineTerminator} | [ \t\f]  // whitespace can be either a line terminator, a space, a tab or special '\f' character

// string and character literals
// StringCharacter = [^\r\n\"\\] old string character
StringCharacter = [0-9a-zA-Z\!\#\$\%\&\'\(\)\*\+\,\-\.\:\;\<\=\>\?\@\[\]\^\_\`\{\|\}\~ ]
InputCharacter  = [^\r\n]

/* ---- comment definition ---- */
EndOfLineComment   = "//" {InputCharacter}* {LineTerminator}?


/* ---- states ---- */
%state STRING , TRADCOM, INTEGER

%%

/* handle initial state */
<YYINITIAL> {

  /* ---- operators (100) ---- */

  "="  { return (new Token(sym.ASSIGN,yyline,yytext()));  } 
  ">"  { return (new Token(sym.GT,yyline,yytext()));      }  
  "<"  { return (new Token(sym.LT,yyline,yytext()));      }
  "!"  { return (new Token(sym.LNEG,yyline,yytext()));    }
  "==" { return (new Token(sym.EQUAL,yyline,yytext()));   }
  "<=" { return (new Token(sym.LTE,yyline,yytext()));     }
  ">=" { return (new Token(sym.GTE,yyline,yytext()));     }
  "!=" { return (new Token(sym.NEQUAL,yyline,yytext()));  }
  "&&" { return (new Token(sym.LAND,yyline,yytext()));    }
  "||" { return (new Token(sym.LOR,yyline,yytext()));     }
  "+"  { return (new Token(sym.PLUS,yyline,yytext()));    }
  "-"  { return (new Token(sym.MINUS,yyline,yytext()));   }
  "*"  { return (new Token(sym.MULTIPLY,yyline,yytext()));}
  "/"  { return (new Token(sym.DIVIDE,yyline,yytext()));  }
  "%"  { return (new Token(sym.MOD,yyline,yytext()));     }

  /* ---- separators (200) ---- */
    
  "("  { return (new Token(sym.LP,yyline,yytext()));      } 
  ")"  { return (new Token(sym.RP,yyline,yytext()));      }  
  "{"  { return (new Token(sym.LCBR,yyline,yytext()));    }
  "}"  { return (new Token(sym.RCBR,yyline,yytext()));    }
  "["  { return (new Token(sym.LB,yyline,yytext()));      }
  "]"  { return (new Token(sym.RB,yyline,yytext()));      }
  ";"  { return (new Token(sym.SEMI,yyline,yytext()));    }
  ","  { return (new Token(sym.COMMA,yyline,yytext()));   }
  "."  { return (new Token(sym.DOT,yyline,yytext()));     }
      
  /* ---- IC keywords (300) ---- */
      
  "class"    { return (new Token(sym.CLASS,yyline,yytext()));    } 
  "extends"  { return (new Token(sym.EXTENDS,yyline,yytext()));  }  
  "static"   { return (new Token(sym.STATIC,yyline,yytext()));   }
  "void"     { return (new Token(sym.VOID,yyline,yytext()));     }
  "if"       { return (new Token(sym.IF,yyline,yytext()));       }
  "else"     { return (new Token(sym.ELSE,yyline,yytext()));     }
  "while"    { return (new Token(sym.WHILE,yyline,yytext()));    }
  "continue" { return (new Token(sym.CONTINUE,yyline,yytext())); }
  "break"    { return (new Token(sym.BREAK,yyline,yytext()));    }
  "boolean"  { return (new Token(sym.BOOLEAN,yyline,yytext()));  }
  "int"      { return (new Token(sym.INT,yyline,yytext()));      }
  "length"   { return (new Token(sym.LENGTH,yyline,yytext()));   }
  "new"      { return (new Token(sym.NEW,yyline,yytext()));      }
  "return"   { return (new Token(sym.RETURN,yyline,yytext()));   }
  "this"     { return (new Token(sym.THIS,yyline,yytext()));     }
  "string"   { return (new Token(sym.STRING,yyline,yytext()));   }

  
  /* boolean literals (400) */
  "false"   { return (new Token(sym.FALSE,yyline,yytext()));    } 
  "true"    { return (new Token(sym.TRUE,yyline,yytext()));     }  
    
  /* ---- null literal (500) ---- */
  "null"    { return (new Token(sym.NULL,yyline,yytext()));     } 

  /* ignore spaces */
  {WhiteSpace}          {}
  
  /* ignore comments */
  {EndOfLineComment}    {} 
  
  /* handle identifier definition (must start with lower case) */
  {IDENT}          { return new Token(sym.ID,yyline,yytext());       }
    
  /* handle class identifier definition (must start with upper case) */
  {CLASS_IDENT}    { return new Token(sym.CLASS_ID,yyline,yytext()); }
    
  /* handle integer literals */
  {IntegerLiteral} { if(isValidInteger (yytext()))
  					   	return new Token(sym.INTEGER,yyline,Integer.parseInt(yytext()));
  					 else 
  					    throw new LexicalError("illegal Integer value "+ yytext() ,yyline);
                                                                     }
                   
   /* handle string literal */
   \"              { qstring.setLength(0); yybegin(STRING);          }
   
   /* handle traditional comment beginning  */
   "/*"		       { yybegin(TRADCOM);                               }
   
   /* handle integer (zero) */
   "0" { yybegin(INTEGER); 											 }
  
  
  /* in case no other character fits */
.        { throw new LexicalError("illegal character \'"+ yytext() + "\'" ,yyline);}


 <<EOF>> { return new Token(sym.EOF,yyline,"EOF"); } 
 
}
 
 
 /* handle string state (tokens like "this is string\n") */
 <STRING> 
{
 
  // in case we got the char " again, then the string is finished, and we go back to the initial state
  \"    { yybegin(YYINITIAL); return new Token(sym.QUOTE, yyline,qstring.toString()); }
  
  // in case we got any sequence of string characters, append it to qstring
  {StringCharacter}+    { qstring.append( yytext()); }
  
  /* ---- escape sequences ---- */
  "\\\""                { qstring.append( "\\\"" ); }
  "\\\\"                { qstring.append( "\\\\" ); }
  "\\t"                 { qstring.append( "\\t" ); }
  "\\n"                 { qstring.append( "\\n" ); }
  "/"                   { qstring.append( "/" ); }
  
  /* error cases */
  {LineTerminator}      { throw new LexicalError("unterminated string at end of line",yyline); }
  <<EOF>>               { throw new LexicalError("unterminated string at end of line",yyline); } 
  
  .                     { throw new LexicalError("illegal string character \'"+ yytext() + "\'" ,yyline);}
}



/* handle traditional comment state */
 <TRADCOM> 
{
   /* the comment is finished, and we must return to initial state */
   "*/"                 { yybegin(YYINITIAL);     }
   
   /* in any other case just ignore the input */
   //.                  {}
   [^\*]                {}
   "*"                  {}
   
   
   /* error cases */
   <<EOF>>              {throw new LexicalError("unterminated comment at end of line",yyline);  } 
} 

/* handle zero appearance */
<INTEGER>
{

   /* ignore aditional zeroes */
   [0]                  {} 
   
   [1-9]                { throw new LexicalError("leading zero in an integer literal is illegal", yyline); } 

   .                    {   
                          yypushback(1); //Push the last letter back into the input stream to be read again
						  yybegin(YYINITIAL); 
                          return new Token(sym.INTEGER, yyline, 0);
                        }
                     
   <<EOF>>              { yybegin(YYINITIAL); 
                          return new Token(sym.INTEGER, yyline, 0); 
                        }									
}
 
 
 /* in case no other character fits (fallback) */
.        { throw new LexicalError("illegal character \'"+ yytext() + "\'" ,yyline);}


 <<EOF>> { return new Token(sym.EOF,yyline,"EOF"); } 

