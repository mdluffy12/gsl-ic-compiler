package Test;

import java.io.FileNotFoundException;

import IC.AST.Program;
import IC.Parser.GenLexer;
import IC.Parser.GenParser;

public class GraphTester {
	public static void main(String[] args) {
		String file_path = "C:\\Users\\micha\\workspace\\IC_COMPILER\\src\\test.txt";

		file_path = "C:\\files\\parser_tests\\function tests\\the fantasic eight.ic";

		GenLexer fileLexer = null;
		try {
			fileLexer = new GenLexer(file_path);
		} catch (FileNotFoundException e) {
			System.out.println("file not found at : " + file_path);
			return;
		}

		GenParser parser = new GenParser(fileLexer.getLexer(), file_path, null);
		if (parser.ExecuteParser() == false)
			return;

		Program root = (Program) parser.getRoot();

		GraphBuilder gBuilder = new GraphBuilder();

		gBuilder.initStart();
		gBuilder.Build(root);
		gBuilder.initEnd();

		gBuilder.Print();

		gBuilder.CreateImg("C:\\files\\parser_tests\\function tests\\",
				"mygraph");
	}
}