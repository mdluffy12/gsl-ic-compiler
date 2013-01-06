/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC;

import java.io.FileNotFoundException;

import IC.AST.ICClass;
import IC.Parser.Debugger;
import IC.Parser.GenLexer;
import IC.Parser.GenParser;
import IC.Parser.LibraryUtils;
import IC.Parser.SemanticError;
import SymbolTable.SymbolTable;
import Visitors.SemanticChecks;
import Visitors.SymTableConstructor;
import Visitors.SymTableUtils;

/**
 * This class takes a filename as an argument (.ic file) , it reads that file
 * breaks it into tokens, and successively calls the 'next token' method of the
 * generated scanner to print a representation of the file as a series of tokens
 * to the standard output, one token per line. the function also receives a
 * library path name as an argument, which
 */

public class Compiler {

	/* global variables */
	private static String debugFilePathDirectory = "C:\\filesdd\\";
	public static GenLexer fileLexer = null;
	public static GenLexer libLexer = null;
	public static GenParser parser = null;
	public static GenParser libParser = null;
	public static boolean printAST = false;

	/* this is the main function of our compiler */
	public static void main(String[] args) {

		/* check argument input */
		if (args.length == 0) {
			System.out.println("error - no argument given as input");
		} else {

			String libraryPath = null;

			// initial debugger instance
			// initDebugger();

			if (args.length >= 2) {

				for (String arg : args) {
					if (arg.startsWith("-L")) {
						libraryPath = arg;
					}

					if (arg.equals("-print-ast")) {
						printAST = true;
					}
				}

				// get library path from input libraryPath
				if (libraryPath != null) {
					libraryPath = LibraryUtils.GetLibraryPath(libraryPath);
				}
			}

			if (ExecuteLexicalAnalysis(args[0], libraryPath) == true) {
				// run syntax analyze
				if (ExecuteSyntaxAnalyzer(args[0], libraryPath) == true) {
					ExecuteSemanticAnalyzer(args[0]);
				}
			}

		}
	}

	/**
	 * Initializes debug preferences
	 * 
	 */
	private static void initDebugger() {
		Debugger.initDebugger(true, Debugger.DebugPrintPref.all,
				debugFilePathDirectory);
	}

	/**
	 * Executes Full Lexical Analysis of the file
	 * 
	 * @param file_path
	 *            the path of the file which will be analyzed
	 * @param libFile_path
	 *            (optional) the path of the library file which will be analyzed
	 * 
	 *            returns true iff there are no lexical errors in the file(s)
	 */
	public static boolean ExecuteLexicalAnalysis(String file_path,
			String libFile_path) {

		try {
			fileLexer = new GenLexer(file_path);
		} catch (FileNotFoundException e) {
			System.out.println("file not found at : " + file_path);
			return false;
		}

		if (libFile_path != null) {
			try {
				libLexer = new GenLexer(libFile_path);
			} catch (FileNotFoundException e) {
				System.out.println("file not found at : " + file_path);
				return false;
			}
		}

		return true;
	}

	/**
	 * Executes syntax Analysis of the file
	 * 
	 * the function takes a CUP program - basically an LALR(1) parsable grammar,
	 * and generates a Java program that will parse input that satisfies that
	 * grammar.
	 * 
	 * 
	 * @param file_path
	 *            the path of the file which will be analyzed
	 * 
	 * @param library_path
	 *            (optional) the path of the library path to be parsed (null if
	 *            does not exist)
	 * 
	 *            returns true iff there are no syntax errors in the file(s)
	 */
	public static boolean ExecuteSyntaxAnalyzer(String file_path,
			String library_path) {

		ICClass libRoot = null;

		// parse library and get library root
		if (library_path != null) {
			libRoot = LibraryUtils.ParseLibrary(libLexer.getLexer());
			if (libRoot == null)
				return false;

			// LibraryUtils.PrintLibraryAST(libroot, library_path); // for debug
		}

		// parse .ic file
		parser = new GenParser(fileLexer.getLexer(), file_path, libRoot);
		if (parser.ExecuteParser() == false)
			return false;

		// add library as a ligit class of the .ic file to be parsed
		parser.AddLibraryAsClass(); // TBD: needed for print or not?

		// print AST representation
		if (printAST)
			parser.PrintAST();

		return true;

	}

	/**
	 * Executes semantic Analysis of the file
	 * 
	 */
	public static boolean ExecuteSemanticAnalyzer(String file_path) {

		SymTableConstructor symBuilder = new SymTableConstructor(file_path);
		 
		SemanticChecks semanticChecks = new SemanticChecks();
		try {
			SymbolTable globalTable = (SymbolTable) parser.getRoot().accept(
					symBuilder);

			parser.getRoot().accept(semanticChecks);
 
			SymTableUtils.printTable(globalTable);

		} catch (SemanticError e) {
			System.out.println(e);
			return false;
		}

		return true;

	}
}
