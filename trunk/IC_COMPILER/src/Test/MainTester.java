/* Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */

package Test;

public class MainTester {

	public static void main(String[] args) {
		SyntaxTester synTester = new SyntaxTester(args[0], ".ast.txt",
				".ast.txt", true);
		synTester.ExecuteSyntaxTest();

		SyntaxTester.DeleteAllFilesWithSuffix(args[0], "_tree");
	}

}
