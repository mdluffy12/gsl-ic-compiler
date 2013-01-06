/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.Parser;

import java.io.File;
import java.util.ArrayList;

import java_cup.runtime.Symbol;
import IC.AST.ASTNode;
import IC.AST.ICClass;
import IC.AST.PrettyPrinter;
import IC.AST.Program;

public class GenParser {

	private final String file_path;
	private File file;
	private Symbol parseSymbol;
	private final Parser parser;
	private Program root;
	private final ICClass libRoot;

	/**
	 * GenParser Constructor
	 * 
	 * @param lexer
	 *            scanner which will be used in the file parsing (to break file
	 *            into tokens and thus defining all the grammer terminals)
	 * 
	 * @param file_path
	 *            path of the file to be parsed
	 * 
	 * @param libRoot
	 *            ICClass instance of the AST root of the library
	 */
	public GenParser(Lexer lexer, String file_path, ICClass libRoot) {

		this.parser = new Parser(lexer);
		this.file_path = file_path;
		this.libRoot = libRoot;
	}

	/**
	 * Prints AST Description
	 * 
	 * will print at System.out a textual description of the constructed AST.
	 * Each AST node will be printed on a separate line, and will have
	 * information about what the children nodes are.
	 * 
	 */
	public void PrintAST() {

		PrettyPrinter printer = new PrettyPrinter(file.getName());

		try {
			System.out.println(root.accept(printer));
		} catch (SemanticError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Prints AST Description to file
	 * 
	 * @ param file output file path to be written the AST to
	 * 
	 */
	public void PrintAST(String ouputfilePath) {

		File file = FileUtils.OverrideFile(ouputfilePath);

		PrettyPrinter printer = new PrettyPrinter(file.getName());

		/*
		 * FileUtils.AppendStringToFile(file, "Parsed " + file.getName() +
		 * " successfully!");
		 * 
		 * FileUtils.AppendStringToFile(file, (String) root.accept(printer));
		 */
	}

	/**
	 * Executes the parser
	 * 
	 * executes the parser and return the parser value iff parsing is successful
	 * otherwise, returns null.
	 * 
	 */
	public boolean ExecuteParser() {

		file = new File(file_path);

		try {
			parseSymbol = parser.parse();

			System.out.println("Parsed " + file.getName() + " successfully!");
			root = (Program) parseSymbol.value;
		} catch (LexicalError e) {
			System.out.println(e);
			return false;
		} catch (SyntaxError e) {
			System.out.println(e);
			return false;
		} catch (Exception e) {
			System.out.println("unexpected exception : " + e);
			return false;
		}
		return true;
	}

	public Parser getParser() {
		return this.parser;
	}

	public ASTNode getRoot() {

		return this.root;
	}

	public void AddLibraryAsClass() {

		// check for null just in case
		if (libRoot != null) {
			ArrayList<ICClass> programClasses = new ArrayList<ICClass>();
			programClasses.add(libRoot);
			programClasses.addAll(root.getClasses());
			root.setClasses(programClasses);
		}
	}

}
