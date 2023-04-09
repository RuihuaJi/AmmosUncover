package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HandleDuplicatedFileWrite {
/**
 * When writing to files, sometimes these file should no have been created, or the file will be
 * write into duplicated content. 
 * 
 * The change happens in one test cycle.
 * */
	private int dupNuminTestCycle;
	private String timeStamp;
	private String label;
	private DateTimeFormatter formatter;
	
	public HandleDuplicatedFileWrite() {
		this.label = "_Dup";
		
		this.dupNuminTestCycle = 0;
		this.timeStamp = "";
		
		this.formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
	}
	
	private String generateNewFileName(String originalFileName) {
		
		String src_0 = originalFileName.substring(0, originalFileName.lastIndexOf("."));
		String src_1 = originalFileName.substring(originalFileName.lastIndexOf("."));
		
		this.timeStamp = LocalDateTime.now().format(this.formatter);
		this.dupNuminTestCycle = 0;
		String newFN = "";
		this.updatedDupNum(originalFileName);
		newFN = src_0 + this.label + "[" + this.dupNuminTestCycle + "][" + this.timeStamp + "]" + src_1;
		
		return newFN;
	}
	
	private void updatedDupNum(String originalFileName) {
		String currentDirName = originalFileName.substring(0, originalFileName.lastIndexOf("/"));
		File dirFile = new File(currentDirName);
		if(!dirFile.isDirectory()) {
			if(dirFile.isFile()) {
				System.out.println("#################### {[Error in handleDuplicatedFileWrite(needtoUpdatedDupNum)][isFile][DupNum][" + this.dupNuminTestCycle + "]} ####################");
			}
			System.out.println("#################### {[Error in handleDuplicatedFileWrite(needtoUpdatedDupNum)][isnot File or Dir][DupNum]["+ this.dupNuminTestCycle + "]} ####################");
			return ;
		}
		String[] fileList = dirFile.list();
		
		String comparePartofFileName_full = originalFileName.substring(originalFileName.lastIndexOf("/") + 1);
		String comparePartofFileName = comparePartofFileName_full.substring(0, comparePartofFileName_full.lastIndexOf("."));//original: FileName.txt
		comparePartofFileName = comparePartofFileName + this.label + "[" + this.dupNuminTestCycle + "]";
		int lenDirFile = fileList.length;
		for(int i = 0; i < lenDirFile; i ++) {
			if(!comparePartofFileName_full.equals(fileList[i])) {
				
				if(fileList[i].contains("][") && comparePartofFileName.equals(fileList[i].substring(0, fileList[i].lastIndexOf("[")))) {
					this.dupNuminTestCycle ++;
					updatedDupNum(originalFileName);
					break;
				}
			}
		}
		
	}
	
	private void ChangeDupFileName(String originalFileName, String newFileName) {
		File f = new File(originalFileName);
		if(!f.exists()) {
			System.out.println("#################### {[Error in handleDuplicatedFileWrite(ChangeDupFileName)][original file does not exist]} ####################");
			return ;
		}
		newFileName = newFileName.trim();
		if("".equals(newFileName) || newFileName == null) {
			System.out.println("#################### {[Error in handleDuplicatedFileWrite(ChangeDupFileName)][new file name is empty or null]} ####################");
			return ;
		}
		//In this context, whether the original file is directory does not need to judge.
		File nf = new File(newFileName);
		f.renameTo(nf);
	}
	
	public boolean doHandleDuplicatedFileWrite(String originalFileName) {
		System.out.println("==================== doHandleDuplicatedFileWrite[Start: original file name: " + originalFileName + "] ====================");
		File f = new File(originalFileName);
		if(f.exists()) {
			
			String newFileName  = this.generateNewFileName(originalFileName);
			this.ChangeDupFileName(originalFileName, newFileName);
			System.out.println("==================== doHandleDuplicatedFileWrite[original file name: " + originalFileName + "][new file name: " + newFileName + "] ====================");
			return true;
		}
		
		return false;
	}
	
	
}
