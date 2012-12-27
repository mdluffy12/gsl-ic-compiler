package Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import java_cup.runtime.Symbol;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.Parser.Lexer;
import IC.Parser.LexicalError;
import IC.Parser.Parser;
import IC.Parser.SyntaxError;

public class SyntaxTester extends GenTester {

	/* global variables */
	private static String myASTfilePath = null;

	private static String syntaxSuffix;
	private static String mySyntaxSuffix;
	private final String testedFolder;
	private final boolean printTrees;

	public SyntaxTester(String testedFolder, String mySyntaxSuffix,
			String syntaxSuffix, boolean printTrees) {
		super(GenTester.OutputType.all);
		this.testedFolder = testedFolder;
		SyntaxTester.syntaxSuffix = syntaxSuffix;
		SyntaxTester.mySyntaxSuffix = mySyntaxSuffix;
		this.printTrees = printTrees;
		setOutputFile(CreateSyntaxOutputFile());
	}

	public void ExecuteSyntaxTest() {

		List<File> files = new ArrayList<File>();
		int success_counter = 0;
		CreateFileList(new File(testedFolder), files);

		for (File file : files) {
			ParseFile(file);
		}

		Tprint("\n-------- executing AST file comperator ----------\n", ot);

		int counter = 1;
		for (File file : files) {

			Tprint("--------- testing " + file.getName() + " ---------" + "("
					+ counter + "/" + files.size() + ")", ot);

			if (TestFileSyntax(file) == true) {
				success_counter++;
			}

			counter++;

			Tprint("\n\n", ot);
		}

		if (files.size() > 0 && success_counter == files.size()) {
			Tprint("All files tested successfully!!", ot);
		} else {
			Tprint(success_counter + "/" + files.size()
					+ " files tested successfully", ot);

		}

	}

	// creates the file list data structure, a map enumerating all the files in
	// the folder, and their folder path
	private static void CreateFileList(File path, List<File> fileList) {

		File files[];

		// get all files listed in current folder
		files = path.listFiles();

		Arrays.sort(files);
		for (int i = 0, n = files.length; i < n; i++) {

			if (isLegalForParcing(files[i]) && !files[i].isDirectory())
				fileList.add(files[i]);
			if (files[i].isDirectory()) {
				CreateFileList(files[i], fileList);
			}
		}
	}

	private static boolean isLegalForParcing(File file) {
		String fileName = file.getName();
		if (Contains(".tokens", fileName) || Contains(".mytokens", fileName)
				|| Contains("TEST_", fileName) || Contains(".myast", fileName)
				|| Contains(".ast", fileName) || Contains("DEBUG_", fileName)
				|| Contains(syntaxSuffix, fileName)
				|| Contains(mySyntaxSuffix, fileName)
				|| Contains("graph_", fileName) || Contains(".png", fileName))
			return false;

		return true;
	}

	private void ParseFile(File file) {

		String inputFilePath = file.getAbsolutePath();
		String fileName = file.getName();
		try {
			String outputFilePath = file.getCanonicalPath() + mySyntaxSuffix;
			Parser p = new Parser(new Lexer(new FileReader(inputFilePath)));

			Symbol parseSymbol = p.parse();
			Program root = (Program) parseSymbol.value;

			PrettyPrinter printer = new PrettyPrinter(file.getName());
			CreateASToutputFile(file, root.accept(printer).toString());

			if (printTrees) {
				PrintTree(root, file);
			}

		} catch (LexicalError lexErr) {
			Tprint("error in: " + fileName + "\n" + lexErr.toString(), ot);
		} catch (SyntaxError synErr) {
			Tprint("error in: " + fileName + "\n" + synErr.toString(), ot);

		} catch (Exception e) {
			Tprint("parsing  of file:  " + fileName + " failed  !", ot);
		}

	}

	private void CreateASToutputFile(File inputFile, String astString) {
		String fileDirectory = null;
		String path = inputFile.getAbsolutePath();
		int lastDef = path.lastIndexOf("\\");
		fileDirectory = path.substring(0, lastDef + 1);
		myASTfilePath = fileDirectory + inputFile.getName() + mySyntaxSuffix;

		String parsedSuccess = "Parsed " + inputFile.getName()
				+ " successfully!";
		File myASTfile = OverrideFile(myASTfilePath);
		AppendStringToFile(myASTfile, parsedSuccess);
		AppendStringToFile(myASTfile, astString);

	}

	private boolean TestFileSyntax(File inputfile) {
		String fileDirectory = null;
		try {
			fileDirectory = inputfile.getCanonicalPath();
			myASTfilePath = fileDirectory + mySyntaxSuffix;
		} catch (IOException e) {
		}

		String ASTfilePath = fileDirectory + syntaxSuffix;

		File ASTfile = new File(ASTfilePath);
		if (!ASTfile.exists()) {
			Tprint("no comperable file found for file at " + ASTfilePath
					+ "\nskipping file..", ot);
		} else {
			return CompareFiles(new File(myASTfilePath), ASTfile);
		}

		return false;
	}

	private String getOutputFileName() {
		String dateStr = new Date().toString(), dayStr = dateStr
				.substring(0, 3), MonthStr = dateStr.substring(4, 7), MonthDayStr = dateStr
				.substring(8, 10);
		return "SYNTAX_TEST_" + dayStr + "_" + MonthStr + "_" + MonthDayStr;

	}

	private File CreateSyntaxOutputFile() {
		String outputPath = testedFolder + "\\" + getOutputFileName() + ".txt";
		return OverrideFile(outputPath);
	}

	private static File OverrideFile(String file_path) {
		File file = new File(file_path);
		file.delete();
		try {
			file.createNewFile();
		} catch (Exception e) {
		}

		return file;
	}

	private void PrintTree(Program root, File inputFile) {

		try {

			String directory = null, fileName = null;
			try {
				String canPath = inputFile.getCanonicalPath();
				int lastDef = canPath.lastIndexOf('\\');
				directory = canPath.substring(0, lastDef + 1);

				fileName = inputFile.getName();
			} catch (Exception e) {
				return;
			}

			GraphBuilder gBuilder = new GraphBuilder();

			gBuilder.initStart();
			gBuilder.Build(root);
			gBuilder.initEnd();

			String fixed_fileName = fileName
					.substring(0, fileName.indexOf('.'));
			gBuilder.CreateImg(directory, fixed_fileName);

		} catch (Exception e) {

		}

	}

	public static void DeleteAllFilesWithSuffix(String testedFolder,
			String suffix) {
		List<File> files = new ArrayList<File>();
		CreateFileList(new File(testedFolder), files);

		try {
			for (File file : files) {
				if (Contains(suffix, file.getAbsolutePath())) {
					file.delete();
				}
			}

		} catch (Exception e) {

		}
	}

}
