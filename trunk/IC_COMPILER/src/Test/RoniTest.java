package Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RoniTest {

	private static boolean CompareFiles(File file1, File file2) {

		List<String> wrongLines = new ArrayList<String>();
		String line;
		System.out.println("Comparing " + file1.getName() + " and "
				+ file2.getName());
		Scanner s1, s2;
		int[][] asdf = new int[2][2];
		int[] asdf2 = asdf[1];
		try {
			s1 = new Scanner(file1);
			s2 = new Scanner(file2);
			String st1, st2;
			do {
				st1 = s1.nextLine();
				st2 = s2.nextLine();
				if (st1.equals(st2)) {
					line = "\"" + st1 + "\"" + " == " + "\"" + st2 + "\""
							+ ".............. OK!";
				} else {
					line = "\"" + st1 + "\"" + " != " + "\"" + st2 + "\""
							+ ".............. check!";

					wrongLines.add(line);
				}

				System.out.println(line);

			} while (s1.hasNext() && s2.hasNext());

			if (s1.hasNext() || s2.hasNext()) {

				System.out.println("files are different!");
				s1.close();
				s2.close();
				return false;
			}

			s1.close();
			s2.close();
		} catch (Exception e) {
		}

		if (wrongLines.size() == 0) {
			System.out.println("\n --- " + "the files " + file1.getName()
					+ " and " + file2.getName() + " are equal!! --- \n");
		} else {

			// print all wrong lines to check
			System.out
					.println("\n\n files are different! check all following line: \n\n");

			for (String wLine : wrongLines) {
				System.out.println(wLine);
			}

		}
		return true;
	}

	public static void main(String[] args) {

		CompareFiles(new File(args[0]), new File(args[1]));
	}

}
