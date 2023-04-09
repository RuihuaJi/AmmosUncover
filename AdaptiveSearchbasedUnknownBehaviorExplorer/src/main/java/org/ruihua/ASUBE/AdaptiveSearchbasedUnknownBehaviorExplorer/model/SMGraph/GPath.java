package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.SMGraph;

import java.util.*;

public class GPath {
	
	public List<Edge> gPath;
	
	public GPath(){
		this.gPath = new ArrayList<Edge>();
	}
	
	//Func: Reverse
	public void reversePath(){
		List<Edge> t = new ArrayList<Edge>();
		int l = this.gPath.size();
		for(int i = l - 1; i >= 0; i --){
			t.add(this.gPath.get(i));
		}
		this.gPath = t;
	}
	
	public void printPath(){
		
		int l = gPath.size();
		
		for(int i = 0; i < l; i ++){
			
			Edge eTemp = gPath.get(i);
			
			System.out.print("[----------]{[v-" + eTemp.front.id + ", e-"
					+ eTemp.id + ", ");
			
		}
		if( l > 0){
			System.out.println("v-" + gPath.get(l - 1).next.id + " End.]}");
		}
	}
	
	public static void main(String[] args){
		
		GPath p = new GPath();
		
		Vertex v0 = new Vertex();
		v0.id = 0;
		Vertex v1 = new Vertex();
		v1.id = 1;
		Edge e0 = new Edge();
		e0.id = 0;
		e0.front = v0;
		e0.next = v1;
		p.gPath.add(e0);
		
		Vertex v2 = new Vertex();
		v2.id = 2;
		Edge e1 = new Edge();
		e1.id = 1;
		e1.front = v1;
		e1.next = v2;
		p.gPath.add(e1);
		
		p.printPath();
		
	}
	
	
	
}
