package project;

/* 
 * Name: Tiara, William, Tarik
 * Date: 2026.01.12
 * Description: implementing the class to save and load feature
 * creating the manager class
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class PersistanceManager {

	public static final String FILE_NAME = "schedule.txt";

	// save a schedule code

	public void save (Schedule scheduel) {
		try {
			FileWriter Writer = new FileWriter(FILE_NAME);
			Writer.write(Schedule.getName());
			Writer.close();
		} catch(IOException e) {
			System.out.print("Error saving schedule.");
			
		}
	}

	
	//  Load a schedule code

}
