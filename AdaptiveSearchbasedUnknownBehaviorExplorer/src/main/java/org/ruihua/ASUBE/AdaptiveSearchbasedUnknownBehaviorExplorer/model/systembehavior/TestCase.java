package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestCase {
	
	
	private State startState;//First Part
	
	private List<State> states;
	private List<Transition> transitions;
	private SystemBehavior behaviourPair;//End Part
	
	public TestCase(){
		this.startState = new State();
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		this.behaviourPair = new SystemBehavior();
	}
	public TestCase( State startState){
		this.startState = startState;
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		this.addonestate(startState);
		this.behaviourPair = new SystemBehavior();
	}
	public TestCase( SystemBehavior BP, StateMachine S){
		
		this.startState = S.getstartstate();
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		this.addonestate( this.startState);
		
		State targetState = BP.getsourcestate();
		if( this.startState.getStateName().equals(targetState.getStateName())){
			//do nothing
		}
		else if( targetState.getStateName().equals("Connected_1")){
			Transition t = this.startState.getoutTransitions().get(0);
			this.addonetransition( t);
			this.addonestate(t.gettargetstate());
			
		}
		else if( targetState.getStateName().equals("NotFull")){
			Transition t1 = this.startState.getoutTransitions().get(0);
			State s = t1.gettargetstate();
			Transition t2 = s.getoutTransitions().get(1);//Careful!
			this.addonetransition(t1);
			this.addonetransition(t2);
			this.addonestate(s);
			this.addonestate(t2.gettargetstate());
			
		}
		else if( targetState.getStateName().equals("Full")){
			Transition t1 = this.startState.getoutTransitions().get(0);
			State s1 = t1.gettargetstate();
			Transition t2 = s1.getoutTransitions().get(1);
			State s2 = t2.gettargetstate();
			Transition t3 = s2.getoutTransitions().get(1);
			
			this.addonetransition(t1);
			this.addonetransition(t2);
			this.addonetransition(t3);
			this.addonestate(s1);
			this.addonestate(s2);
			this.addonestate(targetState);
			
		}
		else {
			//null
		}
		
		
		
		
		
		this.behaviourPair = BP;
	}
	
	
	
	public SystemBehavior getBehaviourPair(){
		return this.behaviourPair;
	}
	
	public List<State> getallstates(){
		return this.states;
	}
	public void addonestate(State s){
		this.states.add(s);
	}
	
	public List<Transition> getalltransitions(){
		return this.transitions;
	}
	public void addonetransition(Transition t){
		this.transitions.add(t);
	}
	
	public void setstartstate(State s){
		this.startState = s;
	}
	public State getstartstate(){
		return this.startState;
	}
	
	
	
	
	
	
}
