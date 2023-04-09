package org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel;

import java.util.*;

public class SystemBehavior {

	
	private State sourcestate;
	private State targetstate;
	
	private List<String> triggers;
	
	private NetworkCondition networkenvironmentVariables;
	
	public SystemBehavior(){
		this.sourcestate = new State();
		this.targetstate = new State();
		this.triggers = new ArrayList<String>();
		this.networkenvironmentVariables = new NetworkCondition();
	}
	public SystemBehavior(State source, List<String> t, State target, NetworkCondition ne){//network part unfinished
		this.sourcestate = source;
		this.targetstate = target;
		this.triggers = t;
		this.networkenvironmentVariables = ne;
	}
	
	public static List<SystemBehavior> StateMachineParser(StateMachine statemachine){
		
		List<SystemBehavior> behaviourpairs = new ArrayList<SystemBehavior>();
		
		return behaviourpairs;
	}
	
	
	public State getsourcestate(){
		return this.sourcestate;
	}
	public void setsourcestate(State s){
		this.sourcestate = s;
	}
	public State gettargetstate(){
		return this.targetstate;
	}
	public void settargetstate(State s){
		this.targetstate = s;
	}
	public List<String> gettrigger(){
		return this.triggers;
	}
	public void settrigger(List<String> t){
		this.triggers = t;
	}
	public NetworkCondition getnetworkenvironment(){
		return this.networkenvironmentVariables;
	}
	public void setnetworkenvironment(NetworkCondition ne){
		this.networkenvironmentVariables = ne;
	}
}

