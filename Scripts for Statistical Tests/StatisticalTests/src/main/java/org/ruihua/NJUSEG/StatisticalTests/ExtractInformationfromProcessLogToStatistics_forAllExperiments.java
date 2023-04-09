package org.ruihua.NJUSEG.StatisticalTests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.Problem.*;
import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.ReadandWriteModule.*;
import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.*;


public class ExtractInformationfromProcessLogToStatistics_forAllExperiments {

	
	public static final String STRING_DATA_FOLDER_TRAIL_LOOP_NUMBER = "TrialLoop";
	public static final String STRING_DATA_FOLDER_TEST_CYCLE_NUMBER = "TestCycle";
	public static final String STRING_DATA_FILE_JMETAL = "OutputResults_jMetal";
	public static final String STRING_NULL_DATA_FOLDER_DEFAULT_START_IN_MAC = ".";
	public static final String STRING_NOUSE_DATA_FOLDER_DEFAULT_END_IN_LINUX = "~";
	
	public static final String STRING_DATA_FOLDER_LEVEL_ONE = "Num";//Out of time
	
	
	
	
	
	
	
	public static HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> traverseDataFileDirToCollectInternalResults(String dataDirectory, String MOSAType, int iterativeSetting) {
		
		//To return >> HashMap:trails >> trail:cycles >> cycle:tempResult
		HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> resultForWholeStatistics = new HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>();
		
		int count = 0;
		
		//For validation
		String SAType = MOSAType;
		int numberofTCsperTestCycle = iterativeSetting;
		
		dataDirectory = dataDirectory + MOSAType + "/TestCasesperTestCycle_" + iterativeSetting + "/";
		
		File dirFile = new File(dataDirectory);
		if(!dirFile.isDirectory()) {
			if(dirFile.isFile()) {
				System.out.println("#################### Error: basic dir isFile ####################");
			}
			System.out.println("#################### Error: basic dir is not file or dir ####################");
			return null;
		}
		
		
		
		String[] fileList = dirFile.list();
		int lenDirFiles = fileList.length;
		for(int i = 0; i < lenDirFiles; i ++) {
			
			
			
			if(fileList[i].startsWith(STRING_DATA_FOLDER_TRAIL_LOOP_NUMBER)) {
				
				System.out.println(fileList[i]);
				
				//once trail
				HashMap<Integer, HashMap<String, Integer>> trailTemp = new HashMap<Integer, HashMap<String, Integer>>();
				
				//Key: trail NO
				String[] src4TrailNo = fileList[i].split(STRING_DATA_FOLDER_TRAIL_LOOP_NUMBER + "_");
				int trailNOTemp = Integer.parseInt(src4TrailNo[1]);
				
				String subFileDir = dataDirectory + "/" + fileList[i];
				File subdirFile = new File(subFileDir);
				 
				 
				if(!subdirFile.isDirectory()) {
					if(subdirFile.isFile()) {
						System.out.println("#################### Error: basic dir isFile ####################");
					}
					System.out.println("#################### Error: basic dir is not file or dir ####################");
					return null;
				}
				 
				String[] subfileList = subdirFile.list();
				int lenSubDirFiles = subfileList.length;
				String[] orderedCycleTitle = new String[30];
				for(int jj = 0; jj < 30; jj ++) {
					orderedCycleTitle[jj] = null;
				}
				for(int j = 0; j < lenSubDirFiles; j ++) {
					if(subfileList[j].startsWith(STRING_DATA_FOLDER_TEST_CYCLE_NUMBER)) {
//						System.out.println(subfileList[j]);
						String[] src = subfileList[j].split("_");
						int seqNo = Integer.parseInt(src[1]);
						orderedCycleTitle[seqNo] = subfileList[j];
					}
				}
				 
//				for(int jj = 0; jj < 25; jj ++) {
//					System.out.println(orderedCycleTitle[jj]);
//				}
				 
				int countCycleSeq = 1;
				while(orderedCycleTitle[countCycleSeq] != null) {
					System.out.println(orderedCycleTitle[countCycleSeq]);
					countCycleSeq ++;
				}
				countCycleSeq = countCycleSeq - 2;
//				System.out.println("# fileDirSeq: " + countCycleSeq);
				for(int j = 1; j <= countCycleSeq; j ++) {
					String outputDir = subdirFile + "/" + orderedCycleTitle[j];
					File outputDirFile = new File(outputDir);
					
					if(!outputDirFile.isDirectory()) {
						if(outputDirFile.isFile()) {
							System.out.println("#################### Error: basic dir isFile ####################");
						}
						System.out.println("#################### Error: basic dir is not file or dir ####################");
						return null;
					}
					
					String SATypeRealTime = "";
					String[] outputDirList = outputDirFile.list();
					int outputDirLength = outputDirList.length;
//					System.out.println("# fileDir: " + outputDir);
//					System.out.println("# len: " + outputDirLength);
					for(int k = 0; k < outputDirLength; k ++) {
//						System.out.println("### " + outputDirList[k]);
						if(outputDirList[k].startsWith(STRING_DATA_FILE_JMETAL)) {
							String[] src4Validation = outputDirList[k].split("OutputResults_jMetal_");
							SATypeRealTime = src4Validation[1];
						}
					}
					 
					HashMap<String, Integer> resultTemp = null;
					
					String logfilePath = outputDir + "/log.txt";
//					System.out.println("Before validate: " + logfilePath);
					File logFile = new File(logfilePath);
					if(logFile.isFile()) {
//						System.out.println("validate: " + logfilePath);
						System.out.println("Cycle No: " + j);
						 
						//deal with log files
						resultTemp = ExtractInformationfromProcessLogToStatistics_forAllExperiments.extractTimeCostFromOneCycleLogFile(logfilePath);
						 
					}
					 
					if(!SAType.equals(SATypeRealTime) 
//							 || !(numberofTCsperTestCycle == resultTemp.get("totalExecutions"))
							) {
						System.out.println("++++++++++++++++++ Error when collecting results![ SAType: " + SAType + ", SATypeRealTime: " + SATypeRealTime + "] ++++++++++++++++++");
						return null;
					}
					
					trailTemp.put(j, resultTemp);
					
					
				}
				 
				resultForWholeStatistics.put(trailNOTemp, trailTemp);
				 
			}
			
			
			
			
		}
		
		
		
		
		
		return resultForWholeStatistics;
		
	}
	
	
	public static HashMap<String, Integer> extractTimeCostFromOneCycleLogFile(String logfile) {
		
		HashMap<String, Integer> resultTemp = new HashMap<String, Integer>();
		ReadFromFiles.readCostandEffectivenessFromNewVersionLogFile_v202109(logfile, resultTemp);
		
		System.out.println("TimeCostofMOSASearch: " + resultTemp.get("TimeCostofMOSASearch"));
		System.out.println("OtherTimeCost_1: " + resultTemp.get("OtherTimeCost_1"));
		System.out.println("TimeCostofExecution: " + resultTemp.get("TimeCostofExecution"));
		System.out.println("TotalTimeCost: " + resultTemp.get("TotalTimeCost"));
		System.out.println("TotalExecutions: " + resultTemp.get("TotalExecutions"));
		System.out.println("NumberofNewExecutions: " + resultTemp.get("NumberofNewExecutions"));
		System.out.println("NumberofNewTransitions: " + resultTemp.get("NumberofNewTransitions"));
		System.out.println("NumberofNewStates: " + resultTemp.get("NumberofNewStates"));
		
		return resultTemp;
	}
	
	public static void printOnceTrailResult(HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> result) {
		
		System.out.println("======== Begin to print ==========");
		
		Map<Integer, HashMap<Integer, HashMap<String, Integer>>> mapForTrails = result;
		for(Map.Entry<Integer, HashMap<Integer, HashMap<String, Integer>>> entryTrail: mapForTrails.entrySet()) {
			System.out.println("Trail: " + entryTrail.getKey());
			Map<Integer, HashMap<String, Integer>> mapForCycles = entryTrail.getValue();
			
			List<HashMap<String, Integer>> sortedCycles = new ArrayList<HashMap<String, Integer>>();
			for(int i = 0; i < mapForCycles.size() + 1; i ++) {
				sortedCycles.add(new HashMap<String, Integer>());
			}
			for(Map.Entry<Integer, HashMap<String, Integer>> entryCycle: mapForCycles.entrySet()) {
				sortedCycles.add(entryCycle.getKey(), entryCycle.getValue());
				sortedCycles.remove(entryCycle.getKey()+1);
			}
			
//			for(Map.Entry<Integer, HashMap<String, Integer>> entryCycle: mapForCycles.entrySet()) {
//				System.out.println("  Cycle: " + entryCycle.getKey());
//				System.out.println("    timeCostofMOSASearch: " + entryCycle.getValue().get("timeCostofMOSASearch"));
//				System.out.println("    otherTimeCost_1: " + entryCycle.getValue().get("otherTimeCost_1"));
//				System.out.println("    timeCostofExecution: " + entryCycle.getValue().get("timeCostofExecution"));
//				System.out.println("    totalTimeCost: " + entryCycle.getValue().get("totalTimeCost"));
//				System.out.println("    totalExecutions: " + entryCycle.getValue().get("totalExecutions"));
//				System.out.println("    numberofNewExecutions: " + entryCycle.getValue().get("numberofNewExecutions"));
//				System.out.println("    numberofNewTransitions: " + entryCycle.getValue().get("numberofNewTransitions"));
//				System.out.println("    numberofNewStates: " + entryCycle.getValue().get("numberofNewStates"));
//				
//			}
			for(int i = 1; i < mapForCycles.size() + 1; i ++) {
				HashMap<String, Integer> temp = sortedCycles.get(i);
				System.out.println("  Cycle: " + i);
				System.out.println("    TimeCostofMOSASearch: " + temp.get("TimeCostofMOSASearch"));
				System.out.println("    OtherTimeCost_1: " + temp.get("OtherTimeCost_1"));
				System.out.println("    TimeCostofExecution: " + temp.get("TimeCostofExecution"));
				System.out.println("    TotalTimeCost: " + temp.get("TotalTimeCost"));
				System.out.println("    TotalExecutions: " + temp.get("TotalExecutions"));
				System.out.println("    NumberofNewExecutions: " + temp.get("NumberofNewExecutions"));
				System.out.println("    NumberofNewTransitions: " + temp.get("NumberofNewTransitions"));
				System.out.println("    NumberofNewStates: " + temp.get("NumberofNewStates"));
			}
			
			
			
			
			
		}
		
		
	}
	
	public static void writeToExcelFileForStatisticsPreparation(HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> result, String fileName, String sheetName) {
		
		String data = "";
		
		Workbook wb = null;
		Sheet sheet = null;
		
		
		try {
			
			File excelFile = new File(fileName);
			if(!excelFile.exists()) {
				excelFile.createNewFile();
				wb = new HSSFWorkbook();
				OutputStream stream = new FileOutputStream(excelFile);
				wb.write(stream);
				stream.close();
				
			}
			
			InputStream is = new FileInputStream(excelFile);
			
			wb = new HSSFWorkbook(is);
			sheet = (Sheet) wb.createSheet(sheetName);
			
//			Row row = sheet.createRow(0);
//			Cell cell = row.createCell(0);
//			cell.setCellValue("");
			
			
			
			Row rowTemp = null;
			Cell cellTemp = null;
			
			int countTrails = -1;
			
			Map<Integer, HashMap<Integer, HashMap<String, Integer>>> mapForTrails = result;
			for(Map.Entry<Integer, HashMap<Integer, HashMap<String, Integer>>> entryTrail: mapForTrails.entrySet()) {
				
				countTrails ++;
				
				sheet.autoSizeColumn(0, true);
				
				Row rowTemp_0 = sheet.createRow(countTrails*9);
				cellTemp = rowTemp_0.createCell(0);
				cellTemp.setCellValue("Trail: " + entryTrail.getKey());
				
				Row rowTemp_1 = sheet.createRow(countTrails*9+1);
				cellTemp = rowTemp_1.createCell(0);
				cellTemp.setCellValue("TimeCostofMOSASearch");
				
				Row rowTemp_2 = sheet.createRow(countTrails*9+2);
				cellTemp = rowTemp_2.createCell(0);
				cellTemp.setCellValue("OtherTimeCost_1");
				
				Row rowTemp_3 = sheet.createRow(countTrails*9+3);
				cellTemp = rowTemp_3.createCell(0);
				cellTemp.setCellValue("TimeCostofExecution");
				
				Row rowTemp_4 = sheet.createRow(countTrails*9+4);
				cellTemp = rowTemp_4.createCell(0);
				cellTemp.setCellValue("TotalTimeCost");
				
				Row rowTemp_5 = sheet.createRow(countTrails*9+5);
				cellTemp = rowTemp_5.createCell(0);
				cellTemp.setCellValue("TotalExecutions");
				
				Row rowTemp_6 = sheet.createRow(countTrails*9+6);
				cellTemp = rowTemp_6.createCell(0);
				cellTemp.setCellValue("NumberofNewExecutions");
				
				Row rowTemp_7 = sheet.createRow(countTrails*9+7);
				cellTemp = rowTemp_7.createCell(0);
				cellTemp.setCellValue("NumberofNewTransitions");
				
				Row rowTemp_8 = sheet.createRow(countTrails*9+8);
				cellTemp = rowTemp_8.createCell(0);
				cellTemp.setCellValue("NumberofNewStates");
				
				System.out.println("Trail: " + entryTrail.getKey());
				Map<Integer, HashMap<String, Integer>> mapForCycles = entryTrail.getValue();
				
				List<HashMap<String, Integer>> sortedCycles = new ArrayList<HashMap<String, Integer>>();
				for(int i = 0; i < mapForCycles.size() + 1; i ++) {
					sortedCycles.add(new HashMap<String, Integer>());
				}
				for(Map.Entry<Integer, HashMap<String, Integer>> entryCycle: mapForCycles.entrySet()) {
					sortedCycles.add(entryCycle.getKey(), entryCycle.getValue());
					sortedCycles.remove(entryCycle.getKey()+1);
				}
				
				for(int i = 1; i < mapForCycles.size() + 1; i ++) {
					HashMap<String, Integer> temp = sortedCycles.get(i);
					System.out.println("  Cycle: " + i);
					
					System.out.println("    TimeCostofMOSASearch: " + temp.get("TimeCostofMOSASearch"));
					System.out.println("    OtherTimeCost_1: " + temp.get("OtherTimeCost_1"));
					System.out.println("    TimeCostofExecution: " + temp.get("TimeCostofExecution"));
					System.out.println("    TotalTimeCost: " + temp.get("TotalTimeCost"));
					System.out.println("    TotalExecutions: " + temp.get("TotalExecutions"));
					System.out.println("    NumberofNewExecutions: " + temp.get("NumberofNewExecutions"));
					System.out.println("    NumberofNewTransitions: " + temp.get("NumberofNewTransitions"));
					System.out.println("    NumberofNewStates: " + temp.get("NumberofNewStates"));
				}
				
				for(int i = 1; i < mapForCycles.size() + 1; i ++) {
					HashMap<String, Integer> temp = sortedCycles.get(i);
					
					cellTemp = rowTemp_0.createCell(i);
					cellTemp.setCellValue("Cycle " + i);
					
					cellTemp = rowTemp_1.createCell(i);
					cellTemp.setCellValue(temp.get("TimeCostofMOSASearch"));
					cellTemp = rowTemp_2.createCell(i);
					cellTemp.setCellValue(temp.get("OtherTimeCost_1"));
					cellTemp = rowTemp_3.createCell(i);
					cellTemp.setCellValue(temp.get("TimeCostofExecution"));
					cellTemp = rowTemp_4.createCell(i);
					cellTemp.setCellValue(temp.get("TotalTimeCost"));
					cellTemp = rowTemp_5.createCell(i);
					cellTemp.setCellValue(temp.get("TotalExecutions"));
					cellTemp = rowTemp_6.createCell(i);
					cellTemp.setCellValue(temp.get("NumberofNewExecutions"));
					cellTemp = rowTemp_7.createCell(i);
					cellTemp.setCellValue(temp.get("NumberofNewTransitions"));
					cellTemp = rowTemp_8.createCell(i);
					cellTemp.setCellValue(temp.get("NumberofNewStates"));
					
				}
				
				
			}
			
			
			
			OutputStream stream = new FileOutputStream(excelFile);
			wb.write(stream);
			stream.close();
			
//			data = data + "\n";
//			
//			FileWriter fileWritter = new FileWriter(fileName, true);
//			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//			bufferWritter.write(data);
//			bufferWritter.flush();
//			
//			bufferWritter.close();
//			fileWritter.close();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	
	public static HashMap<Integer, HashMap<String, Double>> addUpAllTestCycles(HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> result) {
		
		HashMap<Integer, HashMap<String, Double>> addUpResult = new HashMap<Integer, HashMap<String, Double>>();
		
		Map<Integer, HashMap<Integer, HashMap<String, Integer>>> mapForTrails = result;
		for(Map.Entry<Integer, HashMap<Integer, HashMap<String, Integer>>> entryTrail: mapForTrails.entrySet()) {
			System.out.println("Trail: " + entryTrail.getKey());
			Map<Integer, HashMap<String, Integer>> mapForCycles = entryTrail.getValue();
			
			List<HashMap<String, Integer>> sortedCycles = new ArrayList<HashMap<String, Integer>>();
			for(int i = 0; i < mapForCycles.size() + 1; i ++) {
				sortedCycles.add(new HashMap<String, Integer>());
			}
			for(Map.Entry<Integer, HashMap<String, Integer>> entryCycle: mapForCycles.entrySet()) {
				sortedCycles.add(entryCycle.getKey(), entryCycle.getValue());
				sortedCycles.remove(entryCycle.getKey()+1);
			}
			
			int timeCostofMOSASearch = 0;
			int otherTimeCost_1 = 0;
			int timeCostofExecution = 0;
			int totalTimeCost = 0;
			int totalExecutions = 0;
			int numberofNewExecutions = 0;
			int numberofNewTransitions = 0;
			int numberofNewStates = 0;
			
			for(int i = 1; i < mapForCycles.size() + 1; i ++) {
				HashMap<String, Integer> temp = sortedCycles.get(i);
				System.out.println("  Cycle: " + i);
				System.out.println("    TimeCostofMOSASearch: " + temp.get("TimeCostofMOSASearch"));
				System.out.println("    OtherTimeCost_1: " + temp.get("OtherTimeCost_1"));
				System.out.println("    TimeCostofExecution: " + temp.get("TimeCostofExecution"));
				System.out.println("    TotalTimeCost: " + temp.get("TotalTimeCost"));
				System.out.println("    TotalExecutions: " + temp.get("TotalExecutions"));
				System.out.println("    NumberofNewExecutions: " + temp.get("NumberofNewExecutions"));
				System.out.println("    NumberofNewTransitions: " + temp.get("NumberofNewTransitions"));
				System.out.println("    NumberofNewStates: " + temp.get("NumberofNewStates"));
				
				timeCostofMOSASearch = timeCostofMOSASearch + temp.get("TimeCostofMOSASearch");
				otherTimeCost_1 = otherTimeCost_1 + temp.get("OtherTimeCost_1");
				timeCostofExecution = timeCostofExecution + temp.get("TimeCostofExecution");
				totalTimeCost = totalTimeCost + temp.get("TotalTimeCost");
				totalExecutions = totalExecutions + temp.get("TotalExecutions");
				numberofNewExecutions = numberofNewExecutions + temp.get("NumberofNewExecutions");
				numberofNewTransitions = numberofNewTransitions + temp.get("NumberofNewTransitions");
				numberofNewStates = numberofNewStates + temp.get("NumberofNewStates");
				
			}
			
			HashMap<String, Double> onceTrailTemp = new HashMap<String, Double>();
			onceTrailTemp.put("TimeCostofMOSASearch", (double)timeCostofMOSASearch);
			onceTrailTemp.put("OtherTimeCost_1", (double)otherTimeCost_1);
			onceTrailTemp.put("TimeCostofExecution", (double)timeCostofExecution);
			onceTrailTemp.put("TotalTimeCost", (double)totalTimeCost);
			onceTrailTemp.put("TotalExecutions", (double)totalExecutions);
			onceTrailTemp.put("NumberofNewExecutions", (double)numberofNewExecutions);
			onceTrailTemp.put("NumberofNewTransitions", (double)numberofNewTransitions);
			onceTrailTemp.put("NumberofNewStates", (double)numberofNewStates);
			//add efficiency measures
			double NEDivAllT = (double)numberofNewExecutions*60000/(double)totalTimeCost;
			onceTrailTemp.put("NumberofNewExecutionsDivAllT", NEDivAllT);
			double NTDivAllT = (double)numberofNewTransitions*60000/(double)totalTimeCost;
			onceTrailTemp.put("NumberofNewTransitionsDivAllT", NTDivAllT);
			double NSDivAllT = (double)numberofNewStates*60000/(double)totalTimeCost;
			onceTrailTemp.put("NumberofNewStatesDivAllT", NSDivAllT);
			
			addUpResult.put(entryTrail.getKey(), onceTrailTemp);
			
		}
		
		
		return addUpResult;
	}
	
	public static void printAddUpOnceTrailResult(HashMap<Integer, HashMap<String, Double>> addUpResult) {
		
		HashMap<Integer, HashMap<String, Double>> mapForTrails = addUpResult;
		for(Map.Entry<Integer, HashMap<String, Double>> entryTrail: mapForTrails.entrySet()) {
			System.out.println("Trail: " + entryTrail.getKey());
			
			System.out.println("    TimeCostofMOSASearch: " + entryTrail.getValue().get("TimeCostofMOSASearch"));
			System.out.println("    OtherTimeCost_1: " + entryTrail.getValue().get("OtherTimeCost_1"));
			System.out.println("    TimeCostofExecution: " + entryTrail.getValue().get("TimeCostofExecution"));
			System.out.println("    TotalTimeCost: " + entryTrail.getValue().get("TotalTimeCost"));
			System.out.println("    TotalExecutions: " + entryTrail.getValue().get("TotalExecutions"));
			System.out.println("    NumberofNewExecutions: " + entryTrail.getValue().get("NumberofNewExecutions"));
			System.out.println("    NumberofNewTransitions: " + entryTrail.getValue().get("NumberofNewTransitions"));
			System.out.println("    NumberofNewStates: " + entryTrail.getValue().get("NumberofNewStates"));
			System.out.println("    NumberofNewExecutionsDivAllT: " + entryTrail.getValue().get("NumberofNewExecutionsDivAllT"));
			System.out.println("    NumberofNewTransitionsDivAllT: " + entryTrail.getValue().get("NumberofNewTransitionsDivAllT"));
			System.out.println("    NumberofNewStatesDivAllT: " + entryTrail.getValue().get("NumberofNewStatesDivAllT"));
		}
		
	}
	
	
	
	
	
	public static HashMap<String, HashMap<String, Double>> statisticTestForAvaluePvalue(HashMap<Integer, HashMap<String, Double>> addUpResult_1, HashMap<Integer, HashMap<String, Double>> addUpResult_2) {//Case A1 and Case A2: label A1A2
		
		//Calculate A12 value
		List<Double> A12 = calculateA12Value(addUpResult_1, addUpResult_2);
		
		//Calculate p value
		List<Double> p_value = calculatePValuebyCallPython(addUpResult_1, addUpResult_2);
		
		//Merge results
		HashMap<String, HashMap<String, Double>> statisticResults = new HashMap<String, HashMap<String, Double>>();
		
		HashMap<String, Double> A12Collection = new HashMap<String, Double>();
		A12Collection.put("TimeCostofMOSASearch", A12.get(0));
		A12Collection.put("OtherTimeCost_1", A12.get(1));
		A12Collection.put("TimeCostofExecution", A12.get(2));
		A12Collection.put("TotalTimeCost", A12.get(3));
		A12Collection.put("TotalExecutions", A12.get(4));
		A12Collection.put("NumberofNewExecutions", A12.get(5));
		A12Collection.put("NumberofNewTransitions", A12.get(6));
		A12Collection.put("NumberofNewStates", A12.get(7));
		A12Collection.put("NumberofNewExecutionsDivAllT", A12.get(8));
		A12Collection.put("NumberofNewTransitionsDivAllT", A12.get(9));
		A12Collection.put("NumberofNewStatesDivAllT", A12.get(10));
		
		HashMap<String, Double> pvalueCollection = new HashMap<String, Double>();
		pvalueCollection.put("TimeCostofMOSASearch", p_value.get(0));
		pvalueCollection.put("OtherTimeCost_1", p_value.get(1));
		pvalueCollection.put("TimeCostofExecution", p_value.get(2));
		pvalueCollection.put("TotalTimeCost", p_value.get(3));
		pvalueCollection.put("TotalExecutions", p_value.get(4));
		pvalueCollection.put("NumberofNewExecutions", p_value.get(5));
		pvalueCollection.put("NumberofNewTransitions", p_value.get(6));
		pvalueCollection.put("NumberofNewStates", p_value.get(7));
		pvalueCollection.put("NumberofNewExecutionsDivAllT", p_value.get(8));
		pvalueCollection.put("NumberofNewTransitionsDivAllT", p_value.get(9));
		pvalueCollection.put("NumberofNewStatesDivAllT", p_value.get(10));
		
		statisticResults.put("A12", A12Collection);
		statisticResults.put("P", pvalueCollection);
		
		return statisticResults;
	}
	
	public static List<Double> calculateA12Value(HashMap<Integer, HashMap<String, Double>> addUpResult_1, HashMap<Integer, HashMap<String, Double>> addUpResult_2) {
		
		List<Double> timeCostofMOSASearch_A1 = new ArrayList<Double>();
		List<Double> otherTimeCost_1_A1 = new ArrayList<Double>();
		List<Double> timeCostofExecution_A1 = new ArrayList<Double>();
		List<Double> totalTimeCost_A1 = new ArrayList<Double>();
		List<Double> totalExecutions_A1 = new ArrayList<Double>();
		List<Double> numberofNewExecutions_A1 = new ArrayList<Double>();
		List<Double> numberofNewTransitions_A1 = new ArrayList<Double>();
		List<Double> numberofNewStates_A1 = new ArrayList<Double>();
		List<Double> numberofNewExecutionsDivAllT_A1 = new ArrayList<Double>();
		List<Double> numberofNewTransitionsDivAllT_A1 = new ArrayList<Double>();
		List<Double> numberofNewStatesDivAllT_A1 = new ArrayList<Double>();
		
		HashMap<Integer, HashMap<String, Double>> mapForTrails_1 = addUpResult_1;
		for(Map.Entry<Integer, HashMap<String, Double>> entryTrail: mapForTrails_1.entrySet()) {
			System.out.println("Trail: " + entryTrail.getKey());
			
			timeCostofMOSASearch_A1.add(entryTrail.getValue().get("TimeCostofMOSASearch"));
			otherTimeCost_1_A1.add(entryTrail.getValue().get("OtherTimeCost_1"));
			timeCostofExecution_A1.add(entryTrail.getValue().get("TimeCostofExecution"));
			totalTimeCost_A1.add(entryTrail.getValue().get("TotalTimeCost"));
			totalExecutions_A1.add(entryTrail.getValue().get("TotalExecutions"));
			numberofNewExecutions_A1.add(entryTrail.getValue().get("NumberofNewExecutions"));
			numberofNewTransitions_A1.add(entryTrail.getValue().get("NumberofNewTransitions"));
			numberofNewStates_A1.add(entryTrail.getValue().get("NumberofNewStates"));
			numberofNewExecutionsDivAllT_A1.add(entryTrail.getValue().get("NumberofNewExecutionsDivAllT"));
			numberofNewTransitionsDivAllT_A1.add(entryTrail.getValue().get("NumberofNewTransitionsDivAllT"));
			numberofNewStatesDivAllT_A1.add(entryTrail.getValue().get("NumberofNewStatesDivAllT"));
		}
		
		List<Double> timeCostofMOSASearch_A2 = new ArrayList<Double>();
		List<Double> otherTimeCost_1_A2 = new ArrayList<Double>();
		List<Double> timeCostofExecution_A2 = new ArrayList<Double>();
		List<Double> totalTimeCost_A2 = new ArrayList<Double>();
		List<Double> totalExecutions_A2 = new ArrayList<Double>();
		List<Double> numberofNewExecutions_A2 = new ArrayList<Double>();
		List<Double> numberofNewTransitions_A2 = new ArrayList<Double>();
		List<Double> numberofNewStates_A2 = new ArrayList<Double>();
		List<Double> numberofNewExecutionsDivAllT_A2 = new ArrayList<Double>();
		List<Double> numberofNewTransitionsDivAllT_A2 = new ArrayList<Double>();
		List<Double> numberofNewStatesDivAllT_A2 = new ArrayList<Double>();
		
		HashMap<Integer, HashMap<String, Double>> mapForTrails_2 = addUpResult_2;
		for(Map.Entry<Integer, HashMap<String, Double>> entryTrail: mapForTrails_2.entrySet()) {
			System.out.println("Trail: " + entryTrail.getKey());
			
			timeCostofMOSASearch_A2.add(entryTrail.getValue().get("TimeCostofMOSASearch"));
			otherTimeCost_1_A2.add(entryTrail.getValue().get("OtherTimeCost_1"));
			timeCostofExecution_A2.add(entryTrail.getValue().get("TimeCostofExecution"));
			totalTimeCost_A2.add(entryTrail.getValue().get("TotalTimeCost"));
			totalExecutions_A2.add(entryTrail.getValue().get("TotalExecutions"));
			numberofNewExecutions_A2.add(entryTrail.getValue().get("NumberofNewExecutions"));
			numberofNewTransitions_A2.add(entryTrail.getValue().get("NumberofNewTransitions"));
			numberofNewStates_A2.add(entryTrail.getValue().get("NumberofNewStates"));
			numberofNewExecutionsDivAllT_A2.add(entryTrail.getValue().get("NumberofNewExecutionsDivAllT"));
			numberofNewTransitionsDivAllT_A2.add(entryTrail.getValue().get("NumberofNewTransitionsDivAllT"));
			numberofNewStatesDivAllT_A2.add(entryTrail.getValue().get("NumberofNewStatesDivAllT"));
		}
		
		List<Double> results = new ArrayList<Double>();
		
		double timeCostofMOSASearch_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(timeCostofMOSASearch_A1, timeCostofMOSASearch_A2);
		double otherTimeCost_1_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(otherTimeCost_1_A1, otherTimeCost_1_A2);;
		double timeCostofExecution_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(timeCostofExecution_A1, timeCostofExecution_A2);;
		double totalTimeCost_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(totalTimeCost_A1, totalTimeCost_A2);;
		double totalExecutions_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(totalExecutions_A1, totalExecutions_A2);;
		double numberofNewExecutions_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(numberofNewExecutions_A1, numberofNewExecutions_A2);;
		double numberofNewTransitions_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(numberofNewTransitions_A1, numberofNewTransitions_A2);;
		double numberofNewStates_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(numberofNewStates_A1, numberofNewStates_A2);;
		double numberofNewExecutionsDivAllT_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(numberofNewExecutionsDivAllT_A1, numberofNewExecutionsDivAllT_A2);;
		double numberofNewTransitionsDivAllT_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(numberofNewTransitionsDivAllT_A1, numberofNewTransitionsDivAllT_A2);;
		double numberofNewStatesDivAllT_A12 = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAValueUsingA1A2(numberofNewStatesDivAllT_A1, numberofNewStatesDivAllT_A2);;
		
		
		results.add(timeCostofMOSASearch_A12);
		results.add(otherTimeCost_1_A12);
		results.add(timeCostofExecution_A12);
		results.add(totalTimeCost_A12);
		results.add(totalExecutions_A12);
		results.add(numberofNewExecutions_A12);
		results.add(numberofNewTransitions_A12);
		results.add(numberofNewStates_A12);
		results.add(numberofNewExecutionsDivAllT_A12);
		results.add(numberofNewTransitionsDivAllT_A12);
		results.add(numberofNewStatesDivAllT_A12);
		
		return results;
	}
	
	public static double calculateAValueUsingA1A2(List<Double> A1, List<Double> A2) {
		
		int lenA1 = A1.size();
		int lenA2 = A2.size();
		
		int count = 0;
		int biggerA1A2 = 0;
		
		for(int i = 0; i < lenA1; i ++) {
			double factorFromA1Temp = A1.get(i);
			for(int j = 0; j < lenA2; j ++) {
				count ++;
				
				if(factorFromA1Temp > A2.get(j)) {
					biggerA1A2 ++;
				}
			}
			
		}
		
		double re = (double)biggerA1A2 / (double)count;
		
//		System.out.println("A values: " + re);
		
		
		return re;
	}
	
	
	
	public static List<Double> calculatePValuebyCallPython(HashMap<Integer, HashMap<String, Double>> addUpResult_1, HashMap<Integer, HashMap<String, Double>> addUpResult_2) {
		
		String constructedDeliveringSourceDataString_1 = "";
		String constructedDeliveringSourceDataString_2 = "";
		
		String timeCostofMOSASearch = "[timeCostofMOSASearch]";
		String otherTimeCost_1 = "[otherTimeCost_1]";
		String timeCostofExecution = "[timeCostofExecution]";
		String totalTimeCost = "[totalTimeCost]";
		String totalExecutions = "[totalExecutions]";
		String numberofNewExecutions = "[numberofNewExecutions]";
		String numberofNewTransitions = "[numberofNewTransitions]";
		String numberofNewStates = "[numberofNewStates]";
		String numberofNewExecutionsDivAllT = "[numberofNewExecutionsDivAllT]";
		String numberofNewTransitionsDivAllT = "[numberofNewTransitionsDivAllT]";
		String numberofNewStatesDivAllT = "[numberofNewStatesDivAllT]";
		
		
		HashMap<Integer, HashMap<String, Double>> mapForTrails_1 = addUpResult_1;
		for(Map.Entry<Integer, HashMap<String, Double>> entryTrail: mapForTrails_1.entrySet()) {
			System.out.println("Trail: " + entryTrail.getKey());
			
			timeCostofMOSASearch = timeCostofMOSASearch + " " + entryTrail.getValue().get("TimeCostofMOSASearch");
			otherTimeCost_1 = otherTimeCost_1 + " " + entryTrail.getValue().get("OtherTimeCost_1");
			timeCostofExecution = timeCostofExecution + " " + entryTrail.getValue().get("TimeCostofExecution");
			totalTimeCost = totalTimeCost + " " + entryTrail.getValue().get("TotalTimeCost");
			totalExecutions = totalExecutions + " " + entryTrail.getValue().get("TotalExecutions");
			numberofNewExecutions = numberofNewExecutions + " " + entryTrail.getValue().get("NumberofNewExecutions");
			numberofNewTransitions = numberofNewTransitions + " " + entryTrail.getValue().get("NumberofNewTransitions");
			numberofNewStates = numberofNewStates + " " + entryTrail.getValue().get("NumberofNewStates");
			numberofNewExecutionsDivAllT = numberofNewExecutionsDivAllT + " " + String.format("%.6f", entryTrail.getValue().get("NumberofNewExecutionsDivAllT"));
			numberofNewTransitionsDivAllT = numberofNewTransitionsDivAllT + " " + String.format("%.6f", entryTrail.getValue().get("NumberofNewTransitionsDivAllT"));
			numberofNewStatesDivAllT = numberofNewStatesDivAllT + " " + String.format("%.6f", entryTrail.getValue().get("NumberofNewStatesDivAllT"));
			
		}
		
		constructedDeliveringSourceDataString_1 = timeCostofMOSASearch + "; "
				+ otherTimeCost_1 + "; "
				+ timeCostofExecution + "; "
				+ totalTimeCost + "; "
				+ totalExecutions + "; "
				+ numberofNewExecutions + "; "
				+ numberofNewTransitions + "; "
				+ numberofNewStates + "; "
				+ numberofNewExecutionsDivAllT + "; "
				+ numberofNewTransitionsDivAllT + "; "
				+ numberofNewStatesDivAllT + "; "
				;
		
		timeCostofMOSASearch = "[timeCostofMOSASearch]";
		otherTimeCost_1 = "[otherTimeCost_1]";
		timeCostofExecution = "[timeCostofExecution]";
		totalTimeCost = "[totalTimeCost]";
		totalExecutions = "[totalExecutions]";
		numberofNewExecutions = "[numberofNewExecutions]";
		numberofNewTransitions = "[numberofNewTransitions]";
		numberofNewStates = "[numberofNewStates]";
		numberofNewExecutionsDivAllT = "[numberofNewExecutionsDivAllT]";
		numberofNewTransitionsDivAllT = "[numberofNewTransitionsDivAllT]";
		numberofNewStatesDivAllT = "[numberofNewStatesDivAllT]";
		
		HashMap<Integer, HashMap<String, Double>> mapForTrails_2 = addUpResult_2;
		for(Map.Entry<Integer, HashMap<String, Double>> entryTrail: mapForTrails_2.entrySet()) {
			System.out.println("Trail: " + entryTrail.getKey());
			
			timeCostofMOSASearch = timeCostofMOSASearch + " " + entryTrail.getValue().get("TimeCostofMOSASearch");
			otherTimeCost_1 = otherTimeCost_1 + " " + entryTrail.getValue().get("OtherTimeCost_1");
			timeCostofExecution = timeCostofExecution + " " + entryTrail.getValue().get("TimeCostofExecution");
			totalTimeCost = totalTimeCost + " " + entryTrail.getValue().get("TotalTimeCost");
			totalExecutions = totalExecutions + " " + entryTrail.getValue().get("TotalExecutions");
			numberofNewExecutions = numberofNewExecutions + " " + entryTrail.getValue().get("NumberofNewExecutions");
			numberofNewTransitions = numberofNewTransitions + " " + entryTrail.getValue().get("NumberofNewTransitions");
			numberofNewStates = numberofNewStates + " " + entryTrail.getValue().get("NumberofNewStates");
			numberofNewExecutionsDivAllT = numberofNewExecutionsDivAllT + " " + String.format("%.6f", entryTrail.getValue().get("NumberofNewExecutionsDivAllT"));
			numberofNewTransitionsDivAllT = numberofNewTransitionsDivAllT + " " + String.format("%.6f", entryTrail.getValue().get("NumberofNewTransitionsDivAllT"));
			numberofNewStatesDivAllT = numberofNewStatesDivAllT + " " + String.format("%.6f", entryTrail.getValue().get("NumberofNewStatesDivAllT"));
			
		}
		
		constructedDeliveringSourceDataString_2 = timeCostofMOSASearch + "; "
				+ otherTimeCost_1 + "; "
				+ timeCostofExecution + "; "
				+ totalTimeCost + "; "
				+ totalExecutions + "; "
				+ numberofNewExecutions + "; "
				+ numberofNewTransitions + "; "
				+ numberofNewStates + "; "
				+ numberofNewExecutionsDivAllT + "; "
				+ numberofNewTransitionsDivAllT + "; "
				+ numberofNewStatesDivAllT + "; "
				;
		
		
		//call python
		String exe = "-";//python exe location
		String pythonFileName = "../StatisticsTest.py";//The python file name
		
		System.out.println(constructedDeliveringSourceDataString_1);
		System.out.println(constructedDeliveringSourceDataString_2);
		
		String[] cmdArr = new String[] {exe
				, pythonFileName
				, constructedDeliveringSourceDataString_1
				, constructedDeliveringSourceDataString_2
				};
		
		Process process = null;
		List<String> re = new ArrayList<String>();
		
		System.out.println("=== Begin to illustrate Output from Python! ===");
		
		try {
			process = Runtime.getRuntime().exec(cmdArr);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            int lineStartingIndex = 0;
            while ((line = in.readLine()) != null) {
            	
//            	System.out.println(line);
                if(lineStartingIndex > 0) {
                	lineStartingIndex --;
                }
                else {
                	re.add(line);
                }
                
            }
            
            in.close();
			process.waitFor();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<Double> results = new ArrayList<Double>();
		
		for(int i = 0; i < re.size(); i ++) {
			System.out.println(re.get(i));
			results.add(Double.parseDouble(re.get(i)));
		}
		
		return results;
	}
	
	
	public static HashMap<String, HashMap<String, Double>> calculateAverageOfAllTrails(HashMap<String, HashMap<Integer, HashMap<String, Double>>> allAddUpResult) {
		
		HashMap<String, HashMap<String, Double>> averageOfAllTrailResults = new HashMap<String, HashMap<String, Double>>();
		
		HashMap<String, HashMap<Integer, HashMap<String, Double>>> mapForExperiments = allAddUpResult;
		for(Map.Entry<String, HashMap<Integer, HashMap<String, Double>>> entryExp: mapForExperiments.entrySet()) {
			System.out.println("Trail: " + entryExp.getKey());
			Map<Integer, HashMap<String, Double>> mapForTrails = entryExp.getValue();
			
			double countTrails = 0;
			
			double timeCostofMOSASearch = 0;
			double otherTimeCost_1 = 0;
			double timeCostofExecution = 0;
			double totalTimeCost = 0;
			double totalExecutions = 0;
			double numberofNewExecutions = 0;
			double numberofNewTransitions = 0;
			double numberofNewStates = 0;
			
			double NEDivAllT = 0;
			double NTDivAllT = 0;
			double NSDivAllT = 0;
			
			for(Map.Entry<Integer, HashMap<String, Double>> entryTrail: mapForTrails.entrySet()) {
				countTrails = countTrails + 1;
				
				System.out.println("  Cycle: " + countTrails);
				System.out.println("    TimeCostofMOSASearch: " + entryTrail.getValue().get("TimeCostofMOSASearch"));
				System.out.println("    OtherTimeCost_1: " + entryTrail.getValue().get("OtherTimeCost_1"));
				System.out.println("    TimeCostofExecution: " + entryTrail.getValue().get("TimeCostofExecution"));
				System.out.println("    TotalTimeCost: " + entryTrail.getValue().get("TotalTimeCost"));
				System.out.println("    TotalExecutions: " + entryTrail.getValue().get("TotalExecutions"));
				System.out.println("    NumberofNewExecutions: " + entryTrail.getValue().get("NumberofNewExecutions"));
				System.out.println("    NumberofNewTransitions: " + entryTrail.getValue().get("NumberofNewTransitions"));
				System.out.println("    NumberofNewStates: " + entryTrail.getValue().get("NumberofNewStates"));
				
				System.out.println("    NEDivAllT: " + entryTrail.getValue().get("NumberofNewExecutionsDivAllT"));
				System.out.println("    NTDivAllT: " + entryTrail.getValue().get("NumberofNewTransitionsDivAllT"));
				System.out.println("    NSDivAllT: " + entryTrail.getValue().get("NumberofNewStatesDivAllT"));
				
				timeCostofMOSASearch = timeCostofMOSASearch + entryTrail.getValue().get("TimeCostofMOSASearch");
				otherTimeCost_1 = otherTimeCost_1 + entryTrail.getValue().get("OtherTimeCost_1");
				timeCostofExecution = timeCostofExecution + entryTrail.getValue().get("TimeCostofExecution");
				totalTimeCost = totalTimeCost + entryTrail.getValue().get("TotalTimeCost");
				totalExecutions = totalExecutions + entryTrail.getValue().get("TotalExecutions");
				numberofNewExecutions = numberofNewExecutions + entryTrail.getValue().get("NumberofNewExecutions");
				numberofNewTransitions = numberofNewTransitions + entryTrail.getValue().get("NumberofNewTransitions");
				numberofNewStates = numberofNewStates + entryTrail.getValue().get("NumberofNewStates");
				
				NEDivAllT = NEDivAllT + entryTrail.getValue().get("NumberofNewExecutionsDivAllT");
				NTDivAllT = NTDivAllT + entryTrail.getValue().get("NumberofNewTransitionsDivAllT");
				NSDivAllT = NSDivAllT + entryTrail.getValue().get("NumberofNewStatesDivAllT");
				
			}
			
			
			HashMap<String, Double> onceExpTemp = new HashMap<String, Double>();
			onceExpTemp.put("TimeCostofMOSASearch", (double)timeCostofMOSASearch/(countTrails*60000));
			onceExpTemp.put("OtherTimeCost_1", (double)otherTimeCost_1/countTrails);
			onceExpTemp.put("TimeCostofExecution", (double)timeCostofExecution/countTrails);
			onceExpTemp.put("TotalTimeCost", (double)totalTimeCost/(countTrails*60000));
			onceExpTemp.put("TotalExecutions", (double)totalExecutions/countTrails);
			onceExpTemp.put("NumberofNewExecutions", (double)numberofNewExecutions/countTrails);
			onceExpTemp.put("NumberofNewTransitions", (double)numberofNewTransitions/countTrails);
			onceExpTemp.put("NumberofNewStates", (double)numberofNewStates/countTrails);
			//add efficiency measures
			onceExpTemp.put("NumberofNewExecutionsDivAllT", NEDivAllT/countTrails);
			onceExpTemp.put("NumberofNewTransitionsDivAllT", NTDivAllT/countTrails);
			onceExpTemp.put("NumberofNewStatesDivAllT", NSDivAllT/countTrails);
			
			averageOfAllTrailResults.put(entryExp.getKey(), onceExpTemp);
			
		}
		
		
		return averageOfAllTrailResults;
	}
	
	
	public static void writeAverageResultsToExcelFile(HashMap<String, HashMap<String, Double>> collectedAveResults, String targetExcelFileName, String sheetName) {
		
		Workbook wb = null;
		Sheet sheet = null;
		
		
		try {
			
			File excelFile = new File(targetExcelFileName);
			if(!excelFile.exists()) {
				excelFile.createNewFile();
				wb = new HSSFWorkbook();
				OutputStream stream = new FileOutputStream(excelFile);
				wb.write(stream);
				stream.close();
				
			}
			
			InputStream is = new FileInputStream(excelFile);
			
			wb = new HSSFWorkbook(is);
			sheet = (Sheet) wb.createSheet(sheetName);
			
			Row rowTemp_0 = sheet.createRow(0);
			for(int i = 0; i < 12; i ++) {
				sheet.autoSizeColumn(i, true);
			}
			Cell cellTemp_in_Row_0 = null;
			cellTemp_in_Row_0 = rowTemp_0.createCell(0);
			cellTemp_in_Row_0.setCellValue("");
			cellTemp_in_Row_0 = rowTemp_0.createCell(1);
			cellTemp_in_Row_0.setCellValue("TimeCostofMOSASearch");
			cellTemp_in_Row_0 = rowTemp_0.createCell(2);
			cellTemp_in_Row_0.setCellValue("OtherTimeCost_1");
			cellTemp_in_Row_0 = rowTemp_0.createCell(3);
			cellTemp_in_Row_0.setCellValue("TimeCostofExecution");
			cellTemp_in_Row_0 = rowTemp_0.createCell(4);
			cellTemp_in_Row_0.setCellValue("TotalTimeCost");
			cellTemp_in_Row_0 = rowTemp_0.createCell(5);
			cellTemp_in_Row_0.setCellValue("TotalExecutions");
			cellTemp_in_Row_0 = rowTemp_0.createCell(6);
			cellTemp_in_Row_0.setCellValue("NumberofNewExecutions");
			cellTemp_in_Row_0 = rowTemp_0.createCell(7);
			cellTemp_in_Row_0.setCellValue("NumberofNewTransitions");
			cellTemp_in_Row_0 = rowTemp_0.createCell(8);
			cellTemp_in_Row_0.setCellValue("NumberofNewStates");
			cellTemp_in_Row_0 = rowTemp_0.createCell(9);
			cellTemp_in_Row_0.setCellValue("NumberofNewExecutionsDivAllT");
			cellTemp_in_Row_0 = rowTemp_0.createCell(10);
			cellTemp_in_Row_0.setCellValue("NumberofNewTransitionsDivAllT");
			cellTemp_in_Row_0 = rowTemp_0.createCell(11);
			cellTemp_in_Row_0.setCellValue("NumberofNewStatesDivAllT");
			
			List<String> expsSetting5 = new ArrayList<String>();
			List<String> expsSetting10 = new ArrayList<String>();
			List<String> expsSetting15 = new ArrayList<String>();
			String iterativeSetting = "";
			Map<String, HashMap<String, Double>> mapForExps_ = collectedAveResults;
			for(Map.Entry<String, HashMap<String, Double>> entryTrail: mapForExps_.entrySet()) {
				String temp = entryTrail.getKey();
				if(temp.contains("RS_NoObjective")) {
					iterativeSetting = temp;
				}
				else {
					if(temp.endsWith("-5")) {
						expsSetting5.add(temp);
					}
					else if(temp.endsWith("-10")) {
						expsSetting10.add(temp);
					}
					else {
						expsSetting15.add(temp);
					}
				}
			}
			List<String> expsSettingAll = new ArrayList<String>();
			int index = 0;
			for(index = 0; index < expsSetting5.size(); index ++) {
				expsSettingAll.add(expsSetting5.get(index));
			}
			for(index = 0; index < expsSetting10.size(); index ++) {
				expsSettingAll.add(expsSetting10.get(index));
			}
			for(index = 0; index < expsSetting15.size(); index ++) {
				expsSettingAll.add(expsSetting15.get(index));
			}
			expsSettingAll.add(iterativeSetting);
			
			
			Row rowTemp = null;
			Cell cellTemp = null;
			
			int countExps = 0;
			
			Map<String, HashMap<String, Double>> mapForExps = collectedAveResults;
//			for(Map.Entry<String, HashMap<String, Double>> entryTrail: mapForExps.entrySet()) {
			for(int i = 0; i < expsSettingAll.size(); i ++) {	
				countExps ++;
				
				rowTemp = sheet.createRow(countExps);
				cellTemp = rowTemp.createCell(0);
				cellTemp.setCellValue(expsSettingAll.get(i));
				
				System.out.println(expsSettingAll.get(i));
				Map<String, Double> mapForMeasures = collectedAveResults.get(expsSettingAll.get(i));
				
				cellTemp = rowTemp.createCell(1);
				cellTemp.setCellValue(mapForMeasures.get("TimeCostofMOSASearch"));
				cellTemp = rowTemp.createCell(2);
				cellTemp.setCellValue(mapForMeasures.get("OtherTimeCost_1"));
				cellTemp = rowTemp.createCell(3);
				cellTemp.setCellValue(mapForMeasures.get("TimeCostofExecution"));
				cellTemp = rowTemp.createCell(4);
				cellTemp.setCellValue(mapForMeasures.get("TotalTimeCost"));
				cellTemp = rowTemp.createCell(5);
				cellTemp.setCellValue(mapForMeasures.get("TotalExecutions"));
				cellTemp = rowTemp.createCell(6);
				cellTemp.setCellValue(mapForMeasures.get("NumberofNewExecutions"));
				cellTemp = rowTemp.createCell(7);
				cellTemp.setCellValue(mapForMeasures.get("NumberofNewTransitions"));
				cellTemp = rowTemp.createCell(8);
				cellTemp.setCellValue(mapForMeasures.get("NumberofNewStates"));
				cellTemp = rowTemp.createCell(9);
				cellTemp.setCellValue(mapForMeasures.get("NumberofNewExecutionsDivAllT"));
				cellTemp = rowTemp.createCell(10);
				cellTemp.setCellValue(mapForMeasures.get("NumberofNewTransitionsDivAllT"));
				cellTemp = rowTemp.createCell(11);
				cellTemp.setCellValue(mapForMeasures.get("NumberofNewStatesDivAllT"));
				
			}
			
			
			
			OutputStream stream = new FileOutputStream(excelFile);
			wb.write(stream);
			stream.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	
	//Process Step 1: from raw data file read results and write to an excel file
	public static void process_1_rawData2ExcelFile(String dataFileDir, String SAKind, int iterativeSetting, String excelFileName) {
		
		HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> result = ExtractInformationfromProcessLogToStatistics_forAllExperiments.traverseDataFileDirToCollectInternalResults(
				dataFileDir,
				SAKind,
				iterativeSetting
				);
		
		ExtractInformationfromProcessLogToStatistics_forAllExperiments.writeToExcelFileForStatisticsPreparation(
				result,
				excelFileName,
				SAKind + "-" + iterativeSetting
				);
		
	}
	
	//Process 1.2 add up results
	public static void process_1_2_readFromExcelFileToBasicStatisticsResultWriteToExcelFile(String sourceExcelFileNameFromS1, String targetExcelFileNameFile) {
		
		
		//S0. Organize all the data
		HashMap<String, HashMap<Integer, HashMap<String, Double>>> allExperimenetResults = ExtractInformationfromProcessLogToStatistics_forAllExperiments.readCollectedDataFromExcelFile(
				sourceExcelFileNameFromS1
				);
		
		for(Map.Entry<String, HashMap<Integer, HashMap<String, Double>>> entry: allExperimenetResults.entrySet()) {
			System.out.println(entry.getKey());
		}
		
		//S1. calculate the average of trails
		HashMap<String, HashMap<String, Double>> averageResultsForAllExps = ExtractInformationfromProcessLogToStatistics_forAllExperiments.calculateAverageOfAllTrails(
				allExperimenetResults
				);
		
		//S2. write to excel the file. Start from collectedResults
		ExtractInformationfromProcessLogToStatistics_forAllExperiments.writeAverageResultsToExcelFile(
				averageResultsForAllExps
				, targetExcelFileNameFile
				, "ST"
				);
		
	}
	
	
	//Process Step 2: from raw data file read results and write to an excel file
	public static void process_2_readFromExcelFileToStatisticsResultWriteToExcelFile(List<List<String>> statisticsSampleCouples, String sourceExcelFileNameFromS1, String targetExcelFileNameFile) {
		
		
		//S0. Organize all the data
		HashMap<String, HashMap<Integer, HashMap<String, Double>>> allExperimenetResults = ExtractInformationfromProcessLogToStatistics_forAllExperiments.readCollectedDataFromExcelFile(
				sourceExcelFileNameFromS1
				);
		
		for(Map.Entry<String, HashMap<Integer, HashMap<String, Double>>> entry: allExperimenetResults.entrySet()) {
			System.out.println(entry.getKey());
		}
		
		//S1. Choose the target of Statistics Test: to every couple, calculate A and p on all the measures
		//S1.1. set statistics couples
		
		
		//S1.2. calculate A and p
		HashMap<String, HashMap<String, HashMap<String, Double>>> collectedResults = new HashMap<String, HashMap<String, HashMap<String, Double>>>();
		for(int i = 0; i < statisticsSampleCouples.size(); i ++) {
			String titleTemp = statisticsSampleCouples.get(i).get(0) + " vs " + statisticsSampleCouples.get(i).get(1);
			
			HashMap<String, HashMap<String, Double>> statisticsResultTemp = ExtractInformationfromProcessLogToStatistics_forAllExperiments.statisticTestForAvaluePvalue(
					allExperimenetResults.get(statisticsSampleCouples.get(i).get(0))
					, allExperimenetResults.get(statisticsSampleCouples.get(i).get(1))
					);
			collectedResults.put(titleTemp, statisticsResultTemp);
		}
		
		//S2. write to excel the file. Start from collectedResults
		ExtractInformationfromProcessLogToStatistics_forAllExperiments.writeStatisticsResultsToExcelFile(
				collectedResults
				, targetExcelFileNameFile
				, "ST"
				);
		
	}
	
	public static HashMap<String, HashMap<Integer, HashMap<String, Double>>> readCollectedDataFromExcelFile(String excelFileName) {
		
		
		Workbook wb = null;
		Sheet sheet = null;
		String sheetName = "";
		
		HashMap<String, HashMap<Integer, HashMap<String, Double>>> allExperimenetResults 
		= new HashMap<String, HashMap<Integer, HashMap<String, Double>>>();
		
		HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> onceExperimentResultTemp = null;
		
		try {
			
			File excelFile = new File(excelFileName);
			if(!excelFile.exists()) {
				System.out.println("************ Error! The Excel File is Not Found! ************");
				return null;
			}
			
			InputStream is = new FileInputStream(excelFile);
			wb = new HSSFWorkbook(is);
			
			for(int sheetNum = 0; sheetNum < wb.getNumberOfSheets(); sheetNum ++) {
				
				onceExperimentResultTemp = new HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>();
				
				sheet = wb.getSheetAt(sheetNum);
				sheetName = sheet.getSheetName();
				
				int firstRowNum = sheet.getFirstRowNum();
				int lastRowNum = sheet.getLastRowNum();
				
				System.out.println("firstRowNum, lastRowNum: " + firstRowNum + ", " + lastRowNum);
				
				
				HashMap<Integer, HashMap<String, Integer>> onceTrailResultTemp = null;
				int trailNo = -1;
				
				for(int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum ++) {
					
					Row rowTemp = sheet.getRow(rowNum);
					if(rowTemp == null) {
						continue;
					}
					int firstCellNum = rowTemp.getFirstCellNum();
					int lastCellNum = rowTemp.getLastCellNum();
					
					System.out.println("firstCellNum, lastCellNum: " + firstCellNum + ", " + lastCellNum);
					
					Cell cellFirstTemp = rowTemp.getCell(firstCellNum);
					String cellFirstContentTemp = getCellValue(cellFirstTemp);
					if(cellFirstContentTemp.startsWith("Trail:")) {
						
						if(onceTrailResultTemp == null) {
							//do nothing
						}
						else {
							onceExperimentResultTemp.put(trailNo, onceTrailResultTemp);
						}
						
						String[] src_trailNo = cellFirstContentTemp.split(": ");
						trailNo = Integer.parseInt(src_trailNo[1]);
						
						onceTrailResultTemp = new HashMap<Integer, HashMap<String, Integer>>();
						for(int cellNum = firstCellNum+1; cellNum < lastCellNum; cellNum ++) {
							onceTrailResultTemp.put(cellNum, new HashMap<String, Integer>());
						}	
					}
					else {
						//Cells recording list
						String keyLabel = null;
						for(int cellNum = firstCellNum; cellNum < lastCellNum; cellNum ++) {
							Cell cellTemp = rowTemp.getCell(cellNum);
							String cellContentTemp = getCellValue(cellTemp);
							System.out.println(cellContentTemp);
							if(cellNum == firstCellNum) {
								keyLabel = cellContentTemp;
//								System.out.println("Test-1: " + keyLabel);
							}
							else {
//								System.out.println("Test-2-: " + keyLabel + ", " + Integer.parseInt(cellContentTemp));
								onceTrailResultTemp.get(cellNum).put(keyLabel, Integer.parseInt(cellContentTemp));
//								HashMap<String, Integer> tttt = onceTrailResultTemp.get(cellNum);
//								tttt.put(keyLabel, Integer.parseInt(cellContentTemp));
//								onceTrailResultTemp.put(cellNum, tttt);
//								ExtractInformationfromProcessLogToStatistics.printAddUpOnceTrailResult(onceTrailResultTemp);
							}
						}
//						ExtractInformationfromProcessLogToStatistics.printAddUpOnceTrailResult(onceTrailResultTemp);
					}
					
				}
				
//				printOnceTrailResult(onceExperimentResultTemp);
				
				HashMap<Integer, HashMap<String, Double>> addUpOnceExperimentResultTemp = 
						ExtractInformationfromProcessLogToStatistics_forAllExperiments.addUpAllTestCycles(onceExperimentResultTemp);
				allExperimenetResults.put(sheetName, addUpOnceExperimentResultTemp);
				
			}
			
			
			
			wb.close();//close sheet?
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return allExperimenetResults;
	}
	
	public static String getCellValue(Cell cell) {
		String cellValue = "";
		if(cell == null) {
			return cellValue;
		}
		cell.setCellType(Cell.CELL_TYPE_STRING);
//		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
//			cell.setCellType(Cell.CELL_TYPE_STRING);
//		}
//		switch(cell.getCellType()) {
//		case Cell.CELL_TYPE_NUMERIC:
//			cellValue = String.valueOf(cell.getNumericCellValue());
//		}
		cellValue = cell.getStringCellValue();
		
		
		return cellValue;
	}
	
	public static void writeStatisticsResultsToExcelFile(HashMap<String, HashMap<String, HashMap<String, Double>>> collectedResults, String targetExcelFileName, String sheetName) {
		
		Workbook wb = null;
		Sheet sheet = null;
		
		
		try {
			
			File excelFile = new File(targetExcelFileName);
			if(!excelFile.exists()) {
				excelFile.createNewFile();
				wb = new HSSFWorkbook();
				OutputStream stream = new FileOutputStream(excelFile);
				wb.write(stream);
				stream.close();
				
			}
			
			InputStream is = new FileInputStream(excelFile);
			
			wb = new HSSFWorkbook(is);
			sheet = (Sheet) wb.createSheet(sheetName);
			
			
			
			Row rowTemp = null;
			Cell cellTemp = null;
			
			int countTrails = -1;
			
			Map<String, HashMap<String, HashMap<String, Double>>> mapForTrails = collectedResults;
			for(Map.Entry<String, HashMap<String, HashMap<String, Double>>> entryTrail: mapForTrails.entrySet()) {
				
				countTrails ++;
				
				sheet.autoSizeColumn(0, true);
				
				Row rowTemp_0 = sheet.createRow(countTrails*3);
				cellTemp = rowTemp_0.createCell(0);
				cellTemp.setCellValue(entryTrail.getKey());
				
				Row rowTemp_1 = sheet.createRow(countTrails*3+1);
				cellTemp = rowTemp_1.createCell(0);
				cellTemp.setCellValue("A12");
				
				Row rowTemp_2 = sheet.createRow(countTrails*3+2);
				cellTemp = rowTemp_2.createCell(0);
				cellTemp.setCellValue("P");
				
				System.out.println(entryTrail.getKey());
				Map<String, HashMap<String, Double>> mapForCycles = entryTrail.getValue();
				
				HashMap<String, Double> A12ResultTemp = mapForCycles.get("A12");
				HashMap<String, Double> pValueResultTemp = mapForCycles.get("P");
				
				int countCellinRow = 0;
				
				for(Map.Entry<String, Double> entryMeasure: A12ResultTemp.entrySet()) {
					countCellinRow ++;
					
					String measureNameTemp = entryMeasure.getKey();
					double a12ValueTemp = entryMeasure.getValue();
					double pValueTemp = pValueResultTemp.get(measureNameTemp);
					
					cellTemp = rowTemp_0.createCell(countCellinRow);
					cellTemp.setCellValue(measureNameTemp);
					
					cellTemp = rowTemp_1.createCell(countCellinRow);
					cellTemp.setCellValue(a12ValueTemp);
					
					cellTemp = rowTemp_2.createCell(countCellinRow);
					cellTemp.setCellValue(pValueTemp);
				}
				
			}
			
			
			
			OutputStream stream = new FileOutputStream(excelFile);
			wb.write(stream);
			stream.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		
		
		
	}
	
	
	
	public static void writeNoIterativeResultsToExcelFile(HashMap<Integer, HashMap<Integer, List<Integer>>> collectedResults, String targetExcelFileName, String sheetName) {
		
		Workbook wb = null;
		Sheet sheet = null;
		
		try {
			
			File excelFile = new File(targetExcelFileName);
			if(!excelFile.exists()) {
				excelFile.createNewFile();
				wb = new HSSFWorkbook();
				OutputStream stream = new FileOutputStream(excelFile);
				wb.write(stream);
				stream.close();
				
			}
			
			InputStream is = new FileInputStream(excelFile);
			
			wb = new HSSFWorkbook(is);
			sheet = (Sheet) wb.createSheet(sheetName);
			
			Row rowTemp = null;
			Cell cellTemp = null;
			
			int countTrails = -1;
			
			Map<Integer, HashMap<Integer, List<Integer>>> mapForTrails = collectedResults;
			for(Map.Entry<Integer, HashMap<Integer, List<Integer>>> entryTrail: mapForTrails.entrySet()) {
				
				countTrails ++;
				
				sheet.autoSizeColumn(0, true);
				
				Row rowTemp_0 = sheet.createRow(countTrails*3);
//				cellTemp = rowTemp_0.createCell(0);
//				cellTemp.setCellValue(entryTrail.getKey());
				
				Row rowTemp_1 = sheet.createRow(countTrails*3+1);
//				cellTemp = rowTemp_1.createCell(0);
//				cellTemp.setCellValue("A12");
				
				Row rowTemp_2 = sheet.createRow(countTrails*3+2);
//				cellTemp = rowTemp_2.createCell(0);
//				cellTemp.setCellValue("P");
				
				System.out.println(entryTrail.getKey());
				HashMap<Integer, List<Integer>> mapForCycles = entryTrail.getValue();
				
				List<Integer> cycle_1_ResultTemp = mapForCycles.get(1);
				
				for(int x = 0; x < 24; x ++) {
					
					List<Integer> cycleResultTemp = mapForCycles.get(x+1);
					
					cellTemp = rowTemp_0.createCell(x);
					cellTemp.setCellValue(cycleResultTemp.get(0));
					
					cellTemp = rowTemp_1.createCell(x);
					cellTemp.setCellValue(cycleResultTemp.get(1));
					
					cellTemp = rowTemp_2.createCell(x);
					cellTemp.setCellValue(cycleResultTemp.get(2));
				}
				
			}
			
			OutputStream stream = new FileOutputStream(excelFile);
			wb.write(stream);
			stream.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public static HashMap<Integer, HashMap<Integer, List<Integer>>> traverseDataFileDirToCollectExecutions4NoIterativeExp(String dataDirectory, String MOSAType, int iterativeSetting, String repository4CollectAllExecutions) {
		
		//To return >> HashMap:trails >> trail:cycles >> cycle:tempResult
//		HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> resultForWholeStatistics = new HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>();
		HashMap<Integer, HashMap<Integer, List<Integer>>> resultForWholeStatistics = new HashMap<Integer, HashMap<Integer, List<Integer>>>();
		
		int count = 0;
		
		//For validation
		String SAType = MOSAType;
		int numberofTCsperTestCycle = iterativeSetting;
		
		dataDirectory = dataDirectory + MOSAType + "/TestCasesperTestCycle_" + iterativeSetting + "/";
		
		File dirFile = new File(dataDirectory);
		if(!dirFile.isDirectory()) {
			if(dirFile.isFile()) {
				System.out.println("#################### Error: basic dir isFile ####################");
			}
			System.out.println("#################### Error: basic dir is not file or dir ####################");
			return null;
		}
		
		
		
		String[] fileList = dirFile.list();
		int lenDirFiles = fileList.length;
		for(int i = 0; i < lenDirFiles; i ++) {
			
			
			
			if(fileList[i].startsWith(STRING_DATA_FOLDER_TRAIL_LOOP_NUMBER)) {
				
				System.out.println(fileList[i]);
				
				//once trail
//				HashMap<Integer, HashMap<String, Integer>> trailTemp = new HashMap<Integer, HashMap<String, Integer>>();
				HashMap<Integer, List<Integer>> trailTemp = new HashMap<Integer, List<Integer>>();
				
				//Key: trail NO
				String[] src4TrailNo = fileList[i].split(STRING_DATA_FOLDER_TRAIL_LOOP_NUMBER + "_");
				int trailNOTemp = Integer.parseInt(src4TrailNo[1]);
				
				String subFileDir = dataDirectory + "/" + fileList[i];
				File subdirFile = new File(subFileDir);
				 
				 
				if(!subdirFile.isDirectory()) {
					if(subdirFile.isFile()) {
						System.out.println("#################### Error: basic dir isFile ####################");
					}
					System.out.println("#################### Error: basic dir is not file or dir ####################");
					return null;
				}
				 
				String[] subfileList = subdirFile.list();
				int lenSubDirFiles = subfileList.length;
				String[] orderedCycleTitle = new String[30];
				for(int jj = 0; jj < 30; jj ++) {
					orderedCycleTitle[jj] = null;
				}
				for(int j = 0; j < lenSubDirFiles; j ++) {
					if(subfileList[j].startsWith(STRING_DATA_FOLDER_TEST_CYCLE_NUMBER)) {
//						System.out.println(subfileList[j]);
						String[] src = subfileList[j].split("_");
						int seqNo = Integer.parseInt(src[1]);
						orderedCycleTitle[seqNo] = subfileList[j];
					}
				}
				 
//				for(int jj = 0; jj < 25; jj ++) {
//					System.out.println(orderedCycleTitle[jj]);
//				}
				 
				int countCycleSeq = 1;
				while(orderedCycleTitle[countCycleSeq] != null) {
					System.out.println(orderedCycleTitle[countCycleSeq]);
					countCycleSeq ++;
				}
				countCycleSeq = countCycleSeq - 2;
				
				StateMachine statemachine = null;
//				System.out.println("# fileDirSeq: " + countCycleSeq);
				for(int j = 1; j <= countCycleSeq; j ++) {
					String outputDir = subdirFile + "/" + orderedCycleTitle[j];
					File outputDirFile = new File(outputDir);
					
					if(!outputDirFile.isDirectory()) {
						if(outputDirFile.isFile()) {
							System.out.println("#################### Error: basic dir isFile ####################");
						}
						System.out.println("#################### Error: basic dir is not file or dir ####################");
						return null;
					}
					
					String SATypeRealTime = "";
					String[] outputDirList = outputDirFile.list();
					int outputDirLength = outputDirList.length;
//					System.out.println("# fileDir: " + outputDir);
//					System.out.println("# len: " + outputDirLength);
					for(int k = 0; k < outputDirLength; k ++) {
//						System.out.println("### " + outputDirList[k]);
						if(outputDirList[k].startsWith(STRING_DATA_FILE_JMETAL)) {
							String[] src4Validation = outputDirList[k].split("OutputResults_jMetal_");
							SATypeRealTime = src4Validation[1];
						}
					}
					 
					HashMap<String, Integer> resultTemp = null;
					
					String logfilePath = outputDir + "/log.txt";
					String repositoryExecutionsFile = outputDir + "/" + "05BehaviourPairs-2.0.txt";
					String stateMachineFile = outputDir + "/" + "02StateMachine-1.0.txt";
					String repositoryForAll = repository4CollectAllExecutions;
					
					if(j == 1) {
						statemachine = ReadFromFiles.ReadStateMachineFromFile_v201606(stateMachineFile);
					}
					
					List<Solution> executions_temp = ExtractInformationfromProcessLogToStatistics_forAllExperiments.extractExecutionsFromOneCycle(
							 repositoryExecutionsFile, 
							 statemachine, 
							 repositoryForAll
							 );
					//check the new
					List<Integer> reTemp = ExtractInformationfromProcessLogToStatistics_forAllExperiments.checkNewOutputs(executions_temp, statemachine);
					
					
					
//					System.out.println("Before validate: " + logfilePath);
//					File logFile = new File(logfilePath);
//					if(logFile.isFile()) {
////						System.out.println("validate: " + logfilePath);
//						System.out.println("Cycle No: " + j);
//						 
//						//deal with data files
//						resultTemp = ExtractInformationfromProcessLogToStatistics.extractExecutionsFromOneDataFile(logfilePath);
//						 
//					}
					 
					if(!SAType.equals(SATypeRealTime) 
//							 || !(numberofTCsperTestCycle == resultTemp.get("totalExecutions"))
							) {
						System.out.println("++++++++++++++++++ Error when collecting results![ SAType: " + SAType + ", SATypeRealTime: " + SATypeRealTime + "] ++++++++++++++++++");
						return null;
					}
					
					trailTemp.put(j, reTemp);
					
					
				}
				 
				resultForWholeStatistics.put(trailNOTemp, trailTemp);
				 
			}
			
			
			
			
		}
		
		
		
		
		
		return resultForWholeStatistics;
		
	}
	
	public static List<Solution> extractExecutionsFromOneCycle(String repositoryFile, StateMachine originalStateMachine, String repository4CollectExecutions) {
		
		List<Solution> executions = ReadFromFiles.ReadBehaviourPairsFromFile_v201606(repositoryFile, originalStateMachine);
		
//		Write2Files.WriteSolution2File_v202108(executions, statemachine, repository4CollectExecutions, originalStateMachineFile);
		return executions;
	}
	
	public static List<Integer> checkNewOutputs(List<Solution> executions, StateMachine stateMachine) {
		
		List<Integer> results = new ArrayList<Integer>();
		List<Transition> SetofPossibleNewTransition = new ArrayList<Transition>();
		
		//transform executions to transitions
		for(int x  = 0; x < executions.size(); x ++) {
			
			Solution s_bp = executions.get(x);
			SystemBehavior bp = Solution.TransformSolution2BPairValueSet_v201606(s_bp, stateMachine);
			Transition transitionfromBehaviorPair = Solution.TransformBehaviourPair2Transition_v201610(bp);
			
			SetofPossibleNewTransition.add(transitionfromBehaviorPair);
		}
		
		int countNewExecutions = 0;
		int countNewTransitions = 0;
		int countNewStates = 0;
		
		for(int i = 0; i < SetofPossibleNewTransition.size(); i ++) {
			
			Transition transitionTemp = SetofPossibleNewTransition.get(i);
			int zi = stateMachine.CheckNewTransition(transitionTemp);
			if(zi == 1 || zi == 2) {
				countNewExecutions ++;
			}
			
		}
		
		for(int i = 0; i < SetofPossibleNewTransition.size(); i ++) {
			
			Transition transitionTemp = SetofPossibleNewTransition.get(i);
			int zi = stateMachine.CheckNewTransition(transitionTemp);
			if(zi == 1 || zi == 2) {
				countNewTransitions ++;
				if(zi == 1) {
					countNewStates ++;
				}
			}
			stateMachine.UpdateStateMachinebyAddANewTransition(transitionTemp);
		}
		
		results.add(countNewExecutions);
		results.add(countNewTransitions);
		results.add(countNewStates);
		
		return results;
	}
	
	
	
	
	
	
	
	
	
	public static void main(String args[]) {
		
		System.out.println("Read Log Files to Statistics...");
		
		String targetExcelFileName = "。.xls";
		String excelFileName = ".xls";
		
		
		//================ process 1 =======================
		String dataFileDir = "/";
		String SAKind = "SPEA2";
		int iterativeSetting = 5;
		
		targetExcelFileName = ".xls";
		excelFileName = ".xls";
		
		
		
		ExtractInformationfromProcessLogToStatistics_forAllExperiments.process_1_rawData2ExcelFile(
				dataFileDir, 
				SAKind, 
				iterativeSetting, 
				excelFileName
				);
		
		
		
		//================ process 1.2 =======================
		targetExcelFileName = ".xls";
		
		ExtractInformationfromProcessLogToStatistics_forAllExperiments.process_1_2_readFromExcelFileToBasicStatisticsResultWriteToExcelFile(
				excelFileName
				, targetExcelFileName
				);
		
		
		
		//================ process 2 =======================
		List<String> sampleCoupleTemp = null;
		List<List<String>> statisticsSampleCouples = new ArrayList<List<String>>();
		
		
		
		
//		targetExcelFileName = ".xls";
		targetExcelFileName = ".xls";
		
		
		
		List<String> titles = new ArrayList<String>();
		titles.add("NSGA-II-5"); titles.add("NSGA-II-10"); titles.add("NSGA-II-15");
		titles.add("MOCell-5"); titles.add("MOCell-10"); titles.add("MOCell-15");
		titles.add("SPEA2-5"); titles.add("SPEA2-10"); titles.add("SPEA2-15");
		titles.add("NSGA-III-5"); titles.add("NSGA-III-10"); titles.add("NSGA-III-15");
		titles.add("CellDE-5"); titles.add("CellDE-10"); titles.add("CellDE-15");
		
//		System.out.println(titles.size());
		
		for(int i = 0; i < titles.size() - 1; i ++) {
			
			for(int j = i + 1; j < titles.size(); j ++) {
				sampleCoupleTemp = new ArrayList<String>();
				sampleCoupleTemp.add(titles.get(i)); sampleCoupleTemp.add(titles.get(j)); statisticsSampleCouples.add(sampleCoupleTemp);
			}
			
		}
		
		System.out.println(statisticsSampleCouples.size());
		
		
		//Process 2: Read from the excel file and conduct statistics test, and write the results to another excel file
		ExtractInformationfromProcessLogToStatistics_forAllExperiments.process_2_readFromExcelFileToStatisticsResultWriteToExcelFile(
				statisticsSampleCouples
				, excelFileName
				, targetExcelFileName
				);
		
		
		
		
	}
	
	
}
