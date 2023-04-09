package org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.ValueSet.*;

public class State {

	
	private String stateLabel;
	private HashMap<String, ValueSet> systemVariables_ValueSet;
	
	
	
	
	private String stateName;//0.state name
	
	private HashMap<String, Object> system_variables;//1.system variables
	
	private List<Transition> in_transitions;//2.in-transitions
	private List<Transition> out_transitions;//3.out-transitions
	
	
	
	/**
	 * Construct methods
	 * */
	public State(){
		this.system_variables = new HashMap<String, Object>();
		this.in_transitions = new ArrayList<Transition>();
		this.out_transitions = new ArrayList<Transition>();
		
		this.stateLabel = "PredictedState";
	}
	public State(HashMap<String, Object> variables, String name){
		this.system_variables = variables;
		this.in_transitions = new ArrayList<Transition>();
		this.out_transitions = new ArrayList<Transition>();
		this.stateName = name;
		
		this.stateLabel = "PredictedState";
	}
	
	public State(String name, HashMap<String, ValueSet> variables){
		this.systemVariables_ValueSet = variables;
		this.in_transitions = new ArrayList<Transition>();
		this.out_transitions = new ArrayList<Transition>();
		this.stateName = name;
		
		this.stateLabel = "StateMachineState";
	}
	
	
	public boolean isequal(State s){
		
		//if this.stateLabel
		
		if( this.getSystemVariables().size() == s.getSystemVariables().size()){
			HashMap<String, Object> vals = this.system_variables;
			Iterator it = vals.entrySet().iterator();
			while( it.hasNext()){
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
				String key = entry.getKey();
				Object val = entry.getValue();//need to test whether it is right!o==o.
				if( s.getSystemVariables().get(key) != null){
					if( s.getSystemVariables().get(key) == val){
						//do nothing
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
			}
		}
		else {
			return false;
		}
		return true;
	}
	
	public boolean isequal_ValueSet(State s){
		
		//if this.stateLabel
		
		if( this.getSystemVariables_ValueSet().size() == s.getSystemVariables_ValueSet().size()){
			HashMap<String, ValueSet> vals = this.systemVariables_ValueSet;
			Iterator it = vals.entrySet().iterator();
			while( it.hasNext()){
				Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>)it.next();
				String key = entry.getKey();
				ValueSet val = entry.getValue();//need to test whether it is right!o==o.
				if( s.getSystemVariables_ValueSet().get(key) != null){
					if( s.getSystemVariables_ValueSet().get(key).isequal(val)){
						//do nothing
					}
					else {
						return false;
					}
				}
				else {
					return false;
				}
			}
		}
		else {
			return false;
		}
		return true;
	}
	
	public int checkRelationshipBetweenTwoStates(State newState){
		
		List<Integer> checkR = new ArrayList<Integer>();
		
		if( this.getSystemVariables_ValueSet().size() == newState.getSystemVariables_ValueSet().size()){
			
			HashMap<String, ValueSet> vals = this.systemVariables_ValueSet;
			Iterator it = vals.entrySet().iterator();
			while( it.hasNext()){
				Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>)it.next();
				String key = entry.getKey();
				//System.out.print("key:" + key);
				ValueSet val = entry.getValue();//need to test whether it is right!o==o.
				//System.out.println(", value: " + val.getValueSet().get(0) + ", new state: " + newState.getSystemVariables_ValueSet().get(key).getValueSet().get(0));
				if( newState.getSystemVariables_ValueSet().get(key) != null){
					int c = checkValueSetRelationship( newState.getSystemVariables_ValueSet().get(key), val);
					checkR.add(c);
				}
				else {
					System.out.println("#################### {[Warning][wrong state check!][State-checkRelationshipBetweenTwoStates](1)} ####################");
					return -1;
				}
			}
			
			int l_cr = checkR.size();
			int tag_0 = 0;
			int tag_1 = 0;
			int tag_2 = 0;
			for( int i = 0; i < l_cr; i ++){
				if( checkR.get(i) == 0){
					tag_0 ++;
				}
				else if( checkR.get(i) == 1){
					tag_1 ++;
				}
				else if( checkR.get(i) == 2){
					tag_2 ++;
				}
			}
			
			if( tag_0 == 0){
				if( tag_1 == 0){
					if( tag_2 == 0){
						System.out.println("#################### {[Warning][check error!][State-checkRelationshipBetweenTwoStates](2)} ####################");
						return -1;
					}
					else {
						return 2;
					}
				}
				else {
					return 1;
				}
			}
			else {
				return 0;
			}
			
			
			
			
		}
		else {
			System.out.println("#################### {[Warning][wrong state check!][State-checkRelationshipBetweenTwoStates](3)} ####################");
			return -1;
		}
		
		
	}
	public int checkValueSetRelationship(ValueSet v1, ValueSet v2){
		
		int l_v1 = v1.getValueSet().size();
		int l_v2 = v2.getValueSet().size();
		
		int checkResult = -1;
		
		
		if( l_v1 == 1 && l_v2 == 1){
			
			String con0 = v1.getValueSet().get(0);
			String con1 = v2.getValueSet().get(0);
			
			String[] srcs0 = con0.split(" ");
			String[] srcs1 = con1.split(" ");
			
			if( srcs0[1].equals("==") && srcs1[1].equals("==")){
				int temp0 = Integer.parseInt(srcs0[2]);
				int temp1 = Integer.parseInt(srcs1[2]);
				
				//System.out.print("----- " + temp0 + ", " + temp1 + "; ");
				
				if( temp0 == temp1){
					checkResult = 2;
				}
				else {
					checkResult = 0;
				}
				
			}
			else {
				
				System.out.println("#################### {[Warning][Illegel!][State-checkValueSetRelationship](1)} ####################");
			}
		}
		else if( l_v1 == 1 && l_v2 == 2){//ex. a < x <b
			
			String con00 = v1.getValueSet().get(0);
			String con10 = v2.getValueSet().get(0);
			String con11 = v2.getValueSet().get(1);
			
			int temp00 = 0;
			int temp10 = 0;
			int temp11 = 0;
			
			String[] srcs00 = con00.split(" ");
			String[] srcs10 = con10.split(" ");
			String[] srcs11 = con11.split(" ");
			
			if( srcs00[1].equals("==")){
				temp00 = Integer.parseInt(srcs00[2]);
			}
			else {
				System.out.println("#################### {[Warning][Illegel!][State-checkValueSetRelationship](2)} ####################");
			}
			
			if( ( srcs10[1].equals("<") || srcs10[1].equals("<="))&& ( srcs11[1].equals(">") || srcs11[1].equals(">="))){
				temp11 = Integer.parseInt( srcs10[2]);
				temp10 = Integer.parseInt( srcs11[2]);
			}
			else if( ( srcs10[1].equals(">") || srcs10[1].equals(">="))&& ( srcs11[1].equals("<") || srcs11[1].equals("<="))){
				temp11 = Integer.parseInt( srcs11[2]);
				temp10 = Integer.parseInt( srcs10[2]);
			}
			else {
				System.out.println("#################### {[Warning][Illegel!][State-checkValueSetRelationship](3)} ####################");
			}
			
			if( temp11 > temp00 && temp10 < temp00){
				checkResult = 1;
			}
			else {
				checkResult = 0;
			}
			
		}
		else {
			System.out.println("#################### {[Warning][illegel!][State-checkValueSetRelationship](4)} ####################");
		}
		
		return checkResult;
	}
	
	
	
	
	
	
	
	
	
	public void addinTransition(Transition t){
		this.in_transitions.add(t);
	}
	public boolean removeinTransition(Transition t){
		if( this.in_transitions.isEmpty()){
			return false;
		}
		else {
			int len = this.in_transitions.size();
			for(int i = 0; i < len; i ++){
				if( this.in_transitions.get(i).isequal(t)){
					this.in_transitions.remove(i);
					System.out.println("[----------]{[number of transitions:" + len + ", " + 
					this.in_transitions.size() + "]}");
				}
			}
			return true;
		}
	}
	public List<Transition> getinTransitions(){
		return this.in_transitions;
	}
	
	public void addoutTransition(Transition t){
		this.out_transitions.add(t);
	}
	public boolean removeoutTransition(Transition t){
		if( this.out_transitions.isEmpty()){
			return false;
		}
		else {
			int len = this.out_transitions.size();
			for(int i = 0; i < len; i ++){
				if( this.out_transitions.get(i).isequal(t)){
					this.out_transitions.remove(i);
					System.out.println("[----------]{[number of transitions:" + len + ", " + 
					this.out_transitions.size() + "]}");
				}
			}
			return true;
		}
	}
	public List<Transition> getoutTransitions(){
		return this.out_transitions;
	}
	
	
	public void setSystemVariables(HashMap<String, Object> variables){
		Iterator it = variables.entrySet().iterator();
		while( it.hasNext()){
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
			String key = entry.getKey();
			Object val = entry.getValue();
			this.setOneSystemVariable(val, key);
		}
	}
	public void setOneSystemVariable(Object value, String variablename){
		this.system_variables.put(variablename, value);
	}
	public HashMap<String, Object> getSystemVariables(){
		return this.system_variables;
		//It is better to have a new variable as the returned result.
	}
	public HashMap<String, ValueSet> getSystemVariables_ValueSet(){
		return this.systemVariables_ValueSet;
	}
	
	
	
	
	
	
	
	public String getStateName(){
		return this.stateName;
	}
	public void setStateName(String statename){
		this.stateName = statename;
	}
	
	
	
	
	public static void main(String[] args){
		//test all the methods
	}
	
}
