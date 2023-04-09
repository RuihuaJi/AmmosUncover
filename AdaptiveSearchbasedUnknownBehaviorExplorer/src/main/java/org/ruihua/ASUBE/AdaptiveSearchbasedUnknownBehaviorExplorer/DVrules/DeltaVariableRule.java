package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.DVrules;

import java.util.*;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.ValueSet;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.*;


public class DeltaVariableRule {
	
	private int deltaAC;
	private int deltaVQ;
	private String trigger;
	private double nc_delay;
	private double nc_packetloss;
	private double nc_packetduplication;
	private double nc_packetcorruption;
	
	
	private SubspaceStateUOp sourceSubSpace;//This variable labels the source of the DVRule, and it will be used to analysis!
	
	

	public DeltaVariableRule() {}
	
	public DeltaVariableRule(Solution b, StateMachine s) {
		
		HashMap<String, Object> solution_val = b.getsolution();
		State sourcestate = s.getallstates().get( (int)solution_val.get("SourceState"));
		
		deltaAC = CalculateDeltaVariable( (int)solution_val.get("activecall"), sourcestate.getSystemVariables_ValueSet().get("activecall"));
		deltaVQ = CalculateDeltaVariable( (int)solution_val.get("videoquality"), sourcestate.getSystemVariables_ValueSet().get("videoquality"));
		
		int useroperation = (int)solution_val.get("UserOperation");
		if( useroperation == 0) { trigger = "null";}
		else if( useroperation == 1) { trigger = "dial";}
		else if( useroperation == 2) { trigger = "disconnect";}
		else { trigger = "UnknownCase"; System.out.println("***** Warning: DVRule Construction, calculate trigger value missing! *****");}
		
		nc_delay = (double)solution_val.get("PacketDelay");
		nc_packetloss = (double)solution_val.get("PacketLoss");
		nc_packetduplication = (double)solution_val.get("PacketDuplication");
		nc_packetcorruption = (double)solution_val.get("PacketCorruption");
		
		SubspaceStateUOp sS = new SubspaceStateUOp();
		sS.setState(sourcestate);
		sS.setUserOp(this.trigger);
		this.sourceSubSpace = sS;
		
	}
	
	
	public DeltaVariableRule(Solution b, StateMachine s, String calculateLabel) {
		
		
		
		HashMap<String, Object> solution_val = b.getsolution();
		State sourcestate = s.getallstates().get( (int)solution_val.get("SourceState"));
		int acvalue = (int)solution_val.get("activecall");
		int vqvalue = (int)solution_val.get("videoquality");
		
		if(calculateLabel.equals("VQMetrix")) {
			deltaAC = CalculateDeltaVariable( acvalue, sourcestate.getSystemVariables_ValueSet().get("activecall"));
			deltaVQ = CalculateDeltaVariable( VideoQuality.VQValueTransformRV2V(vqvalue), sourcestate.getSystemVariables_ValueSet().get("videoquality"));
		}
		else {
			deltaAC = CalculateDeltaVariable( (int)solution_val.get("activecall"), sourcestate.getSystemVariables_ValueSet().get("activecall"));
			deltaVQ = CalculateDeltaVariable( (int)solution_val.get("videoquality"), sourcestate.getSystemVariables_ValueSet().get("videoquality"));
		}
		
		
		int useroperation = (int)solution_val.get("UserOperation");
		if( useroperation == 0) { trigger = "null";}
		else if( useroperation == 1) { trigger = "dial";}
		else if( useroperation == 2) { trigger = "disconnect";}
		else { trigger = "UnknownCase"; System.out.println("***** Warning: DVRule Construction, calculate trigger value missing! *****");}
		
		nc_delay = (double)solution_val.get("PacketDelay");
		nc_packetloss = (double)solution_val.get("PacketLoss");
		nc_packetduplication = (double)solution_val.get("PacketDuplication");
		nc_packetcorruption = (double)solution_val.get("PacketCorruption");
		
		SubspaceStateUOp sS = new SubspaceStateUOp();
		sS.setState(sourcestate);
		sS.setUserOp(this.trigger);
		this.sourceSubSpace = sS;
		
	}
	
	public int CalculateDeltaVariable(double Vv, ValueSet BPc){
		
		
		List<ValueSetConstraint> BPCList = new ArrayList<ValueSetConstraint>();
		
		
		if( BPc == null){
			System.out.println("[>>>>>>>>>>]BPc == null");
		}
		if( BPc.getValueSet().isEmpty()){
			System.out.println("[>>>>>>>>>>]BPc.getValueSet().isEmpty()");
		}
		int l = BPc.getValueSet().size();
		
		for( int i = 0; i < l; i ++){
			String[] srcs = BPc.getValueSet().get(i).split(" ");
			ValueSetConstraint temp = new ValueSetConstraint();
			temp.varLeft = srcs[0];
			temp.operator = srcs[1];
			temp.varRight = srcs[2];
			BPCList.add(temp);
			
			//System.out.println("Operator:" + temp.operator);
			
		}
		
		
		double BPcValue = Double.parseDouble(BPCList.get(0).varRight);
		int dis = (int)(Vv - BPcValue);
		
		return dis;
		
		
		
	}
	
	
	
	
	/**Get and Set Methods**/
	public int getDeltaAC() { return this.deltaAC;}
	public void setDeltaAC(int dAC) { this.deltaAC = dAC;}
	public int getDeltaVQ() { return this.deltaVQ;}
	public void setDeltaVQ(int dVQ) { this.deltaAC = dVQ;}
	public String getTrigger() { return this.trigger;}
	public void setTrigger(String tri) { this.trigger = tri;}
	public double getNc_delay() { return this.nc_delay;}
	public void setNc_delay(double delay) { this.nc_delay = delay;}
	public double getNc_packetloss() { return this.nc_packetloss;}
	public void setNc_packetloss(double packetloss) { this.nc_packetloss = packetloss;}
	public double getNc_packetduplication() { return this.nc_packetduplication;}
	public void setNc_packetduplication(double packetduplication) { this.nc_packetduplication = packetduplication;}
	public double getNc_packetcorruption() { return this.nc_packetcorruption;}
	public void setNc_packetcorruption(double packetcorruption) { this.nc_packetcorruption = packetcorruption;}
	public SubspaceStateUOp getSourceSubSpace() {return this.sourceSubSpace;}
	public void setSourceSubspace(SubspaceStateUOp sssp) {this.sourceSubSpace = sssp;}
	
	public static void main(String[] args) {
		
	}
}
