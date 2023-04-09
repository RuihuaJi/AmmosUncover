package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;


public class WriteStateMachine2File {

	
	public static void writeStateMachine2File(StateMachine s,String fileName){

		
		
		File file = new File(fileName);
		
		try {
			if(!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if(!file.exists()){
				file.createNewFile();
			}
			
			FileWriter fileWritter = new FileWriter(fileName, true);
			BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
			
			List<State> states=s.getallstates();
			for(State state:states){
				String stateData="State";
				String name=state.getStateName();
				HashMap<String, ValueSet> vs = new HashMap<String, ValueSet>();
				vs=state.getSystemVariables_ValueSet();
				List<String> values=vs.get("activecall").getValueSet();
				String ac=values.get(0);
				values=vs.get("videoquality").getValueSet();
				String vq=values.get(0);
				stateData=stateData+" {"+name+"}; activecall {"+ac+"}; videoquality {"+vq+"}\n";
				bufferWritter.write(stateData);
			}
			
			
			List<Transition> transitions=s.getalltransitions();
			for(Transition transition:transitions){
				String transitionData="Transition\n";
				String sourcestate=transition.getsourcestate().getStateName();
				String targetstate=transition.gettargetstate().getStateName();
				List<String> triggers=transition.getTriggers();
				String trigger="";
				if(!triggers.isEmpty()) trigger=triggers.get(0);
				
				List<String> values=transition.getConditions().get("PacketLoss").getValueSet();
				String packetloss="";
				for(String pl:values){
					packetloss=packetloss+pl;
				}
				values=transition.getConditions().get("PacketDelay").getValueSet();
				String packetdelay="";
				for(String pd:values){
					packetdelay=packetdelay+pd;
				}
				values=transition.getConditions().get("PacketDuplication").getValueSet();
				String packetduplication="";
				for(String pdu:values){
					packetduplication=packetduplication+pdu;
				}
				values=transition.getConditions().get("PacketDelay").getValueSet();
				String packetcorruption="";
				for(String pc:values){
					packetcorruption=packetcorruption+pc;
				}
				String condition="  PacketLoss {"+packetloss+"}, PacketDelay {"+packetdelay+"}, PacketDuplication {"+packetduplication+"}, PacketCorruption {"+packetcorruption+"}\n";
				transitionData=transitionData+"  Source_State {"+sourcestate+"}\n"+"  Target_State {"+targetstate+"}\n"+"  trigger {"+trigger+"}\n"+condition;
				bufferWritter.write(transitionData);
			}
			
			bufferWritter.flush();
			
			bufferWritter.close();
			fileWritter.close();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

				


	}
	
	

}
