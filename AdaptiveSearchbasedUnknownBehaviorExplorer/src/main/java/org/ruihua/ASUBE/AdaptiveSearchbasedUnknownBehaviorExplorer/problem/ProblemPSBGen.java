package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem;

import java.util.*;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;

public class ProblemPSBGen implements Problem {
	
	/**
	 * @author jiruihua
	 * */
	
	private ConstraintList constraints;//Its set method uses BPair
	private StateMachine existingstatemachine;
	private List<SystemBehavior> ListofExistingBehaviourPairs;
	private List<SystemBehavior> ListofExistingTestCases;
	
	private List<Solution> SolutionsOfExistingBehaviourPairs;
	private List<Solution> SolutionsOfExistingTestCases;
	
	public List<SystemBehavior> getExistingTestCases(){
		return this.ListofExistingTestCases;
	}
	public void setExistingTestCases(List<SystemBehavior> ExistingTestCases){
		this.ListofExistingTestCases = ExistingTestCases;
	}
	public void AddOneTestCase2ExistingTestCases(SystemBehavior tc){
		this.ListofExistingTestCases.add(tc);
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
	
	/**
	 * Construct Methods
	 * */
	public ProblemPSBGen() {}
	public ProblemPSBGen(StateMachine existingstatemachine){
		this.setexistingstatemachine(existingstatemachine);
	}
	
	/**
	 * ===== This set function is only for initial. ====
	 * ===== When updating, need another function, which only change other but constraints. =====
	 * */
	public void setexistingstatemachine(StateMachine existingstatemachine){
		this.existingstatemachine = existingstatemachine;
		this.ListofExistingBehaviourPairs = SystemBehavior.StateMachineParser(this.existingstatemachine);
		System.out.println("========== length ListofExistingBehaviourPairs: " + this.ListofExistingBehaviourPairs.size());
		//this.setConstraints();
	}
	public StateMachine getexistingstatemachine(){
		return this.existingstatemachine;
	}
	
	
	
	public void EmptySetOfSolutionsOfExistingBehaviourPairs(){
		this.SolutionsOfExistingBehaviourPairs = null;
	}
	public void FillSetofSolutionsOfExistingBehaviourPairs(List<SystemBehavior> ExistingBehaviourPairs){
		this.SolutionsOfExistingBehaviourPairs = new ArrayList<Solution>();
		int len = ExistingBehaviourPairs.size();
		for(int i = 0; i < len; i ++){
			Solution temp = TransformBPair2Solution(ExistingBehaviourPairs.get(i));
			this.SolutionsOfExistingBehaviourPairs.add(temp);
		}
	}
	public void UpdateSetofSolutionsOfExistingBehaviourPairs(List<SystemBehavior> ExistingBehaviourPairs){
		EmptySetOfSolutionsOfExistingBehaviourPairs();
		FillSetofSolutionsOfExistingBehaviourPairs(ExistingBehaviourPairs);
	}
	
	public void EmptySetOfExistingTestCases(){
		this.SolutionsOfExistingTestCases = null;
	}
	public void InitialSetOfExistingTestCases(){
		//EmptySetOfExistingTestCases();
		this.SolutionsOfExistingTestCases = new ArrayList<Solution>();
	}
	public void AddOneTestCase2SetOfExistingTestCases(Solution tc){
		this.SolutionsOfExistingTestCases.add(tc);
	}
	
	
	
	
	//not static, because to find the source state we need to check the related state machine
	public SystemBehavior TransformSolution2BPair(Solution v){
		SystemBehavior BPair = new SystemBehavior();
		
		State sourceState = this.existingstatemachine.getallstates().get((int)v.getsolution().get("SourceState"));
		
		BPair.setsourcestate( sourceState);
		
		State targetState = new State();
		targetState.setStateName("new" + this.existingstatemachine.getallstates().size());
		HashMap<String, Object> variables = new HashMap<String, Object>();
		variables.put("activecall", (int)v.getsolution().get("activecall"));
		variables.put("videoquality", (int)v.getsolution().get("videoquality"));
		
		targetState.setSystemVariables(variables);
		
		BPair.settargetstate(targetState);
		
		
		
		
		//=============Network environment setting===============
		NetworkCondition ne = new NetworkCondition();
		ne.setPacketLoss( (double)v.getsolution().get("PacketLoss"));
		ne.setPacketDelay( (double)v.getsolution().get("PacketDelay"));
		ne.setPacketDuplication( (double)v.getsolution().get("PacketDuplication"));
		ne.setPacketCorruption( (double)v.getsolution().get("PacketCorruption"));
		BPair.setnetworkenvironment(ne);
		
		Transition t = new Transition( sourceState, targetState);
		
		List<String> triggers = t.getTriggers();
		if( (int)v.getsolution().get("UserOperation") == 0){
			triggers.add("null");
		}
		else if( (int)v.getsolution().get("UserOperation") == 1){
			triggers.add("dial");
		}
		else if( (int)v.getsolution().get("UserOperation") == 2){
			triggers.add("disconnect");
		}
		
		
		
		return BPair;
	}
	public SystemBehavior TransformSolution2BPair_ValueSet(Solution v){
		SystemBehavior BPair = new SystemBehavior();
		
		State sourceState = this.existingstatemachine.getallstates().get((int)v.getsolution().get("SourceState"));
		
		BPair.setsourcestate( sourceState);
		
		
		HashMap<String, ValueSet> variables_valueset = new HashMap<String, ValueSet>();
		ValueSet ac = new ValueSet();
		String con_ac = "activecall == " + v.getsolution().get("activecall");
		ac.AddConstriantsForValueSet(con_ac);
		ValueSet vq = new ValueSet();
		String con_vq = "videoquality == " + v.getsolution().get("videoquality");
		vq.AddConstriantsForValueSet(con_vq);
		variables_valueset.put("activecall", ac);
		variables_valueset.put("videoquality", vq);
		
		String sname = "new" + (this.existingstatemachine.getallstates().size() + 1);//!
		State targetState = new State(sname, variables_valueset);
		
		BPair.settargetstate(targetState);
		
		
		//=============Network environment setting===============
		NetworkCondition ne = new NetworkCondition();
		ne.setPacketLoss( (double)v.getsolution().get("PacketLoss"));
		ne.setPacketDelay( (double)v.getsolution().get("PacketDelay"));
		ne.setPacketDuplication( (double)v.getsolution().get("PacketDuplication"));
		ne.setPacketCorruption( (double)v.getsolution().get("PacketCorruption"));
		BPair.setnetworkenvironment(ne);
		
		Transition t = new Transition( sourceState, targetState);
		
		List<String> triggers = t.getTriggers();
		if( (int)v.getsolution().get("UserOperation") == 0){
			triggers.add("null");
		}
		else if( (int)v.getsolution().get("UserOperation") == 1){
			triggers.add("dial");
		}
		else if( (int)v.getsolution().get("UserOperation") == 2){
			triggers.add("disconnect");
		}
		
		
		
		return BPair;
	}
	
	
	
	
	
	//To output fitness value.
	public Solution TransformBPair2Solution(SystemBehavior bpair){
		Solution v = new Solution();
		
		//Source State
		State sourceState = bpair.getsourcestate();
		List<State> allexistingstates = this.existingstatemachine.getallstates();
		int len = allexistingstates.size();
		for(int i = 0; i < len; i ++){
			State currentState = allexistingstates.get(i);
			if( currentState.getStateName().equals(sourceState.getStateName())){
				v.addsolutionmember("SourceState", i);
				break;
			}
		}
		
		//Target State
		
		State targetState = bpair.gettargetstate();
		v.addsolutionmember("activecall", targetState.getSystemVariables().get("activecall"));
		v.addsolutionmember("videoquality", targetState.getSystemVariables().get("videoquality"));
		
		return v;
	}
	
	public Solution TransformBPair2Solution_ValueSet(SystemBehavior bpair){
		Solution v = new Solution();
		
		//Source State
		State sourceState = bpair.getsourcestate();
		List<State> allexistingstates = this.existingstatemachine.getallstates();
		int len = allexistingstates.size();
		for(int i = 0; i < len; i ++){
			State currentState = allexistingstates.get(i);
			if( currentState.getStateName().equals(sourceState.getStateName())){
				v.addsolutionmember("SourceState", i);
				break;
			}
		}
		
		//Target State
		
		State targetState = bpair.gettargetstate();
		
		int len_ac_con = targetState.getSystemVariables_ValueSet().get("activecall").getValueSet().size();
		if( len_ac_con != 1){
			System.out.println("Warning: A not-predicted bp has been transiform to solution");
			return null;
		}
		String ac = targetState.getSystemVariables_ValueSet().get("activecall").getValueSet().get(0);
		String[] srcs_ac = ac.split(" ");
		int ac_V = Integer.parseInt(srcs_ac[2]);
		v.addsolutionmember("activecall", ac_V);
		String vq = targetState.getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0);
		String[] srcs_vq = vq.split(" ");
		int vq_V = Integer.parseInt(srcs_vq[2]);
		v.addsolutionmember("videoquality", vq_V);
		
		return v;
	}
	
	

	
	
	
	
	
	/**
	 * print methods
	 * */
	public static void printBehaviourPair(SystemBehavior bpair){
		
		
		System.out.println("Source state:" + bpair.getsourcestate().getStateName());
		System.out.println("Target state: activecall " + bpair.gettargetstate().getSystemVariables().get("activecall")
				+ ", videoquality " + bpair.gettargetstate().getSystemVariables().get("videoquality")
				
				);
		
		System.out.println();
		System.out.println("NE: PacketLoss " + bpair.getnetworkenvironment().getPacketLoss() 
				+ ", PakcetDelay " + bpair.getnetworkenvironment().getPacketDelay()
				+ ", PacketDuplication " + bpair.getnetworkenvironment().getPacketDuplication()
				+ ", PacketCorruption " + bpair.getnetworkenvironment().getPacketCorruption());
		
		
	}
	public static void printBehaviourPair_ValueSet(SystemBehavior bpair){
		
		
		System.out.println("Source state:" + bpair.getsourcestate().getStateName());
		System.out.println("Target state: activecall " + bpair.gettargetstate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0)
				+ ", videoquality " + bpair.gettargetstate().getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0)
				
				);
		
		System.out.println();
		System.out.println("NE: PacketLoss " + bpair.getnetworkenvironment().getPacketLoss() 
				+ ", PakcetDelay " + bpair.getnetworkenvironment().getPacketDelay()
				+ ", PacketDuplication " + bpair.getnetworkenvironment().getPacketDuplication()
				+ ", PacketCorruption " + bpair.getnetworkenvironment().getPacketCorruption());
		
		
	}
	
	
	
	
	
	@Override
	
	public void setConstraints() {
		// TODO Auto-generated method stub
		//Scheme: from BehaviourPair to Problem Constraints
		this.constraints = new ConstraintList();
		
		//Source State(search existing states)
		this.constraints.addOneConstraint("SourceState", 0, this.existingstatemachine.getallstates().size() - 1, 0);
		
		//Target State(search variables)
		this.constraints.addOneConstraint("activecall", 0, 3, 2);
		this.constraints.addOneConstraint("videoquality", 0, 3, 2);
		
		
		//The next two parts are both trigger of the transition
		
		this.constraints.addOneConstraint("UserOperation", 0, 2, 0);//0-null, 1-dial, 2-disconnect
		
		
		this.constraints.addOneConstraint("PacketLoss", 0, 100, 3);
		this.constraints.addOneConstraint("PacketDelay", 0, 100, 3);
		this.constraints.addOneConstraint("PacketDuplication", 0, 100, 3);
		this.constraints.addOneConstraint("PacketCorruption", 0, 100, 3);
		
		
	}
	
	@Override
	public ConstraintList getConstraints() {
		// TODO Auto-generated method stub
		return this.constraints;
	}

	
	
	@Override
	public double getFitness(Solution v) {
		System.out.println("===== ProblemPSBGen =====");
		
		
		double result = 0;
		
		return result;
		
	}
	
	
	
	
	
	
	
	
	
	public double SimilarityOfTwoSolutions(Solution v1, Solution v2){
		
		double result = 1 - DiversityOfTwoSolutions(v1, v2);
		
		return result;
	}
	public double DiversityOfTwoSolutions(Solution v1, Solution v2){//Distance of two solutions
		
		//map all variables into the interval (0, 1), then use measures, for instance, Euclidean metric.
		
		HashMap<String, Object> solution_v1 = v1.getsolution();
		HashMap<String, Object> solution_v2 = v2.getsolution();
		int len_solution_v1 = v1.getsolutionlength();
		int len_solution_v2 = v2.getsolutionlength();
		
		if( solution_v1.size() != solution_v2.size()){
			System.out.println("******* Error: Solutions have different length! *******");
			return -1;
		}
		if( solution_v1.size() != len_solution_v1 || solution_v2.size() != len_solution_v2){
			System.out.println("solution_v1.size():" + solution_v1.size() + 
					", len_solution_v1:" + len_solution_v1 +
					", solution_v2.size():" + solution_v2.size() + 
					", len_solution_v2:" + len_solution_v2);
			System.out.println("******* Warning: variable of length of solution is wrong! *******");
		}
		
		
		//v1
		//source state
		State sourceState_v1 = this.existingstatemachine.getallstates().get( (int)solution_v1.get("SourceState"));
		int activecall_sourceState_v1 = (int)sourceState_v1.getSystemVariables().get("activecall");
		int videoquality_sourceState_v1 = (int)sourceState_v1.getSystemVariables().get("videoquality");
		
		double nor_activecall_sourceState_v1 = normalization((double)activecall_sourceState_v1);
		double nor_videoquality_sourceState_v1 = normalization((double)videoquality_sourceState_v1);
		
		
		//target state
		
		int activecall_targetState_v1 = (int)solution_v1.get("activecall");
		int videoquality_targetState_v1 = (int)solution_v1.get("videoquality");
		
		double nor_activecall_targetState_v1 = normalization((double)activecall_targetState_v1);
		double nor_videoquality_targetState_v1 = normalization((double)videoquality_targetState_v1);
		
		
		//transition, trigger 1, user operations
		int useroperation_v1 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v1 = normalization((double)useroperation_v1);
		
		//trigger 2, network environment
		double packetloss_v1 = (double)solution_v1.get("PacketLoss");
		double packetdelay_v1 = (double)solution_v1.get("PacketDelay");
		double packetduplication_v1 = (double)solution_v1.get("PacketDuplication");
		double packetcorruption_v1 = (double)solution_v1.get("PacketCorruption");
		double nor_packetloss_v1 = normalization(packetloss_v1);
		double nor_packetdelay_v1 = normalization(packetdelay_v1);
		double nor_packetduplication_v1 = normalization(packetduplication_v1);
		double nor_packetcorruption_v1 = normalization(packetcorruption_v1);
		
		
		//v2
		//source state
		State sourceState_v2 = this.existingstatemachine.getallstates().get( (int)solution_v2.get("SourceState"));
		int activecall_sourceState_v2 = (int)sourceState_v2.getSystemVariables().get("activecall");
		int videoquality_sourceState_v2 = (int)sourceState_v2.getSystemVariables().get("videoquality");
		
		double nor_activecall_sourceState_v2 = normalization((double)activecall_sourceState_v2);
		double nor_videoquality_sourceState_v2 = normalization((double)videoquality_sourceState_v2);
		
		
		//target state
		int activecall_targetState_v2 = (int)solution_v2.get("activecall");
		int videoquality_targetState_v2 = (int)solution_v2.get("videoquality");
		
		double nor_activecall_targetState_v2 = normalization((double)activecall_targetState_v2);
		double nor_videoquality_targetState_v2 = normalization((double)videoquality_targetState_v2);
		
		
		//transition, trigger 1, user operations
		int useroperation_v2 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v2 = normalization((double)useroperation_v2);
		
		//trigger 2, network environment
		double packetloss_v2 = (double)solution_v1.get("PacketLoss");
		double packetdelay_v2 = (double)solution_v1.get("PacketDelay");
		double packetduplication_v2 = (double)solution_v1.get("PacketDuplication");
		double packetcorruption_v2 = (double)solution_v1.get("PacketCorruption");
		double nor_packetloss_v2 = normalization(packetloss_v2);
		double nor_packetdelay_v2 = normalization(packetdelay_v2);
		double nor_packetduplication_v2 = normalization(packetduplication_v2);
		double nor_packetcorruption_v2 = normalization(packetcorruption_v2);
		
		
		//Eculidean Metric
		double result = 0;
		result = Math.pow((nor_activecall_sourceState_v1 - nor_activecall_sourceState_v2), 2)
				+ Math.pow((nor_videoquality_sourceState_v1 - nor_videoquality_sourceState_v2), 2)
				+ Math.pow((nor_activecall_targetState_v1 - nor_activecall_targetState_v2), 2)
				+ Math.pow((nor_videoquality_targetState_v1 - nor_videoquality_targetState_v2), 2)
				+ Math.pow((nor_useroperation_v1 - nor_useroperation_v2), 2)
				+ Math.pow((nor_packetloss_v1 - nor_packetloss_v2), 2)
				+ Math.pow((nor_packetdelay_v1 - nor_packetdelay_v2), 2)
				+ Math.pow((nor_packetduplication_v1 - nor_packetduplication_v2), 2)
				+ Math.pow((nor_packetcorruption_v1 - nor_packetcorruption_v2), 2);
		result = Math.sqrt(result);
		
		result = normalization(result);
		
		
		return result;
	}
	
	public static double normalization(double x){
		double nor = x/(x+1);
		return nor;
	}
	public static double max_min_normalization(double x, double min, double max){
		double nor = (x-min)/(max-min);
		return nor;
	}
	
	
	public double variableConstraintConstraintDistance(ValueSet Vc, ValueSet BPc){
		
		int l_Vc = Vc.getValueSet().size();
		int l_BPc = BPc.getValueSet().size();
		double distance = 0;
		
		if( l_Vc == 1 && l_BPc == 1){
			
			String con0 = Vc.getValueSet().get(0);
			String con1 = BPc.getValueSet().get(0);
			
			String[] srcs0 = con0.split(" ");
			String[] srcs1 = con1.split(" ");
			
			if( srcs0[1].equals("==") && srcs1[1].equals("==")){
				int temp0 = Integer.parseInt(srcs0[2]);
				int temp1 = Integer.parseInt(srcs1[2]);
				
				distance = normalization( (double)temp0) - normalization( (double)temp1);
				
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			
		}
		else if( l_Vc == 1 && l_BPc == 2){
			
			String con00 = Vc.getValueSet().get(0);
			String con10 = BPc.getValueSet().get(0);
			String con11 = BPc.getValueSet().get(1);
			
			int temp00 = 0;
			int temp10 = 0;
			int temp11 = 0;
			
			String[] srcs00 = con00.split(" ");
			String[] srcs10 = con10.split(" ");
			String[] srcs11 = con11.split(" ");
			
			double mid1 = 1;
			
			if( srcs00[1].equals("==")){
				temp00 = Integer.parseInt(srcs00[2]);
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			if( ( srcs10[1].equals("<") || srcs10[1].equals("<="))&& ( srcs11[1].equals(">") || srcs11[1].equals(">="))){
				temp11 = Integer.parseInt( srcs10[2]);
				temp10 = Integer.parseInt( srcs11[2]);
				mid1 = ((double)temp11 - (double)temp10)/2 + (double)temp10;
			}
			else if( ( srcs10[1].equals(">") || srcs10[1].equals(">="))&& ( srcs11[1].equals("<") || srcs11[1].equals("<="))){
				temp11 = Integer.parseInt( srcs11[2]);
				temp10 = Integer.parseInt( srcs10[2]);
				mid1 = ((double)temp11 - (double)temp10)/2 + (double)temp10;
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			distance = normalization( (double)temp00) - normalization( mid1);
			
			
		}
		else if( l_Vc == 2 && l_BPc == 1){
			
			String con00 = Vc.getValueSet().get(0);
			String con01 = Vc.getValueSet().get(1);
			String con11 = BPc.getValueSet().get(0);
			
			int temp00 = 0;
			int temp01 = 0;
			int temp11 = 0;
			
			String[] srcs00 = con00.split(" ");
			String[] srcs01 = con01.split(" ");
			String[] srcs11 = con11.split(" ");
			
			double mid0 = 1;
			
			
			
			if( ( srcs00[1].equals("<") || srcs00[1].equals("<="))&& ( srcs01[1].equals(">") || srcs01[1].equals(">="))){
				temp01 = Integer.parseInt( srcs00[2]);
				temp00 = Integer.parseInt( srcs01[2]);
				mid0 = ((double)temp01 - (double)temp00)/2 + (double)temp00;
			}
			else if( ( srcs00[1].equals(">") || srcs00[1].equals(">="))&& ( srcs01[1].equals("<") || srcs01[1].equals("<="))){
				temp01 = Integer.parseInt( srcs01[2]);
				temp00 = Integer.parseInt( srcs00[2]);
				mid0 = ((double)temp01 - (double)temp00)/2 + (double)temp00;
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			if( srcs11[1].equals("==")){
				temp11 = Integer.parseInt(srcs11[2]);
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			
			distance = normalization( mid0) - normalization( (double)temp11);
			
			
			
			
		}
		else if( l_Vc == 2 && l_BPc == 2){
			
			String con00 = Vc.getValueSet().get(0);
			String con01 = Vc.getValueSet().get(1);
			String con10 = BPc.getValueSet().get(0);
			String con11 = BPc.getValueSet().get(1);
			
			int temp00 = 0;
			int temp01 = 0;
			int temp10 = 0;
			int temp11 = 0;
			
			String[] srcs00 = con00.split(" ");
			String[] srcs01 = con01.split(" ");
			String[] srcs10 = con10.split(" ");
			String[] srcs11 = con11.split(" ");
			
			double mid0 = 0;
			double mid1 = 1;
			
			if( ( srcs00[1].equals("<") || srcs00[1].equals("<="))&& ( srcs01[1].equals(">") || srcs01[1].equals(">="))){
				temp01 = Integer.parseInt( srcs00[2]);
				temp00 = Integer.parseInt( srcs01[2]);
				mid0 = ((double)temp01 - (double)temp00)/2 + (double)temp00;
			}
			else if( ( srcs00[1].equals(">") || srcs00[1].equals(">="))&& ( srcs01[1].equals("<") || srcs01[1].equals("<="))){
				temp01 = Integer.parseInt( srcs01[2]);
				temp00 = Integer.parseInt( srcs00[2]);
				mid0 = ((double)temp01 - (double)temp00)/2 + (double)temp00;
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			if( ( srcs10[1].equals("<") || srcs10[1].equals("<="))&& ( srcs11[1].equals(">") || srcs11[1].equals(">="))){
				temp11 = Integer.parseInt( srcs10[2]);
				temp10 = Integer.parseInt( srcs11[2]);
				mid1 = ((double)temp11 - (double)temp10)/2 + (double)temp10;
			}
			else if( ( srcs10[1].equals(">") || srcs10[1].equals(">="))&& ( srcs11[1].equals("<") || srcs11[1].equals("<="))){
				temp11 = Integer.parseInt( srcs11[2]);
				temp10 = Integer.parseInt( srcs10[2]);
				mid1 = ((double)temp11 - (double)temp10)/2 + (double)temp10;
			}
			else {
				System.out.println("Warning: Illegel!");
			}
			
			distance = normalization( mid1) - normalization( mid0);
			
		}
		else {
			System.out.println("Warning: Illegel constraints!");
		}
		
		
		return distance;
	}
	public double variableValueConstraintDistance(int Vv, ValueSet BPc){
		
		int l = BPc.getValueSet().size();
		
		double distance = 0;
		
		
		if( l == 1){
			String con = BPc.getValueSet().get(0);
			String[] srcs = con.split(" ");
			if( srcs[1].equals("==")){
				
				int tempv1 = Integer.parseInt( srcs[2]);
				//System.out.println("srcs[2]: " + srcs[2]);
				
				distance = normalization( (double)Vv) - normalization( (double)tempv1);
				
			}
			else if( srcs[1].equals("<")){
				System.out.println("For now, Illegal constraint.");
			}
			else if( srcs[1].equals("<=")){
				System.out.println("For now, Illegal constraint.");
			}
			else if( srcs[1].equals(">")){
				System.out.println("For now, Illegal constraint.");
			}
			else if( srcs[1].equals(">=")){
				System.out.println("For now, Illegal constraint.");
			}
			else {
				System.out.println("Warning: Illegal constraint!");
			}
			
		}
		else if( l == 2){
			
			
			
			String con0 = BPc.getValueSet().get(0);
			String con1 = BPc.getValueSet().get(1);
			
			int tempv1 = 0;
			int tempv2 = 0;
			
			String[] srcs0 = con0.split(" ");
			if( srcs0[1].equals("==")){
				System.out.println("For now, Illegal constraint.");
			}
			else if( srcs0[1].equals("<")){
				tempv2 = Integer.parseInt(srcs0[2]);
			}
			else if( srcs0[1].equals("<=")){
				tempv2 = Integer.parseInt(srcs0[2]);
			}
			else if( srcs0[1].equals(">")){
				tempv1 = Integer.parseInt(srcs0[2]);
			}
			else if( srcs0[1].equals(">=")){
				tempv1 = Integer.parseInt(srcs0[2]);
			}
			else {
				System.out.println("Warning: Illegal constraint!");
			}
			
			String[] srcs1 = con1.split(" ");
			if( srcs1[1].equals("==")){
				System.out.println("For now, Illegal constraint.");
			}
			else if( srcs1[1].equals("<")){
				tempv2 = Integer.parseInt(srcs0[2]);
			}
			else if( srcs1[1].equals("<=")){
				tempv2 = Integer.parseInt(srcs0[2]);
			}
			else if( srcs1[1].equals(">")){
				tempv1 = Integer.parseInt(srcs0[2]);
			}
			else if( srcs1[1].equals(">=")){
				tempv1 = Integer.parseInt(srcs0[2]);
			}
			else {
				System.out.println("Warning: Illegal constraint!");
			}
			
			double mid = ((double)tempv2 - (double)tempv1)/2 + (double)tempv1;
			distance = normalization( (double)Vv) - normalization( mid);
			
		}
		else {
			System.out.println("Warning: illegal ValueSet!");
		}
		
		
		return distance;
	}
	public double SimilarityBetweenSolutionAndBehaviourPair_2_ValueSet(Solution v1, SystemBehavior BP){
		
		double result = 1 - DistanceBetweenSolutionAndBehaviourPair_2_ValueSet(v1, BP);
		
		return result;
	}
	public double DistanceBetweenSolutionAndBehaviourPair_2_ValueSet(Solution v1, SystemBehavior BP){//Distance of two solutions
		
		//map all variables into the interval (0, 1), then use measures, for instance, Euclidean metric.
		
		HashMap<String, Object> solution_v1 = v1.getsolution();
		int len_solution_v1 = v1.getsolutionlength();
		
		//Distances of the source states
		State sourceState_v1 = this.existingstatemachine.getallstates().get( (int)solution_v1.get("SourceState"));
		State sourceState_BP = BP.getsourcestate();
		double distance_source_ac = variableConstraintConstraintDistance(sourceState_v1.getSystemVariables_ValueSet().get("activecall"),
				sourceState_BP.getSystemVariables_ValueSet().get("activecall"));
		double distance_source_vq = variableConstraintConstraintDistance(sourceState_v1.getSystemVariables_ValueSet().get("videoquality"),
				sourceState_BP.getSystemVariables_ValueSet().get("videoquality"));
		
		//Distances of the target states
		int activecall_targetState_v1 = (int)solution_v1.get("activecall");
		int videoquality_targetState_v1 = (int)solution_v1.get("videoquality");
		ValueSet activecall_targetState_BP = BP.gettargetstate().getSystemVariables_ValueSet().get("activecall");
		ValueSet videoquality_targetState_BP = BP.gettargetstate().getSystemVariables_ValueSet().get("videoquality");
		
		double distance_target_ac = variableValueConstraintDistance( activecall_targetState_v1, activecall_targetState_BP);
		double distance_target_vq = variableValueConstraintDistance( videoquality_targetState_v1, videoquality_targetState_BP);
		
		//Distances of the triggers
		int useroperation_v1 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v1 = normalization((double)useroperation_v1);
		int useroperation_BP;
		String useroperation_String_BP = "";
		
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
			System.out.println("Warning!");
		}
		double nor_useroperation_BP = normalization((double)useroperation_BP);
		double distance_trigger = nor_useroperation_BP - nor_useroperation_v1;
		
		//Distances of Network Environment
		double packetloss_v1 = (double)solution_v1.get("PacketLoss");
		double packetdelay_v1 = (double)solution_v1.get("PacketDelay");
		double packetduplication_v1 = (double)solution_v1.get("PacketDuplication");
		double packetcorruption_v1 = (double)solution_v1.get("PacketCorruption");
		double nor_packetloss_v1 = normalization(packetloss_v1);
		double nor_packetdelay_v1 = normalization(packetdelay_v1);
		double nor_packetduplication_v1 = normalization(packetduplication_v1);
		double nor_packetcorruption_v1 = normalization(packetcorruption_v1);
		double packetloss_BP = BP.getnetworkenvironment().getPacketLoss();
		double packetdelay_BP = BP.getnetworkenvironment().getPacketDelay();
		double packetduplication_BP = BP.getnetworkenvironment().getPacketDuplication();
		double packetcorruption_BP = BP.getnetworkenvironment().getPacketCorruption();
		double nor_packetloss_BP = normalization(packetloss_BP);
		double nor_packetdelay_BP = normalization(packetdelay_BP);
		double nor_packetduplication_BP = normalization(packetduplication_BP);
		double nor_packetcorruption_BP = normalization(packetcorruption_BP);
		
		double distance_packetloss = nor_packetloss_v1 - nor_packetloss_BP;
		double distance_packetdelay = nor_packetdelay_v1 - nor_packetdelay_BP;
		double distance_packetduplication = nor_packetduplication_v1 - nor_packetduplication_BP;
		double distance_packetcorruption = nor_packetcorruption_v1 - nor_packetcorruption_BP;
		
		//Eculidean Metric
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
		
		result = normalization(result);
		
		
		
		
		return result;
	}
	
	
	/**
	 * 201606 ValueSet the second version
	 * */
	
	public double SimilarityOfTwoSolutions_v201606(Solution v1, Solution v2){
		
		double result = 1 - DiversityOfTwoSolutions_v201606(v1, v2);
		
		return result;
	}
	public double DiversityOfTwoSolutions_v201606(Solution v1, Solution v2){//Distance of two solutions
		
		//map all variables into the interval (0, 1), then use measures, for instance, Euclidean metric.
		
		HashMap<String, Object> solution_v1 = v1.getsolution();
		HashMap<String, Object> solution_v2 = v2.getsolution();
		int len_solution_v1 = v1.getsolutionlength();
		int len_solution_v2 = v2.getsolutionlength();
		
		if( solution_v1.size() != solution_v2.size()){
			System.out.println("******* Error: Solutions have different length! *******");
			return -1;
		}
		if( solution_v1.size() != len_solution_v1 || solution_v2.size() != len_solution_v2){
			System.out.println("solution_v1.size():" + solution_v1.size() + 
					", len_solution_v1:" + len_solution_v1 +
					", solution_v2.size():" + solution_v2.size() + 
					", len_solution_v2:" + len_solution_v2);
			System.out.println("******* Warning: variable of length of solution is wrong! *******");
		}
		
		//v1
		//source state
		State sourceState_v1 = this.existingstatemachine.getallstates().get( (int)solution_v1.get("SourceState"));
		//source state v2
		State sourceState_v2 = this.existingstatemachine.getallstates().get( (int)solution_v2.get("SourceState"));
		double distance_source_ac = variableCalculateConstraintDistance_v201606(sourceState_v1.getSystemVariables_ValueSet().get("activecall"),
				sourceState_v2.getSystemVariables_ValueSet().get("activecall"));
		double distance_source_vq = variableCalculateConstraintDistance_v201606(sourceState_v1.getSystemVariables_ValueSet().get("videoquality"),
				sourceState_v2.getSystemVariables_ValueSet().get("videoquality"));
		
		//target state
		int activecall_targetState_v1 = (int)solution_v1.get("activecall");
		int videoquality_targetState_v1 = (int)solution_v1.get("videoquality");
		
		double nor_activecall_targetState_v1 = normalization((double)activecall_targetState_v1);
		double nor_videoquality_targetState_v1 = normalization((double)videoquality_targetState_v1);
		
		
		//transition, trigger 1, user operations
		int useroperation_v1 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v1 = normalization((double)useroperation_v1);
		
		//trigger 2, network environment
		double packetloss_v1 = (double)solution_v1.get("PacketLoss");
		double packetdelay_v1 = (double)solution_v1.get("PacketDelay");
		double packetduplication_v1 = (double)solution_v1.get("PacketDuplication");
		double packetcorruption_v1 = (double)solution_v1.get("PacketCorruption");
		double nor_packetloss_v1 = normalization(packetloss_v1);
		double nor_packetdelay_v1 = normalization(packetdelay_v1);
		double nor_packetduplication_v1 = normalization(packetduplication_v1);
		double nor_packetcorruption_v1 = normalization(packetcorruption_v1);
		
		
		//v2
		
		
		//target state
		int activecall_targetState_v2 = (int)solution_v2.get("activecall");
		int videoquality_targetState_v2 = (int)solution_v2.get("videoquality");
		
		double nor_activecall_targetState_v2 = normalization((double)activecall_targetState_v2);
		double nor_videoquality_targetState_v2 = normalization((double)videoquality_targetState_v2);
		
		
		//transition, trigger 1, user operations
		int useroperation_v2 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v2 = normalization((double)useroperation_v2);
		
		//trigger 2, network environment
		double packetloss_v2 = (double)solution_v2.get("PacketLoss");
		double packetdelay_v2 = (double)solution_v2.get("PacketDelay");
		double packetduplication_v2 = (double)solution_v2.get("PacketDuplication");
		double packetcorruption_v2 = (double)solution_v2.get("PacketCorruption");
		double nor_packetloss_v2 = normalization(packetloss_v2);
		double nor_packetdelay_v2 = normalization(packetdelay_v2);
		double nor_packetduplication_v2 = normalization(packetduplication_v2);
		double nor_packetcorruption_v2 = normalization(packetcorruption_v2);
		
		
		//Eculidean Metric
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
		
		result = normalization(result);
		
		
		return result;
	}
	
	public double SimilarityBetweenSolutionAndTransition_v201606(Solution v1, Transition t1){
		
		double result = 1 - DistanceBetweenSolutionAndTransition_v201606(v1, t1);
		
		return result;
	}
	public double DistanceBetweenSolutionAndTransition_v201606(Solution v1, Transition t1){//Distance of two solutions
		
		//map all variables into the interval (0, 1), then use measures, for instance, Euclidean metric.
		
		HashMap<String, Object> solution_v1 = v1.getsolution();
		int len_solution_v1 = v1.getsolutionlength();
		
		//Distances of the source states
		State sourceState_v1 = this.existingstatemachine.getallstates().get( (int)solution_v1.get("SourceState"));
		State sourceState_t = t1.getsourcestate();
		double distance_source_ac = variableCalculateConstraintDistance_v201606(sourceState_v1.getSystemVariables_ValueSet().get("activecall"),
				t1.getsourcestate().getSystemVariables_ValueSet().get("activecall"));
		double distance_source_vq = variableCalculateConstraintDistance_v201606(sourceState_v1.getSystemVariables_ValueSet().get("videoquality"),
				t1.getsourcestate().getSystemVariables_ValueSet().get("videoquality"));
		
		
		//Distances of the target states
		int activecall_targetState_v1 = (int)solution_v1.get("activecall");
		int videoquality_targetState_v1 = (int)solution_v1.get("videoquality");
		ValueSet activecall_targetState_t = t1.gettargetstate().getSystemVariables_ValueSet().get("activecall");
		ValueSet videoquality_targetState_t = t1.gettargetstate().getSystemVariables_ValueSet().get("videoquality");
		
		double distance_target_ac = variableCalculateConstraintDistance_v201606( activecall_targetState_v1, activecall_targetState_t);
		double distance_target_vq = variableCalculateConstraintDistance_v201606( videoquality_targetState_v1, videoquality_targetState_t);
		
		//Distances of the triggers
		int useroperation_v1 = (int)solution_v1.get("UserOperation");
		double nor_useroperation_v1 = normalization((double)useroperation_v1);
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
			System.out.println("***** Warning: calculate triggers! *****");
		}
		double nor_useroperation_BP = normalization((double)useroperation_BP);
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
		double distance_packetloss = variableCalculateConstraintDistance_v201606(packetloss_v1, packetloss_t);
		double distance_packetdelay = variableCalculateConstraintDistance_v201606(packetdelay_v1, packetdelay_t);
		double distance_packetduplication = variableCalculateConstraintDistance_v201606(packetduplication_v1, packetduplication_t);
		double distance_packetcorruption = variableCalculateConstraintDistance_v201606(packetcorruption_v1, packetcorruption_t);
		
		
		//Eculidean Metric
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
		
		result = normalization(result);
		
		
		
		
		
		
		return result;
	}
	
	
	public double variableCalculateConstraintDistance_v201606(double Vv, ValueSet BPc){
			
		
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
		
		int numOfLessThan = 0;
		int numOfGreaterThan = 0;
		int numOfEqualTo = 0;
		
		for( int i = 0; i < l; i ++){
			
			if( BPCList.get(i).operator.equals("==")){
				numOfEqualTo ++;
			}
			else if( BPCList.get(i).operator.equals("<=") || BPCList.get(i).operator.equals("<")){
				numOfLessThan ++;
				
			}
			else if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
				numOfGreaterThan ++;
				
			}
			else {
				System.out.println("***** Error: Wrong operator! --- *****");
				return -1;
			}
			
		}
		
		if( ( numOfLessThan >= 2) || ( numOfGreaterThan >= 2) || ( numOfLessThan == 1 && numOfGreaterThan >= 2)
				|| ( numOfLessThan >= 2 && numOfGreaterThan == 1)){
			System.out.println("***** Warning: Unsolved situation 1! *****");
			return -1;
		}
		
		double distance = 0;
		
		//Normalization
		Vv = normalization(Vv);
		
		if( numOfLessThan == 0 && numOfGreaterThan == 0){
			if( numOfEqualTo == 1){
				distance = Vv - normalization(Double.parseDouble( BPCList.get(0).varRight));
				if( distance < 0){
					distance = distance * (-1);
				}
			}
			else {
				System.out.println("***** Warning: Some Impossible situation! *****");
				return -1;
			}
		}
		else if( numOfLessThan == 0 && numOfGreaterThan == 1){
			double scrop = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
					scrop = Double.parseDouble( BPCList.get(i).varRight);
				}
			}
			if( Vv >= scrop){
				distance = 0;
			}
			else {
				distance = normalization(scrop) - Vv;
			}
		}
		else if( numOfLessThan == 1 && numOfGreaterThan == 0){
			double scrop = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals("<") || BPCList.get(i).operator.equals("<=")){
					scrop = Double.parseDouble( BPCList.get(i).varRight);
				}
			}
			if( Vv <= scrop){
				distance = 0;
			}
			else {
				distance = Vv - normalization(scrop);
			}
		}
		else if( numOfLessThan == 1 && numOfGreaterThan == 1){
			double scropG = 0;
			double scropL = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
					scropG = Double.parseDouble( BPCList.get(i).varRight);
				}
				if( BPCList.get(i).operator.equals("<") || BPCList.get(i).operator.equals("<=")){
					scropL = Double.parseDouble( BPCList.get(i).varRight);	
				}
			}
			if( scropG <= scropL){
				if( Vv <= scropL && Vv >= scropG){
					distance = 0;
				}
				else if( Vv <= scropL && Vv <= scropG){
					distance = normalization(scropG) - Vv;
				}
				else if( Vv >= scropL && Vv >= scropG){
					distance = Vv - normalization(scropL);
				}
				else{
					System.out.println("***** Warning: Impossible situation! *****");
					return -1;
				}
			}
			else {
				if( Vv >= scropG || Vv <= scropL){
					distance = 0;
				}
				else {
					double t1 = normalization(scropG) - Vv;
					double t2 = Vv - normalization(scropL);
					if( t1 >= t2){
						distance = t2;
					}
					else {
						distance = t1;
					}
				}
			}
			
			
		}
		else {
			System.out.println("***** Warning: Unsolved situation 2! *****");
			return -1;
		}
		
		
		return distance;
	}
	public double variableCalculateConstraintDistance_v201606(ValueSet Vv, ValueSet BPc){
			
		
		List<ValueSetConstraint> BPCList_v = new ArrayList<ValueSetConstraint>();
		List<ValueSetConstraint> BPCList_bp = new ArrayList<ValueSetConstraint>();
		
		int l_v = Vv.getValueSet().size();
		int l_bp = BPc.getValueSet().size();
		
		for( int i = 0; i < l_v; i ++){
			String[] srcs = Vv.getValueSet().get(i).split(" ");
			ValueSetConstraint temp = new ValueSetConstraint();
			temp.varLeft = srcs[0];
			temp.operator = srcs[1];
			temp.varRight = srcs[2];
			BPCList_v.add(temp);
		}
		for( int i = 0; i < l_bp; i ++){
			String[] srcs = BPc.getValueSet().get(i).split(" ");
			ValueSetConstraint temp = new ValueSetConstraint();
			temp.varLeft = srcs[0];
			temp.operator = srcs[1];
			temp.varRight = srcs[2];
			BPCList_bp.add(temp);
		}
		
		int numOfLessThan_v = 0;
		int numOfGreaterThan_v = 0;
		int numOfEqualTo_v = 0;
		int numOfLessThan_bp = 0;
		int numOfGreaterThan_bp = 0;
		int numOfEqualTo_bp = 0;
		
		for( int i = 0; i < l_v; i ++){
			if( BPCList_v.get(i).operator.equals("==")){
				numOfEqualTo_v ++;
			}
			else if( BPCList_v.get(i).operator.equals("<=") || BPCList_v.get(i).operator.equals("<")){
				numOfLessThan_v ++;
			}
			else if( BPCList_v.get(i).operator.equals(">=") || BPCList_v.get(i).operator.equals(">")){
				numOfGreaterThan_v ++;
			}
			else {
				System.out.println("***** Error: Wrong operator1! *****");
				return -1;
			}
		}
		for( int i = 0; i < l_bp; i ++){
			if( BPCList_bp.get(i).operator.equals("==")){
				numOfEqualTo_bp ++;
			}
			else if( BPCList_bp.get(i).operator.equals("<=") || BPCList_bp.get(i).operator.equals("<")){
				numOfLessThan_bp ++;
			}
			else if( BPCList_bp.get(i).operator.equals(">=") || BPCList_bp.get(i).operator.equals(">")){
				numOfGreaterThan_bp ++;
			}
			else {
				System.out.println("***** Error: Wrong operator2! *****");
				return -1;
			}
		}
		
		
		
		if( ( numOfLessThan_v >= 2) || ( numOfGreaterThan_v >= 2) || ( numOfLessThan_bp >= 2)
				|| ( numOfGreaterThan_bp >= 2)){
			System.out.println("***** Warning: Unsolved situation 3! *****");
			return -1;
		}
		if( l_v != l_bp){
			return 1;
			
		}
		
		double distance = 0;
		
		
		if( numOfLessThan_v == numOfLessThan_bp && numOfGreaterThan_v == numOfGreaterThan_bp){
			if( numOfLessThan_v == 0 && numOfGreaterThan_v == 0){
				if( numOfEqualTo_v == 1 && numOfEqualTo_bp == 1){
					if( Double.parseDouble( BPCList_v.get(0).varRight)
							== Double.parseDouble( BPCList_bp.get(0).varRight)){
						distance = 0;
					}
					else {
						distance = 1;
					}
				}
				else {
					System.out.println("***** Warning: Unsolved situation 5! *****");
					return -1;
				}
			}
			else if( numOfLessThan_v == 1 && numOfGreaterThan_v == 0){
				double scrop_v = 0;
				double scrop_bp = 0;
				for( int i = 0; i < l_v; i ++){
					if( BPCList_v.get(i).operator.equals("<") || BPCList_v.get(i).operator.equals("<=")){
						scrop_v = Double.parseDouble( BPCList_v.get(i).varRight);
					}
				}
				for( int i = 0; i < l_bp; i ++){
					if( BPCList_bp.get(i).operator.equals("<") || BPCList_bp.get(i).operator.equals("<=")){
						scrop_bp = Double.parseDouble( BPCList_bp.get(i).varRight);
					}
				}
				if( scrop_v == scrop_bp){
					distance = 0;
				}
				else {
					distance = 1;
				}
			}
			else if( numOfLessThan_v == 0 && numOfGreaterThan_v == 1){
				double scrop_v = 0;
				double scrop_bp = 0;
				for( int i = 0; i < l_v; i ++){
					if( BPCList_v.get(i).operator.equals(">") || BPCList_v.get(i).operator.equals(">=")){
						scrop_v = Double.parseDouble( BPCList_v.get(i).varRight);
					}
				}
				for( int i = 0; i < l_bp; i ++){
					if( BPCList_bp.get(i).operator.equals(">") || BPCList_bp.get(i).operator.equals(">=")){
						scrop_bp = Double.parseDouble( BPCList_bp.get(i).varRight);
					}
				}
				if( scrop_v == scrop_bp){
					distance = 0;
				}
				else {
					distance = 1;
				}
			}
			else if( numOfLessThan_v == 1 && numOfGreaterThan_v == 1){
				double scropG_v = 0;
				double scropL_v = 0;
				double scropG_bp = 0;
				double scropL_bp = 0;
				for( int i = 0; i < l_v; i ++){
					if( BPCList_v.get(i).operator.equals("<") || BPCList_v.get(i).operator.equals("<=")){
						scropL_v = Double.parseDouble( BPCList_v.get(i).varRight);
					}
					if( BPCList_v.get(i).operator.equals(">") || BPCList_v.get(i).operator.equals(">=")){
						scropG_v = Double.parseDouble( BPCList_v.get(i).varRight);
					}
					
				}
				for( int i = 0; i < l_bp; i ++){
					if( BPCList_bp.get(i).operator.equals("<") || BPCList_bp.get(i).operator.equals("<=")){
						scropL_bp = Double.parseDouble( BPCList_bp.get(i).varRight);
					}
					if( BPCList_bp.get(i).operator.equals(">") || BPCList_bp.get(i).operator.equals(">=")){
						scropG_bp = Double.parseDouble( BPCList_bp.get(i).varRight);
					}
				}
				if( scropL_v == scropL_bp && scropG_v == scropG_bp){
					distance = 0;
				}
				else {
					distance = 1;
				}
			}
			else {
				System.out.println("***** Warning: Unsolved situation 6! *****");
				return -1;
			}
			
			
		}
		else {
			System.out.println("***** Warning: Unsolved situation 7! *****");
			return -1;
		}
		
		
		return distance;
	}
	class ValueSetConstraint{
		String varLeft;
		String operator;
		String varRight;
	}
	
	public SystemBehavior TransformSolution2BPairValueSet_v201606(Solution v){
		SystemBehavior BPair = new SystemBehavior();
		
		State sourceState = this.existingstatemachine.getallstates().get((int)v.getsolution().get("SourceState"));
		
		BPair.setsourcestate( sourceState);
		
		HashMap<String, ValueSet> variables_valueset = new HashMap<String, ValueSet>();
		ValueSet ac = new ValueSet();
		String con_ac = "activecall == " + v.getsolution().get("activecall");
		ac.AddConstriantsForValueSet(con_ac);
		ValueSet vq = new ValueSet();
		String con_vq = "videoquality == " + v.getsolution().get("videoquality");
		vq.AddConstriantsForValueSet(con_vq);
		variables_valueset.put("activecall", ac);
		variables_valueset.put("videoquality", vq);
		
		String sname = "new" + (this.existingstatemachine.getallstates().size() + 1);//!
		State targetState = new State(sname, variables_valueset);
		
		BPair.settargetstate(targetState);
		
		
		//=============Network environment setting===============
		NetworkCondition ne = new NetworkCondition();
		ne.setPacketLoss( (double)v.getsolution().get("PacketLoss"));
		ne.setPacketDelay( (double)v.getsolution().get("PacketDelay"));
		ne.setPacketDuplication( (double)v.getsolution().get("PacketDuplication"));
		ne.setPacketCorruption( (double)v.getsolution().get("PacketCorruption"));
		BPair.setnetworkenvironment(ne);
		
		Transition t = new Transition( sourceState, targetState);
		
		List<String> triggers = t.getTriggers();
		if( (int)v.getsolution().get("UserOperation") == 0){
			triggers.add("null");
		}
		else if( (int)v.getsolution().get("UserOperation") == 1){
			triggers.add("dial");
		}
		else if( (int)v.getsolution().get("UserOperation") == 2){
			triggers.add("disconnect");
		}
		
		BPair.settrigger(triggers);
		
		
		
		
		
		
		return BPair;
	}
	
	public Transition TransformBehaviourPair2Transition_v201610(SystemBehavior bp){
		
		State sourceState_t = bp.getsourcestate();
		State targetState_t = bp.gettargetstate();
		
		Transition Ttemp = new Transition(sourceState_t, targetState_t);
		Ttemp.addTriggers(bp.gettrigger().get(0));
		
		HashMap<String, ValueSet> cs = Ttemp.getConditions();
		ValueSet vs_l= new ValueSet();
		vs_l.getValueSet().add("PacketLoss == " + bp.getnetworkenvironment().getPacketLoss());
		cs.put("PacketLoss", vs_l);
		ValueSet vs_d= new ValueSet();
		vs_d.getValueSet().add("PacketDelay == " + bp.getnetworkenvironment().getPacketDelay());
		cs.put("PacketDelay", vs_d);
		ValueSet vs_du= new ValueSet();
		vs_du.getValueSet().add("PacketDuplication == " + bp.getnetworkenvironment().getPacketDuplication());
		cs.put("PacketDuplication", vs_du);
		ValueSet vs_c= new ValueSet();
		vs_c.getValueSet().add("PacketCorruption == " + bp.getnetworkenvironment().getPacketCorruption());
		cs.put("PacketCorruption", vs_c);
		
		sourceState_t.addoutTransition(Ttemp);
		targetState_t.addinTransition(Ttemp);
		
		//Ttemp
		
		return Ttemp;
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args){
		
		
	}
	
	
	

}
