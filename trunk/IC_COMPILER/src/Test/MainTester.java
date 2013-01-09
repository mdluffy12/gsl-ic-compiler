/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Test;


public class MainTester {

	public static final boolean RunTest = true;
	public static final boolean DeleteOutputFiles = true;
	public static final boolean withLib = true;
	
	public static void main(String[] args) {

		String testedFolder = args[0];
		SemanticTester semanticTester = new SemanticTester(testedFolder,
				".table.txt", ".out.txt",withLib);

		if (DeleteOutputFiles) {
			SemanticTester.deleteOutputFiles(testedFolder);
		}

		if (RunTest) {

			try {
				semanticTester.executeSemanticTester();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

}
