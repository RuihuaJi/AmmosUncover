package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives;

import java.util.*;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.DVrules.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.ValueSet;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.*;


/**
 * O3: 
 * Maximize similarity
 * Maximumly similarify network conditions that possibly lead to unknown system behaviors.
 * 
 * O4: 
 * Maximize diversity
 * Maximumly diversify network conditions that lead to known system behaviors.
 * 
 * @author jiruihua
 * 
 */

public class WDVRuleGuidance_v20191121 {
	private DVRuleGlobalSet ruleSet;
	private List<WeightedDVRule> UnknownBehaviorRules;
	private List<WeightedDVRule> KnownBehaviorRules;
	
	public WDVRuleGuidance_v20191121() {
		this.ruleSet = new DVRuleGlobalSet();
		this.UnknownBehaviorRules = new ArrayList<WeightedDVRule>();
		this.KnownBehaviorRules = new ArrayList<WeightedDVRule>();
	}
	public WDVRuleGuidance_v20191121(StateMachine statemachine, List<Solution> listofinputSystemBehaviors) {
		
		this.ruleSet = new DVRuleGlobalSet(statemachine);
		this.ruleSet.TakeBehaviorToGenerateDVRulesAndInsertToPartition(listofinputSystemBehaviors, "VQMetrix");
		
		this.UnknownBehaviorRules = new ArrayList<WeightedDVRule>();
		this.KnownBehaviorRules = new ArrayList<WeightedDVRule>();
	}
	
	
	//Use these methods after ChooseOptionalWDVRules()
	public double Objective3_DeltaVariableRuleBasedNetworkConditionSimilarity(Solution currentsolution) {
		if(this.UnknownBehaviorRules.isEmpty()) { return 0;}
		
		double maxSimilarity = -1;
		double tempmaxsimilarity = -1;
		int lengthUnknownRuleList = this.UnknownBehaviorRules.size();
		for(int i = 0; i < lengthUnknownRuleList; i ++) {
			tempmaxsimilarity = O3SimilarityCalculate(currentsolution.getsolution(), this.UnknownBehaviorRules.get(i));
			if(tempmaxsimilarity > maxSimilarity) {
				maxSimilarity = tempmaxsimilarity;
			}
		}
		return maxSimilarity;
	}
	public double Objective4_DeltaVariableRuleBasedNetworkConditionDiversity(Solution currentsolution) {
		if(this.KnownBehaviorRules.isEmpty()) { return 0;}
		
		double totaldistance = 0;
		int lengthKnownRuleList = this.KnownBehaviorRules.size();
		for(int i = 0; i < lengthKnownRuleList; i ++) {
			totaldistance = totaldistance + O4DiversityCalculate(currentsolution.getsolution(), this.KnownBehaviorRules.get(i));
		}
		double averageDistance = totaldistance/lengthKnownRuleList;
		return averageDistance;
	}
	
	public double O3SimilarityCalculate(HashMap<String, Object> s, WeightedDVRule wdvr) {
		double distance = DistanceCalculate(s, wdvr);
		double nor_distance = NormalizationFunction(distance);
		return 1 - nor_distance;
	}
	public double O4DiversityCalculate(HashMap<String, Object> s, WeightedDVRule wdvr) {
		double distance = DistanceCalculate(s, wdvr);
		double nor_distance = NormalizationFunction(distance);
		return nor_distance;
	}
	
	public double DistanceCalculate(HashMap<String, Object> s, WeightedDVRule wdvr) {
		
		double packetloss_s = (double)s.get("PacketLoss");
		double packetdelay_s = (double)s.get("PacketDelay");
		double packetduplication_s = (double)s.get("PacketDuplication");
		double packetcorruption_s = (double)s.get("PacketCorruption");
		
		DeltaVariableRule dvr = wdvr.getDVRule();
		
		
		double distance = (dvr.getNc_packetloss() - packetloss_s)*(dvr.getNc_packetloss() - packetloss_s)
				+ (dvr.getNc_delay() - packetdelay_s)*(dvr.getNc_delay() - packetdelay_s)
				+ (dvr.getNc_packetduplication() - packetduplication_s)*(dvr.getNc_packetduplication() - packetduplication_s)
				+ (dvr.getNc_packetcorruption() - packetcorruption_s)*(dvr.getNc_packetcorruption() - packetcorruption_s);
		
		
		return distance;
	}
	public double NormalizationFunction(double x) {
		double Beta = 1.0;
		return x / (x + Beta);
	}
	
	
	
	
	
	
	public void ChooseOptionalWDVRules(Solution currentsolution) {
		
		int lengthRuleList = this.ruleSet.getWeightedDVRuleSet().size();
		for(int i = 0; i < lengthRuleList; i ++) {
			WeightedDVRule wdvruleTemp = this.ruleSet.getWeightedDVRuleSet().get(i);
			if(leadToUnknownBehavior(this.ruleSet.getStateMachine().getallstates().get((int)currentsolution.getsolution().get("SourceState")), wdvruleTemp)) {
				this.UnknownBehaviorRules.add(wdvruleTemp);
			}
			else {
				this.KnownBehaviorRules.add(wdvruleTemp);
			}
		}
	}
	public boolean leadToUnknownBehavior(State sstate, WeightedDVRule wdvrule) {
		int variableAC = CalculateVariableUseSStateAndRule(wdvrule.getDVRule().getDeltaAC(), sstate.getSystemVariables_ValueSet().get("activecall"));
		int variableVQ = CalculateVariableUseSStateAndRule(wdvrule.getDVRule().getDeltaVQ(), sstate.getSystemVariables_ValueSet().get("videoquality"));
		
		return isUnknownBehavior(sstate, variableAC, variableVQ);
	}
	public int CalculateVariableUseSStateAndRule(double Vv, ValueSet BPc){
		
		List<ValueSetConstraint> BPCList = new ArrayList<ValueSetConstraint>();
		
		if( BPc == null){
			System.out.println("[++++++++++]BPc == null");
		}
		if( BPc.getValueSet().isEmpty()){
			System.out.println("[++++++++++]BPc.getValueSet().isEmpty()");
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
		int newV = (int)(Vv + BPcValue);
		
		return newV;
		
	}
	
	public boolean isUnknownBehavior(State sstate, int AC, int VQ) {
		
		StateMachine statemachineTemp = this.ruleSet.getStateMachine();
		
		List<Transition> alltransitions = statemachineTemp.getalltransitions();
		int lengthAllTransitions = alltransitions.size();
		for(int j = 0; j < lengthAllTransitions; j ++) {
			Transition tTemp = alltransitions.get(j);
			if(CalculateVariableEqual(AC, tTemp.getsourcestate().getSystemVariables_ValueSet().get("activecall")) && CalculateVariableEqual(VQ, tTemp.getsourcestate().getSystemVariables_ValueSet().get("videoquality"))) {
				return false;
			}
		}
		
		return true;
	}
	public boolean CalculateVariableEqual(int Vv, ValueSet BPc){
		
		List<ValueSetConstraint> BPCList = new ArrayList<ValueSetConstraint>();
		
		
		if( BPc == null){
			System.out.println("[++++++++++]BPc == null");
		}
		if( BPc.getValueSet().isEmpty()){
			System.out.println("[++++++++++]BPc.getValueSet().isEmpty()");
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
		
		
		int BPcValue = Integer.parseInt(BPCList.get(0).varRight);
		
		if(Vv == BPcValue) {
			return true;
		}
		else {
			return false;
		}
		
		
	}
	
	
	
	
	
	
	public DVRuleGlobalSet getDVRuleGlobalSet() { return this.ruleSet;}
	public void setDVRuleGlobalSet(DVRuleGlobalSet rs) { this.ruleSet = rs;}
	public List<WeightedDVRule> getUnknownBehaviorRules() { return this.UnknownBehaviorRules;}
	public void setUnknownBehaviorRules(List<WeightedDVRule> u) { this.UnknownBehaviorRules = u;}
	public List<WeightedDVRule> getKnownBehaviorRules() { return this.KnownBehaviorRules;}
	public void setKnownBehaviorRules(List<WeightedDVRule> k) { this.KnownBehaviorRules = k;}
	
}
