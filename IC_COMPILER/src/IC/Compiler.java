/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC;

import java.io.FileNotFoundException;

import IC.AST.ICClass;
import IC.Parser.Debugger;
import IC.Parser.GenLexer;
import IC.Parser.GenParser;
import IC.Parser.LibraryUtils;

/**
 * This class takes a filename as an argument (.ic file) , it reads that file
 * breaks it into tokens, and successively calls the 'next token' method of the
 * generated scanner to print a representation of the file as a series of tokens
 * to the standard output, one token per line. the function also receives a
 * library path name as an argument, which
 */

public class Compiler {

	/* global variables */
	private static String debugFilePathDirectory = "C:\\files\\";
	public static GenLexer fileLexer = null;
	public static GenLexer libLexer = null;
	public static GenParser parser = null;
	public static GenParser libParser = null;

	/* this is the main function of our compiler */
	public static void main(String[] args) {

		/* check argument input */
		if (args.length == 0) {
			System.out.println("error - no argument given as input");
		} else if (args.length > 2) {
			System.out.println("error - too many arguments given as input");
		} else {

			String libFile_path = null;

			// initial debugger instance
			initDebugger();

			if (args.length == 2) {

				// get library path from input
				libFile_path = LibraryUtils.GetLibraryPath(args[1]);
			}

			if (ExecuteLexicalAnalysis(args[0], libFile_path) == true) {
				// run syntax analyze
				ExecuteSyntaxAnalyzer(args[0], libFile_path);
			}
		}
	}

	/**
	 * Creates an instance of debugger to be used later
	 * 
	 */
	private static void initDebugger() {
		Debugger.initDebugger(debugFilePathDirectory,
				Debugger.DebugPrintPref.none);
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

		if (library_path != null) {
			libRoot = LibraryUtils.ParseLibrary(libLexer.getLexer());
			if (libRoot == null)
				return false;

			// LibraryUtils.PrintLibraryAST(libroot, library_path); // for debug
		}

		parser = new GenParser(fileLexer.getLexer(), file_path, libRoot);
		if (parser.ExecuteParser() == false)
			return false;

		parser.AddLibraryAsClass();
		parser.PrintAST();
		parser.PrintAST("C:\\files\\Quicksort.ic.myast.txt");
		return true;

	}
}
