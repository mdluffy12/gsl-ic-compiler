/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import java_cup.runtime.Symbol;
import IC.AST.ICClass;
import IC.AST.PrettyPrinter;
import IC.AST.Program;
import IC.Parser.FileUtils;
import IC.Parser.Lexer;
import IC.Parser.LexicalError;
import IC.Parser.LibraryParser;
import IC.Parser.Parser;
import IC.Parser.SemanticError;
import IC.Parser.SyntaxError;
import SymbolTable.ISymbolTableOperations;
import SymbolTable.SymbolTable;
import Types.TypeTable;
import Visitors.SemanticChecks;
import Visitors.SymTableConstructor;
import Visitors.SymTableUtils;
import Visitors.TypeChecker;
import Visitors.TypeEvaluator;

public class SemanticTester extends GenTester {

	/* global variables */
	private static String filePath = null;
	private static File listerFile = null;
	private static String syffix;
	private static String mySuffix;
	private final String testedFolder;
	private final boolean withLib;
	private final String libPath;
	private static int numOfFiles = 0;
	private static int errCounter = 0;
	
	public SemanticTester(String testedFolder, String mySyntaxSuffix,
			String syntaxSuffix,boolean withLib) {
		super(GenTester.OutputType.file);
		this.testedFolder = testedFolder;
		listerFile = new File(testedFolder + File.separator + "Test_Files.txt");
		SemanticTester.syffix = syntaxSuffix;
		SemanticTester.mySuffix = mySyntaxSuffix;
		// setOutputFile(CreateOutputFile());
		this.withLib = withLib;
		libPath = System.getProperty("user.dir") + File.separator + "libic.sig";
	}

	public void executeSemanticTester() throws Exception {
		List<File> files = new ArrayList<File>();

		CreateFileList(new File(testedFolder), files, "ic");

		numOfFiles = files.size();
		
		for (File file : files) {
			createSymbolTableFile(file);
		}
		
		
		System.out.println("found compilation errors in : " + errCounter + "/" + numOfFiles + " files");

	}

	private void createSymbolTableFile(File file) throws Exception {
		String inputFilePath = file.getAbsolutePath();
		String errFilePath = null;
		String errStr = null;
		String outputFilePath = null;
		
		
		
		if(file.getName().equals("IlegalDec.ic")){
			outputFilePath = outputFilePath + "";
		}
		try {
			
			StringBuilder sb = new StringBuilder();
			
			outputFilePath = file.getCanonicalPath() + mySuffix;
			errFilePath = file.getCanonicalPath() + ".err.txt";

			this.setOutputFile(new File(outputFilePath));

			Parser p = new Parser(new Lexer(new FileReader(inputFilePath)));

			Symbol parseSymbol = p.parse();
			
			Program root = (Program) parseSymbol.value;

			//PrettyPrinter astPrinter = new PrettyPrinter(new File(inputFilePath).getName());
			//sb.append(root.accept(astPrinter));
			
			
			if(withLib){
				LibraryParser libParser = new LibraryParser(new Lexer(new FileReader(libPath)));
				Symbol libParseSymbol = libParser.parse();
				ICClass libRoot = (ICClass) libParseSymbol.value;
				
				ArrayList<ICClass> programClasses = new ArrayList<ICClass>();
				programClasses.add(libRoot);
				programClasses.addAll(root.getClasses());
				root.setClasses(programClasses);
			}
			
		 
			SymTableConstructor symBuilder = new SymTableConstructor(inputFilePath);
			 
			SemanticChecks semanticChecks = new SemanticChecks();
			
			ISymbolTableOperations symUtils = new SymTableUtils();
			
			TypeEvaluator typeEvaluator = new TypeEvaluator(symUtils);
			TypeChecker typeChecks = new TypeChecker(typeEvaluator);
			
			 
				TypeTable.initialize(root, inputFilePath);
				
				SymbolTable globalTable = (SymbolTable) root.accept(
						symBuilder);
				
				sb.append(globalTable.toString());

				root.accept(typeChecks);
				root.accept(semanticChecks);

				String typeTableStr = TypeTable.asString();
				sb.append(typeTableStr.substring(0, typeTableStr.length()-1));

			FileUtils.OverrideFile(outputFilePath);

			Tprint("\n", ot);
			Tprint(sb.toString(), ot);

		} catch (LexicalError lexicalErr) {
			errStr = lexicalErr.toString();
		    
		} catch (SyntaxError syntaxErr) {
			errStr = syntaxErr.toString();
	 
		} catch (SemanticError semanticErr) {
			errStr = semanticErr.toString();
	 
		} catch (Exception e) {
			e.printStackTrace();
			deleteOutputFiles(testedFolder);
			throw new Exception("error on file: " + file.getName());
		} finally {
			this.setOutputFile(new File(errFilePath));
			if (errStr != null && errStr.length() > 0) {
				errCounter++;
				int prevLastIndex = errFilePath.lastIndexOf(File.separatorChar);
				String prefixStr = errFilePath.substring(0,prevLastIndex-1);
				prevLastIndex = prefixStr.lastIndexOf(File.separatorChar);
				
				System.out.println("compilation error in : " + errFilePath.substring(prevLastIndex+1,errFilePath.length()));
				Tprint(errStr + "\n", OutputType.all);
			}
			
			if(errFilePath != null){
				listFile(errFilePath);
			}
			
			if(outputFilePath != null){
				listFile(outputFilePath);
			}
			 
		

		}

	}

	private void listFile(String filePath) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(listerFile, true)));
			out.println(filePath);
			out.close();
		} catch (Exception e) {
		 
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
		int lastDef = path.lastIndexOf(File.separator);
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
		String outputPath = testedFolder + File.separator + getOutputFileName() + ".txt";
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
				int lastDef = canPath.lastIndexOf(File.separator);
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
	
	public static void deleteOutputFiles(String testedFolder) {
		File listerFile = new File(testedFolder + File.separator + "Test_Files.txt");
		String filePath = null;
		if (!listerFile.exists()) {
			return;
		}

		try {
			@SuppressWarnings("resource")
			Scanner s = new Scanner(new FileReader(listerFile));
			while (s.hasNextLine()) {

				// read line
				filePath = s.nextLine();

				try {
					new File(filePath).delete();
				} catch (Exception e) {
				}

			}
		 
			listerFile.delete();
		} catch (Exception e) {
	
		}
		
		

	}

}
