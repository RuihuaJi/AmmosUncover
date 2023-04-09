package org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.Problem;


import java.util.*;

import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.*;
import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.ValueSet.ValueSet;

public class Solution {
	
	
	private HashMap<String, Object> solution;
	private int solutionlength;
	
	public Solution(){
		this.solution = new HashMap<String, Object>();
		this.solutionlength = 0;
	}
	
	
	public static SystemBehavior TransformSolution2BPairValueSet_v201606(Solution v, StateMachine stateMachine){
		SystemBehavior BPair = new SystemBehavior();
		
		State sourceState = stateMachine.getallstates().get(Integer.valueOf(v.getsolution().get("SourceState").toString()));
		
		BPair.setsourcestate( sourceState);
		
		HashMap<String, ValueSet> variables_valueset = new HashMap<String, ValueSet>();
		ValueSet ac = new ValueSet();
		String con_ac = "activecall == " + v.getsolution().get("activecall");
		ac.AddConstriantsForValueSet(con_ac);
		ValueSet vq = new ValueSet();
		String con_vq = "videoquality == " + v.getsolution().get("videoquality");
		vq.AddConstriantsForValueSet(con_vq);
		variables_valueset.put("activecall", ac);
		variables_valueset.put("videoquality", vq);
		
		String sname = "new" + stateMachine.getallstates().size();//!
		State targetState = new State(sname, variables_valueset);
		
		BPair.settargetstate(targetState);
		
		
		//=============Network environment setting===============
		NetworkCondition ne = new NetworkCondition();
		ne.setPacketLoss(Double.valueOf(v.getsolution().get("PacketLoss").toString()));
		ne.setPacketDelay(Double.valueOf(v.getsolution().get("PacketDelay").toString()));
		ne.setPacketDuplication(Double.valueOf(v.getsolution().get("PacketDuplication").toString()));
		ne.setPacketCorruption(Double.valueOf(v.getsolution().get("PacketCorruption").toString()));
		BPair.setnetworkenvironment(ne);
		
		Transition t = new Transition( sourceState, targetState);
		
		List<String> triggers = t.getTriggers();
		if( Integer.valueOf(v.getsolution().get("UserOperation").toString()) == 0){
			triggers.add("null");
		}
		else if( Integer.valueOf(v.getsolution().get("UserOperation").toString()) == 1){
			triggers.add("dial");
		}
		else if( Integer.valueOf(v.getsolution().get("UserOperation").toString()) == 2){
			triggers.add("disconnect");
		}
		
		BPair.settrigger(triggers);
		//=============Conditions should be only one operation existing.=============
		//t.setNetworkEnvironment(ne);
		//BPair.settransition(t);
		
		
		
		return BPair;
	}
	
	public static Transition TransformBehaviourPair2Transition_v201610(SystemBehavior bp){
		
		State sourceState_t = bp.getsourcestate();
		State targetState_t = bp.gettargetstate();
		
		Transition Ttemp = new Transition(sourceState_t, targetState_t);
		Ttemp.addTriggers(bp.gettrigger().get(0));
		
		HashMap<String, ValueSet> cs = Ttemp.getConditions();
		ValueSet vs_l= new ValueSet();
		vs_l.getValueSet().add("PacketLoss == " + bp.getnetworkenvironment().getPacketLoss());
		cs.put("PacketLoss", vs_l);
		ValueSet vs_d= new ValueSet();
		vs_d.getValueSet().add("PacketDelay == " + bp.getnetworkenvironment().getPacketDelay());
		cs.put("PacketDelay", vs_d);
		ValueSet vs_du= new ValueSet();
		vs_du.getValueSet().add("PacketDuplication == " + bp.getnetworkenvironment().getPacketDuplication());
		cs.put("PacketDuplication", vs_du);
		ValueSet vs_c= new ValueSet();
		vs_c.getValueSet().add("PacketCorruption == " + bp.getnetworkenvironment().getPacketCorruption());
		cs.put("PacketCorruption", vs_c);
		
		sourceState_t.addoutTransition(Ttemp);
		targetState_t.addinTransition(Ttemp);
		
		//Ttemp
		
		return Ttemp;
	}
	
	
	
	
	
	public int getsolutionlength(){
		return this.solutionlength;
	}
	public void setsolutionlength(int len){
		this.solutionlength = len;
	}
	
	public void addsolutionmember(String membername, Object o){
		this.solution.put(membername, o);
		this.solutionlength ++;
	}
	public void addsolutionmemberWithoutChangeLength(String membername, Object o){
		this.solution.put(membername, o);
	}
	public void emptysolution(){
		this.solution = new HashMap<String, Object>();
		this.solutionlength = 0;
	}
	public HashMap<String, Object> getsolution(){
		return this.solution;
	}
	public static void printSolution(Solution v){
		String printstring = "Solution -";
		Iterator iter = v.getsolution().entrySet().iterator();
		while( iter.hasNext()){
			Map.Entry entry = (Map.Entry)iter.next();
			String key = (String)entry.getKey();
			Object val = entry.getValue();
			printstring = printstring + " " + key + ":" + val;
		}
		System.out.println(printstring);
	}

}
