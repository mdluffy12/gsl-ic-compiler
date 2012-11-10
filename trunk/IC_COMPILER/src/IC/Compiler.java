/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import IC.Parser.*;

/**
 * This class takes a single filename as an argument, it reads that file breaks
 * it into tokens, and successively calls the 'next token' method of the
 * generated scanner to print a representation of the file as a series of tokens
 * to the standard output, one token per line
 */

public class Compiler {
	

	/* this is the main function of our compiler */
	public static void main(String[] args) {

		/* check argument input */
		if (args.length == 0) {
			System.out.println("error - no file paths were given as input");
		} else if (args.length > 1) {
			System.out
					.println("error - too many file paths were given as input");
		} else {
			ExecuteLexicalAnalysis(args[0]);
		}

	}

	/**
	 * Executes Full Lexical Analysis of the file
	 * 
	 * We break the file into predefined tokens appropriately to IC language.
	 * The function uses the JFlex tool for lexical analysis. Each token will be
	 * printed in the following format: "LINE: ID(VALUE)"
	 * 
	 * 
	 * @param file_path
	 *            the path of the file which will be analyzed
	 *            
	 * returns true iff there are no lexical errors in the file
	 */
	public static boolean ExecuteLexicalAnalysis(String file_path) {

		// define lexer object
		Lexer lexer = null;
		Token token = null;
		try {

			// create input stream for reading character files
			FileReader reader = new FileReader(file_path);

			/* create a new lexer object with our file_path from argument list */
			lexer = new Lexer(reader);
			do {

				/* get next token from file */
				token = lexer.next_token();

				if (token != null) {
					System.out.println(token);
				}

			} while (!token.isEOF());

		}

		catch (FileNotFoundException e) {
			System.out.println("File not found : \"" + file_path + "\"");
			return false;
		}

		catch (IOException e) {
			System.out.println("IO error scanning file \"" + file_path + "\"");
			System.out.println(e);
			return false;
		}

		catch (LexicalError e) {
			System.out.println(e.getMessage());
			return false;
		}

		catch (Exception e) {
			System.out.println("Unexpected exception:");
			e.printStackTrace();
			return false;
		}
		
		return true;

	}
}
