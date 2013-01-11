/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class GenTester {

	public enum OutputType {
		standart, file, treePrint, all;
	};

	public enum TestType {
		lexical, syntax, all;
	};

	public enum LexicalTestType {
		keywords, ascii, files, all;
	};

	protected OutputType ot;
	private File outputFile;

	public GenTester(OutputType ot, File outputFile) {
		this.ot = ot;
		this.outputFile = outputFile;
	}

	public GenTester(OutputType ot) {
		this.ot = ot;
		this.outputFile = null;
	}

	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public boolean CompareFiles(File file1, File file2) {

		Map<String, String> missingLines = new HashMap<String, String>();
		Tprint("Comparing " + file1.getName() + " and " + file2.getName(), ot);
		Scanner s1, s2;

		try {
			s1 = new Scanner(file1);
			s2 = new Scanner(file2);
			String st1, st2;
			do {
				st1 = s1.nextLine();
				st2 = s2.nextLine();

				Tprint(st1 + "\n" + st2, ot);

				if (st1.equals(st2)) {
					Tprint("......... OK!", ot);
				} else {
					Tprint("......... WRONG!", ot);
					missingLines.put(st1, st2);
				}

				Tprint("\n", ot);

			} while (s1.hasNext() && s2.hasNext());

			if (s1.hasNext() || s2.hasNext()) {

				st1 = s1.nextLine();
				st2 = s2.nextLine();

				while (s1.hasNext()) {
					Tprint(st1, ot);
					Tprint("......... MISSING!", ot);
					missingLines.put(st1, null);
					st1 = s1.nextLine();
				}

				while (s2.hasNext()) {
					Tprint(st2, ot);
					Tprint("......... MISSING!", ot);
					missingLines.put(st2, null);
					st2 = s2.nextLine();
				}

				if (st1 != null) {
					missingLines.put(st1, null);
				}
				if (st2 != null) {
					missingLines.put(st2, null);
				}
			}

			s1.close();
			s2.close();
		} catch (Exception e) {
		}

		if (missingLines.isEmpty()) {
			Tprint("\n --- " + "the files " + file1.getName() + " and "
					+ file2.getName() + " are equal!! --- \n", ot);
		} else {
			Tprint("\ncheck all following lines:\n ", ot);

			Iterator<Entry<String, String>> it = missingLines.entrySet()
					.iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> pairs = it.next();
				Tprint(pairs.getKey() + "\nshould be: \n" + pairs.getValue(),
						ot);
				Tprint("\n", ot);
				it.remove();
			}

		}

		return missingLines.isEmpty();
	}

	public static boolean Contains(String pattern, String str) {

		if (pattern.length() > str.length())
			return false;
		try {
			for (int i = 0; i <= str.length() - pattern.length(); i++) {

				if (str.startsWith(pattern, i))
					return true;
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		return false;
	}

	public void Tprint(String str, OutputType output_type) {
		switch (output_type) {
		case standart: {
			System.out.println(str);
			break;
		}
		case file: {
			AppendStringToFile(outputFile, str);
			break;
		}
		case all: {
			System.out.println(str);
			AppendStringToFile(outputFile, str);
			break;
		}
		}
	}

	protected static void AppendStringToFile(File file, String data) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(file, true)));
			out.println(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static boolean isLegalForParcing(File file) {
		String fileName = file.getName();
		if (Contains(".tokens", fileName) || Contains(".mytokens", fileName)
				|| Contains("TEST_", fileName) || Contains(".myast", fileName)
				|| Contains(".ast", fileName) || Contains("DEBUG_", fileName)
				|| Contains("graph_", fileName) || Contains(".png", fileName))
			return false;

		return true;
	}

	// creates the file list data structure, a map enumerating all the files in
	// the folder, and their folder path
	protected static void CreateFileList(File path, List<File> fileList) {

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

	// creates the file list data structure, a map enumerating all the files in
	// the folder, and their folder path
	protected static void CreateFileList(File path, List<File> fileList,
			String suffix) {

		File files[];

		// get all files listed in current folder
		files = path.listFiles();

		if (files == null)
			return;

		Arrays.sort(files);

		for (int i = 0, n = files.length; i < n; i++) {

			if (!files[i].isDirectory() && files[i].getName().endsWith(suffix)) {
				fileList.add(files[i]);
			}else if (files[i].isDirectory()) {
				CreateFileList(files[i], fileList, suffix);
			}
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
