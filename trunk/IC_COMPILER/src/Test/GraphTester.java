package Test;

import java.io.File;
import java.io.FileNotFoundException;

import IC.AST.Program;
import IC.Parser.GenLexer;
import IC.Parser.GenParser;

public class GraphTester {
	public static void main(String[] args) {
		GraphTester p = new GraphTester();
		p.start();
	}

	/**
	 * Construct a DOT graph in memory, convert it to image and store the image
	 * in the file system.
	 */
	private void start() {

		String file_path = "C:\\Users\\micha\\workspace\\IC_COMPILER\\src\\test.txt";

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

		gBuilder.CreateImg("mygraph");
	}

	/**
	 * Read the DOT source from a file, convert to image and store the image in
	 * the file system.
	 */
	private void start2() {
		// String dir = "/home/jabba/eclipse2/laszlo.sajat/graphviz-java-api";
		// // Linux
		// String input = dir + "/sample/simple.dot";
		String input = "c:/eclipse.ws/graphviz-java-api/sample/simple.dot";

		GraphViz gv = new GraphViz();
		gv.readSource(input);
		System.out.println(gv.getDotSource());

		String type = "gif";
		// String type = "dot";
		// String type = "fig"; // open with xfig
		// String type = "pdf";
		// String type = "ps";
		// String type = "svg"; // open with inkscape
		// String type = "png";
		// String type = "plain";
		File out = new File("c:/eclipse.ws/graphviz-java-api/tmp/simple."
				+ type); // Windows
		gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
	}
}
