package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives;

import java.util.HashMap;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.ValueSet;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.State;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.StateMachine;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.Transition;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.Solution;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives.util.VarCalMethods;

public class GlobalStructureSimilarity {
	/**
	 * Objective 1st (O1): global structure similarity, expect maximizing similarity
	 * This obejctive is seperated from class ProblemPSBGen
	 * 
	 * @author jiruihua
	 * */
	private StateMachine existingstatemachine;
	
	
	
	public double Objective1_GlobalStructureSimilarity(Solution v) {
		
		
		double result_similarity = 0;
		List<Transition> lt = this.getexistingstatemachine().getalltransitions();
		int len1 = lt.size();
		for( int i = 0; i < len1; i ++){
			result_similarity = result_similarity + SimilarityBetweenSolutionAndTransition(v, lt.get(i));
		}
		result_similarity = result_similarity/len1;
		
		
		return result_similarity;
	}
	
	/**
	 * 201606 version
	 * */
	public double SimilarityBetweenSolutionAndTransition(Solution v1, Transition t1){
		
		double result = 1 - DistanceBetweenSolutionAndTransition(v1, t1);
		
		return result;
	}
	
	/**
	 * 201606 version
	 * */
	public double DistanceBetweenSolutionAndTransition(Solution v1, Transition t1){//Distance of two solutions
		
		//map all variables into the interval (0, 1), then use measures, for instance, Euclidean metric.
		
		HashMap<String, Object> solution_v1 = v1.getsolution();
		int len_solution_v1 = v1.getsolutionlength();
		
		//Distances of the source states
		State sourceState_v1 = this.existingstatemachine.getallstates().get( (int)solution_v1.get("SourceState"));
		State sourceState_t = t1.getsourcestate();
		double distance_source_ac = VarCalMethods.variableCalculateConstraintDistance(sourceState_v1.getSystemVariables_ValueSet().get("activecall"),
				t1.getsourcestate().getSystemVariables_ValueSet().get("activecall"));
		double distance_source_vq = VarCalMethods.variableCalculateConstraintDistance(sourceState_v1.getSystemVariables_ValueSet().get("videoquality"),
				t1.getsourcestate().getSystemVariables_ValueSet().get("videoquality"));
		
		
		//Distances of the target states
		int activecall_targetState_v1 = (int)solution_v1.get("activecall");
		int videoquality_targetState_v1 = (int)solution_v1.get("videoquality");
		ValueSet activecall_targetState_t = t1.gettargetstate().getSystemVariables_ValueSet().get("activecall");
		ValueSet videoquality_targetState_t = t1.gettargetstate().getSystemVariables_ValueSet().get("videoquality");
		
		double distance_target_ac = VarCalMethods.variableCalculateConstraintDistance( activecall_targetState_v1, activecall_targetState_t);
		double distance_target_vq = VarCalMethods.variableCalculateConstraintDistance( videoquality_targetState_v1, videoquality_targetState_t);
		
		//Distances of the triggers
		int useroperation_v1 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v1 = VarCalMethods.normalization((double)useroperation_v1);
		int useroperation_BP;
		String useroperation_String_BP = t1.getTriggers().get(0);
		if( useroperation_String_BP.equals("null")){
			useroperation_BP = 0;
		}
		else if( useroperation_String_BP.equals("dial")){
			useroperation_BP = 1;
		}
		else if( useroperation_String_BP.equals("disconnect")){
			useroperation_BP = 2;
		}
		else {
			useroperation_BP = -1;
			System.out.println("#################### {[Warning][Wrong triggers!][GlobalStructureSimilarity]} ####################");
		}
		double nor_useroperation_BP = VarCalMethods.normalization((double)useroperation_BP);
		double distance_trigger = nor_useroperation_BP - nor_useroperation_v1;
		
		//Distances of Network Environment
		double packetloss_v1 = (double)solution_v1.get("PacketLoss");
		double packetdelay_v1 = (double)solution_v1.get("PacketDelay");
		double packetduplication_v1 = (double)solution_v1.get("PacketDuplication");
		double packetcorruption_v1 = (double)solution_v1.get("PacketCorruption");
		ValueSet packetloss_t = t1.getConditions().get("PacketLoss");
		ValueSet packetdelay_t = t1.getConditions().get("PacketDelay");
		ValueSet packetduplication_t = t1.getConditions().get("PacketDuplication");
		ValueSet packetcorruption_t = t1.getConditions().get("PacketCorruption");
		double distance_packetloss = VarCalMethods.variableCalculateConstraintDistance(packetloss_v1, packetloss_t);
		double distance_packetdelay = VarCalMethods.variableCalculateConstraintDistance(packetdelay_v1, packetdelay_t);
		double distance_packetduplication = VarCalMethods.variableCalculateConstraintDistance(packetduplication_v1, packetduplication_t);
		double distance_packetcorruption = VarCalMethods.variableCalculateConstraintDistance(packetcorruption_v1, packetcorruption_t);
		
		
		double result = 0;
		result = Math.pow(distance_source_ac, 2)
				+ Math.pow(distance_source_vq, 2)
				+ Math.pow(distance_target_ac, 2)
				+ Math.pow(distance_target_vq, 2)
				+ Math.pow(distance_trigger, 2)
				+ Math.pow(distance_packetloss, 2)
				+ Math.pow(distance_packetdelay, 2)
				+ Math.pow(distance_packetduplication, 2)
				+ Math.pow(distance_packetcorruption, 2);
		result = Math.sqrt(result);
		
		result = VarCalMethods.normalization(result);
		
		
		return result;
	}
	
	
	
	
	
	public StateMachine getexistingstatemachine() {
		return this.existingstatemachine;
	}
	public void setexistingstatemachine(StateMachine existingstatemachine){
		this.existingstatemachine = existingstatemachine;
	}
	
}
