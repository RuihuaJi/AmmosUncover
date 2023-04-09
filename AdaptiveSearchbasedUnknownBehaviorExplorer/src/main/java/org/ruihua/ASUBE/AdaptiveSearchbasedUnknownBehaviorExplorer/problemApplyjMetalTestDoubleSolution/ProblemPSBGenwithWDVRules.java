package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives.GlobalStructureSimilarity;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives.TestInputSpaceCoverageDiversity;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives.WDVRuleGuidance_v20191121;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.ValueSet;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.NetworkCondition;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.State;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.StateMachine;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.SystemBehavior;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.Transition;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.ConstraintList;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem.Solution;

public class ProblemPSBGenwithWDVRules extends AbstractDoubleProblem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4799123171288135842L;
	
	private int[] variables;
	private int[] objs;
	
	private ConstraintList constraints;
	private StateMachine existingstatemachine;
	
	private GlobalStructureSimilarity O1_globalstructuresimilarity;
	private TestInputSpaceCoverageDiversity O2_testinputspacecoveragediversity;
	private WDVRuleGuidance_v20191121 O3O4_WDVRuleGuidance;
	
	public int label = 0;
	
	public ProblemPSBGenwithWDVRules() {
		
	}
	
	public void preparation(StateMachine s, List<Solution> l) {
		
		this.setexistingstatemachine(s);
		
		if(this.existingstatemachine == null) {
			System.out.println("[++++++++++]{[existingstatemachine == null]}");
		}
		else {
			System.out.println("[++++++++++]{[existingstatemachine != null]}");
		}
		
		this.O1_globalstructuresimilarity = new GlobalStructureSimilarity();
		this.O1_globalstructuresimilarity.setexistingstatemachine(existingstatemachine);
		
		List<Solution> listExtraction = new ArrayList<>();
		int len = l.size();
		for(int i = 0; i < len; i ++) {
			if((int)l.get(i).getsolution().get("videoquality") < 0) {}
			else {
				listExtraction.add(l.get(i));
			}
		}
		
		this.O2_testinputspacecoveragediversity = new TestInputSpaceCoverageDiversity();
		this.O2_testinputspacecoveragediversity.setexistingstatemachine(existingstatemachine);
		this.O2_testinputspacecoveragediversity.setSolutionsOfExistingTestCases(listExtraction);
		
		
		
		this.O3O4_WDVRuleGuidance = new WDVRuleGuidance_v20191121(existingstatemachine, l);
		
	}
	
	
	
	public void initial() {
		this.setConstraints();
		
		if(this.constraints != null && objs != null) {
			setNumberOfVariables(this.constraints.getconstraints().size());
			setNumberOfObjectives(this.objs.length);
			System.out.println("[++++++++++]{[getNumberOfVariables " + getNumberOfVariables() + ", getNumberOfObjectives " + getNumberOfObjectives() + "]}");
			
			List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());
			List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());
			
			lowerLimit.add((double)this.constraints.getconstraints().get("SourceState").get(0));
			upperLimit.add((double)this.constraints.getconstraints().get("SourceState").get(1));
			
			lowerLimit.add((double)this.constraints.getconstraints().get("UserOperation").get(0));
			upperLimit.add((double)this.constraints.getconstraints().get("UserOperation").get(1));
			
			lowerLimit.add((double)this.constraints.getconstraints().get("PacketLoss").get(0));
			upperLimit.add((double)this.constraints.getconstraints().get("PacketLoss").get(1));
			
			lowerLimit.add((double)this.constraints.getconstraints().get("PacketDelay").get(0));
			upperLimit.add((double)this.constraints.getconstraints().get("PacketDelay").get(1));
			
			lowerLimit.add((double)this.constraints.getconstraints().get("PacketDuplication").get(0));
			upperLimit.add((double)this.constraints.getconstraints().get("PacketDuplication").get(1));
			
			lowerLimit.add((double)this.constraints.getconstraints().get("PacketCorruption").get(0));
			upperLimit.add((double)this.constraints.getconstraints().get("PacketCorruption").get(1));
			
			lowerLimit.add((double)this.constraints.getconstraints().get("activecall").get(0));
			upperLimit.add((double)this.constraints.getconstraints().get("activecall").get(1));
			
			lowerLimit.add((double)this.constraints.getconstraints().get("videoquality").get(0));
			upperLimit.add((double)this.constraints.getconstraints().get("videoquality").get(1));
			
			setLowerLimit(lowerLimit);
			setUpperLimit(upperLimit);
			
		}
		
	}
	
	public static Solution TransformDoubleSolution2Solution(DoubleSolution solution) {
		Solution s = new Solution();
		//source state (state id)
		int sourcestateID = (int)Math.round(solution.getVariableValue(0));
		s.addsolutionmember("SourceState", sourcestateID);
		
		//trigger - UserOperation
		int uo = (int)Math.round(solution.getVariableValue(1));
		s.addsolutionmember("UserOperation", uo);
		
		//Network Environment - PacketLoss
		double loss = solution.getVariableValue(2);
		s.addsolutionmember("PacketLoss", loss);
		
		//Network Environment - PacketDelay
		double delay = solution.getVariableValue(3);
		s.addsolutionmember("PacketDelay", delay);
		
		//Network Environment - PacketDuplication
		double duplication = solution.getVariableValue(4);
		s.addsolutionmember("PacketDuplication", duplication);
		
		//Network Environment - PacketCorruption
		double corruption = solution.getVariableValue(5);
		s.addsolutionmember("PacketCorruption", corruption);
		
		//target state's variable - activecall
		int ac = (int)Math.round(solution.getVariableValue(6));
		s.addsolutionmember("activecall", ac);
		
		//target state's variable - videoquality
		int vq = (int)Math.round(solution.getVariableValue(7));
		s.addsolutionmember("videoquality", vq);
		
		return s;
	}
	

	@Override
	public void evaluate(DoubleSolution solution) {
		// TODO Auto-generated method stub
		
		System.out.print("[++++++++++]{[evaluation " + label + " solutions]} ");
		label++;
		
		//Solution from the input DoubleSolution
		Solution s = TransformDoubleSolution2Solution(solution);
		
		//O3 and O4's pre-condition, and this could prepare in steps before
		this.O3O4_WDVRuleGuidance.ChooseOptionalWDVRules(s);
		
		double ov = -1;
		
		int i = 0;
		if(objs[0] == 1) {
			//Objective 1st (O1): global structure similarity, expect maximizing similarity
			ov = this.O1_globalstructuresimilarity.Objective1_GlobalStructureSimilarity(s);
			solution.setObjective(i, 1 - ov);
			i++;
		}
		if(objs[1] == 1) {
			//Objective 2nd (O2): test input space coverage diversity, expect maximizing diversity
			ov = this.O2_testinputspacecoveragediversity.Objective2_TestInputSpaceDiversity(s);
			solution.setObjective(i, 1 - ov);
			i++;
		}
		if(objs[2] == 1) {
			//Objective 3rd (O3): delta variable rule-based network condition similarity, expect maximizing similarity
			ov = this.O3O4_WDVRuleGuidance.Objective3_DeltaVariableRuleBasedNetworkConditionSimilarity(s);
			solution.setObjective(i, 1 - ov);
			i++;
		}
		if(objs[3] == 1) {
			//Objective 4th (O4): delta variable rule-based network condition diversity, expect maximizing diversity
			ov = this.O3O4_WDVRuleGuidance.Objective4_DeltaVariableRuleBasedNetworkConditionDiversity(s);
			solution.setObjective(i, 1 - ov);
			i++;
		}
		for(int j = 0; j < solution.getNumberOfVariables(); j ++) {
			System.out.print(solution.getVariableValueString(j) + ",");
		}
		
		System.out.println("[++++++++++]{[objs-" + String.format("%.8f",solution.getObjective(0)) + "," + String.format("%.8f",solution.getObjective(1)) + "," + String.format("%.8f",solution.getObjective(2)) + "," + String.format("%.8f",solution.getObjective(3)) + "]}");
		
	}
	
	
	public void setConstraints() {
		// TODO Auto-generated method stub
		//Scheme: from BehaviourPair to Problem Constraints
		this.constraints = new ConstraintList();
		
		this.constraints.addOneConstraint("SourceState", 0, this.existingstatemachine.getallstates().size() - 1, 0);
		this.constraints.addOneConstraint("UserOperation", 0, 2, 0);//0-null, 1-dial, 2-disconnect
		
		this.constraints.addOneConstraint("PacketLoss", 0, 100, 3);
		this.constraints.addOneConstraint("PacketDelay", 0, 100, 3);
		this.constraints.addOneConstraint("PacketDuplication", 0, 100, 3);
		this.constraints.addOneConstraint("PacketCorruption", 0, 100, 3);
		
		this.constraints.addOneConstraint("activecall", 0, 3, 2);
		this.constraints.addOneConstraint("videoquality", 0, 3, 2);
				
	}
	
	public Solution TransformSystemBehavior2Solution_v2021(SystemBehavior bp) {
		Solution s = new Solution();
		
		State sourceState = bp.getsourcestate();
		List<State> allexistingstates = this.existingstatemachine.getallstates();
		int len = allexistingstates.size();
		for(int i = 0; i < len; i ++){
			State currentState = allexistingstates.get(i);
			if( currentState.getStateName().equals(sourceState.getStateName())){
				s.addsolutionmember("SourceState", i);
				break;
			}
		}
		
		//trigger - UserOperation
		if(bp.gettrigger().get(0) == "null")
			s.addsolutionmember("UserOperation", 0);
		else if(bp.gettrigger().get(0).equals("dial"))
			s.addsolutionmember("UserOperation", 1);
		else if(bp.gettrigger().get(0).equals("disconnect"))
			s.addsolutionmember("UserOperation", 2);
		
		//Network Environment - PacketLoss
		double loss = bp.getnetworkenvironment().getPacketLoss();
		s.addsolutionmember("PacketLoss", loss);
		
		//Network Environment - PacketDelay
		double delay = bp.getnetworkenvironment().getPacketDelay();
		s.addsolutionmember("PacketDelay", delay);
		
		//Network Environment - PacketDuplication
		double duplication = bp.getnetworkenvironment().getPacketDuplication();
		s.addsolutionmember("PacketDuplication", duplication);
		
		//Network Environment - PacketCorruption
		double corruption = bp.getnetworkenvironment().getPacketCorruption();
		s.addsolutionmember("PacketCorruption", corruption);
		
		//target state's variable - activecall
		String ac_String = bp.gettargetstate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0);
		String[] srcs_ac = ac_String.split(" ");
		int ac_V = Integer.parseInt(srcs_ac[2]);
		s.addsolutionmember("activecall", ac_V);
		
		//target state's variable - videoquality
		String vq_String = bp.gettargetstate().getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0);
		String[] srcs_vq = vq_String.split(" ");
		int vq_V = Integer.parseInt(srcs_vq[2]);
		s.addsolutionmember("videoquality", vq_V);
		
		return s;
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
		
		String sname = "new" + this.existingstatemachine.getallstates().size();//!
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
	
	
	
	
	public ConstraintList getConstraints() {
		// TODO Auto-generated method stub
		return this.constraints;
	}
	public void setexistingstatemachine(StateMachine existingstatemachine){
		this.existingstatemachine = existingstatemachine;
	}
	public StateMachine getexistingstatemachine(){
		return this.existingstatemachine;
	}
	public void setVariables(int[] vs) {
		this.variables = vs;
	}
	public void setObjs(int[] os) {
		this.objs = os;
	}
	public void setProblemName(String pName) {
		this.setName(pName);
	}
	
	public DoubleSolution createSolution() {
		return new ASUBEDoubleSolution(this);
	}
	
	
	

}
