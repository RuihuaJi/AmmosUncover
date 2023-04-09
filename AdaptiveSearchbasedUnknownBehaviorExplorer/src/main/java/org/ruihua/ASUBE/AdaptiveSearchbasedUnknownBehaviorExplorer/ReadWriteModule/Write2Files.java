package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.*;

public class Write2Files {
	
	public Write2Files(){
		
	}
	
	
	public static void WriteBehaviourPairs2File(List<SystemBehavior> BehaviourPairs, String fileName){
		
		int len = BehaviourPairs.size();
		
		for( int i = 0; i < len; i ++){
			
			SystemBehavior temp = BehaviourPairs.get(i);
			
			try {
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data = "BP@No." + i + ":\n";
				data = data + "Source_State: " + temp.getsourcestate().getStateName() + "\n";
				data = data + "Target_State: activecall " + temp.gettargetstate().getSystemVariables().get("activecall") 
						+ ", videoquality " + temp.gettargetstate().getSystemVariables().get("videoquality") + "\n";
				data = data + "Transition_Network_Environment: PacketLoss " + temp.getnetworkenvironment().getPacketLoss()
						+ ", PakcetDelay " + temp.getnetworkenvironment().getPacketDelay()
						+ ", PacketDuplication " + temp.getnetworkenvironment().getPacketDuplication()
						+ ", PacketCorruption " + temp.getnetworkenvironment().getPacketCorruption()
						+ ".\n";
				data = data + "\n";
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	//These bp are all predicted.
	public static void WriteBehaviourPairs2File_2_ValueSet(List<SystemBehavior> BehaviourPairs, String fileName){
		
		int len = BehaviourPairs.size();
		
		for( int i = 0; i < len; i ++){
			
			SystemBehavior temp = BehaviourPairs.get(i);
			
			try {
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data = "BP@No." + i + ":\n";
				data = data + "Source_State: " + temp.getsourcestate().getStateName() + "\n";
				data = data + "Target_State: " + temp.gettargetstate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0) 
						+ ", " + temp.gettargetstate().getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0) + "\n";
				//data = data + "Transition_Trigger: " + temp.gettransition().getTriggers().get(0) + ";\n";
				data = data + "Transition_Network_Environment: PacketLoss " + temp.getnetworkenvironment().getPacketLoss()
						+ ", PakcetDelay " + temp.getnetworkenvironment().getPacketDelay()
						+ ", PacketDuplication " + temp.getnetworkenvironment().getPacketDuplication()
						+ ", PacketCorruption " + temp.getnetworkenvironment().getPacketCorruption()
						+ ".\n";
				data = data + "\n";
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void WriteBehaviourPairs2File_v201606(List<Solution> BehaviourPairs, StateMachine s, String fileName){
		
		int len = BehaviourPairs.size();
		
		for( int i = 0; i < len; i ++){
			
			Solution current_bp = BehaviourPairs.get(i);
			
			try {
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data = "BP_" + i + "\n";
				
				//Source State
				String stateName = s.getallstates().get( (int)current_bp.getsolution().get("SourceState")).getStateName();
				data = data + "  Source_State {" + stateName + "}\n";
				
				//Target State
				data = data + "  Target_State {}; ";
				int ac_v = (int)current_bp.getsolution().get("activecall");
				int vc_v = (int)current_bp.getsolution().get("videoquality");
				data = data + "activecall {activecall == " + ac_v + "}; videoquality {videoquality == "
						+ vc_v + "}\n";
				
				//trigger
				String UO = "";
				int trig = (int)current_bp.getsolution().get("UserOperation");
				if( trig == 0){
					UO = "null";
				}
				else if( trig == 1){
					UO = "dial";
				}
				else if( trig == 2){
					UO = "disconnect";
				}
				data = data + "  trigger {" + UO + "}\n";
				
				//Network Environment
				double loss = (double)current_bp.getsolution().get("PacketLoss");
				double delay = (double)current_bp.getsolution().get("PacketDelay");
				double duplication = (double)current_bp.getsolution().get("PacketDuplication");
				double corruption = (double)current_bp.getsolution().get("PacketCorruption");
				data = data + "  PacketLoss {PacketLoss == " + loss + "}, PacketDelay {PacketDelay == "
						+ delay + "}, PacketDuplication {PacketDuplication == " + duplication + 
						"}, PacketCorruption {PacketCorruption == " + corruption + "}";
				
				data = data + "\n";
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	public static void WriteStateMachine2File(StateMachine stateMachine, String fileName){
		
		List<State> AllStates = stateMachine.getallstates();
		List<Transition> AllTransitions = stateMachine.getalltransitions();
		
		int len_number_of_states = AllStates.size();
		for( int i = 0; i < len_number_of_states; i ++){
			
			try {
				
				State tempState = AllStates.get(i);
				
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data = "State: ";
				data = data + tempState.getStateName() + ";";
				data = data + " activecall " + tempState.getSystemVariables().get("activecall") 
						+ ", videoquality " + tempState.getSystemVariables().get("videoquality") + ".\n";
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		int len_number_of_transitions = AllTransitions.size();
		for( int j = 0; j < len_number_of_transitions; j ++){
			
			try {
				Transition tempTransition = AllTransitions.get(j);
				
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data_1 = "Transition:\n";
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data_1);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public static void WriteStateMachine2File_2_ValueSet(StateMachine stateMachine, String fileName){
		
		List<State> AllStates = stateMachine.getallstates();
		List<Transition> AllTransitions = stateMachine.getalltransitions();
		
		int len_number_of_states = AllStates.size();
		for( int i = 0; i < len_number_of_states; i ++){
			
			try {
				
				State tempState = AllStates.get(i);
				
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data = "State: ";
				data = data + tempState.getStateName() + ";";
				
				ValueSet t1 = tempState.getSystemVariables_ValueSet().get("activecall");
				int j = 0;
				for( j = 0; j < t1.getValueSet().size(); j ++){
					data = data + " " + t1.getValueSet().get(j);
					if( j == t1.getValueSet().size() - 1){
						data = data + ";";
					}
					else {
						data = data + ",";
					}
				}
				ValueSet t2 = tempState.getSystemVariables_ValueSet().get("videoquality");
				for( j = 0; j < t2.getValueSet().size(); j ++){
					data = data + " " + t2.getValueSet().get(j);
					if( j == t2.getValueSet().size() - 1){
						data = data + ";\n";
					}
					else {
						data = data + ",";
					}
				}
				
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		int len_number_of_transitions = AllTransitions.size();
		for( int j = 0; j < len_number_of_transitions; j ++){
			
			try {
				Transition tempTransition = AllTransitions.get(j);
				
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data_1 = "Transition:\n";
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data_1);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	
	
	
	
	public static void WriteTestCases2File(List<TestCase> testCases, String fileName){
		
		int len = testCases.size();
		
		for( int i = 0; i < len; i ++){
			
			TestCase tc = testCases.get(i);
			
			try {
				
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data = "TC[" + i + "]:\n";
				
				List<State> TC_States = tc.getallstates();
				List<Transition> TC_Transitions = tc.getalltransitions();
				
				int len_TC_States = TC_States.size();
				int len_TC_Transitions = TC_Transitions.size();
				//System.out.println(i + ": " + len_TC_States + ", " + len_TC_Transitions);
				
				int j = 0; 
				int k = 0;
				for( ; j < len_TC_States; j ++){
					
					data = data + "  L[" + (j+k) + "]:\n";
					State tempState = TC_States.get(j);
					data = data + "    Type: State\n";
					data = data + "    " + tempState.getStateName()
							+ ", activecall " + tempState.getSystemVariables().get("activecall")
							+ ", videoquality " + tempState.getSystemVariables().get("videoquality")
							+ ";\n";
					
					
					if( k < len_TC_Transitions){
						data = data + "  L[" + (j+k+1) + "]:\n";
						Transition tempTransition = TC_Transitions.get(k);
						k = k + 1;
						
						data = data + "    Type: Transition\n";
						
					}
					
					
					
				}
				
				SystemBehavior tempBP = tc.getBehaviourPair();
				data = data + "  BP:\n";
				data = data + "    Source_State: " + tempBP.getsourcestate().getStateName() + "\n";
				data = data + "    Target_State: activecall " + tempBP.gettargetstate().getSystemVariables().get("activecall") 
						+ ", videoquality " + tempBP.gettargetstate().getSystemVariables().get("videoquality") + "\n";
				//data = data + "    Transition_Trigger: " + tempBP.gettransition().getTriggers().get(0) + ";\n";
				data = data + "    Transition_Network_Environment: PacketLoss " + tempBP.getnetworkenvironment().getPacketLoss()
						+ ", PakcetDelay " + tempBP.getnetworkenvironment().getPacketDelay()
						+ ", PacketDuplication " + tempBP.getnetworkenvironment().getPacketDuplication()
						+ ", PacketCorruption " + tempBP.getnetworkenvironment().getPacketCorruption()
						+ ".\n";
				
				
				
				
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
	}
	public static void WriteTestCases2File_2_ValueSet(List<TestCase> testCases, String fileName){
		
		int len = testCases.size();
		
		for( int i = 0; i < len; i ++){
			
			TestCase tc = testCases.get(i);
			
			try {
				
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data = "TC[" + i + "]:\n";
				
				List<State> TC_States = tc.getallstates();
				List<Transition> TC_Transitions = tc.getalltransitions();
				
				int len_TC_States = TC_States.size();
				int len_TC_Transitions = TC_Transitions.size();
				//System.out.println(i + ": " + len_TC_States + ", " + len_TC_Transitions);
				
				int j = 0; 
				int k = 0;
				for( ; j < len_TC_States; j ++){
					
					data = data + "  L[" + (j+k) + "]:\n";
					State tempState = TC_States.get(j);
					data = data + "    Type: State\n";
					data = data + "    " + tempState.getStateName() + ", ";
					int len_ac_con = tempState.getSystemVariables_ValueSet().get("activecall").getValueSet().size();
					if( len_ac_con == 1){
						data = data	+ tempState.getSystemVariables_ValueSet().get("activecall").getValueSet().get(0);
					}
					else if( len_ac_con == 2){
						data = data	+ tempState.getSystemVariables_ValueSet().get("activecall").getValueSet().get(0) + ", ";
						data = data	+ tempState.getSystemVariables_ValueSet().get("activecall").getValueSet().get(1);
					}
					else {
						System.out.println("Illegel!");
					}
					
					data = data + ", ";
					
					int len_vq_con = tempState.getSystemVariables_ValueSet().get("videoquality").getValueSet().size();
					if( len_vq_con == 1){
						data = data	+ tempState.getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0);
					}
					else if( len_vq_con == 2){
						data = data	+ tempState.getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0) + ", ";
						data = data	+ tempState.getSystemVariables_ValueSet().get("videoquality").getValueSet().get(1);
					}
					else {
						System.out.println("Illegel!");
					}
					
					
					data = data + ";\n";
					
					
					if( k < len_TC_Transitions){
						data = data + "  L[" + (j+k+1) + "]:\n";
						Transition tempTransition = TC_Transitions.get(k);
						k = k + 1;
						
						data = data + "    Type: Transition\n";
						
					}
					
					
					
				}
				
				SystemBehavior tempBP = tc.getBehaviourPair();
				data = data + "  BP:\n";
				data = data + "    Source_State: " + tempBP.getsourcestate().getStateName() + "\n";
				data = data + "    Target_State: " + tempBP.gettargetstate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0) 
						+ ", " + tempBP.gettargetstate().getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0) + "\n";
				data = data + "    Transition_Network_Environment: PacketLoss " + tempBP.getnetworkenvironment().getPacketLoss()
						+ ", PakcetDelay " + tempBP.getnetworkenvironment().getPacketDelay()
						+ ", PacketDuplication " + tempBP.getnetworkenvironment().getPacketDuplication()
						+ ", PacketCorruption " + tempBP.getnetworkenvironment().getPacketCorruption()
						+ ".\n";
				
				
				
				
				
				FileWriter fileWritter = new FileWriter(fileName, true);
				BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
				bufferWritter.write(data);
				bufferWritter.flush();
				
				bufferWritter.close();
				fileWritter.close();
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String args[]){
		
		
		
		
		
	}
	
}
