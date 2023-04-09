package org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.ReadandWriteModule;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.Problem.*;
import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.*;
import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.ValueSet.*;

public class ReadFromFiles {
	
	public ReadFromFiles(){}
	
	
	
	
	public static StateMachine ReadStateMachineFromFile_v201606(String fileName){
		
		File file = new File(fileName);
		BufferedReader reader = null;
		
		StateMachine S = new StateMachine();
		Transition t = null;
		State sourceState = null;
		State targetState = null;
		
		try {
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			
			boolean inTransition = false;
			int initial_label = 0;//Used to tag the start state
			
			
			
			while ((tempString = reader.readLine()) != null) {
				
				initial_label ++;
                
                System.out.println("[..........][OriginalFile (line)]>>>" + tempString);
                
                if( !inTransition){//Not in One transition.
	                
	                String[] srcs = tempString.split(" ");
	                
	                if( srcs[0].equals("State")){
	                	System.out.println("[..........][Read One State]");
	                	
	                	if( !srcs[1].startsWith("{")){
	                		System.out.println("#################### {[Error][The format of State Machine is not correct!]} ####################");
	                		return null;
	                	}
	                	
	                	String stateName = srcs[1].substring(1, srcs[1].length() - 2);
	                	HashMap<String, ValueSet> vs = new HashMap<String, ValueSet>();
	                	
	                	srcs = null;
	                	srcs = tempString.split("; ");
	                	if( srcs.length != 3){
	                		System.out.println("#################### {[Error][Something wrong with format of State and its variables!]} ####################");
	                		return null;
	                	}
	                	
	                	
	                	int i_var = 1;
	                	
	                	for(; i_var <= srcs.length - 1; ){
	                		
	                		String[] var_with_constraints = srcs[i_var].split(" \\{");
	                		
	                		String var_constraints = var_with_constraints[1].substring(0, var_with_constraints[1].length() - 1);
	                		System.out.println("Constraint:" + var_constraints);
	                		ValueSet valueset_temp = new ValueSet();
	                		String[] constraints_of_one_var = var_constraints.split(", ");
	                		for( int i_constraint = 0; i_constraint < constraints_of_one_var.length; i_constraint ++){
	                			valueset_temp.AddConstriantsForValueSet(constraints_of_one_var[i_constraint]);
	                		}
	                		
	                		vs.put(var_with_constraints[0], valueset_temp);
	                		
	                		i_var = i_var + 1;
	                	}
	                	
	                	State tempState = new State( stateName, vs);
	                	
	                	if( initial_label == 1){
	                		System.out.println("[..........][Add Start State]");
	                		S.setstartstate(tempState);
	                		S.addonestate(tempState);
	                	}
	                	else {
	                		System.out.println("[..........][Add One State]");
	                		S.addonestate(tempState);
	                	}
	                	
	                }
	                else if( srcs[0].equals("Transition")){
	                	System.out.println("[..........][Read One Transition][begin to read one transition]");
	                	inTransition = true;
	                }
	                else {
	                	System.out.println("#################### {[Error][Line Label Unknown!]} ####################");
	                }
	                
                }
                else {
                	//In one transition.
                	
                	if( tempString.startsWith("  Source_State")){
                		System.out.println("[..........][Read One Transition][read the source state]");
                		
                		String[] src_SourceState = tempString.split(" \\{");
                		String sourceStateName = src_SourceState[1].substring(0, src_SourceState[1].length() - 1);
                		System.out.println("[..........][Source State Name][" + sourceStateName + "]");
                		sourceState = S.getState(sourceStateName);
                		
                	}
                	else if( tempString.startsWith("  Target_State")){
                		System.out.println("[..........][Read One Transition][read the target state]");
                		
                		String[] src_TargetState = tempString.split(" \\{");
                		String targetStateName = src_TargetState[1].substring(0, src_TargetState[1].length() - 1);
                		System.out.println("[..........][Target State Name][" + targetStateName + "]");
                		targetState = S.getState(targetStateName);
                		
                		t = new Transition( sourceState, targetState);
                		sourceState.addoutTransition(t);
                		targetState.addinTransition(t);
                	}
                	else if( tempString.startsWith("  trigger")){
                		System.out.println("[..........][Read One Transition][read the trigger]");
                		
                		String[] src_Trigger = tempString.split(" \\{");
                		String trigger = src_Trigger[1].substring(0, src_Trigger[1].length() - 1);
                		System.out.println("[..........][Trigger Name][" + trigger + "]");
                		
                		t.addTriggers(trigger);
                	}
                	else if( tempString.startsWith("  PacketLoss")){
                		System.out.println("[..........][Read One Transition][read the network environment]");
                		
                		HashMap<String, ValueSet> guardcondition = new HashMap<String, ValueSet>();
                		
                		String tempString_networkenvironment = tempString.substring(2);
                		
                		String[] src_networkenvironment = tempString_networkenvironment.split(", ");
                		
                		int i_NEvar = 0;
	                	
	                	for(; i_NEvar <= src_networkenvironment.length - 1; ){
	                		
	                		String[] NEvar_with_constraints = src_networkenvironment[i_NEvar].split(" \\{");
	                		
	                		
	                		String NEvar_constraints = NEvar_with_constraints[1].substring(0, NEvar_with_constraints[1].length() - 1);
	                		
	                		ValueSet valueset_temp = new ValueSet();
	                		String[] constraints_of_one_NEvar = NEvar_constraints.split(", ");
	                		
	                		for( int i_constraint = 0; i_constraint < constraints_of_one_NEvar.length; i_constraint ++){
	                			valueset_temp.AddConstriantsForValueSet(constraints_of_one_NEvar[i_constraint]);
	                		}
	                		
	                		guardcondition.put(NEvar_with_constraints[0], valueset_temp);
	                		System.out.println("[..........][Guard Condition][" + NEvar_with_constraints[0] + ", " + valueset_temp.getValueSet().get(0) + "]");
	                		
	                		t.setConditions(guardcondition);
	                		
	                		i_NEvar = i_NEvar + 1;
	                	}
                		
                		S.addonetransition(t);
                		
                		inTransition = false;
                		sourceState = null;
                		targetState = null;
                		t = null;
                	}
                	else {
                		System.out.println("#################### {[Error][Line Label Unknown!]} ####################");
                	}
                }
                
                
                
            }
            reader.close();
			
		} catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		
		return S;
	}
	
	
	
	
	
	
	
	public static List<Solution> ReadBehaviourPairsFromFile_v201606(String fileName, StateMachine sm){
		
		File file = new File(fileName);
		BufferedReader reader = null;
		
		//BehaviourPair bp = new BehaviourPair();
		List<Solution> ExistingBP = new ArrayList<Solution>();
		Solution tempBP = null;
		
		try{
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while ((tempString = reader.readLine()) != null) {
				
				System.out.println(tempString);
				
				if( tempString.startsWith("BP")){
					//do nothing or read the NO. of BP
					tempBP = new Solution();
				}
				else if( tempString.startsWith("  Source_State")){
					System.out.println("[..........][Read One Behaviour Pair][read the source state]");
            		
            		String[] src_SourceState = tempString.split(" \\{");
            		String sourceStateName = src_SourceState[1].substring(0, src_SourceState[1].length() - 1);
            		System.out.println("[..........][Source State Name][" + sourceStateName + "]");
            		tempBP.addsolutionmember("SourceState", sm.getStateNO(sourceStateName));
				}
				else if( tempString.startsWith("  Target_State")){
					System.out.println("[..........][Read One Behaviour Pair][read the target state]");
            		
            		String[] src_TargetState = tempString.split(" \\{\\}; ");
            		
            		String[] src_TS_v = src_TargetState[1].split("; ");
            		String[] src_ac = src_TS_v[0].split(" \\{");
            		String src_ac_con = src_ac[1].substring(0, src_ac[1].length() - 1);
            		String[] src_ac_v = src_ac_con.split("== ");
            		int ac_v = Integer.parseInt(src_ac_v[1]);
            		String[] src_vc = src_TS_v[1].split(" \\{");
            		String src_vc_con = src_vc[1].substring(0, src_vc[1].length() - 1);
            		String[] src_vc_v = src_vc_con.split("== ");
            		int vc_v = Integer.parseInt(src_vc_v[1]);
            		
            		tempBP.addsolutionmember("activecall", ac_v);
            		tempBP.addsolutionmember("videoquality", vc_v);
            		
				}
				else if( tempString.startsWith("  trigger")){
					System.out.println("[..........][Read One Behaviour Pair][read the trigger]");
            		
            		String[] trigger = tempString.split(" \\{");
            		String triggerName = trigger[1].substring(0, trigger[1].length() - 1);
            		System.out.println("[..........][Trigger Name][" + triggerName + "]");
            		int useroperation_BP;
            		if( triggerName.equals("null")){
            			useroperation_BP = 0;
            		}
            		else if( triggerName.equals("dial")){
            			useroperation_BP = 1;
            		}
            		else if( triggerName.equals("disconnect")){
            			useroperation_BP = 2;
            		}
            		else {
            			useroperation_BP = -1;
            			System.out.println("#################### {[Warning][Unknown trigger!]} ####################");
            		}
            		tempBP.addsolutionmember("UserOperation", useroperation_BP);
				}
				else if( tempString.startsWith("  PacketLoss")){
					
					System.out.println("[..........][Read One Behaviour Pair][read the network environment]");
            		
            		HashMap<String, ValueSet> guardcondition = new HashMap<String, ValueSet>();
            		
            		String tempString_networkenvironment = tempString.substring(2);
            		
            		String[] src_networkenvironment = tempString_networkenvironment.split(", ");
            		
            		int i_NEvar = 0;
                	
                	for(; i_NEvar <= src_networkenvironment.length - 1; ){
                		
                		String[] NEvar_with_constraints = src_networkenvironment[i_NEvar].split(" \\{");
                		
                		String NEvar_constraints = NEvar_with_constraints[1].substring(0, NEvar_with_constraints[1].length() - 1);
                		
                		String[] src_constraintValue = NEvar_constraints.split(" == ");
                		
                		tempBP.addsolutionmember(src_constraintValue[0], Double.parseDouble(src_constraintValue[1]));
                		
                		System.out.println("[..........][Guard Condition][" + src_constraintValue[0] + ", " + tempBP.getsolution().get(src_constraintValue[0]) + "]");
                		
                		i_NEvar = i_NEvar + 1;
                	}
            		//Add guard condition to transition, then add transition to state machine.
            		
                	ExistingBP.add(tempBP);
    				Solution.printSolution(tempBP);
    				
            		tempBP = null;
					
				}
				else {
					System.out.println("#################### {[Error][Line Label Unknown!]} ####################");
				}
				
				
				
				
				
			}
			
			System.out.println("++++++++++++++++++++ {[BP List Length][" + ExistingBP.size() + "]} ++++++++++++++++++++");
			
            reader.close();
			
		} catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return ExistingBP;
		
		
	}
	
	
	
	//Read Collected Data of JitsiDesktop's Execution Repository
	public static List<Solution> ReadExecutionsFromEmulatedRepository_v202108(String fileName){
		
		File file = new File(fileName);
		BufferedReader reader = null;
		
		//BehaviourPair bp = new BehaviourPair();
		List<Solution> ExistingBP = new ArrayList<Solution>();
		Solution tempBP = null;
		
		try{
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while ((tempString = reader.readLine()) != null) {
				
				System.out.println(tempString);
				
				if( tempString.startsWith("BP")){
					//do nothing or read the NO. of BP
					tempBP = new Solution();
				}
				else if( tempString.startsWith("  Source_State")){
					System.out.println("[..........][Read One Behaviour Pair][read the source state]");
            		
					String[] src_TargetState = tempString.split(" \\{\\}; ");
					
					String[] src_TS_v = src_TargetState[1].split("; ");
            		String[] src_ac = src_TS_v[0].split(" \\{");
            		String src_ac_con = src_ac[1].substring(0, src_ac[1].length() - 1);
            		String[] src_ac_v = src_ac_con.split("== ");
            		int ac_v = Integer.parseInt(src_ac_v[1]);
            		String[] src_vc = src_TS_v[1].split(" \\{");
            		String src_vc_con = src_vc[1].substring(0, src_vc[1].length() - 1);
            		String[] src_vc_v = src_vc_con.split("== ");
            		int vc_v = Integer.parseInt(src_vc_v[1]);
            		
            		tempBP.addsolutionmember("SourceState_activecall", ac_v);
            		tempBP.addsolutionmember("SourceState_videoquality", vc_v);
            		
            		
				}
				else if( tempString.startsWith("  Target_State")){
					System.out.println("[..........][Read One Behaviour Pair][read the target state]");
            		
            		String[] src_TargetState = tempString.split(" \\{\\}; ");
            		
            		String[] src_TS_v = src_TargetState[1].split("; ");
            		String[] src_ac = src_TS_v[0].split(" \\{");
            		String src_ac_con = src_ac[1].substring(0, src_ac[1].length() - 1);
            		String[] src_ac_v = src_ac_con.split("== ");
            		int ac_v = Integer.parseInt(src_ac_v[1]);
            		String[] src_vc = src_TS_v[1].split(" \\{");
            		String src_vc_con = src_vc[1].substring(0, src_vc[1].length() - 1);
            		String[] src_vc_v = src_vc_con.split("== ");
            		int vc_v = Integer.parseInt(src_vc_v[1]);
            		
            		tempBP.addsolutionmember("activecall", ac_v);
            		tempBP.addsolutionmember("videoquality", vc_v);
            		
				}
				else if( tempString.startsWith("  trigger")){
					System.out.println("[..........][Read One Behaviour Pair][read the trigger]");
            		
            		String[] trigger = tempString.split(" \\{");
            		String triggerName = trigger[1].substring(0, trigger[1].length() - 1);
            		System.out.println("[..........][Trigger Name][" + triggerName + "]");
            		int useroperation_BP;
            		if( triggerName.equals("null")){
            			useroperation_BP = 0;
            		}
            		else if( triggerName.equals("dial")){
            			useroperation_BP = 1;
            		}
            		else if( triggerName.equals("disconnect")){
            			useroperation_BP = 2;
            		}
            		else {
            			useroperation_BP = -1;
            			System.out.println("#################### {[Warning][Unknown trigger!]} ####################");
            		}
            		tempBP.addsolutionmember("UserOperation", useroperation_BP);
				}
				else if( tempString.startsWith("  PacketLoss")){
					
					System.out.println("[..........][Read One Behaviour Pair][read the network environment]");
            		
            		HashMap<String, ValueSet> guardcondition = new HashMap<String, ValueSet>();
            		
            		String tempString_networkenvironment = tempString.substring(2);
            		
            		String[] src_networkenvironment = tempString_networkenvironment.split(", ");
            		
            		int i_NEvar = 0;
                	
                	for(; i_NEvar <= src_networkenvironment.length - 1; ){
                		
                		String[] NEvar_with_constraints = src_networkenvironment[i_NEvar].split(" \\{");
                		
                		String NEvar_constraints = NEvar_with_constraints[1].substring(0, NEvar_with_constraints[1].length() - 1);
                		
                		String[] src_constraintValue = NEvar_constraints.split(" == ");
                		
                		tempBP.addsolutionmember(src_constraintValue[0], Double.parseDouble(src_constraintValue[1]));
                		
                		System.out.println("[..........][Guard Condition][" + src_constraintValue[0] + ", " + tempBP.getsolution().get(src_constraintValue[0]) + "]");
                		
                		i_NEvar = i_NEvar + 1;
                	}
            		//Add guard condition to transition, then add transition to state machine.
            		
                	ExistingBP.add(tempBP);
    				Solution.printSolution(tempBP);
    				
            		tempBP = null;
					
				}
				else {
					System.out.println("#################### {[Error][Line Label Unknown!]} ####################");
				}
				
				
				
				
				
			}
			
			System.out.println("++++++++++++++++++++ {[BP List Length][" + ExistingBP.size() + "]} ++++++++++++++++++++");
			
            reader.close();
			
		} catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return ExistingBP;
		
		
	}
	
	
	
	
	public static void readCostandEffectivenessFromNewVersionLogFile_v202109(String fileName, HashMap<String, Integer> result) {
		
		File file = new File(fileName);
		BufferedReader reader = null;
		
		System.out.println(">>>>>> File Name: " + fileName);
		
		int timeCostofMOSASearch = 0;
		int otherTimeCost_1 = 0;
		int timeCostofExecution = 0;
		int totalTimeCost = 0;
		
		int totalExecutions = 0;
		int numberofNewExecutions = 0;
		int numberofNewTransitions = 0;
		int numberofNewStates = 0;
		
		try{
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while ((tempString = reader.readLine()) != null) {
				
				if(tempString.contains("[Time Cost of MOSA Search]")) {
//					System.out.println(tempString);
					String[] src = tempString.split("\\[Time Cost of MOSA Search\\]\\[");
					String[] src_1 = src[1].split(" ms\\]\\}");
//					System.out.println(src_1[0]);
					timeCostofMOSASearch = Integer.parseInt(src_1[0]);
//					System.out.println("Time Cost of MOSA Search:" + timeCostofMOSASearch);
				}
				
				if(tempString.contains("[Other Time Cost (after MOSA Search, begin to manage test cases)]")) {
//					System.out.println(tempString);
					String[] src = tempString.split("\\[Other Time Cost \\(after MOSA Search, begin to manage test cases\\)\\]\\[");
					String[] src_1 = src[1].split(" ms\\]\\}");
//					System.out.println(src_1[0]);
					otherTimeCost_1 = Integer.parseInt(src_1[0]);
//					System.out.println("Other Time Cost_1:" + otherTimeCost_1);
				}
				
				if(tempString.contains("[Execution Time Cost (after manage test cases, begin to update state machine)]")) {
//					System.out.println(tempString);
					String[] src = tempString.split("\\[Execution Time Cost \\(after manage test cases, begin to update state machine\\)\\]\\[");
					String[] src_1 = src[1].split(" ms\\]\\}");
//					System.out.println(src_1[0]);
					timeCostofExecution = Integer.parseInt(src_1[0]);
//					System.out.println("Time Cost of Execution:" + timeCostofExecution);
				}
				
				if(tempString.contains("[Total Time Cost]")) {
//					System.out.println(tempString);
					String[] src = tempString.split("\\[Total Time Cost\\]\\[");
					String[] src_1 = src[1].split(" ms\\]\\}");
//					System.out.println(src_1[0]);
					totalTimeCost = Integer.parseInt(src_1[0]);
//					System.out.println("Total Time Cost:" + totalTimeCost);
				}
				
				
				//new executions
				//Number of test cases in one test cycle
				if(tempString.contains("[Num of New transitions (before update SM, maybe include duplicated ones)]")) {
//					System.out.println(tempString);
					String[] src = tempString.split("\\[Num of New transitions \\(before update SM, maybe include duplicated ones\\)\\]\\[");
					String[] src_1 = src[1].split("\\]\\}");
//					System.out.println(src_1[0]);
					totalExecutions = Integer.parseInt(src_1[0]);
//					System.out.println("Total Executions:" + totalExecutions);
				}
				
				//New executions
				if(tempString.contains("[Possible New Transition (before update SM)(type)][NO.")) {
//					System.out.println(tempString);
					numberofNewExecutions ++;
				}
				
				//New transition and maybe new states
				if(tempString.contains("[Possible New Transition (updating SM)(type)][NO.")) {
//					System.out.println(tempString);
					numberofNewTransitions ++;
					String[] src = tempString.split("\\[Possible New Transition \\(updating SM\\)\\(type\\)\\]\\[NO. ");
					String[] src_1 = src[1].split("\\]\\[");
					String[] src_1_1 = src_1[1].split(" \\(1:having a new state");
//					System.out.println(src_1_1[0]);
					int newTransitionType = Integer.parseInt(src_1_1[0]);
					if(newTransitionType == 1) {
						numberofNewStates ++;
					}
				}
				
				
				
				
			}
			
//			System.out.println("++++++++++++++++++++ {[BP List Length][" + "]} ++++++++++++++++++++");
			
            reader.close();
			
		} catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		
		result.put("TimeCostofMOSASearch", timeCostofMOSASearch);
		result.put("OtherTimeCost_1", otherTimeCost_1);
		result.put("TimeCostofExecution", timeCostofExecution);
		result.put("TotalTimeCost", totalTimeCost);
		
		result.put("TotalExecutions", totalExecutions);
		result.put("NumberofNewExecutions", numberofNewExecutions);
		result.put("NumberofNewTransitions", numberofNewTransitions);
		result.put("NumberofNewStates", numberofNewStates);
		
		
		
		return ;
		
		
	}
	
	
	public static List<List<Double>> readToCollectNetworkEnvironmentsOfNewExecutionsFromNewVersionLogFile_v202109(String fileName) {
		
		File file = new File(fileName);
		BufferedReader reader = null;
		
		System.out.println(">>>>>> File Name: " + fileName);
		
		List<List<Double>> collectionOfNetworkEnvironmentOfNewExecutions = new ArrayList<List<Double>>();
		
		try{
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while ((tempString = reader.readLine()) != null) {
				
				if(tempString.contains("[Possible New Transition (updating SM)(type)][NO.")) {
					break;
				}
				
				
				if(tempString.startsWith("**********----------")
						&& tempString.contains("t.PacketLoss")
						&& tempString.contains("t.PacketDelay")
						&& tempString.contains("t.PacketDuplication")
						&& tempString.contains("t.PacketCorruption")
						) {
					
					System.out.println(tempString);
					
					String[] src = tempString.split("; ");
					String[] PacketLossSrc = src[3].split("==");//loss
					double PacketLossTemp = Double.parseDouble(PacketLossSrc[1]);
					String[] PacketDelaySrc = src[4].split("==");//Delay
					double PacketDelayTemp = Double.parseDouble(PacketDelaySrc[1]);
					String[] PacketDuplicationSrc = src[5].split("==");//PacketDuplication
					double PacketDuplicationTemp = Double.parseDouble(PacketDuplicationSrc[1]);
					String[] PacketCorruptionSrc_1 = src[6].split("==");//PacketCorruption
					String[] PacketCorruptionSrc = PacketCorruptionSrc_1[1].split("\\]\\}");
					double PacketCorruptionTemp = Double.parseDouble(PacketCorruptionSrc[0]);
					
					List<Double> neTemp = new ArrayList<Double>();
					neTemp.add(PacketLossTemp);
					neTemp.add(PacketDelayTemp);
					neTemp.add(PacketDuplicationTemp);
					neTemp.add(PacketCorruptionTemp);
					
					collectionOfNetworkEnvironmentOfNewExecutions.add(neTemp);
				}
				
				
				
				
			}
			
//			System.out.println("++++++++++++++++++++ {[BP List Length][" + "]} ++++++++++++++++++++");
			
            reader.close();
			
		} catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		
		
		
		return collectionOfNetworkEnvironmentOfNewExecutions;
		
		
	}
	
	
	
	
	
	
	
	
	public static void main(String args[]){
		
		
		
	}
	
}
