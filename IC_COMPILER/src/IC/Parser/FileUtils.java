/**
 * Created By Micha Sherman,Tzvika Geft and Rani Lichtman 
 * Compilation course, University of Tel Aviv 2012 ©   
 */
package IC.Parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {

	public static void AppendStringToFile(File file, String data) {
		try {
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new FileWriter(file, true)));
			out.println(data);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static File OverrideFile(String file_path) {
		File file = new File(file_path);
		file.delete();
		try {
			file.createNewFile();
		} catch (Exception e) {
		}

		return file;

	}

}
