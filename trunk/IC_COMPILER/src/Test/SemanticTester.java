package Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java_cup.runtime.Symbol;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.Parser.FileUtils;
import IC.Parser.Lexer;
import IC.Parser.LexicalError;
import IC.Parser.Parser;
import IC.Parser.SemanticError;
import IC.Parser.SyntaxError;
import SymbolTable.SymbolTable;
import Visitors.SemanticChecks;
import Visitors.SymTableConstructor;

public class SemanticTester extends GenTester {

	/* global variables */
	private static String filePath = null;

	private static String syffix;
	private static String mySuffix;
	private final String testedFolder;

	public SemanticTester(String testedFolder, String mySyntaxSuffix,
			String syntaxSuffix) {
		super(GenTester.OutputType.all);
		this.testedFolder = testedFolder;
		SemanticTester.syffix = syntaxSuffix;
		SemanticTester.mySuffix = mySyntaxSuffix;
		// setOutputFile(CreateOutputFile());
	}

	public void executeSemanticTester() {
		List<File> files = new ArrayList<File>();

		CreateFileList(new File(testedFolder), files, "ic");

		for (File file : files) {
			createSymbolTableFile(file);
		}

	}

	private void createSymbolTableFile(File file) {
		String inputFilePath = file.getAbsolutePath();

		try {
			String outputFilePath = file.getCanonicalPath() + mySuffix;

			FileUtils.OverrideFile(outputFilePath);

			this.setOutputFile(new File(outputFilePath));

			Parser p = new Parser(new Lexer(new FileReader(inputFilePath)));

			Symbol parseSymbol = p.parse();
			Program root = (Program) parseSymbol.value;

			SymTableConstructor symBuilder = new SymTableConstructor(
					inputFilePath);

			SemanticChecks semanticChecks = new SemanticChecks();

			SymbolTable rootTable = (SymbolTable) root.accept(symBuilder);

			// parser.getRoot().accept(semanticChecks);

			Tprint("\n", ot);
			Tprint(rootTable.toString(), ot);

		} catch (LexicalError lexicalErr) {
			Tprint(lexicalErr.toString(), ot);
		} catch (SyntaxError syntaxErr) {
			Tprint(syntaxErr.toString(), ot);
		} catch (SemanticError semanticErr) {
			Tprint(semanticErr.toString(), ot);
		} catch (Exception e) {
			System.out.println("error creating table" + e.toString());
		}

	}

	public void executeSemanticComperator() {

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

			if (TestFile(file) == true) {
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

	private void ParseFile(File file) {

		String inputFilePath = file.getAbsolutePath();
		String fileName = file.getName();
		try {
			String outputFilePath = file.getCanonicalPath() + mySuffix;
			Parser p = new Parser(new Lexer(new FileReader(inputFilePath)));

			Symbol parseSymbol = p.parse();
			Program root = (Program) parseSymbol.value;

			PrettyPrinter printer = new PrettyPrinter(file.getName());
			CreateASToutputFile(file, root.accept(printer).toString());

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
		filePath = fileDirectory + inputFile.getName() + mySuffix;

		String parsedSuccess = "Parsed " + inputFile.getName()
				+ " successfully!";
		File myASTfile = OverrideFile(filePath);
		AppendStringToFile(myASTfile, parsedSuccess);
		AppendStringToFile(myASTfile, astString);

	}

	private boolean TestFile(File inputfile) {
		String fileDirectory = null;
		try {
			fileDirectory = inputfile.getCanonicalPath();
			filePath = fileDirectory + mySuffix;
		} catch (IOException e) {
		}

		String ASTfilePath = fileDirectory + syffix;

		File ASTfile = new File(ASTfilePath);
		if (!ASTfile.exists()) {
			Tprint("no comperable file found for file at " + ASTfilePath
					+ "\nskipping file..", ot);
		} else {
			return CompareFiles(new File(filePath), ASTfile);
		}

		return false;
	}

	private String getOutputFileName() {
		String dateStr = new Date().toString(), dayStr = dateStr
				.substring(0, 3), MonthStr = dateStr.substring(4, 7), MonthDayStr = dateStr
				.substring(8, 10);
		return "Semantic_Test_Summary" + dayStr + "_" + MonthStr + "_"
				+ MonthDayStr;

	}

	private File CreateOutputFile() {
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

}