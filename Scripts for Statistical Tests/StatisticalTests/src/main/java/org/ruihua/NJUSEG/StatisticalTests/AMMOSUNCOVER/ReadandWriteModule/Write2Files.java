package org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.ReadandWriteModule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.Problem.*;
import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.*;
import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.ValueSet.*;

public class Write2Files {
	
	public Write2Files(){
		
	}
	
	
	
	public static int WriteSolution2File_v202108(List<Solution> BehaviourPairs, StateMachine s, String fileName, String sourceName){
		
		int len = BehaviourPairs.size();
		
		int count = 0;
		
		for( int i = 0; i < len; i ++){
			
			Solution current_bp = BehaviourPairs.get(i);
			
			try {
				File file = new File(fileName);
				if(!file.exists()){
					file.createNewFile();
				}
				
				String data = "BP_" + i + "\n";
				
				if(Integer.parseInt(current_bp.getsolution().get("SourceState").toString()) == -1) {
					//ErrorHandler
					continue ;
				}
				
				State stateTemp = s.getallstates().get( Integer.parseInt(current_bp.getsolution().get("SourceState").toString()));
//				String stateName = s.getallstates().get( Integer.parseInt(current_bp.getsolution().get("SourceState").toString())).getStateName();
				data = data + "  Source_State {}; ";
				List<String> ac_s_v = stateTemp.getSystemVariables_ValueSet().get("activecall").getValueSet();
				data = data + "activecall {";
				for(int iii = 0; iii < ac_s_v.size(); iii ++) {
					if(iii == ac_s_v.size() - 1) {
						data = data + ac_s_v.get(iii);
						break;
					}
					data = data + ac_s_v.get(iii) + ", ";//!!! take care when reading executions repository
				}
				List<String> vq_s_v = stateTemp.getSystemVariables_ValueSet().get("videoquality").getValueSet();
				data = data + "}; videoquality {";
				for(int iii = 0; iii < vq_s_v.size(); iii ++) {
					if(iii == vq_s_v.size() - 1) {
						data = data + vq_s_v.get(iii);
						break;
					}
					data = data + vq_s_v.get(iii) + ", ";//!!! take care when reading executions repository
				}
				data = data + "}\n";
				
				
				//Target State
				data = data + "  Target_State {}; ";
				int ac_v = Integer.parseInt(current_bp.getsolution().get("activecall").toString());
				int vc_v = Integer.parseInt(current_bp.getsolution().get("videoquality").toString());
				
				if( vc_v < 5){
					vc_v = 0;
				}
				else if( vc_v >= 5 && vc_v < 10){
					vc_v = 1;
				}
				else if( vc_v >= 10 && vc_v < 25){
					vc_v = 2;
				}
				else{
					vc_v = 3;
				}
				
				data = data + "activecall {activecall == " + ac_v + "}; videoquality {videoquality == "
						+ vc_v + "}\n";
				
				//trigger
				String UO = "";
				int trig = Integer.parseInt(current_bp.getsolution().get("UserOperation").toString());
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
				double loss = Double.parseDouble(current_bp.getsolution().get("PacketLoss").toString());
				
//				double delay = Double.parseDouble(current_bp.getsolution().get("PakcetDelay").toString()); // Should be PacketDelay instead of PakcetDelay!!!!
				double delay = Double.parseDouble(current_bp.getsolution().get("PacketDelay").toString()); // For AmmosUncover using the correct ones
				double duplication = Double.parseDouble(current_bp.getsolution().get("PacketDuplication").toString());
				double corruption = Double.parseDouble(current_bp.getsolution().get("PacketCorruption").toString());
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
				
				count ++;
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				System.out.println("############# " + sourceName);
				e.printStackTrace();
			}
			
			
			
		}
		
		
		
		return count;
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String args[]){
		
		
		
	}
	
}
