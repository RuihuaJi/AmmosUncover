package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior;

import java.util.ArrayList;
import java.util.List;

public class TestPath {
	
	
	private StateMachine stateMachine;
	
	private SystemBehavior behaviourPair;
	
	private List<Transition> path;
	
	
	public TestPath(){
		this.stateMachine = new StateMachine();
		this.behaviourPair = new SystemBehavior();
	}
	public TestPath(StateMachine s, SystemBehavior bp){
		this.stateMachine = s;
		this.behaviourPair = bp;
	}
	
	
	public void constructPath(){
		
		
		
		
	}
	
	
	
	
	
	public void setpath(List<Transition> path){
		this.path = path;
	}
	public List<Transition> getpath(){
		return this.path;
	}
	public void setstateMachine(StateMachine s){
		this.stateMachine = s;
	}
	public StateMachine getstateMachine(){
		return this.stateMachine;
	}
	public void setbehaviourPair(SystemBehavior bp){
		this.behaviourPair = bp;
	}
	public SystemBehavior getbehaviourPair(){
		return this.behaviourPair;
	}
	
}

