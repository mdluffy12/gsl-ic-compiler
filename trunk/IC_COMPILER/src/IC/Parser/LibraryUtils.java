/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC.Parser;

import java_cup.runtime.Symbol;
import IC.AST.ICClass;
import IC.AST.PrettyPrinter;

/**
 * This class is a utility class for the library file
 */

public class LibraryUtils {

	/* global variables */
	public static String libPrefix = "-L<";
	public static String libSuffix = ".sig>";

	public static boolean isLibraryPathLegal(String LibraryPath) {

		String libPref = LibraryPath.substring(0, libPrefix.length()), libSuff = LibraryPath
				.substring(LibraryPath.length() - libSuffix.length(),
						LibraryPath.length());

		return libPref.equals(libPrefix) && libSuff.equals(libSuffix);

	}

	public static ICClass ParseLibrary(Lexer lexer) {

		LibraryParser libParser = new LibraryParser(lexer);

		Symbol libParseSym = null;
		try {
			libParseSym = libParser.parse();
		} catch (Exception e) {
			System.out.println(e);
		}

		return (ICClass) libParseSym.value;

	}

	public static String GetLibraryPath(String library_path) {
		if (isLibraryPathLegal(library_path)) {
			return library_path.substring(3, library_path.length() - 1);
		} else {
			System.out.println("library path is illagel");
		}

		return null;
	}

	public static void PrintLibraryAST(ICClass libroot, String libFilePath) {

		PrettyPrinter printer = new PrettyPrinter(libFilePath);
		System.out.println(libroot.accept(printer));

	}
}
