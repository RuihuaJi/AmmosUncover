package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.process;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.search.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.systemcontrolmodule.*;
import org.uma.jmetal.solution.DoubleSolution;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution.ASUBERunner;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution.ProblemPSBGenwithWDVRules;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution.Runner.*;

public class Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator {

	ASUBERunner saRunnerCase;
	int[] objectives;
	String referenceParetoFront;
	
	int TCsOneCycleNumber;//5 or 10, or 15...
	int trialProcessLoopNum;
	int trialTestCycleNum;
	
	String experimentLabelFileFolder;
	
	String originalStateMachineFile; 
	String originalStateMachineFile_InitialBackup;
	
	String originalTestCasesFile;
	String originalTestCasesFile_InitialBackup;
	
	String resultTestCasesFile;
	String resultTestCasesFile_InitialBackup;
	
	String updatedStateMachineFile;
	String updatedStateMachineFile_InitialBackup;
	
	String resultDSwithObjFile;
	String resultDSwithObjFile_InitialBackup;
	
	String testIntermediaResultLabel;
	String testIntermediaResultLabel_InitialBackup;
	
	String executedTestCasesinRealityValuesFile;
	String executedTestCasesinRealityValuesFile_InitialBackup;
	String executedTestCasesinFormalValuesFile;
	String executedTestCasesinFormalValuesFile_InitialBackup;
	
	String allParetoFrontswithObjsFile;
	String allParetoFrontswithObjsFile_InitialBackup;
	
	int maxiExecutedTestCasesNum;
	int maxiTrialProcessLoopNum;
	HandleDuplicatedFileWrite handleDuplicatedFileWrite;
	
	//Construct when initializing based on the input filename string
	StateMachine existingstatemachine;
	List<Solution> existingtestcases;
	String updatedStateMachineFileinNextTestCycle;
	String updatedTestCasesFileinNextTestCycle;
	OutConsole2File outConsole2File;
	
	/**
	 * Data Constructed System
	 * */
	public DSJitsiDesktop dsJitsiDesktop;
	
	
	
	public Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator(ASUBERunner saRC, int[] objs, String refPF, int TCsNum, int tryPLoopNum, int tryTCycleNum, String eLFF, String oSMF, String oTCF, String reTCF, String upSMF, String reDSObF, String testIRLabel, String eTCsinRVsF, String eTCsinFVsF, String aPFswithObjsF) {
		System.out.println("==================== Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator() (all parameters) ====================");
		
		this.saRunnerCase = saRC;
		this.objectives = objs;
		this.referenceParetoFront = refPF;
		
		this.TCsOneCycleNumber = TCsNum;//Number of Test Cases per test cycle, e.g., 5 or 10 or 15
		this.trialProcessLoopNum = tryPLoopNum;//the index of trials (We conduct each configuration for many trails)
		this.trialTestCycleNum = tryTCycleNum;//the index of test cycle.
		
		
		this.experimentLabelFileFolder = eLFF;
		
		this.originalStateMachineFile = oSMF;
		this.originalTestCasesFile = oTCF;
		this.resultTestCasesFile = reTCF;
		this.updatedStateMachineFile = upSMF;
		
		this.resultDSwithObjFile = reDSObF;
		
		this.testIntermediaResultLabel = testIRLabel;
		//Algorithm+NumofTCsperTestCycle+NOofTry+timeformat+[NO.TC+NO.path]
		
		this.executedTestCasesinRealityValuesFile = eTCsinRVsF;
		this.executedTestCasesinFormalValuesFile = eTCsinFVsF;
		
		this.allParetoFrontswithObjsFile = aPFswithObjsF;
		
		
		this.originalStateMachineFile_InitialBackup = this.originalStateMachineFile;
		this.originalTestCasesFile_InitialBackup = this.originalTestCasesFile;
		this.resultTestCasesFile_InitialBackup = this.resultTestCasesFile;
		this.updatedStateMachineFile_InitialBackup = this.updatedStateMachineFile;
		this.resultDSwithObjFile_InitialBackup = this.resultDSwithObjFile;
		this.testIntermediaResultLabel_InitialBackup = this.testIntermediaResultLabel;
		this.executedTestCasesinRealityValuesFile_InitialBackup = this.executedTestCasesinRealityValuesFile;
		this.executedTestCasesinFormalValuesFile_InitialBackup = this.executedTestCasesinFormalValuesFile;
		
		this.allParetoFrontswithObjsFile_InitialBackup = this.allParetoFrontswithObjsFile;
		
		this.maxiExecutedTestCasesNum = 120;//set manually
		this.maxiTrialProcessLoopNum = 10;//set manually
		this.handleDuplicatedFileWrite = new HandleDuplicatedFileWrite();//set manually
		this.outConsole2File = new OutConsole2File();//set manually
	}
	
	//preset some default parameters (construction method)
	public Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator(ASUBERunner saRC, String refPF, int TCsNum, int tryPLoopNum, int tryTCycleNum, String eLFF, int maxiExecutedTestCasesNum, int maxiTrialProcessLoopNum) {
		System.out.println("==================== Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator() (less parameters) ====================");
		
		this.saRunnerCase = saRC;
		this.referenceParetoFront = refPF;
		
		this.TCsOneCycleNumber = TCsNum;//Number of Test Cases per test cycle, e.g., 5 or 10 or 15
		this.trialProcessLoopNum = tryPLoopNum;//the index of trials (We conduct each configuration for many trails)
		this.trialTestCycleNum = tryTCycleNum;//the index of test cycle.
		
		this.experimentLabelFileFolder = eLFF;
		
		this.maxiExecutedTestCasesNum = maxiExecutedTestCasesNum;//the maximum of Executable Test Cases, in AmmosUncover, we set 120.
		this.maxiTrialProcessLoopNum = maxiTrialProcessLoopNum;//the maximum of trials (at least 10 for the randomness of this experiment)
		
	}
	//preset some default parameters (setting method)
	public void setParametersofFileSystem() {
		System.out.println("==================== setParametersofFileSystem() ====================");
		
		int[] objs = {1, 1, 1, 1};
		
		this.objectives = objs;//The operation of activating objectives is fixed in most cases.
		this.originalStateMachineFile = "02StateMachine-1.0.txt"; //the State Machine at the beginning of each test cycle (before updated)
		this.originalTestCasesFile = "03BehaviourPairs-1.0.txt"; //the possibly-existing system execution repository (before adding newly-generated PSE)
		this.resultTestCasesFile = "04BehaviourPairs-1.0.txt"; //the generated possibly-existing system executions
		this.updatedStateMachineFile = "06StateMachine-1.0.txt"; //the updated State Machine
		this.resultDSwithObjFile = "04-BehaviourPairswithObjs-1.0.txt"; //the generated possibly-existing system executions in Pareto Front format with objective values 
		this.testIntermediaResultLabel = "IR/"; //[testIntermediaResultLabel]
		this.executedTestCasesinRealityValuesFile = "05BehaviourPairs-1.0.txt"; //truly-existing system executions in real values
		this.executedTestCasesinFormalValuesFile = "05BehaviourPairs-2.0.txt"; //truly-existing system executions in formal values
		this.allParetoFrontswithObjsFile = "04-ParetoFrontswithObjs-1.0.txt";//the final population of PFs generated from MOSA
		
		this.originalStateMachineFile_InitialBackup = this.originalStateMachineFile;
		this.originalTestCasesFile_InitialBackup = this.originalTestCasesFile;
		this.resultTestCasesFile_InitialBackup = this.resultTestCasesFile;
		this.updatedStateMachineFile_InitialBackup = this.updatedStateMachineFile;
		this.resultDSwithObjFile_InitialBackup = this.resultDSwithObjFile;
		this.testIntermediaResultLabel_InitialBackup = this.testIntermediaResultLabel;
		this.executedTestCasesinRealityValuesFile_InitialBackup = this.executedTestCasesinRealityValuesFile;
		this.executedTestCasesinFormalValuesFile_InitialBackup = this.executedTestCasesinFormalValuesFile;
		this.allParetoFrontswithObjsFile_InitialBackup = this.allParetoFrontswithObjsFile;
		
		this.handleDuplicatedFileWrite = new HandleDuplicatedFileWrite();//set manually
		this.outConsole2File = new OutConsole2File();//set manually
	} 
	
	
	public void initialization() {

		System.out.println("==================== initialization() ====================");
		
		String oSMF = this.originalStateMachineFile_InitialBackup;
		String oTCF = this.originalTestCasesFile_InitialBackup;
		
		System.out.println("----------------------------------- for check: " + this.saRunnerCase.ALGO);
		
		String experimentLabel_0 = this.saRunnerCase.ALGO +
				"/TestCasesperTestCycle_" + this.TCsOneCycleNumber +
				"/TrialLoop_" + this.trialProcessLoopNum + 
				"/TestCycle_" + this.trialTestCycleNum + 
				"/"
				;
		String experimentLabel_1 = "[" 
				+ LocalDateTime.now().format(this.saRunnerCase.formatter) + "]"
				+ "[Test]"
				;
		String experimentLabel_2 = this.saRunnerCase.ALGO + 
				"/TestCasesperTestCycle_" + this.TCsOneCycleNumber +
				"/TrialLoop_" + this.trialProcessLoopNum + 
				"/TestCycle_" + (this.trialTestCycleNum + 1) +
				"/"
				;
		
		//my log files (the only trigger for outputing to files)
		String oC2F = this.experimentLabelFileFolder + experimentLabel_0 + "log.txt";
		File fileforOC2F = new File(oC2F);
		if(!fileforOC2F.getParentFile().exists()) {fileforOC2F.getParentFile().mkdirs();}
		this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(oC2F);
		this.outConsole2File.setOutConsole2File(oC2F);
		
		if(this.trialTestCycleNum > 1) {
			this.originalStateMachineFile = this.experimentLabelFileFolder + experimentLabel_0 + this.originalStateMachineFile_InitialBackup;
			this.originalTestCasesFile = this.experimentLabelFileFolder + experimentLabel_0 + this.originalTestCasesFile_InitialBackup;
		}
		else {//tryLNum(this.trialTestCycleNum) == 1
			this.originalStateMachineFile = this.experimentLabelFileFolder + "InitialFiles/02StateMachine-1.0.txt";
			this.originalTestCasesFile = this.experimentLabelFileFolder + "InitialFiles/03BehaviourPairs-1.0.txt";
		}
		this.resultTestCasesFile = this.experimentLabelFileFolder + experimentLabel_0 + this.resultTestCasesFile_InitialBackup;
		this.updatedStateMachineFile = this.experimentLabelFileFolder + experimentLabel_0 + this.updatedStateMachineFile_InitialBackup;
		this.resultDSwithObjFile = this.experimentLabelFileFolder + experimentLabel_0 + this.resultDSwithObjFile_InitialBackup;
		this.executedTestCasesinRealityValuesFile = this.experimentLabelFileFolder + experimentLabel_0 + this.executedTestCasesinRealityValuesFile_InitialBackup;
		this.executedTestCasesinFormalValuesFile = this.experimentLabelFileFolder + experimentLabel_0 + this.executedTestCasesinFormalValuesFile_InitialBackup;
		
		this.allParetoFrontswithObjsFile = this.experimentLabelFileFolder + experimentLabel_0 + this.allParetoFrontswithObjsFile_InitialBackup;
		
		this.testIntermediaResultLabel = this.experimentLabelFileFolder + experimentLabel_0 + this.testIntermediaResultLabel_InitialBackup + experimentLabel_1;
		
		StateMachine s = ReadFromFiles.ReadStateMachineFromFile_v201606(this.originalStateMachineFile);
		this.existingstatemachine = s;
		if(this.trialTestCycleNum == 1) {//If it is the first test cycle, we need to copy the original state machine to the current test process folder.
			String oSMFforCheck = this.experimentLabelFileFolder + experimentLabel_0 + oSMF;
			this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(oSMFforCheck);
			WriteStateMachine2File.writeStateMachine2File(this.existingstatemachine, oSMFforCheck);
			String oTCFforCheck = this.experimentLabelFileFolder + experimentLabel_0 + oTCF;
			this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(oTCFforCheck);
			File fileforOTCF = new File(oTCFforCheck);
			if(!fileforOTCF.getParentFile().exists()) {fileforOTCF.getParentFile().mkdirs();}
			if(!fileforOTCF.exists()) {
				try {
					fileforOTCF.createNewFile();
				} catch (IOException e) {e.printStackTrace();}
			}
		}
		
		this.existingtestcases = ReadFromFiles.ReadBehaviourPairsFromFile_v201606(this.originalTestCasesFile, this.existingstatemachine);
		
		this.updatedStateMachineFileinNextTestCycle = this.experimentLabelFileFolder + experimentLabel_2 + oSMF;
		this.updatedTestCasesFileinNextTestCycle = this.experimentLabelFileFolder + experimentLabel_2 + oTCF;
		
		//Set the MOSA results into output files
		String jMetalResultFileDir = this.experimentLabelFileFolder + experimentLabel_0 + "OutputResults_jMetal_" + this.saRunnerCase.ALGO + "/";
		this.saRunnerCase.OUTPUT_PATH = jMetalResultFileDir;
		String jMetalResultFile = this.saRunnerCase.ALGO + "_Problem";
		this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(jMetalResultFileDir + jMetalResultFile + "_01_VAR.tsv");
		this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(jMetalResultFileDir + jMetalResultFile + "_02_FUN.tsv");
	}
	
	
	
	
	
	
	
	public void ASUBE_MO_Appium_OneTestCycle_Process() throws IOException {
		
		System.out.println("==================== ASUBE_MO_Appium_OneTestCycle_Process() ====================");
		System.out.println("~~~~~~~~~~~~~~~~~~~~ {[DateTime][Start][" + LocalDateTime.now().format(this.saRunnerCase.formatter) + "]} ~~~~~~~~~~~~~~~~~~~~");
		long start = System.currentTimeMillis();
		
		
		//========== Begin MOSA to generate Test Cases ==========
		System.out.println("==================== Begin to Search(in ASUBE_MO_Appium_OneTestCycle_Process()) ====================");
		//merge all the MOSA
		this.saRunnerCase.run(this.objectives, this.saRunnerCase.ALGO + "_Problem", this.originalStateMachineFile, this.originalTestCasesFile, this.referenceParetoFront);
		//take out the result set storing all the pareto fronts, and use file to store them
		
		long afterSearch = System.currentTimeMillis();
		long searchTimeCost = afterSearch - start;
		System.out.println("~~~~~~~~~~~~~~~~~~~~ {[DateTime][After MOSA Search][" + LocalDateTime.now().format(this.saRunnerCase.formatter) + "]} ~~~~~~~~~~~~~~~~~~~~");
		System.out.println("******************** {[Cost][Time Cost of MOSA Search][" + searchTimeCost + " ms]} ********************");
		
		
		//========== Selected Test Cases, and output to files ==========
		System.out.println("==================== Select TCs from PFs and output (in ASUBE_MO_Appium_OneTestCycle_Process()) ====================");
		List<DoubleSolution> result = saRunnerCase.resultSet;
		int lenSolution = result.size();
		
		int[] indexes = new int[TCsOneCycleNumber];
		for(int i = 0; i < TCsOneCycleNumber; i ++) {
			indexes[i] = RandomGenerator.getGenerator().nextInt(lenSolution);
		}
		System.out.print("++++++++++++++++++++ [");
		for(int i = 0; i < TCsOneCycleNumber; i ++) {
			System.out.print(indexes[i] + " ");
		}
		System.out.println("] ++++++++++++++++++++");
		for(int i = 0; i < TCsOneCycleNumber; i ++) {
			if(i == 0) {
				this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(this.resultTestCasesFile);
				this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(this.updatedTestCasesFileinNextTestCycle);
			}
			write2files_JMetal.WriteOneBehaviourPair2File_JMetal(result.get(indexes[i]), this.existingstatemachine, this.resultTestCasesFile);
			//prepare the original test cases in the next test cycle
			write2files_JMetal.WriteOneBehaviourPair2File_JMetal(result.get(indexes[i]), this.existingstatemachine, this.updatedTestCasesFileinNextTestCycle);
		}
		
		Write2Files.WriteBehaviourPairs2File_v201606(this.existingtestcases, this.existingstatemachine, this.updatedTestCasesFileinNextTestCycle);
		//* store test case into the file of test cases generated in this test cycle and the file including all the test cases
		
		//execute the test cases, and recorde the result as behaviors
		List<DoubleSolution> seletctedPSBs = new ArrayList<DoubleSolution>();
		for(int i = 0; i < TCsOneCycleNumber; i ++) {
			seletctedPSBs.add(result.get(indexes[i]));
		}
		this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(this.resultDSwithObjFile);
		write2files_JMetal.WriteDoubleSolutions2File_JMetal(seletctedPSBs, this.resultDSwithObjFile);
		//write all PFs with their objs
		this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(this.allParetoFrontswithObjsFile);
		write2files_JMetal.WriteDoubleSolutions2File_JMetal(result, this.allParetoFrontswithObjsFile);
		
		
		//Transform DSolutions to Solutions
		List<Solution> seletctedPSBsasSolutionVersion = new ArrayList<Solution>();
		for(int i = 0; i < TCsOneCycleNumber; i++) {
			seletctedPSBsasSolutionVersion.add(ProblemPSBGenwithWDVRules.TransformDoubleSolution2Solution(seletctedPSBs.get(i)));
		}
		
		
		long begintoManageTestCases = System.currentTimeMillis();
		long otherTimeCost = begintoManageTestCases - afterSearch;
		System.out.println("******************** {[Cost][Other Time Cost (after MOSA Search, begin to manage test cases)][" + otherTimeCost + " ms]} ********************");
		System.out.println("~~~~~~~~~~~~~~~~~~~~ {[DateTime][Begin to manage test cases][" + LocalDateTime.now().format(this.saRunnerCase.formatter) + "]} ~~~~~~~~~~~~~~~~~~~~");
		
		
		//========== Manager Test Cases ==========
		System.out.println("==================== Begin to Manage Test Cases(in ASUBE_MO_Appium_OneTestCycle_Process(), will get Transitions) ====================");
		
		
//		manager_Appium ma = new manager_Appium();
//		List<Transition> SetofPossibleNewTransitions = ma.manageProcess(
//				seletctedPSBsasSolutionVersion, //list<solution>
//				(ProblemPSBGenwithWDVRules)saRunnerCase.p, //problem
//				saRunnerCase.existingStateMachine, //state machine
//				this.testIntermediaResultLabel,
//				this.executedTestCasesinRealityValuesFile,
//				this.executedTestCasesinFormalValuesFile,
//				this.handleDuplicatedFileWrite
//				);/**formal*/
		
		
		//Produce the transitions from the Data Constructed Subjective
		List<Transition> SetofPossibleNewTransitions = this.dsJitsiDesktop.easySimulateRunDataConstructedSubjective(
				seletctedPSBsasSolutionVersion
				, (ProblemPSBGenwithWDVRules)saRunnerCase.p
				, saRunnerCase.existingStateMachine
				, this.executedTestCasesinFormalValuesFile
				, this.handleDuplicatedFileWrite
				);
		
		
		
		
		long begintoUpdateSM = System.currentTimeMillis();
		long executionTimeCost = begintoUpdateSM - begintoManageTestCases;
		System.out.println("******************** {[Cost][Execution Time Cost (after manage test cases, begin to update state machine)][" + executionTimeCost + " ms]} ********************");
		System.out.println("~~~~~~~~~~~~~~~~~~~~ {[DateTime][Begin to update state machine][" + LocalDateTime.now().format(this.saRunnerCase.formatter) + "]} ~~~~~~~~~~~~~~~~~~~~");
		
		
		
		//========== Manager Test Cases ==========
		System.out.println("==================== Begin to Update State Machine(in ASUBE_MO_Appium_OneTestCycle_Process()) ====================");
		
		
		//add the test cases into the test cases repository, and add the behaviors into the behavior repository
		/***/
		System.out.println("==========---------- Begin to Update the State Machine ----------==========");
		//In this loop, we do not update the state machine, so the output information is real.
		int lll = SetofPossibleNewTransitions.size();
		System.out.println("++++++++++++++++++++ {[Num of New transitions (before update SM, maybe include duplicated ones)][" + lll + "]} ++++++++++++++++++++");
		for( int iii = 0; iii < lll; iii ++){
			Transition nti = SetofPossibleNewTransitions.get(iii);
			
			int zi = this.existingstatemachine.CheckNewTransition(nti);
			if( zi == 1 || zi == 2){
				System.out.println("******************** {[Possible New Transition (before update SM)(type)][NO. "+ iii +"][" + zi + " (1:having a new state, 2:having no new state)] ********************");
				System.out.println("**********---------- {[Source state: " + nti.getsourcestate().getStateName() + "; Target State: " 
				+ " (" + nti.gettargetstate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0) + ", "
				+ nti.gettargetstate().getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0) + ") "
				+ "; Trigger: " + nti.getTriggers().get(0) 
				+ "; t.PacketLoss :" + nti.getConditions().get("PacketLoss").getValueSet().get(0)
				+ "; t.PacketDelay :" + nti.getConditions().get("PacketDelay").getValueSet().get(0)
				+ "; t.PacketDuplication :" + nti.getConditions().get("PacketDuplication").getValueSet().get(0)
				+ "; t.PacketCorruption :" + nti.getConditions().get("PacketCorruption").getValueSet().get(0)
				+ "]} ----------**********"
				);
			}
			
		}
		//In this loop, we update the state machine, so the output information is changing.
		int ll = SetofPossibleNewTransitions.size();
		for( int ii = 0; ii < ll; ii ++){
			Transition nt = SetofPossibleNewTransitions.get(ii);
			int z = this.existingstatemachine.CheckNewTransition(nt);
			if( z == 1 || z == 2){
				System.out.println("******************** {[Possible New Transition (updating SM)(type)][NO. "+ ii +"][" + z + " (1:having a new state, 2:having no new state)] ********************");
				System.out.println("**********---------- {[Source state: " + nt.getsourcestate().getStateName() + "; Target State: " 
				+ " (" + nt.gettargetstate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0) + ", "
				+ nt.gettargetstate().getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0) + ") "
				+ "; Trigger: " + nt.getTriggers().get(0) 
				+ "; t.PacketLoss :" + nt.getConditions().get("PacketLoss").getValueSet().get(0)
				+ "; t.PacketDelay :" + nt.getConditions().get("PacketDelay").getValueSet().get(0)
				+ "; t.PacketDuplication :" + nt.getConditions().get("PacketDuplication").getValueSet().get(0)
				+ "; t.PacketCorruption :" + nt.getConditions().get("PacketCorruption").getValueSet().get(0)
				+ "]} ----------**********"
				);
			}
			
			System.out.println("*****--------------- {[Update result][" + this.existingstatemachine.UpdateStateMachinebyAddANewTransition(nt) + 
					" (1:having a new state, 2:having no new state)] ---------------*****");
		}
		
		System.out.println("==========---------- Write Updated State Machine to the Update SMFile in the Current TCycle and the Original SMFile in the Next TCycle ----------==========");
		this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(this.updatedStateMachineFile);
		WriteStateMachine2File.writeStateMachine2File(this.existingstatemachine, this.updatedStateMachineFile);
		this.handleDuplicatedFileWrite.doHandleDuplicatedFileWrite(this.updatedStateMachineFileinNextTestCycle);
		WriteStateMachine2File.writeStateMachine2File(this.existingstatemachine, this.updatedStateMachineFileinNextTestCycle);
		
		
		System.out.println("==================== End ASUBE_MO_Appium_OneTestCycle_Process() ====================");
		//time cost
		/**the search time cost*/
		long end = System.currentTimeMillis();
		System.out.println("******************** {[Cost][Total Time Cost][" + (end - start) + " ms]} ********************");
		System.out.println("~~~~~~~~~~~~~~~~~~~~ {[DateTime][End ASUBE_MO_Appium_OneTestCycle_Process][" + LocalDateTime.now().format(this.saRunnerCase.formatter) + "]} ~~~~~~~~~~~~~~~~~~~~");
		
		
		
		System.out.println(this.testIntermediaResultLabel);
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		
		
		Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator ex = new Experiment_AMMOSUNCOVER_MOSA_SysDCS_Simulator(
				new CellDE_ASUBERunner(),//implemented MOSA instances, CellDE
//				new MOCell_ASUBERunner(),//MOCell
//				new NSGAII_ASUBERunner(),//NSGA-II
//				new NSGAIII_ASUBERunner(),//NSGA-III
//				new SPEA2_ASUBERunner(),//SPEA2
				"", //referenceParetoFront is not needed during AmmosUncover process
				10,//Number of Test Cases per test cycle, e.g., 5 or 10 or 15
				1,//the index of trials (We conduct each configuration for many trails)
				1, //the index of test cycle. It should start from 1th test cycle; or we need to manually create the file folder, the original state machine file (02), and the original possibly-existing system execution repository file (03).
				"InputOutputFiles/", //Experiment Label for the File Folder
				120, //the maximum of Executable Test Cases, in AmmosUncover, we set 120.
				10 //the maximum of trials
				);
		
		ex.dsJitsiDesktop = new DSJitsiDesktop(
				"InputOutputFiles/Part_of_Repository_for_Executions_of_JitsiDesktop.txt" // file location of Repository of System Executions
				);
		
		ex.setParametersofFileSystem();//set some default parameters
		ex.initialization();//preparation for run
		
		
		
		
		ex.ASUBE_MO_Appium_OneTestCycle_Process();//run
		
		/**
		 * The following operation is important for executing the rest of the whole test process.
		 * */
		//Automate the rest of all the test process of one selected MOSA
		ex.completeAllTestProcessesofOneMOSA();
		
		
	}
	
	public void completeAllTestProcessesofOneMOSA() throws Exception {
		
		System.out.println("==================== completeAllTestProcessesofOneMOSA() ====================");
		
		this.setTrialTestCycleNum(this.getTrialTestCycleNum() + 1);//Happens after (Loop=1,TCycle=1), make it changes to (Loop=1,TCycle=2)
		
		int currentExecutedTestCasesNum = 0;
		while(this.getMaxiTrialProcessLoopNum() > (this.getTrialProcessLoopNum()-1)) {
			
			currentExecutedTestCasesNum = (this.getTrialTestCycleNum()-1)*this.getTCsOneCycleNumber();
			System.out.println("++++++++++++++++++++ {[Current Executed Test Cases Num][" + currentExecutedTestCasesNum + "]} ++++++++++++++++++++");
			while(this.getMaxiExecutedTestCasesNum() > currentExecutedTestCasesNum) {
				this.initialization();
				this.ASUBE_MO_Appium_OneTestCycle_Process();
				//calculate current executed test cases num
				currentExecutedTestCasesNum = this.getTrialTestCycleNum()*this.getTCsOneCycleNumber();//update while condition
				System.out.println("++++++++++++++++++++ {[Current Executed Test Cases Num][" + currentExecutedTestCasesNum + "]} ++++++++++++++++++++");
				//set new trialTestCycleNum (++)
				this.setTrialTestCycleNum(this.getTrialTestCycleNum() + 1);
			}
			this.setTrialProcessLoopNum(this.getTrialProcessLoopNum() + 1);
			this.setTrialTestCycleNum(1);//initial Test Cycle Num
		}
		
	}
	
	
	
	public void setTestIntermediaResultLabel(String testIRLabel) {
		this.testIntermediaResultLabel = testIRLabel;
	}
	
	public int getTCsOneCycleNumber() {
		return this.TCsOneCycleNumber;
	}
	public void setTCsOneCycleNumber(int TCsNum) {
		this.TCsOneCycleNumber = TCsNum;
	}
	public int getTrialProcessLoopNum() {
		return this.trialProcessLoopNum;
	}
	public void setTrialProcessLoopNum(int tryPLoopNum) {
		this.trialProcessLoopNum = tryPLoopNum;
	}
	public int getTrialTestCycleNum() {
		return this.trialTestCycleNum;
	}
	public void setTrialTestCycleNum(int tryTCycleNum) {
		this.trialTestCycleNum = tryTCycleNum;
	}
	public int getMaxiExecutedTestCasesNum() {
		return this.maxiExecutedTestCasesNum;
	}
	public int getMaxiTrialProcessLoopNum() {
		return this.maxiTrialProcessLoopNum;
	}
}
