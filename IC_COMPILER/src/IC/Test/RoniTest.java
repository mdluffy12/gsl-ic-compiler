package IC.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class RoniTest {
	public static void main(String[] args) throws FileNotFoundException {
		String file1Path = "C:\\Users\\Roni Lichtman\\Desktop\\test1.txt";
		String file2Path = "C:\\Users\\Roni Lichtman\\Desktop\\test2.txt";
		File file1 = new File(file1Path);
		File file2 = new File(file2Path);
	

		try
		{
			Scanner s1 = new Scanner(file1);
			Scanner s2 = new Scanner(file2);
			
			String st1 = "";
			String st2 = "";
			do
			{
				st1 = s1.nextLine();
				st2 = s2.nextLine();
				
				if(st1.equals(st2))
				{
					System.out.println(st1 + "," + st2);
					//return;
				}
			}while(s1.hasNext() && s2.hasNext());
			
			if(s1.hasNext() || s2.hasNext())
			{
				System.out.println("Failure >=[");
				return;
			}
			
			System.out.println("Great Success!!");
			return;
		}
		catch(Exception e)
		{
			System.out.println(e.toString());  
			return;
		}
	}
}
