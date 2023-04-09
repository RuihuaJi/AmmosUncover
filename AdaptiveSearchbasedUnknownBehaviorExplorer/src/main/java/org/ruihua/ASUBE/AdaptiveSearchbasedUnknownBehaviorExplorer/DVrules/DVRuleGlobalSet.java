package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.DVrules;

import java.util.*;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.*;

/**
 * 2019.11.21
 * This version do not need the partitions on the state machine.
 * */


public class DVRuleGlobalSet {
	
	private List<WeightedDVRule> weightedDVRuleSet;
	private StateMachine statemachineUnderPartition;
	
	/**Class Related Functional Methods**/
	public void insertOneWDVRule(WeightedDVRule wdvrule) {
		this.weightedDVRuleSet.add(wdvrule);
	}
	
	public boolean TakeBehaviorToGenerateDVRulesAndInsertToPartition(List<Solution> ListofinputSystemBehaviors, String calculateLabel) {
		if(statemachineUnderPartition == null) { System.out.println("**** Warning: Partition, Generate DVRules, no inseted State Machine ****"); return false;}
		boolean label = true;
		int lengthBlist = ListofinputSystemBehaviors.size();
		for(int i = 0; i < lengthBlist; i ++) {
			Solution behaviorTemp = ListofinputSystemBehaviors.get(i);
			insertSingelWeightedDVRule(behaviorTemp, calculateLabel);
		}
		return label;
	}
	public void insertSingelWeightedDVRule(Solution b, String calculateLabel) {
		DeltaVariableRule dvr = new DeltaVariableRule(b, this.statemachineUnderPartition, calculateLabel);
		WeightedDVRule wdvr = new WeightedDVRule(dvr);
		this.weightedDVRuleSet.add(wdvr);
	}
	
	public void publishWeightedDVRulesBasedOnPartition() {
		System.out.println("[>>>>>>>>>>]{# DVRuleGlobalSet-Allrules: #");
		
		int lengthDVRuleList = this.weightedDVRuleSet.size();
			
		for(int j = 0; j < lengthDVRuleList; j ++) {
			WeightedDVRule wdvruleTemp = this.weightedDVRuleSet.get(j);
			System.out.print("    [Rule" + j + "] DeltaAC " + wdvruleTemp.getDVRule().getDeltaAC() + ", DeltaVQ "
						+ wdvruleTemp.getDVRule().getDeltaVQ() + ", delay " + wdvruleTemp.getDVRule().getNc_delay() 
						+ ", loss " + wdvruleTemp.getDVRule().getNc_packetloss() + ", duplication " 
						+ wdvruleTemp.getDVRule().getNc_packetduplication() + ", corruption " 
						+ wdvruleTemp.getDVRule().getNc_packetcorruption() + ", weight " + wdvruleTemp.getWeight()
						);
		}
		System.out.println("}");
		System.out.println("[>>>>>>>>>>]{* RuleList-END *}");
		
	}
	
	
	/**Construction Methods**/
	public DVRuleGlobalSet() {
		this.weightedDVRuleSet = new ArrayList<WeightedDVRule>();
		this.statemachineUnderPartition = new StateMachine();
	}
	public DVRuleGlobalSet(StateMachine s) {
		this.weightedDVRuleSet = new ArrayList<WeightedDVRule>();
		this.statemachineUnderPartition = s;
	}
	public DVRuleGlobalSet(List<WeightedDVRule> l) {
		this.weightedDVRuleSet = l;
		this.statemachineUnderPartition = new StateMachine();
	}
	public DVRuleGlobalSet(StateMachine s, List<WeightedDVRule> l) {
		this.weightedDVRuleSet = l;
		this.statemachineUnderPartition = s;
	}
	
	
	/**Get and Set Methods**/
	public List<WeightedDVRule> getWeightedDVRuleSet() {
		return this.weightedDVRuleSet;
	}
	public void setWeightedDVRuleSet(List<WeightedDVRule> l) {
		this.weightedDVRuleSet = l;
	}
	public StateMachine getStateMachine() {
		return this.statemachineUnderPartition;
	}
	public void setStateMachine(StateMachine s) {
		this.statemachineUnderPartition = s;
	}
	
	

	
	
	
	
	
	
	public static void main(String[] args) {
		
	}
	
}
