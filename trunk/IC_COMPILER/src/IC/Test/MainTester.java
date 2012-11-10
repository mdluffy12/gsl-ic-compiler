/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package IC.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import IC.Parser.Lexer;
import IC.Parser.LexicalError;
import IC.Parser.Token;

public class MainTester {

	private static String tempFilePath = "C:\\files\\temp.txt";
	private static String outputFileFolder = "C:\\files\\";
	private static File outputFile;
	private static String LexicalTestSuffix = ".tokens";
	private static File tempFile;

	public static ArrayList<String> IC_Keyword_List;
	public static ArrayList<String> Legal_ASCII;
	public static ArrayList<String> Illegal_ASCII;

	public enum OutputType {
		standart, file, standart_and_file
	};

	public enum TestType {
		lexical, all;
	};

	public enum LexicalTestType {
		keywords, ascii, files, all;
	};

	public static OutputType ot = OutputType.standart_and_file;
	public static LexicalTestType lexicaltt = LexicalTestType.files;
	public static TestType tt = TestType.lexical;

	public static void main(String[] args) {
		
		initializeTestVars();

		switch (tt) {
		case lexical: {
			RunLexicalMainTester(args);
			break;
		}
		case all: {
			RunLexicalMainTester(args);
			break;
		}
		}

		dumpResources(); 

	}

	public static void RunLexicalMainTester(String[] args) {

		switch (lexicaltt) {
		case keywords: {
			TestKeywords();
			break;
		}
		case ascii: {
			TestAscii();
			break;
		}
		case files: {
			TestFiles(args);
			break;
		}
		case all: {
			TestKeywords();
			TestAscii();
			TestFiles(args);
		}
		}

	}

	private static String getOutputFileName() {
		String dateStr = new Date().toString(), dayStr = dateStr
				.substring(0, 3), MonthStr = dateStr.substring(4, 7), MonthDayStr = dateStr
				.substring(8, 10);
		return "TEST_" + dayStr + "_" + MonthStr + "_" + MonthDayStr;

	}

	private static void initializeTestVars() {

		// initialize text file

		outputFile = new File(outputFileFolder + getOutputFileName() + ".txt");
		outputFile.delete();
		try {
			outputFile.createNewFile();
		} catch (Exception e) {
		}

		tempFile = new File(tempFilePath);

		// build IC keyword list
		IC_Keyword_List = new ArrayList<String>(Arrays.asList("=", ">", "<",
				"!", "==", "<=", ">=", "!=", "&&", "||", "+", "-", "*", "/",
				"%", "(", ")", "{", "}", "[", "]", ";", ".", ",", "class",
				"extends", "static", "void", "if", "else", "while", "break",
				"boolean", "int", "length", "new", "return", "this", "string",
				"false", "true", "null","continue"));

		// build ASCII characters list
		Legal_ASCII = new ArrayList<String>();
		Illegal_ASCII = new ArrayList<String>();

		for (int c = 1; c < 128; c++) {
			char ch = (char) c;
			if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')
					|| (ch >= '0' && ch <= '9'   )  || (ch == '=') || (ch == '>')
					|| (ch == '<')  || (ch == '!')  || (ch == '-') || (ch == '*')
					|| (ch == '/')  || (ch == '%')  || (ch == '(') || (ch == ')')
					|| (ch == '[')  || (ch == ']')  || (ch == '{') || (ch == '}')
					|| (ch == ';')  || (ch == ',')  || (ch == '.') || (ch == ' ')
					|| (ch == '\t') || (ch == '\n') || (ch == '\f')
					|| (ch == '\r') || (ch == '+')) {
				Legal_ASCII.add(String.valueOf(ch));
			} else {
				Illegal_ASCII.add(String.valueOf(ch));
			}

		}

	}

	private static void TestAscii() {

		Tprint("------- testing ASCII... -------\n", ot);

		String AsciiStr;

		Tprint("---- testing legal ASCII... ----\n", ot);

		int i = 0;
		while (i++ < Legal_ASCII.size() - 1) {
			AsciiStr = Legal_ASCII.get(i);
			CreateFileFromString(AsciiStr);

			Tprint("testing charachter : '" + AsciiStr + "'", ot);
			
			if (RunLexicalTest(tempFilePath) == false) {
				Tprint("----- fail! wrong output on char '" + AsciiStr + "'",
						ot);
				return;
			} else {
				Tprint("charachter : '" + AsciiStr + "'" + " OK!", ot);
			}
		}

		i = 0;

		Tprint("---- testing illegal ASCII... ----\n", ot);

		while (i++ < Illegal_ASCII.size() - 1) {

			AsciiStr = Illegal_ASCII.get(i);

			CreateFileFromString(AsciiStr);

			Tprint("testing charachter : '" + AsciiStr + "'", ot);
			
			if (RunLexicalTest(tempFilePath) == true) {
				Tprint("----- fail! wrong output on char '" + AsciiStr + "'",
						ot);
				return;
			} else {
				Tprint("charachter : '" + AsciiStr + "'" + " OK!", ot);
			}
		}

		Tprint("------- ASCII test OK! -------\n", ot);
	}

	private static void TestKeywords() {

		Tprint("------- testing keywords... -------\n", ot);
		int i = 0;
		while (i++ < IC_Keyword_List.size() - 1) {
			String keywordStr = IC_Keyword_List.get(i);
			CreateFileFromString(keywordStr);

			Tprint("testing keyword: \"" + keywordStr + "\"", ot);
			
			if (RunLexicalTest(tempFilePath) == false)
				return;
			else {
				Tprint("keyword: \"" + keywordStr + "\"" + " OK!", ot);
			}
		}

		Tprint("------- keywords test OK! -------\n", ot);

	}

	private static void TestFiles(String[] paths) {

		int counter = 1, success_counter = 0;
		ArrayList<String> incorrectFiles = new ArrayList<String>();
		for (String path : paths) {

			File file = new File(path);
			Tprint("--------- testing " + file.getName() + " ---------" + "("
					+ counter + "/" + paths.length + ")", ot);
			if (TestFile(file) == false) {
				incorrectFiles.add(Integer.toString(counter));		
			}else {
				success_counter++;
			}
			
			counter++;

			Tprint("\n\n", ot);
		}

		if (paths.length > 0 && success_counter == paths.length) {
			Tprint("All files tested successfully!!", ot);
		} else {
			Tprint(success_counter + "/" + paths.length + " files tested successfully",
					ot);
			
			if(incorrectFiles.size() == 1){
				Tprint("check file number : " + incorrectFiles.get(0),ot);
			}else{
			Tprint("check files number : " + incorrectFiles.toString(),ot);
			}
		}

	}

	private static boolean TestFile(File inputfile) {

		String fileDirectory = inputfile.getParent();
		String fileName = inputfile.getName().substring(0,
				inputfile.getName().indexOf("."));

		String CorrectOutputFilePath = fileDirectory + "\\" + fileName
				+ LexicalTestSuffix + ".txt";

		File CorrectOutputFile = new File(CorrectOutputFilePath);
		if (!CorrectOutputFile.exists()) {
			Tprint("no comperable file found for file at "
					+ inputfile.getPath() + "\nskipping file..", ot);
		} else {
			File myOutputFile = BuildTokenFile(inputfile);
			if (myOutputFile != null)
				return CompareFiles(myOutputFile, CorrectOutputFile);
		}

		return false;

	}

	private static boolean CompareFiles(File file1, File file2) {
		Tprint("Comparing " + file1.getName() + " and " + file2.getName(), ot);
		Scanner s1, s2;
		try {
			s1 = new Scanner(file1);
			s2 = new Scanner(file2);
			String st1, st2;
			do {
				st1 = s1.nextLine();
				st2 = s2.nextLine();
				if (st1.equals(st2)) {
					Tprint("\"" + st1 + "\"" + " == " + "\"" + st2 + "\"", ot);
				} else {
					Tprint("\"" + st1 + "\"" + " != " + "\"" + st2 + "\"", ot);
					Tprint(" --- files are different --- ", ot);
					s1.close();
					s2.close();
					return false;
				}

			} while (s1.hasNext() && s2.hasNext());

			if (s1.hasNext() || s2.hasNext()) {
				Tprint(" --- files are different --- ", ot);
				s1.close();
				s2.close();
				return false;
			}

			s1.close();
			s2.close();
		} catch (Exception e) {
		}

		Tprint("\n --- " + "the files " + file1.getName() + " and "
				+ file2.getName() + " are equal!! --- \n", ot);
		return true;
	}

	// given a string, creates a new file at file_path with the input str
	// if file already exists, overrides it
	private static void CreateFileFromString(String str) {
		try {
			BufferedWriter out = new BufferedWriter(
					new FileWriter(tempFilePath));
			out.write(str);
			out.close();
		} catch (IOException e) {

		}

	}

	private static boolean RunLexicalTest(String file_path) {
		return IC.Compiler.ExecuteLexicalAnalysis(file_path);
	}

	private static void AppendStringToFile(File file, String data) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(file, true)));
			out.println(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void Tprint(String str, OutputType output_type) {
		switch (output_type) {
		case standart: {
			System.out.println(str);
		}
		case file: {
			AppendStringToFile(outputFile, str);
		}
		case standart_and_file: {
			System.out.println(str);
			AppendStringToFile(outputFile, str);
		}
		}
	}

	private static File BuildTokenFile(File file) {
		String fileDirectory = file.getParent();
		String fileName = file.getName().substring(0,
				file.getName().indexOf("."));

		File tokenFile = new File(fileDirectory + "\\" + fileName
				+ ".mytokens.txt");

		tokenFile.delete();
		try {
			tokenFile.createNewFile();
		} catch (Exception e) {
		}

		// define lexer object
		Lexer lexer = null;
		Token token = null;
		try {

			tokenFile.delete();
			tokenFile.createNewFile();

			// create input stream for reading character files
			FileReader reader = new FileReader(file.getAbsolutePath());

			/* create a new lexer object with our file_path from argument list */
			lexer = new Lexer(reader);
			do {

				/* get next token from file */
				token = lexer.next_token();
				AppendStringToFile(tokenFile, token.toString());

			} while (!token.isEOF());

		} catch (LexicalError e) {
			AppendStringToFile(tokenFile, e.toString());
			return tokenFile;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return tokenFile;

	}

	private static void dumpResources() {
		tempFile.delete();
	}

}
