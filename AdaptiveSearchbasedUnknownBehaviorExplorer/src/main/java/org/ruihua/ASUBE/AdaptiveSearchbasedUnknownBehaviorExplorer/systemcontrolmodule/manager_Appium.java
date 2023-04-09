package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.systemcontrolmodule;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.TestCase.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.TestCase.TestCase;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution.ProblemPSBGenwithWDVRules;

import io.appium.java_client.android.AndroidDriver;


public class manager_Appium {
	
	TestCaseElement2AppiumScripts scripts;
	
	List<String> user3;
	List<String> user4;
	List<String> user5;
	List<String> user6;
	
	HashMap<String, String> HostDevice_NO0;
	HashMap<String, String> ClientDevice_NO1;
	HashMap<String, String> ClientDevice_NO2;
	HashMap<String, String> ClientDevice_NO3;
	
	int conferenceID;
	
	String benchmarkDirectoryName;
	
	Random ran;
	
	private DateTimeFormatter formatter;
	
	public manager_Appium(){
		
		scripts = new TestCaseElement2AppiumScripts();
		
		
		this.ran = new Random();
		
		this.HostDevice_NO0 = new HashMap<String, String>();
		this.HostDevice_NO0.put("deviceName", "-"); //To insert -
		this.HostDevice_NO0.put("platformName", "Android");
		this.HostDevice_NO0.put("platformVersion", "-"); //To insert -
		this.HostDevice_NO0.put("udid", "-"); //To insert -
		this.HostDevice_NO0.put("DriverUrl", "http://localhost:4723/wd/hub");
		
		this.ClientDevice_NO1 = new HashMap<String, String>();
		this.ClientDevice_NO1.put("deviceName", "-"); //To insert -
		this.ClientDevice_NO1.put("platformName", "Android");
		this.ClientDevice_NO1.put("platformVersion", "-"); //To insert -
		this.ClientDevice_NO1.put("udid", "-"); //To insert -
		this.ClientDevice_NO1.put("DriverUrl", "http://localhost:4725/wd/hub");
		
		this.ClientDevice_NO2 = new HashMap<String, String>();
		this.ClientDevice_NO2.put("deviceName", "-"); //To insert -
		this.ClientDevice_NO2.put("platformName", "Android");
		this.ClientDevice_NO2.put("platformVersion", "-"); //To insert -
		this.ClientDevice_NO2.put("udid", "-"); //To insert -
		this.ClientDevice_NO2.put("DriverUrl", "http://localhost:4726/wd/hub");
		
		this.ClientDevice_NO3 = new HashMap<String, String>();
		this.ClientDevice_NO3.put("deviceName", "-"); //To insert -
		this.ClientDevice_NO3.put("platformName", "Android");
		this.ClientDevice_NO3.put("platformVersion", "-"); //To insert -
		this.ClientDevice_NO3.put("DriverUrl", "http://localhost:4727/wd/hub");
		
		
		
		this.formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
	}
	
	
	public List<Transition> manageProcess(List<Solution> SetOfBehaviourPairs, ProblemPSBGenwithWDVRules p, StateMachine s, String testIRLabel, String eTCsinRVsF, String eTCsinFVsF, HandleDuplicatedFileWrite hDupFW){
		
		System.out.println("==================== manageProcess() ====================");
		
		List<TestCase> TestCasesList = new ArrayList<TestCase>();
		
		List<Solution> ListforTempRealBehaviour = new ArrayList<Solution>();
		List<Solution> SetofAllRealBehaviour = new ArrayList<Solution>();
		List<Solution> SetofAllFormalBehaviour = new ArrayList<Solution>();
		List<Transition> SetofPossibleNewTransition = new ArrayList<Transition>();
		
		this.conferenceID = 10000 + this.ran.nextInt(10000);
		this.benchmarkDirectoryName = testIRLabel;
		
		System.out.println("[//////////]{[Cancel the Network Condition setting. (Before Set up bench mark)]}");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		NetworkConditionController.initialLocalEquipmentNetworkEnvironment();
		System.out.println("[//////////]{[End.]}");
		
		System.out.println("==========---------- {[Testing manageing][Set up bench mark]} ----------==========");
		long begintoManageSingleTestCase_SetupBenchMark = System.currentTimeMillis();
		System.out.println("~~~~~~~~~~~~~~~~~~~~ {[DateTime][Set up bench mark][" + LocalDateTime.now().format(this.formatter) + " ms]} ~~~~~~~~~~~~~~~~~~~~");
		this.scripts.setUpBenchmarkforAll(this.HostDevice_NO0, this.ClientDevice_NO1, this.ClientDevice_NO2, this.ClientDevice_NO3, this.conferenceID, this.benchmarkDirectoryName);
		long afterSetupBenchMark = System.currentTimeMillis();
		long timeCostofSetupBenchMark = afterSetupBenchMark - begintoManageSingleTestCase_SetupBenchMark;
		System.out.println("******************** {[Cost][Set up bench mark][" + timeCostofSetupBenchMark + " ms]} ********************");
		
		
		int len = SetOfBehaviourPairs.size();
		for(int i = 0;i < len; i ++) {
			
			Solution s_bp = SetOfBehaviourPairs.get(i);
			SystemBehavior bp = p.TransformSolution2BPairValueSet_v201606(s_bp);
			TestCase testcaseTemp = new TestCase(s);
			testcaseTemp.generateTestCase(bp);
			testcaseTemp.printTestCase();//print
			TestCasesList.add(testcaseTemp);
			
			this.conferenceID = 10000 + this.ran.nextInt(10000) + i;
			
			String testFilesLabel = testIRLabel + "_TC_ID_" + i;
			
			System.out.println("[//////////]{[Cancel the Network Condition setting. (Before start managing on test case.)]}");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			NetworkConditionController.initialLocalEquipmentNetworkEnvironment();
			System.out.println("[//////////]{[End.]}");
			
			
			System.out.println("==========---------- {[Testing manageing][The (" + i +")th Test Case]} ----------==========");
			long begintoManageSingleTestCase = System.currentTimeMillis();
			System.out.println("~~~~~~~~~~~~~~~~~~~~ {[DateTime][The " + testFilesLabel +" TC starts][" + LocalDateTime.now().format(this.formatter) + " ms]} ~~~~~~~~~~~~~~~~~~~~");
			SystemVariables_0 sv = manageSingleTestCase(testcaseTemp, testFilesLabel);
			long afterExecuteSingleTestCase = System.currentTimeMillis();
			long timeCostofSingleTC = afterExecuteSingleTestCase - begintoManageSingleTestCase;
			System.out.println("******************** {[Cost][The " + i + "th Test Case][Execution Time Cost][" + timeCostofSingleTC + " ms]} ********************");
			
			
			int new_AC = sv.activecall;
			int new_VQ = (int)sv.videoquality;
			
			s_bp.getsolution().put("activecall", new_AC);
			s_bp.getsolution().put("videoquality", new_VQ);
			
			SetofAllRealBehaviour.add(s_bp);
			//Solution s_bp_copy = 
			
			ListforTempRealBehaviour.add(s_bp);
			if( i == 0) {
				hDupFW.doHandleDuplicatedFileWrite(eTCsinRVsF);
			}
			Write2Files.WriteBehaviourPairs2File_v201606(ListforTempRealBehaviour, s, eTCsinRVsF);
			ListforTempRealBehaviour.clear();
			
			
			if( new_VQ == -100 && new_AC == -100){//the initialized value of SV
				continue;
			}
			//still need to change based on the CV alg
			if( new_VQ < 5){
				new_VQ = 0;
			}
			else if( new_VQ >= 5 && new_VQ < 10){
				new_VQ = 1;
			}
			else if( new_VQ >= 10 && new_VQ < 25){
				new_VQ = 2;
			}
			else{
				new_VQ = 3;
			}
			
			//Revise bp to get new transition
			ValueSet vs_bpTAC = new ValueSet();
			vs_bpTAC.getValueSet().add("activecall == " + new_AC);
			bp.gettargetstate().getSystemVariables_ValueSet().put("activecall", vs_bpTAC);
			ValueSet vs_bpTVQ = new ValueSet();
			vs_bpTVQ.getValueSet().add("videoquality == " + new_VQ);
			bp.gettargetstate().getSystemVariables_ValueSet().put("videoquality", vs_bpTVQ);
			System.out.println("BP-AC: " + bp.gettargetstate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0)
					+ "; BP-VQ: " + bp.gettargetstate().getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0)
					);
			
			Transition transitionfromBehaviorPair = p.TransformBehaviourPair2Transition_v201610(bp);
			SetofPossibleNewTransition.add(transitionfromBehaviorPair);
			
			//bp2solution
			Solution formal_bp = p.TransformSystemBehavior2Solution_v2021(bp);
			SetofAllFormalBehaviour.add(formal_bp);
		}
		
		
		
		hDupFW.doHandleDuplicatedFileWrite(eTCsinFVsF);
		Write2Files.WriteBehaviourPairs2File_v201606(SetofAllFormalBehaviour, s, eTCsinFVsF);
		
		return SetofPossibleNewTransition;
	}
	
	
	public SystemVariables_0 manageSingleTestCase(TestCase tc, String executeResultFile) {
		System.out.println("==================== manageSingleTestCase() ====================");
		
		SystemVariables_0 sv = new SystemVariables_0();
		
		int try_label = 0;
		boolean willTry = false;
		List<AndroidDriver> drivers = null;
		
		System.out.println("==========---------- Start exectuing the current selected Test Case ----------==========");
		do {
			willTry = false;
		
		try {
		
		
		drivers = this.scripts.setUpAppiumDriver(this.HostDevice_NO0, this.ClientDevice_NO1, this.ClientDevice_NO2, this.ClientDevice_NO3);
		this.scripts.setUpIdleState(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID);
		//input path
		int inputPLen = tc.inputPath.size();
		System.out.println("++++++++++++++++++++ {[The length of the input path (exclude BP)][" + inputPLen + "]} ++++++++++++++++++++");
		for(int i = 0 ; i < inputPLen; i ++) {
		
			System.out.println("=====--------------- {[The (" + i + ")th vertex in the test path]} ---------------=====");
			
			
			EdgeVertex evTemp = tc.inputPath.get(i);
			String AC_Value;
			if( i == 0){
				AC_Value = tc.startState.getSystemVariables_ValueSet().get("activecall").getValueSet().get(0);
			}
			else {
				AC_Value = tc.inputPath.get(i - 1).activecall.getValueSet().get(0);
			}
			
			String tarAC = evTemp.activecall.getValueSet().get(0);
			System.out.println("[//////////]{[Source AC (the " + i + "th vertex)][" + AC_Value + "]}");
			System.out.println("[//////////]{[Target AC (the " + i + "th vertex)][" + tarAC + "]}");
			String[] tarACTemp = tarAC.split(" ");
			int tarACValue = Integer.parseInt(tarACTemp[2]);
			
			if(tarACValue >= 4) {
				tarACValue = 3;
				System.out.println("[//////////]{[Change Target AC to 3 (the " + i + "th vertex)][" + tarAC + "]}");
			}
			
			do {
				if( evTemp.trigger.equals("dial")){
					System.out.println("[//////////]{[Trigger (the " + i + "th vertex)][dial]}");
					if( AC_Value.equals("activecall == 0")){
						this.scripts.inputPath_Dial(drivers.get(0), drivers.get(1), this.ClientDevice_NO1.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_0To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_0To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
					}
					else if( AC_Value.equals("activecall == 1")){
						this.scripts.inputPath_Dial(drivers.get(0), drivers.get(2), this.ClientDevice_NO2.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_1To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_1To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						//this.scripts.InputPath_Dial_Call2Conference(user3, user5, evTemp, filenamelabel + "=1To2AC" + "-Path" + i);	
					}
					else if( AC_Value.equals("activecall == 2")){
						this.scripts.inputPath_Dial(drivers.get(0), drivers.get(3), this.ClientDevice_NO3.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_2To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_2To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
					else {
						//Do nothing, because there is the max.
						this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_3To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						
					}
				}
				else if( evTemp.trigger.equals("disconnect")){
					System.out.println("[//////////]{[Trigger (the " + i + "th vertex)][disconnect]}");
					if( AC_Value.equals("activecall == 0")){
						//Do nothing
						this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_0To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						
					}
					else if( AC_Value.equals("activecall == 1")){
						this.scripts.inputPath_Disconnect(drivers.get(0), drivers.get(1), this.ClientDevice_NO1.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_1To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_1To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
					}
					else if( AC_Value.equals("activecall == 2")){
						this.scripts.inputPath_Disconnect(drivers.get(0), drivers.get(2), this.ClientDevice_NO2.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_2To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_2To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
					}
					else {
						this.scripts.inputPath_Disconnect(drivers.get(0), drivers.get(3), this.ClientDevice_NO3.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_3To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_3To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
					}
					
					
				}
				else if( evTemp.trigger.equals("null")){
					System.out.println("[//////////]{[Trigger (the " + i + "th vertex)][null]}");
					
					if( AC_Value.equals("activecall == 0")){
						this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_0To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_0To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
					}
					else if( AC_Value.equals("activecall == 1")){
						this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_1To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_1To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
					}
					else if( AC_Value.equals("activecall == 2")){
						this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_2To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_2To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
					}
					else {
						this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evTemp, executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_3To" + tarACValue, this.benchmarkDirectoryName + "_AC_" + tarACValue);
						System.out.println("------ " + executeResultFile+"_Path_"+i+"_"+evTemp.trigger+"_AC_3To" + tarACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarACValue + " ------");
						
					}
					
				}
				else {
					//shut down local video
					System.out.println("#################### {[Warning][Unknown Trigger!]} ####################");
				}
			}
			while( false);
			
		}
		
		System.out.println("=====--------------- {[The BP Execution]} ---------------=====");
		//Behavior Pair
		SystemBehavior bp = tc.bp;
		
		String tri = bp.gettrigger().get(0);
		String AC_Value = bp.getsourcestate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0);
		EdgeVertex evtemp = new EdgeVertex();
		evtemp.activecall = bp.getsourcestate().getSystemVariables_ValueSet().get("activecall");
		
		evtemp.trigger = tri;
		evtemp.PakcetDelay = bp.getnetworkenvironment().getPacketDelay();
		evtemp.PacketLoss = bp.getnetworkenvironment().getPacketLoss();
		evtemp.PacketCorruption = bp.getnetworkenvironment().getPacketCorruption();
		evtemp.PacketDuplication = bp.getnetworkenvironment().getPacketDuplication();
			
		String tarBPAC = evtemp.activecall.getValueSet().get(0);
		System.out.println("[//////////]{[Source AC (BP)][" + AC_Value + "]}");
		System.out.println("[//////////]{[Target AC (BP)][" + tarBPAC + "]}");
		String[] tarACTemp = tarBPAC.split(" ");
		int tarBPACValue = Integer.parseInt(tarACTemp[2]);
		
		if(tarBPACValue >= 4) {
			tarBPACValue = 3;
			System.out.println("[//////////]{[Change Target AC to 3 (the BP th vertex)][" + tarBPACValue + "]}");
		}
		
		if( tri.equals("dial")){
			System.out.println("[//////////]{[Trigger (BP)][dial]}");
			if( AC_Value.equals("activecall == 0")){
				this.scripts.setUpIdleState(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID);
				sv = this.scripts.inputPath_Dial(drivers.get(0), drivers.get(1), this.ClientDevice_NO1.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_0To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_0To" + tarBPACValue  + ", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
			}
			else if( AC_Value.equals("activecall == 1")){
				sv = this.scripts.inputPath_Dial(drivers.get(0), drivers.get(2), this.ClientDevice_NO2.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_1To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_1To" + tarBPACValue + ", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
						
			}
			else if( AC_Value.equals("activecall == 2")){
				sv = this.scripts.inputPath_Dial(drivers.get(0), drivers.get(3), this.ClientDevice_NO3.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_2To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_2To" + tarBPACValue +", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			else {
				//do nothing			
			}
					
		}
		else if( tri.equals("disconnect")){
			System.out.println("[//////////]{[Trigger (BP)][disconnect]}");
			if( AC_Value.equals("activecall == 0")){
				
				
			}
			else if( AC_Value.equals("activecall == 1")){
				sv = this.scripts.inputPath_Disconnect(drivers.get(0), drivers.get(1), this.ClientDevice_NO1.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_1To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_1To" + tarBPACValue +", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
			}
			else if( AC_Value.equals("activecall == 2")){
				sv = this.scripts.inputPath_Disconnect(drivers.get(0), drivers.get(2), this.ClientDevice_NO2.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_2To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_2To" + tarBPACValue +", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
					
			}
			else {
				sv = this.scripts.inputPath_Disconnect(drivers.get(0), drivers.get(2), this.ClientDevice_NO3.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_3To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_3To" + tarBPACValue +", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
			}
					
					
		}
		else if( tri.equals("null")){
			System.out.println("[//////////]{[Trigger (BP)][null]}");
			if( AC_Value.equals("activecall == 0")){
				sv = this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_0To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_0To" + tarBPACValue +", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
			}
			else if( AC_Value.equals("activecall == 1")){
				sv = this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_1To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);	
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_1To" + tarBPACValue +", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
			}
			else if( AC_Value.equals("activecall == 2")){
				sv = this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_2To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_2To" + tarBPACValue +", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
			}
			else {
				sv = this.scripts.inputPath_Null(drivers.get(0), this.HostDevice_NO0.get("deviceName"), this.conferenceID, evtemp, executeResultFile+"_BP_"+evtemp.trigger+"_AC_3To" + tarBPACValue, this.benchmarkDirectoryName + "_AC_" + tarBPACValue);
				System.out.println("------ " + executeResultFile+"_BP_"+evtemp.trigger+"_AC_3To" + tarBPACValue +", " + this.benchmarkDirectoryName + "_AC_" + tarBPACValue + " ------");
				
				
			}
					
					
		}
		else {
			System.out.println("#################### {[Warning][Unknown Trigger!]} ####################");
		}
				
				
		
		
		} catch(Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(8000);
			} catch (InterruptedException e1) {}
			System.out.println("#################### {[Exception][try label][" + try_label + "]} ####################");
			try {
				if(drivers != null && drivers.size() == 4) {
					drivers.get(0).quit();
					drivers.get(1).quit();
					drivers.get(2).quit();
					drivers.get(3).quit();
				}
			} catch(Exception ed) {System.out.println("#################### {[Error when dealing with Exception][Exception occur when executing driver.quit()]} ####################");}
			
			if(try_label < 2) {
				try_label ++;
				willTry = true;
			}
			else {
				System.out.println("#################### {[Error when dealing with Exception][Failed Execution (try_label beggier than 2)]} ####################");
			}
		}
		
		} while(willTry);
		
		
		this.scripts.getCommunicateWithSUT().resetApp(drivers);
		System.out.println("==========---------- End of Execution of the current selected Test Case  ----------==========");
		
		return sv;
	}
		
}
