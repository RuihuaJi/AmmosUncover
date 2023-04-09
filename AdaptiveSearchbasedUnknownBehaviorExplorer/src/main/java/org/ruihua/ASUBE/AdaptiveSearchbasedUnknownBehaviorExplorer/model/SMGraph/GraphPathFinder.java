package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.SMGraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class GraphPathFinder {
	
	public SMGraph g;
	public Vertex vStart;
	//public Vertex vTarget;
	//public GPath ResultPath;
	
	public HashMap<Integer, Integer> PIv;
	public HashMap<Integer, Integer> Dv;
	public HashMap<Integer, HashMap<Integer,Integer>> Wv;
	
	public GraphPathFinder(){
		this.g = null;
		this.vStart = null;
		//this.vTarget = null;
		//this.ResultPath = null;
		this.PIv = new HashMap<Integer, Integer>();
		this.Dv = new HashMap<Integer, Integer>();
		this.Wv = new HashMap<Integer, HashMap<Integer,Integer>>();
	}
	public GraphPathFinder(SMGraph graph){
		this.g = graph;
		this.vStart = null;
		//this.vTarget = null;
		//this.ResultPath = null;
		this.PIv = new HashMap<Integer, Integer>();
		this.Dv = new HashMap<Integer, Integer>();
		this.Wv = new HashMap<Integer, HashMap<Integer,Integer>>();
		this.initialWeight();
	}
	public void initialWeight(){
		List<Edge> es = g.es;
		int l = es.size();
		for( int i = 0; i < l; i ++){
			int keyFront = es.get(i).front.id;
			if( this.Wv.get(keyFront) == null){
				this.Wv.put(keyFront, new HashMap<Integer, Integer>());
			}
			this.Wv.get(keyFront).put(es.get(i).next.id, es.get(i).getWeight());
		}
	}
	
	//Dijkstra
	public void initialSingleSource_Dijkstra(Vertex vStart){
		List<Vertex> vs = this.g.vs;
		int l = vs.size();
		for(int i = 0; i < l; i ++){
			int key = vs.get(i).id;
			this.Dv.put(key, 10000);
			this.PIv.put(key, -1);
		}
		this.Dv.put(vStart.id, 0);
	}
	public void Relax_Dijkstra(Vertex u, Vertex v){
		if( this.Dv.get(u.id) == 10000){
			System.out.println("#################### {[Error][In relax, vertex U's distance is not limited!][GraphPathFinder]} ####################");
			return;
		}
		int dTemp = this.Dv.get(u.id) + this.Wv.get(u.id).get(v.id);
		if( this.Dv.get(v.id) > dTemp){
			this.Dv.put(v.id, dTemp);
			this.PIv.put(v.id, u.id);
		}
	}
	public Vertex ExtraMin_Dijkstra(List<Vertex> Q){
		int l = Q.size();
		if( l <= 0){
			System.out.println("#################### {[Warning][Q is Empty!][GraphPathFinder]} ####################");
			return null;
		}
		Vertex vTemp = Q.get(0);
		int Dv = this.Dv.get(Q.get(0).id);
		int index = 0;
		
		for(int i = 1; i < l; i ++){
			if( Dv > this.Dv.get(Q.get(i).id)){
				Dv = this.Dv.get(Q.get(i).id);
				vTemp = Q.get(i);
				index = i;
			}
		}
		Q.remove(index);
		return vTemp;
	}
	
	public void GPF_Dijkstra(Vertex vStart){
		
		this.vStart = vStart;
		this.initialSingleSource_Dijkstra(vStart);
		
		List<Vertex> S = new ArrayList<Vertex>();
		List<Vertex> Q = new ArrayList<Vertex>();
		int l = this.g.vs.size();
		for(int i = 0; i < l; i ++){
			Q.add(this.g.vs.get(i));
		}
		
		while( Q.size() != 0){
			Vertex u = ExtraMin_Dijkstra(Q);
			S.add(u);
			int lu = u.OutEdges.size();
			for(int k = 0; k < lu; k ++){
				Relax_Dijkstra(u, u.OutEdges.get(k).next);
			}
			
		}
		
		
		
		
	}
	
	public GPath getPath_Dijkstra(Vertex vStart, Vertex vTarget){
		
		if( vStart.id != this.vStart.id){
			System.out.println("#################### {[Error][Wrong Source Vertex!][GraphPathFinder]} ####################");
			return null;
		}
		
		GPath gPath = new GPath();
		
		Vertex currentV = vTarget;
		
		while( this.PIv.get(currentV.id) != -1){
			
			int frontV = this.PIv.get(currentV.id);
			//int nextV = currentV.id;
			//System.out.println("front-" + frontV + ", next-" + nextV);
			
			int l = currentV.InEdges.size();
			//System.out.println("InEdge Size:" + l);
			Edge tEdge = null;
			for(int i = 0; i < l; i ++){
				if( currentV.InEdges.get(i).front.id == frontV){
					tEdge = currentV.InEdges.get(i);
					gPath.gPath.add(tEdge);
					break;
				}
			}
			if( tEdge != null){
				if( tEdge.next.id == currentV.id && 
						tEdge.front.id == frontV){
					//do
				}
				else {
					System.out.println("#################### {[Error][Do not find the correct Edge!][GraphPathFinder]} ####################");
					break;
				}
			}
			else {
				System.out.println("#################### {[Error][Do not find the correct Edge With the Correct Source V!][GraphPathFinder]} ####################");
				break;
			}
			currentV = this.g.vs.get(this.PIv.get(currentV.id));
		}
		
		gPath.reversePath();
		
		
		return gPath;
		
	}
	
	
	
	
}