/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.Parser;

import java.io.File;
import java.util.Date;

/**
 * Debugger is used for debugging the program, it creates a single instance of
 * debugger on program startup and then uses it to print to stdout, file or no
 * print at all.
 * 
 */

public class Debugger {

	private static String debugDefaultDirectory = "C:\\files\\";
	private static String debugFileDirectory;
	private static File debugFile;
	private static DebugPrintPref printPref;
	public static boolean debugMode;

	public enum DebugPrintPref {
		none, out, file, all
	}

	/**
	 * default Debugger initiation
	 * 
	 * @param debugFileDirectory
	 *            the directory where the debug file will be created in
	 * 
	 */
	public static void initDebugger(boolean debugModeValue) {

		debugMode = debugModeValue;
		debugFileDirectory = debugDefaultDirectory;

		if (debugMode) {
			printPref = DebugPrintPref.all;
			Debugger.debugFile = CreateNewDebugFile();
		} else {
			printPref = DebugPrintPref.none;
		}

	}

	/**
	 * default Debugger initiation
	 * 
	 * @param debugFileDirectory
	 *            the directory where the debug file will be created in
	 * 
	 */
	public static void initDebugger(boolean debugModeValue,
			DebugPrintPref printPreference) {

		debugMode = debugModeValue;
		debugFileDirectory = debugDefaultDirectory;

		if (debugMode) {
			printPref = printPreference;
			Debugger.debugFile = CreateNewDebugFile();
		} else {
			printPref = DebugPrintPref.none;
		}

	}

	/**
	 * default Debugger initiation
	 * 
	 * @param debugFileDirectory
	 *            the directory where the debug file will be created in
	 * 
	 */
	public static void initDebugger(boolean debugModeValue,
			DebugPrintPref printPreference, String folder) {

		initDebugger(debugModeValue, printPreference);
		setDebuggerFolder(folder);
	}

	/**
	 * Debugger initiation
	 * 
	 * @param debugFileDirectory
	 *            the directory where the debug file will be created in
	 * 
	 */
	public static void setDebuggerFolder(String folder) {

		debugFileDirectory = folder;
	}

	public static void Print(String data) {

		switch (Debugger.printPref) {
		case none: {
			break;
		}

		case out: {
			System.out.println(data);
			break;
		}
		case file: {
			FileUtils.AppendStringToFile(Debugger.debugFile, data);
			break;
		}
		case all: {
			System.out.println(data);
			FileUtils.AppendStringToFile(Debugger.debugFile, data);
			break;
		}
		default:
			break;
		}
	}

	private static File CreateNewDebugFile() {

		String file_path;
		String dateStr = new Date().toString(), dayStr = dateStr
				.substring(0, 3), MonthStr = dateStr.substring(4, 7), MonthDayStr = dateStr
				.substring(8, 10);
		file_path = Debugger.debugFileDirectory + "\\" + "DEBUG_" + dayStr
				+ "_" + MonthStr + "_" + MonthDayStr + ".txt";
		File f = FileUtils.OverrideFile(file_path);

		return f;
	}

}
