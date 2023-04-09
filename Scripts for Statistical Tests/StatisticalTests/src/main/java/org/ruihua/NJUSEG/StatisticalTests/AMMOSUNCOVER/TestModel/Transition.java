package org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel;

import java.util.*;

import org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.ValueSet.*;

public class Transition {
	
	
	private HashMap<String, ValueSet> conditions;
	private List<String> triggers;
	
	
	private State sourcestate;
	private State targetstate;
	
	
	
	public Transition(){
		this.conditions = new HashMap<String, ValueSet>();
		this.triggers = new ArrayList<String>();
		this.sourcestate = new State();
		this.targetstate = new State();
		
		
	}
	public Transition(State sourcestate, State targetstate){
		this.conditions = new HashMap<String, ValueSet>();
		this.triggers = new ArrayList<String>();
		this.sourcestate = sourcestate;
		this.targetstate = targetstate;
		
		
	}
	public Transition(NetworkCondition ne){
		this.conditions = new HashMap<String, ValueSet>();
		this.triggers = new ArrayList<String>();
		this.sourcestate = new State();
		this.targetstate = new State();
		
	
	}
	
	
	
	
	
	public static void SetTriggerByNetworkEnvironment(Transition t, NetworkCondition ne){
		
		String delay = "PacketDelay == ";
		double v_delay = ne.getPacketDelay();
		delay = delay + v_delay;
		
		String loss = "PacketLoss == ";
		double v_loss = ne.getPacketLoss();
		loss = loss + v_loss;
		
		String corrupt = "PacketCorruption == ";
		double v_corrupt = ne.getPacketCorruption();
		corrupt = corrupt + v_corrupt;
		
		String duplicate = "PacketDuplication == ";
		double v_duplicate = ne.getPacketDuplication();
		duplicate = duplicate + v_duplicate;
		
		t.addTriggers(delay);
		t.addTriggers(loss);
		t.addTriggers(corrupt);
		t.addTriggers(duplicate);
		
	}
	
	
	
	public static void SetTransitionByNetworkEnvironment(Transition t, NetworkCondition ne){
		
		System.out.println("Method SetTransitionByNetworkEnvironment has been out of time!");
	}
	
	public boolean isequal(Transition t){
		if( !this.sourcestate.isequal(t.sourcestate)){
			return false;
		}
		if( !this.targetstate.isequal(t.targetstate)){
			return false;
		}
		int len = this.triggers.size();
		if( len == t.getTriggers().size()){
			for(int i = 0; i < len; i ++){
				String tempTriggers = this.triggers.get(i);
				
				boolean temp = false;//This means one condition has the same condition in the other transition.
				for(int j = 0; j < len; j ++){
					if(tempTriggers.equals(t.getTriggers().get(j))){
						temp = true;
					}
				}
				if(temp == false){
					return false;
				}
			}
		}
		else {
			return false;
		}
		
		
		return true;
	}
	
	public boolean isequalTriggerAndCondition(Transition t){
		if( !this.sourcestate.isequal_ValueSet(t.sourcestate)){
			return false;
		}
		if( !t.triggers.get(0).equals(this.triggers.get(0))){
			return false;
		}
		
		if( t.getConditions().size() == this.getConditions().size()){
			HashMap<String, ValueSet> vals = this.conditions;
			Iterator it = vals.entrySet().iterator();
			while( it.hasNext()){
				Map.Entry<String, ValueSet> entry = (Map.Entry<String, ValueSet>)it.next();
				String key = entry.getKey();
				ValueSet val = entry.getValue();
				if( t.getConditions().get(key) != null){
					if( t.getConditions().get(key).isequal(val)){
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
	public void setConditions(HashMap<String, ValueSet> GC){
		this.conditions = GC;
	}
	public void addOneCondition(String var, String condition){
		this.conditions.get(var).getValueSet().add(condition);
	}
	public void addConditions(String var, ValueSet vs){
		this.conditions.put(var, vs);
	}
	public HashMap<String, ValueSet> getConditions(){
		return this.conditions;
	}
	public void addTriggers(String trigger){
		this.triggers.add(trigger);
	}
	public List<String> getTriggers(){
		return this.triggers;
	}
	
	
	
	
	public static void main(String[] args){
		
	}
	
	
}
