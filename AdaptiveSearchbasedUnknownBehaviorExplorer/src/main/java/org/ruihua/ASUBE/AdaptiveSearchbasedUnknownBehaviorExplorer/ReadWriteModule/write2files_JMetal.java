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
import org.uma.jmetal.solution.DoubleSolution;

public class write2files_JMetal {
	
	public write2files_JMetal(){
		
	}
	
	
	public static void WriteOneBehaviourPair2File_JMetal(DoubleSolution behavior, StateMachine s, String fileName){
		
		
			
		try {
			File file = new File(fileName);
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			
			String data = "BP_jmetal_" + 0 + "\n";
			
			//Source State
			String stateName = s.getallstates().get( (int)Math.round(behavior.getVariableValue(0))).getStateName();
			data = data + "  Source_State {" + stateName + "}\n";
			
			//Target State
			data = data + "  Target_State {}; ";
			int ac_v = (int)Math.round(behavior.getVariableValue(6));
			int vc_v = (int)Math.round(behavior.getVariableValue(7));
			data = data + "activecall {activecall == " + ac_v + "}; videoquality {videoquality == "
					+ vc_v + "}\n";
			
			//trigger
			String UO = "";
			int trig = (int)Math.round(behavior.getVariableValue(1));
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
			double loss = behavior.getVariableValue(2);
			double delay = behavior.getVariableValue(3);
			double duplication = behavior.getVariableValue(4);
			double corruption = behavior.getVariableValue(5);
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
	
	public static void WriteDoubleSolutions2File_JMetal(List<DoubleSolution> behaviors, String fileName){
		
		
		try {
			File file = new File(fileName);
			if(!file.exists()){
				file.createNewFile();
			}
			
			String outputdata = "";
			
			int len = behaviors.size();
			int lenVars = 0;
			int lenObjs = 0;
			if(len > 0) {
				lenVars = behaviors.get(0).getNumberOfVariables();
				lenObjs = behaviors.get(0).getNumberOfObjectives();
			}
			
			for(int i = 0; i < len; i ++) {
				outputdata = outputdata + "Sol " + i + ": ";
				for(int j = 0; j < lenVars; j ++) {
					outputdata = outputdata + behaviors.get(i).getVariableValue(j) + " ";
				}
				outputdata = outputdata + "; Objs: ";
				for(int k = 0; k < lenObjs; k ++) {
					String temp = String.format("%.8f", behaviors.get(i).getObjective(k));
					outputdata = outputdata + temp + " ";
				}
				outputdata = outputdata + "\n";
			}
			
			FileWriter fileWritter = new FileWriter(fileName, true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			bufferWritter.write(outputdata);
			bufferWritter.flush();
			
			bufferWritter.close();
			fileWritter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
	public static void main(String args[]){
		
		
	}
	
}
