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

public class WDVRuleGuidance {
	private DVRulePartitionOnStateMachine partition;
	private List<WeightedDVRule> UnknownBehaviorRules;
	private List<WeightedDVRule> KnownBehaviorRules;
	
	public WDVRuleGuidance() {
		this.UnknownBehaviorRules = new ArrayList<WeightedDVRule>();
		this.KnownBehaviorRules = new ArrayList<WeightedDVRule>();
	}
	public WDVRuleGuidance(StateMachine statemachine, List<Solution> listofinputSystemBehaviors) {
		
		this.partition = new DVRulePartitionOnStateMachine();
		this.partition.setStateMachine(statemachine);
		this.partition.ConstructPartsToPartitionBasedOnStateMachine();
		this.partition.TakeBehaviorToGenerateDVRulesAndInsertToPartition(listofinputSystemBehaviors, "VQMetrix");
		//this.partition.publishWeightedDVRulesBasedOnPartition();
		
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
		
		distance = Math.sqrt(distance);
		distance = distance*wdvr.getWeight();
		
		return distance;
	}
	public double NormalizationFunction(double x) {
		double Beta = 1.0;
		return x / (x + Beta);
	}
	
	
	
	
	
	
	public void ChooseOptionalWDVRules(Solution currentsolution) {
		SingleStateMachinePart targetpart = findTargetPart(currentsolution);
		if(targetpart == null) {
			targetpart = new SingleStateMachinePart();
			SubspaceStateUOp ssuop = new SubspaceStateUOp();
			State stateTemp = this.partition.getStateMachine().getallstates().get((int)currentsolution.getsolution().get("SourceState"));
			String tri = null;
			int trigger = (int)currentsolution.getsolution().get("UserOperation");
			if(trigger == 0) { tri = "null";}
			else if(trigger == 1) { tri = "dial";}
			else { tri = "disconnect";}
			ssuop.setState(stateTemp);
			ssuop.setUserOp(tri);
			targetpart.setSubspaceStateUOp(ssuop);
			List<WeightedDVRule> partWeightedDVRulesExample = this.partition.getPartition().get(0).getPartWeightedDVRules();
			int lenExample = partWeightedDVRulesExample.size();
			for(int i = 0; i < lenExample; i ++) {
				WeightedDVRule WDRuleTemp = partWeightedDVRulesExample.get(i);
				targetpart.getPartWeightedDVRules().add(new WeightedDVRule( WDRuleTemp.getDVRule(), 1.0));
			}
		}
		
		int lengthRuleList = targetpart.getPartWeightedDVRules().size();
		for(int i = 0; i < lengthRuleList; i ++) {
			WeightedDVRule wdvruleTemp = targetpart.getPartWeightedDVRules().get(i);
			if(leadToUnknownBehavior(targetpart.getSubspaceStateUOp().getState(), wdvruleTemp)) {
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
			System.out.println("BPc == null");
		}
		if( BPc.getValueSet().isEmpty()){
			System.out.println("BPc.getValueSet().isEmpty()");
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
		
		
		double BPcValue = Double.parseDouble(BPCList.get(0).varRight);
		int newV = (int)(Vv + BPcValue);
		
		return newV;
		
		
	}
	
	public boolean isUnknownBehavior(State sstate, int AC, int VQ) {
		
		StateMachine statemachineTemp = this.partition.getStateMachine();
		
		List<State> states = statemachineTemp.getallstates();
		int lengthStates = states.size();
		for(int i = 0; i< lengthStates; i ++) {
			State sTemp = states.get(i);
			ValueSet acTemp = sTemp.getSystemVariables_ValueSet().get("activecall");
			ValueSet vqTemp = sTemp.getSystemVariables_ValueSet().get("videoquality");
			if(CalculateVariableEqual(AC, acTemp) && CalculateVariableEqual(VQ, vqTemp)) {
				
				List<Transition> alltransitions = this.partition.getStateMachine().getalltransitions();
				int lengthAllTransitions = alltransitions.size();
				for(int j = 0; j < lengthAllTransitions; j ++) {
					Transition tTemp = alltransitions.get(j);
					if(tTemp.getsourcestate().isequal_ValueSet(sstate) && tTemp.gettargetstate().isequal_ValueSet(sTemp)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	public boolean CalculateVariableEqual(int Vv, ValueSet BPc){
		
		List<ValueSetConstraint> BPCList = new ArrayList<ValueSetConstraint>();
		
		
		if( BPc == null){
			System.out.println("BPc == null");
		}
		if( BPc.getValueSet().isEmpty()){
			System.out.println("BPc.getValueSet().isEmpty()");
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
	
	
	
	
	
	
	
	public SingleStateMachinePart findTargetPart(Solution currentsolution) {
		State sstate = this.partition.getStateMachine().getallstates().get((int)currentsolution.getsolution().get("SourceState"));
		String trigger = null;
		int useroperation = (int)currentsolution.getsolution().get("UserOperation");
		if( useroperation == 0) { trigger = "null";}
		else if( useroperation == 1) { trigger = "dial";}
		else if( useroperation == 2) { trigger = "disconnect";}
		else { trigger = "UnknownCase"; System.out.println("***** Warning: DVRule Partition TBs2GenDVRules, calculate trigger value missing! *****");}
		
		
		int lengthPartsList = this.partition.getPartition().size();
		
		for(int i = 0; i < lengthPartsList; i ++) {
			
			if(this.partition.isequaltoSSMPart(sstate, trigger, this.partition.getPartition().get(i))) {
				return this.partition.getPartition().get(i);
			}
			if(i == lengthPartsList - 1) {
				
			}
			
		}
		return null;
	}
	
	
	
	
	public DVRulePartitionOnStateMachine getPartition() { return this.partition;}
	public void setPartition(DVRulePartitionOnStateMachine p) { this.partition = p;}
	public List<WeightedDVRule> getUnknownBehaviorRules() { return this.UnknownBehaviorRules;}
	public void setUnknownBehaviorRules(List<WeightedDVRule> u) { this.UnknownBehaviorRules = u;}
	public List<WeightedDVRule> getKnownBehaviorRules() { return this.KnownBehaviorRules;}
	public void setKnownBehaviorRules(List<WeightedDVRule> k) { this.KnownBehaviorRules = k;}
	
}
