package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class OutConsole2File {

	private PrintStream printS;
	public OutConsole2File() { printS = null;};
	public void setOutConsole2File(String fileName) {
		try {
			if( printS != null) {
				printS.flush();
				printS.close();
			}
			printS = new PrintStream(fileName);
			System.setOut(printS);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
