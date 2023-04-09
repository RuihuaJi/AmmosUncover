package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.TestCase;


import java.util.ArrayList;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.ValueSet;

public class EdgeVertex {
	
	
	//Edge
	public String trigger;
	public Double PacketLoss;
	public Double PakcetDelay;
	public Double PacketDuplication;
	public Double PacketCorruption;
	
	
	//Vertex
	public ValueSet activecall;
	public ValueSet videoquality;
	
	
	public EdgeVertex(){
		this.activecall = new ValueSet();
		this.videoquality = new ValueSet();
		
		this.trigger = "";
		this.PacketLoss = 0.0;
		this.PakcetDelay = 0.0;
		this.PacketDuplication = 0.0;
		this.PacketCorruption = 0.0;
	}
	
	public double Constraints2Value(ValueSet BPc){
		
		List<ValueSetConstraint> BPCList = new ArrayList<ValueSetConstraint>();
		if( BPc == null){
			System.out.println("[----------]{[BPc == null]}");
		}
		if( BPc.getValueSet().isEmpty()){
			System.out.println("[----------]{[BPc.getValueSet().isEmpty()]}");
		}
		int l = BPc.getValueSet().size();
		
		for( int i = 0; i < l; i ++){
			String[] srcs = BPc.getValueSet().get(i).split(" ");
			ValueSetConstraint temp = new ValueSetConstraint();
			temp.varLeft = srcs[0];
			temp.operator = srcs[1];
			temp.varRight = srcs[2];
			BPCList.add(temp);
			
		}
		
		int numOfLessThan = 0;
		int numOfGreaterThan = 0;
		int numOfEqualTo = 0;
		
		for( int i = 0; i < l; i ++){
			
			if( BPCList.get(i).operator.equals("==")){
				
				numOfEqualTo ++;
			}
			else if( BPCList.get(i).operator.equals("<=") || BPCList.get(i).operator.equals("<")){
				numOfLessThan ++;
				
			}
			else if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
				numOfGreaterThan ++;
				
			}
			else {
				System.out.println("#################### {[Error][Wrong operator!][EdgeVertex]} ####################");
				return -1;
			}
			
		}
		
		if( ( numOfLessThan >= 2) || ( numOfGreaterThan >= 2) || ( numOfLessThan == 1 && numOfGreaterThan >= 2)
				|| ( numOfLessThan >= 2 && numOfGreaterThan == 1)){
			System.out.println("#################### {[Warning][Unsolved situation 1!][EdgeVertex]} ####################");
			return -1;
		}
		
		double value = 0;
		if( numOfLessThan == 0 && numOfGreaterThan == 0){
			if( numOfEqualTo == 1){
				value = Double.parseDouble( BPCList.get(0).varRight);
			}
			else {
				System.out.println("#################### {[Warning][Some Impossible situation!][EdgeVertex]} ####################");
				return -1;
			}
		}
		else if( numOfLessThan == 0 && numOfGreaterThan == 1){
			double scrop = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
					scrop = Double.parseDouble( BPCList.get(i).varRight);
				}
			}
			value = scrop;
		}
		else if( numOfLessThan == 1 && numOfGreaterThan == 0){
			double scrop = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals("<") || BPCList.get(i).operator.equals("<=")){
					scrop = Double.parseDouble( BPCList.get(i).varRight);
				}
			}
			value = scrop;
		}
		else if( numOfLessThan == 1 && numOfGreaterThan == 1){
			double scropG = 0;
			double scropL = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
					scropG = Double.parseDouble( BPCList.get(i).varRight);
				}
				if( BPCList.get(i).operator.equals("<") || BPCList.get(i).operator.equals("<=")){
					scropL = Double.parseDouble( BPCList.get(i).varRight);	
				}
			}
			if( scropG <= scropL){
				value = (scropL + scropG)/2;
			}
			else {
				value = scropG;
			}
			
		}
		else {
			System.out.println("#################### {[Warning][Unsolved situation 2!][EdgeVertex]} ####################");
			return -1;
		}
		
		
		
		
		
		
		return value;
	}
	class ValueSetConstraint{
		String varLeft;
		String operator;
		String varRight;
	}
	
	public void setTrigger(String trigger){
		this.trigger = trigger;
	}
	public String getTrigger(){
		return this.trigger;
	}
	public void setPacketLoss(Double packetLoss){
		this.PacketLoss = packetLoss;
	}
	public Double getPacketLoss(){
		return this.PacketLoss;
	}
	public void setPacketDelay(Double packetDelay){
		this.PakcetDelay = packetDelay;
	}
	public Double getPacketDelay(){
		return this.PakcetDelay;
	}
	public void setPacketDuplication(Double packetDuplication){
		this.PacketDuplication = packetDuplication;
	}
	public Double getPacketDuplication(){
		return this.PacketDuplication;
	}
	public void setPacketCorruption(Double packetCorruption){
		this.PacketCorruption = packetCorruption;
	}
	public Double getPacketCorruption(){
		return this.PacketCorruption;
	}
	

	public void setActivecall(ValueSet ac){
		this.activecall = ac;
	}
	public ValueSet getActivecall(){
		return this.activecall;
	}
	public void setVideoquality(ValueSet vq){
		this.videoquality = vq;
	}
	public ValueSet getVideoquality(){
		return this.videoquality;
	}
	
	
	
	
	
	
}
