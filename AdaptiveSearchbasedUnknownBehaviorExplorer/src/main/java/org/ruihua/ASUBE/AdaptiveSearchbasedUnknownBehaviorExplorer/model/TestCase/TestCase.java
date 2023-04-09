package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.TestCase;


import java.util.*;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.SMGraph.*;

public class TestCase {
	
	
	public State startState;
	public List<EdgeVertex> inputPath;
	public SystemBehavior bp;
	public StateMachine s;
	
	public TestCase(){
		this.startState = null;
		this.s = null;
		this.inputPath = new ArrayList<EdgeVertex>();
		this.bp = null;
	}
	public TestCase(StateMachine s){
		this.startState = s.getstartstate();
		this.s = s;
		this.inputPath = new ArrayList<EdgeVertex>();
		this.bp = null;
	}
	
	public void generateTestCase(SystemBehavior bp){
		
		State targetState = bp.getsourcestate();
		
		SMGraph g = new SMGraph(this.s);
		g.setGraphByDijkstra(g.StartNode);
		GPath p = g.getGraphPath(g.StartNode, g.State2Vertex.get(targetState));
		
		List<Transition> SMP = g.GPath2StateMachinePath(p);
		int l = SMP.size();
		for( int i = 0; i < l ; i ++){
			EdgeVertex evTemp = new EdgeVertex();
			Transition tTemp = SMP.get(i);
			
			evTemp.activecall = tTemp.gettargetstate().getSystemVariables_ValueSet().get("activecall");
			
			evTemp.videoquality = tTemp.gettargetstate().getSystemVariables_ValueSet().get("videoquality");
			
			evTemp.trigger = tTemp.getTriggers().get(0);
			
			evTemp.PacketLoss = evTemp.Constraints2Value(tTemp.getConditions().get("PacketLoss"));
			
			evTemp.PakcetDelay = evTemp.Constraints2Value(tTemp.getConditions().get("PacketDelay"));
			
			evTemp.PacketDuplication = evTemp.Constraints2Value(tTemp.getConditions().get("PacketDuplication"));
			
			evTemp.PacketCorruption = evTemp.Constraints2Value(tTemp.getConditions().get("PacketCorruption"));
			this.inputPath.add(evTemp);
		}
		
		this.bp = bp;
	}
	
	public void printTestCase(){
		System.out.println("++++++++++++++++++++ {[Start State][" + this.startState.getStateName() + "]} ++++++++++++++++++++");
		int l = this.inputPath.size();
		for(int i = 0; i < l; i ++){
			System.out.print("++++++++++++++++++++ {[Trigger: " + this.inputPath.get(i).trigger);
			System.out.print("PacketLoss" + this.inputPath.get(i).PacketLoss + " ");
			System.out.print("PakcetDelay" + this.inputPath.get(i).PakcetDelay + " ");
			System.out.print("PacketDuplication" + this.inputPath.get(i).PacketDuplication + " ");
			System.out.print("PacketCorruption" + this.inputPath.get(i).PacketCorruption + " ");
			System.out.println("]} ++++++++++++++++++++");
		}
		System.out.println("++++++++++++++++++++ {[BP-AC: " + this.bp.gettargetstate().getSystemVariables_ValueSet().get("activecall").getValueSet().get(0)
				+ "; BP-VQ: " + this.bp.gettargetstate().getSystemVariables_ValueSet().get("videoquality").getValueSet().get(0)
				+ "]} ++++++++++++++++++++"
				);
	}
	
	
	public static void main(String[] args){
		
		
		
		
		
	}
	
}
