/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.Parser;

import java.io.File;

import java_cup.runtime.Symbol;
import IC.AST.ICClass;
import IC.AST.PrettyPrinter;

/**
 * This class is a utility class for the library file
 */

public class LibraryUtils {

	/* global variables */
	public static String libPrefix = "-L";
	public static File libraryFile = null;

	public void setLibraryFile(File file) {
		libraryFile = file;
	}

	public static boolean isLibraryPathLegal(String LibraryPath) {
		return LibraryPath.startsWith(libPrefix);
	}

	public static ICClass ParseLibrary(Lexer lexer) {

		LibraryParser libParser = new LibraryParser(lexer);

		Symbol libParseSym = null;
		try {

			libParseSym = libParser.parse();
			System.out.println("Parsed " + libraryFile.getName()
					+ " successfully!");
		} catch (LexicalError e) {
			System.out.println(e);
			return null;
		} catch (SyntaxError e) {
			System.out.println(e);
			return null;
		} catch (Exception e) {
			System.out.println("unexpected exception: " + e);
			return null;
		}

		return (ICClass) libParseSym.value;

	}

	public static String GetLibraryPath(String library_path) {

		String fixedLibPath = library_path;
		fixedLibPath = fixedLibPath.replace(libPrefix, "");
		libraryFile = new File(fixedLibPath);

		return fixedLibPath;

	}

	public static void PrintLibraryAST(ICClass libroot, String libFilePath) {

		PrettyPrinter printer = new PrettyPrinter(libFilePath);
		try {
			System.out.println(libroot.accept(printer));
		} catch (SemanticError e) {
			//e.printStackTrace();
		}

	}
}
