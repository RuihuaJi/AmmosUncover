package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;

public class ReadFromFiles {
	
	public ReadFromFiles(){}
	
	
	/**
	 * State Machine
	 * */
	public static StateMachine ReadStateMachineFromFile(String fileName){
		
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
			int initial_label = 0;
			
			
			
			while ((tempString = reader.readLine()) != null) {
				
				initial_label ++;
                
                System.out.println(tempString);
                
                if( !inTransition){//Not in One transition.
	                
	                String[] srcs = tempString.split(" ");
	                
	                if( srcs[0].equals("State:")){
	                	System.out.println("Label: State.");
	                	String stateName = srcs[1];
	                	String[] srcs1 = stateName.split(";");
	                	stateName = srcs1[0];
	                	
	                	
	                	String[] srcs2 = srcs[3].split(",");
	                	int activecall = Integer.parseInt( srcs2[0]);
	                	String srcs3 = srcs[5].substring(0, 1);
	                	//System.out.println(srcs3);
	                	int videoquality = Integer.parseInt( srcs3);
	                	HashMap<String, Object> variables = new HashMap<String, Object>();
	                	variables.put("activecall", activecall);
	            		variables.put("videoquality", videoquality);
	                	
	                	State tempState = new State( variables, stateName);
	                	
	                	if( initial_label == 1){
	                		System.out.println("Add start state.");
	                		S.setstartstate(tempState);
	                		S.addonestate(tempState);
	                	}
	                	else {
	                		System.out.println("Add one state.");
	                		S.addonestate(tempState);
	                	}
	                	
	                }
	                else if( srcs[0].equals("Transition:")){
	                	System.out.println("Label: Transition.");
	                	inTransition = true;
	                }
	                else {
	                	System.out.println("######### Warning: Line Label Unknown! #########");
	                }
	                
                }
                else {
                	
                	if( tempString.startsWith("  Source_State:")){
                		System.out.println("Source_State");
                		
                		String[] srct = tempString.split(": ");
                		int t_len_String = srct[1].length();
                		String sourceStateName = srct[1].substring(0, t_len_String - 1);
                		System.out.println("Source State Name: " + sourceStateName);
                		sourceState = S.getState(sourceStateName);
                		
                	}
                	else if( tempString.startsWith("  Target_State:")){
                		System.out.println("Target_State");
                		
                		String[] srct = tempString.split(": ");
                		int t_len_String = srct[1].length();
                		String targetStateName = srct[1].substring(0, t_len_String - 1);
                		System.out.println("Target State Name: " + targetStateName);
                		targetState = S.getState(targetStateName);
                		
                		t = new Transition( sourceState, targetState);
                		sourceState.addoutTransition(t);
                		targetState.addinTransition(t);
                	}
                	else if( tempString.startsWith("  trigger:")){
                		System.out.println("trigger");
                		
                		String[] srct = tempString.split(": ");
                		int t_len_String = srct[1].length();
                		String trigger = srct[1].substring(0, t_len_String - 1);
                		System.out.println("Trigger Name: " + trigger);
                		
                		t.addTriggers(trigger);
                	}
                	else if( tempString.startsWith("  network_environment:")){
                		System.out.println("network_environment");
                		
                		S.addonetransition(t);
                		
                		inTransition = false;
                		sourceState = null;
                		targetState = null;
                		t = null;
                	}
                	else {
                		System.out.println("####### Warning: Unknown Line from Line! #######");
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
	public static StateMachine ReadStateMachineFromFile_2_ValueSet(String fileName){
		
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
			int initial_label = 0;
			
			
			
			while ((tempString = reader.readLine()) != null) {
				
				initial_label ++;
                
                System.out.println(tempString);
                
                if( !inTransition){//Not in One transition.
	                
	                String[] srcs = tempString.split(" ");
	                
	                if( srcs[0].equals("State:")){
	                	System.out.println("Label: State.");
	                	
	                	String stateName = srcs[1];
	                	String[] srcs1 = stateName.split(";");
	                	stateName = srcs1[0];
	                	
	                	
	                	String[] n_srcs = tempString.split(";");
	                	ValueSet ac = new ValueSet();
	                	if( n_srcs[1].contains(",")){
	                		String[] n_srcs_ac = n_srcs[1].split(", ");
	                		String ac1 = n_srcs_ac[0].substring(1, n_srcs_ac[0].length());
	                		System.out.println("AC1: " + ac1);
	                		ac.AddConstriantsForValueSet(ac1);
	                		
	                		for( int ii = 1; ii < n_srcs_ac.length; ii++){
	                			System.out.println("AC" + ii + ": " + n_srcs_ac[ii]);
	                			ac.AddConstriantsForValueSet(n_srcs_ac[ii]);
	                		}
	                		
	                	}
	                	else {
	                		
	                		String ac1 = n_srcs[1].substring(1, n_srcs[1].length());
	                		System.out.println("AC1: " + ac1);
	                		ac.AddConstriantsForValueSet(ac1);
	                	}
	                	ValueSet vq = new ValueSet();
	                	if( n_srcs[2].contains(",")){
	                		String[] n_srcs_vq = n_srcs[2].split(", ");
	                		String vq1 = n_srcs_vq[0].substring(1, n_srcs_vq[0].length());
	                		System.out.println("VQ1: " + vq1);
	                		vq.AddConstriantsForValueSet(vq1);
	                		
	                		for( int ii = 1; ii < n_srcs_vq.length; ii++){
	                			System.out.println("VQ" + ii + ": " + n_srcs_vq[ii]);
	                			vq.AddConstriantsForValueSet(n_srcs_vq[ii]);
	                		}
	                		
	                	}
	                	else {
	                		String vq1 = n_srcs[2].substring(1, n_srcs[2].length());
	                		System.out.println("VQ1: " + vq1);
	                		vq.AddConstriantsForValueSet(vq1);
	                	}
	                	
	                	
	                	
	                	HashMap<String, ValueSet> variables_valueset = new HashMap<String, ValueSet>();
	                	variables_valueset.put("activecall", ac);
	                	variables_valueset.put("videoquality", vq);
	                	
	                	State tempState = new State( stateName, variables_valueset);
	                	
	                	if( initial_label == 1){
	                		System.out.println("Add start state.");
	                		S.setstartstate(tempState);
	                		S.addonestate(tempState);
	                	}
	                	else {
	                		System.out.println("Add one state.");
	                		S.addonestate(tempState);
	                	}
	                	
	                }
	                else if( srcs[0].equals("Transition:")){
	                	System.out.println("Label: Transition.");
	                	inTransition = true;
	                }
	                else {
	                	System.out.println("######### Warning: Line Label Unknown! #########");
	                }
	                
                }
                else {
                	
                	if( tempString.startsWith("  Source_State:")){
                		System.out.println("Source_State");
                		
                		String[] srct = tempString.split(": ");
                		int t_len_String = srct[1].length();
                		String sourceStateName = srct[1].substring(0, t_len_String - 1);
                		System.out.println("Source State Name: " + sourceStateName);
                		sourceState = S.getState(sourceStateName);
                		
                	}
                	else if( tempString.startsWith("  Target_State:")){
                		System.out.println("Target_State");
                		
                		String[] srct = tempString.split(": ");
                		int t_len_String = srct[1].length();
                		String targetStateName = srct[1].substring(0, t_len_String - 1);
                		System.out.println("Target State Name: " + targetStateName);
                		targetState = S.getState(targetStateName);
                		
                		t = new Transition( sourceState, targetState);
                		sourceState.addoutTransition(t);
                		targetState.addinTransition(t);
                	}
                	else if( tempString.startsWith("  trigger:")){
                		System.out.println("trigger");
                		
                		String[] srct = tempString.split(": ");
                		int t_len_String = srct[1].length();
                		String trigger = srct[1].substring(0, t_len_String - 1);
                		System.out.println("Trigger Name: " + trigger);
                		
                		t.addTriggers(trigger);
                	}
                	else if( tempString.startsWith("  network_environment:")){
                		System.out.println("network_environment");
                		
                		S.addonetransition(t);
                		
                		inTransition = false;
                		sourceState = null;
                		targetState = null;
                		t = null;
                	}
                	else {
                		System.out.println("####### Warning: Unknown Line from Line! #######");
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
			int initial_label = 0;
			
			
			
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
	
	
	
	
	
	
	/**
	 * Behavior Pair
	 * 
	 * */
	public static List<SystemBehavior> ReadBehaviourPairsFromFile(String fileName){
		
		File file = new File(fileName);
		BufferedReader reader = null;
		
		//BehaviourPair bp = new BehaviourPair();
		List<SystemBehavior> ExistingBP = new ArrayList<SystemBehavior>();
		SystemBehavior tempBP = null;
		
		try {
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while ((tempString = reader.readLine()) != null) {
				
				System.out.println(tempString);
				
				if( tempString.startsWith("BP")){
					tempBP = new SystemBehavior();
				}
				else if( tempString.startsWith("Source_State:")){
					
					String[] srcb = tempString.split(" ");
					tempBP.getsourcestate().setStateName(srcb[1]);
					
				}
				else if( tempString.startsWith("Target_State:")){
					
					String[] srcb = tempString.split(": ");
					String[] srcb1 = srcb[1].split(", ");
					String[] ac = srcb1[0].split(" ");
					String[] vq = srcb1[1].split(" ");
					System.out.println(ac[1] + ", " + vq[1]);
					int activecall = Integer.parseInt(ac[1]);
					int videoquality = Integer.parseInt(vq[1]);
					HashMap<String, Object> variables = new HashMap<String, Object>();
                	variables.put("activecall", activecall);
            		variables.put("videoquality", videoquality);
					
            		String stateName = srcb[1];
            		
            		State tempState = new State( variables, stateName);
            		
            		tempBP.settargetstate(tempState);
					
				}
				else if( tempString.startsWith("Transition_Trigger:")){
					
					String[] srcb = tempString.split(" ");
            		int l_srcb = srcb[1].length();
            		String trigger = srcb[1].substring(0, l_srcb - 1);
					
            		
				}
				else if( tempString.startsWith("Transition_Network_Environment:")){
					
				}
				else {
					System.out.println("End of One BP.");
					ExistingBP.add(tempBP);
					
					
					tempBP = null;
				}
				
            }
			System.out.println("BP List Length: " + ExistingBP.size());
			
			
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
	public static List<SystemBehavior> ReadBehaviourPairsFromFile_2_ValueSet(String fileName){
		
		File file = new File(fileName);
		BufferedReader reader = null;
		
		//BehaviourPair bp = new BehaviourPair();
		List<SystemBehavior> ExistingBP = new ArrayList<SystemBehavior>();
		SystemBehavior tempBP = null;
		
		try {
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			
			while ((tempString = reader.readLine()) != null) {
				
				System.out.println(tempString);
				
				if( tempString.startsWith("BP")){
					tempBP = new SystemBehavior();
				}
				else if( tempString.startsWith("Source_State:")){
					
					String[] srcb = tempString.split(" ");
					tempBP.getsourcestate().setStateName(srcb[1]);
					
				}
				else if( tempString.startsWith("Target_State:")){
					
					String[] srcb = tempString.split(": ");
					String[] srcb1 = srcb[1].split(", ");
					String ac_con = srcb1[0];
					String vq_con = srcb1[1];
					
					ValueSet ac = new ValueSet();
					ac.AddConstriantsForValueSet(ac_con);
					ValueSet vq = new ValueSet();
					vq.AddConstriantsForValueSet(vq_con);
					
					HashMap<String, ValueSet> variables = new HashMap<String, ValueSet>();
                	variables.put("activecall", ac);
            		variables.put("videoquality", vq);
					
            		String stateName = srcb[1];
            		
            		State tempState = new State( stateName, variables);
            		
            		tempBP.settargetstate(tempState);
					
				}
				else if( tempString.startsWith("Transition_Trigger:")){
					
					String[] srcb = tempString.split(" ");
            		int l_srcb = srcb[1].length();
            		String trigger = srcb[1].substring(0, l_srcb - 1);
					
				}
				else if( tempString.startsWith("Transition_Network_Environment:")){
					
				}
				else {
					System.out.println("End of One BP.");
					ExistingBP.add(tempBP);
					
					tempBP = null;
				}
				
            }
			System.out.println("BP List Length: " + ExistingBP.size());
			
			
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
	
	
	
	
}
