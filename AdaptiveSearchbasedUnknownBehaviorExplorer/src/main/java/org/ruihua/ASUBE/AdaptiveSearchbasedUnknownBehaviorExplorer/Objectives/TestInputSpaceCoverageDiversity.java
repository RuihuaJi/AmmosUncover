package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives.util.VarCalMethods;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.State;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.StateMachine;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.SystemBehavior;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.Solution;

public class TestInputSpaceCoverageDiversity {
	/**
	 * Objective 2nd (O2): test input space coverage diversity, expect maximizing diversity
	 * This obejctive is seperated from class ProblemPSBGen
	 * 
	 * @author jiruihua
	 * */
	private StateMachine existingstatemachine;
	private List<Solution> SolutionsOfExistingTestCases;
	private List<SystemBehavior> ListofExistingBehaviourPairs;
	
	public void InitialSetOfExistingTestCases(){
		this.SolutionsOfExistingTestCases = new ArrayList<Solution>();
	}
	
	
	public double Objective2_TestInputSpaceDiversity(Solution v) {
		int len2 = this.SolutionsOfExistingTestCases.size();
		
		double result_diversity = 0;
		for(int i = 0; i < len2; i ++){
			double temp = DiversityOfTwoSolutions(
					v, this.SolutionsOfExistingTestCases.get(i));
			
			result_diversity = result_diversity + temp;
		}
		if(len2 == 0) {
			result_diversity = 1;
		} 
		else {
			result_diversity = result_diversity/len2;
		}
		
		
		return result_diversity;
	}
	
	/**
	 * 201606 version
	 * */
	public double DiversityOfTwoSolutions(Solution v1, Solution v2){//Distance of two solutions
		
		
		HashMap<String, Object> solution_v1 = v1.getsolution();
		HashMap<String, Object> solution_v2 = v2.getsolution();
		int len_solution_v1 = v1.getsolutionlength();
		int len_solution_v2 = v2.getsolutionlength();
		
		if( solution_v1.size() != solution_v2.size()){
			System.out.println("#################### {[Error][Solutions have different length!][TestInputSpaceCoverageDiversity]} ####################");
			return -1;
		}
		if( solution_v1.size() != len_solution_v1 || solution_v2.size() != len_solution_v2){
			System.out.println("solution_v1.size():" + solution_v1.size() + 
					", len_solution_v1:" + len_solution_v1 +
					", solution_v2.size():" + solution_v2.size() + 
					", len_solution_v2:" + len_solution_v2);
			System.out.println("#################### {[Warning][variable of length of solution is wrong!][TestInputSpaceCoverageDiversity]} ####################");
		}
		
		
		//v1
		//source state
		State sourceState_v1 = this.existingstatemachine.getallstates().get( (int)solution_v1.get("SourceState"));
		//source state v2
		State sourceState_v2 = this.existingstatemachine.getallstates().get( (int)solution_v2.get("SourceState"));
		double distance_source_ac = VarCalMethods.variableCalculateConstraintDistance(sourceState_v1.getSystemVariables_ValueSet().get("activecall"),
				sourceState_v2.getSystemVariables_ValueSet().get("activecall"));
		double distance_source_vq = VarCalMethods.variableCalculateConstraintDistance(sourceState_v1.getSystemVariables_ValueSet().get("videoquality"),
				sourceState_v2.getSystemVariables_ValueSet().get("videoquality"));
		
		
		//target state
		//System.out.println("v1 target state active call:" + solution_v1.get("activecall"));
		int activecall_targetState_v1 = (int)solution_v1.get("activecall");
		int videoquality_targetState_v1 = (int)solution_v1.get("videoquality");
		double nor_activecall_targetState_v1 = VarCalMethods.normalization((double)activecall_targetState_v1);
		double nor_videoquality_targetState_v1 = VarCalMethods.normalization((double)videoquality_targetState_v1);
		//transition, trigger 1, user operations
		int useroperation_v1 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v1 = VarCalMethods.normalization((double)useroperation_v1);
		
		//trigger 2, network environment
		double packetloss_v1 = (double)solution_v1.get("PacketLoss");
		double packetdelay_v1 = (double)solution_v1.get("PacketDelay");
		double packetduplication_v1 = (double)solution_v1.get("PacketDuplication");
		double packetcorruption_v1 = (double)solution_v1.get("PacketCorruption");
		double nor_packetloss_v1 = VarCalMethods.normalization(packetloss_v1);
		double nor_packetdelay_v1 = VarCalMethods.normalization(packetdelay_v1);
		double nor_packetduplication_v1 = VarCalMethods.normalization(packetduplication_v1);
		double nor_packetcorruption_v1 = VarCalMethods.normalization(packetcorruption_v1);
		
		//v2
		
		
		//target state
		int activecall_targetState_v2 = (int)solution_v2.get("activecall");
		int videoquality_targetState_v2 = (int)solution_v2.get("videoquality");
		//int audioquality_targetState_v2 = (int)solution_v2.get("audioquality");
		//int match_targetState_v2 = (int)solution_v2.get("match");
		double nor_activecall_targetState_v2 = VarCalMethods.normalization((double)activecall_targetState_v2);
		double nor_videoquality_targetState_v2 = VarCalMethods.normalization((double)videoquality_targetState_v2);
		int useroperation_v2 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v2 = VarCalMethods.normalization((double)useroperation_v2);
		
		double packetloss_v2 = (double)solution_v2.get("PacketLoss");
		double packetdelay_v2 = (double)solution_v2.get("PacketDelay");
		double packetduplication_v2 = (double)solution_v2.get("PacketDuplication");
		double packetcorruption_v2 = (double)solution_v2.get("PacketCorruption");
		
		double nor_packetloss_v2 = VarCalMethods.normalization(packetloss_v2);
		double nor_packetdelay_v2 = VarCalMethods.normalization(packetdelay_v2);
		double nor_packetduplication_v2 = VarCalMethods.normalization(packetduplication_v2);
		double nor_packetcorruption_v2 = VarCalMethods.normalization(packetcorruption_v2);
		
		
		
		double result = 0;
		result = Math.pow(distance_source_ac, 2)
				+ Math.pow(distance_source_vq, 2)
				+ Math.pow((nor_activecall_targetState_v1 - nor_activecall_targetState_v2), 2)
				+ Math.pow((nor_videoquality_targetState_v1 - nor_videoquality_targetState_v2), 2)
				+ Math.pow((nor_useroperation_v1 - nor_useroperation_v2), 2)
				+ Math.pow((nor_packetloss_v1 - nor_packetloss_v2), 2)
				+ Math.pow((nor_packetdelay_v1 - nor_packetdelay_v2), 2)
				+ Math.pow((nor_packetduplication_v1 - nor_packetduplication_v2), 2)
				+ Math.pow((nor_packetcorruption_v1 - nor_packetcorruption_v2), 2);
		
		
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
	public void setSolutionsOfExistingTestCases(List<Solution> ExistingTestCases){
		this.SolutionsOfExistingTestCases = ExistingTestCases;
	}
	
	public List<Solution> getSolutionsOfExistingTestCases(){
		return this.SolutionsOfExistingTestCases;
	}
	public List<SystemBehavior> getListofExistingBehaviourPairs(){
		return this.ListofExistingBehaviourPairs;
	}
}
