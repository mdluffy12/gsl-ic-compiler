/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class GenLexer {
	private Lexer lexer;

	public GenLexer(String file_path) throws FileNotFoundException {
		FileReader reader;
		try {
			reader = new FileReader(file_path);
			this.lexer = new Lexer(reader);
		} catch (FileNotFoundException e) {
			throw e;
		}

	}

	public GenLexer(Lexer lexer) {
		this.setLexer(lexer);
	}

	public Lexer getLexer() {
		return lexer;
	}

	public void setLexer(Lexer lexer) {
		this.lexer = lexer;
	}

	/* return true iff no errors (lexical or others) occurred */
	public boolean PrintTokens() {

		// define token object
		Token token = null;
		do {
			try {
				/* get next token from file */
				token = this.lexer.next_token();

				if (token != null) {
					System.out.println(token);
				}

			} catch (LexicalError lex_err) {
				System.out.println(lex_err);
				return false;
			} catch (Exception e) {
				System.out.println("unexpected exception: " + e);
				return false;
			}

		} while (!token.isEOF());

		return true;
	}

	public File CreateTokenFile(String file_path) {

		File tokenFile = FileUtils.OverrideFile(file_path);
		Token token = null;
		try {
			do {

				/* get next token from file */
				token = this.lexer.next_token();
				FileUtils.AppendStringToFile(tokenFile, token.toString());

			} while (!token.isEOF());

		} catch (LexicalError e) {
			FileUtils.AppendStringToFile(tokenFile, e.toString());
			return tokenFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return tokenFile;
	}

}
