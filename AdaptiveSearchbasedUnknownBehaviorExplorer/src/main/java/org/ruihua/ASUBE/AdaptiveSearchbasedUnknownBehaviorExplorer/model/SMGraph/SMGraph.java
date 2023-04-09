package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.SMGraph;

import java.util.*;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.ReadWriteModule.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.systembehavior.*;

public class SMGraph {
	
	public StateMachine s;
	public List<Vertex> vs;
	public List<Edge> es;
	public GraphPathFinder gPF;
	
	public Vertex StartNode;
	public HashMap<Vertex, State> Vertex2State;
	public HashMap<Edge, Transition> Edge2Transition;
	public HashMap<State, Vertex> State2Vertex;
	public HashMap<Transition, Edge> Transition2Edge;
	
	
	
	public SMGraph(){
		this.vs = new ArrayList<Vertex>();
		this.es = new ArrayList<Edge>();
		this.s = null;
		this.gPF = null;
		this.StartNode = null;
	}
	
	public SMGraph(StateMachine s){
		this.s = s;
		this.vs = new ArrayList<Vertex>();
		this.es = new ArrayList<Edge>();
		this.gPF = null;
		this.StartNode = new Vertex();
		this.State2Vertex = new HashMap<State, Vertex>();
		this.Vertex2State = new HashMap<Vertex, State>();
		this.Transition2Edge = new HashMap<Transition, Edge>();
		this.Edge2Transition = new HashMap<Edge, Transition>();
		
		this.StateMachine2Graph();
		
		
	}
	
	public void StateMachine2Graph(){
		
		int lstate = this.s.getallstates().size();
		int ltransition = this.s.getalltransitions().size();
		
		List<State> states = this.s.getallstates();
		List<Transition> transitions = this.s.getalltransitions();
		
		for(int i = 0; i < lstate; i ++){
			Vertex vTemp = new Vertex();
			vTemp.id = i;
			this.State2Vertex.put(states.get(i), vTemp);
			this.Vertex2State.put(vTemp, states.get(i));
		}
		
		for(int i = 0; i < ltransition; i ++){
			Edge eTemp = new Edge();
			eTemp.id = i;
			this.Transition2Edge.put(transitions.get(i), eTemp);
			this.Edge2Transition.put(eTemp, transitions.get(i));
		}
		
		for(int i = 0; i < ltransition; i ++){
			Transition tTemp = transitions.get(i);
			Edge eTemp = Transition2Edge.get(tTemp);
			
			State ssTemp = tTemp.getsourcestate();
			State tsTemp = tTemp.gettargetstate();
			
			eTemp.front = State2Vertex.get(ssTemp);
			eTemp.next = State2Vertex.get(tsTemp);
			
			State2Vertex.get(ssTemp).OutEdges.add(eTemp);
			State2Vertex.get(tsTemp).InEdges.add(eTemp);
		}
		
		//Construct es and vs
		for( int i = 0; i < lstate; i ++){
			this.vs.add(this.State2Vertex.get(states.get(i)));
		}
		for( int i = 0; i < ltransition; i ++){
			this.es.add(this.Transition2Edge.get(transitions.get(i)));
		}
		
		//Start Node
		this.StartNode = this.State2Vertex.get(this.s.getstartstate());
	}
	public List<Transition> GPath2StateMachinePath(GPath p){
		
		List<Transition> SMPath = new ArrayList<Transition>();
		
		int l = p.gPath.size();
		for(int i = 0; i < l; i ++){
			SMPath.add(this.Edge2Transition.get(p.gPath.get(i)));
		}
		
		return SMPath;
	}
	
	
	
	
	
	//Func: Find Path.
	public void setGraphByDijkstra(Vertex vStart){
		this.gPF = new GraphPathFinder(this);
		gPF.GPF_Dijkstra(vStart);
	}
	public GPath getGraphPath(Vertex vStart, Vertex vTarget){
		
		GPath p = this.gPF.getPath_Dijkstra(vStart, vTarget);
		
		return p;
	}
	
	
	
	
	
}
