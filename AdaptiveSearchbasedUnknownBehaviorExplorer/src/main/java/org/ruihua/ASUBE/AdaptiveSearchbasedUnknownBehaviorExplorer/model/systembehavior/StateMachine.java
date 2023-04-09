package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior;

import java.util.*;

public class StateMachine {

	
	
	private State startstate;
	
	private List<State> states;
	private List<Transition> transitions;
	
	
	
	
	public StateMachine(){
		this.startstate = null;
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		
	}
	public StateMachine(State startstate){
		this.startstate = startstate;
		this.states = new ArrayList<State>();
		this.transitions = new ArrayList<Transition>();
		this.addonestate(this.startstate);
	}
	
	
	public void initial(){
		//do something
	}
	
	
	
	
	public State getState(String stateName){
		State r = null;
		
		int l = this.getallstates().size();
		for( int i = 0; i < l; i ++){
			if(stateName.equals( this.getallstates().get(i).getStateName())){
				r = this.getallstates().get(i);
			}
		}
		return r;
	}
	public int getStateNO(String stateName){
		
		int r = -1;
		
		int l = this.getallstates().size();
		for( int i = 0; i < l; i ++){
			if(stateName.equals( this.getallstates().get(i).getStateName())){
				r = i;
			}
		}
		return r;
	}
	
	
	
	public boolean isANewState(State s){
		boolean checkResult = true;
		int l = this.states.size();
		for(int i = 0; i < l; i ++){
			if( s.isequal_ValueSet( this.states.get(i))){
				//do nothing
			}
			else {
				checkResult = false;
			}
		}
		return checkResult;
	}
	
	public boolean isANewTransition(Transition t){
		boolean checkResult = true;
		
		int l = this.transitions.size();
		
		for(int i = 0; i < l; i ++){
			if( this.transitions.get(i).isequalTriggerAndCondition(t))
				break;
		}
		
		return checkResult;
	}
	
	public int UpdateStateMachinebyAddANewTransition(Transition t){
		
		int l_state = this.states.size();
		
		State sourceState_t_inSM = null;
		State targetState_t = t.gettargetstate();
		
		int labeb_whether_break = 0;
		
		for(int i = 0; i < l_state; i ++){
			
			int cr = this.states.get(i).checkRelationshipBetweenTwoStates(targetState_t);
			
			
			if( cr == 1 || cr == 2){
				
				State ts = this.states.get(i);
				
				List<Transition> lt = ts.getinTransitions();
				
				int l_lt = lt.size();
				
				for(int j = 0; j < l_lt; j ++){
					
					if( lt.get(j).getsourcestate().isequal_ValueSet(t.getsourcestate())){
						
						sourceState_t_inSM = lt.get(j).getsourcestate();
						break;
					}
				}
				
				
				
				if( sourceState_t_inSM == null){
					
					for( int k = 0; k < l_state; k ++){
						if( this.states.get(k).isequal_ValueSet(t.getsourcestate())){
							sourceState_t_inSM = this.states.get(k);
							break;
						}
					}
					
					sourceState_t_inSM.addoutTransition(t);
					
					t.settargetstate(ts);
					
					ts.addinTransition(t);
					
					this.transitions.add(t);
					
					return 1;
				}
				else {
					
				}
				
				labeb_whether_break = 1;
				break;
			}
			else {
				
				//do nothing
			}
			
		}
		if( labeb_whether_break == 0){
			
			for( int k = 0; k < l_state; k ++){
				if( this.states.get(k).isequal_ValueSet(t.getsourcestate())){
					sourceState_t_inSM = this.states.get(k);
					break;
				}
			}
			
			sourceState_t_inSM.addoutTransition(t);
			
			this.transitions.add(t);
			this.states.add(t.gettargetstate());
			
			
			t.gettargetstate().setStateName("new" + this.states.size());
			
			return 2;
		}
		return 0;
		
	}
	public int CheckNewTransition(Transition t){
		int l_state = this.states.size();
		
		State sourceState_t_inSM = null;
		State targetState_t = t.gettargetstate();
		
		int labeb_whether_break = 0;
		
		for(int i = 0; i < l_state; i ++){
			
			int cr = this.states.get(i).checkRelationshipBetweenTwoStates(targetState_t);
			
			if( cr == 1 || cr == 2){
				
				State ts = this.states.get(i);
				
				List<Transition> lt = ts.getinTransitions();
				
				int l_lt = lt.size();
				for(int j = 0; j < l_lt; j ++){
					if( lt.get(j).getsourcestate().isequal_ValueSet(t.getsourcestate())){
						sourceState_t_inSM = lt.get(j).getsourcestate();
						break;
					}
				}
				
				
				if( sourceState_t_inSM == null){
					
					for( int k = 0; k < l_state; k ++){
						if( this.states.get(k).isequal_ValueSet(t.getsourcestate())){
							sourceState_t_inSM = this.states.get(k);
							break;
						}
					}
					
					if(sourceState_t_inSM == null) {
						System.out.println("#################### {[Error!][It should be a new transition, but we can't find its source state from the State Machine! (1)][StateMachine]} ####################");
					}
					
					return 1;
				}
				else {
					
				}
				
				labeb_whether_break = 1;
				break;
			}
			else {
				
				//do nothing
			}
		}
		if( labeb_whether_break == 0){
			
			for( int k = 0; k < l_state; k ++){
				if( this.states.get(k).isequal_ValueSet(t.getsourcestate())){
					sourceState_t_inSM = this.states.get(k);
					break;
				}
			}
			
			if(sourceState_t_inSM == null) {
				System.out.println("#################### {[Error!][It should be a new transition, but we can't find its source state from the State Machine! (2)][StateMachine]} ####################");
			}
			return 2;
		}
		return 0;
		
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
		this.startstate = s;
	}
	public State getstartstate(){
		return this.startstate;
	}
	
}

