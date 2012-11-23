/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
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

	private static Debugger debugger = null;
	private final static String debugDefaultDirectory = "C:\\files\\";
	private final String debugFileDirectory;
	private final File debugFile;
	private final DebugPrintPref printPref;

	public enum DebugPrintPref {
		none, out, file, all
	}

	/**
	 * default Singleton Debugger initiation
	 * 
	 * @param debugFileDirectory
	 *            the directory where the debug file will be created in
	 * 
	 */
	public static void initDebugger() {

		if (debugger == null) {
			debugger = new Debugger(debugDefaultDirectory);
		}

	}

	/**
	 * Singleton Debugger initiation
	 * 
	 * @param debugFileDirectory
	 *            the directory where the debug file will be created in
	 * 
	 */
	public static void initDebugger(String debugFileDirectory) {

		if (debugger == null)
			debugger = new Debugger(debugFileDirectory);

	}

	/**
	 * Singleton Debugger initiation
	 * 
	 * @param debugFileDirectory
	 *            the directory where the debug file will be created in
	 * @param printPref
	 *            debugger print preference
	 */
	public static void initDebugger(String debugFileDirectory,
			DebugPrintPref printPref) {

		if (debugger == null)
			debugger = new Debugger(debugFileDirectory, printPref);

	}

	public static Debugger getDebuggerInstance() {
		if (debugger == null) {
			initDebugger();
		}

		return debugger;
	}

	protected Debugger(String debugFileDirectory) {
		this.debugFileDirectory = debugFileDirectory;
		this.debugFile = CreateNewDebugFile();
		this.printPref = DebugPrintPref.out; // default
	}

	protected Debugger(String debugFileDirectory, DebugPrintPref printPref) {
		this.debugFileDirectory = debugFileDirectory;
		this.debugFile = CreateNewDebugFile();
		this.printPref = printPref;
	}

	public void Print(String data) {

		switch (this.printPref) {
		case out: {
			System.out.println(data);
			break;
		}
		case file: {
			FileUtils.AppendStringToFile(this.debugFile, data);
			break;
		}
		case all: {
			System.out.println(data);
			FileUtils.AppendStringToFile(this.debugFile, data);
			break;
		}
		default:
			break;
		}
	}

	private File CreateNewDebugFile() {

		String file_path;
		String dateStr = new Date().toString(), dayStr = dateStr
				.substring(0, 3), MonthStr = dateStr.substring(4, 7), MonthDayStr = dateStr
				.substring(8, 10);
		file_path = this.debugFileDirectory + "\\" + "DEBUG_" + dayStr + "_"
				+ MonthStr + "_" + MonthDayStr + ".txt";
		File f = FileUtils.OverrideFile(file_path);

		return f;
	}

}
