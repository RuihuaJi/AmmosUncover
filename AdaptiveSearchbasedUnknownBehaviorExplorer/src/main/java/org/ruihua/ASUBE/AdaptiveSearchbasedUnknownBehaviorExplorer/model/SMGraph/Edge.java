package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.SMGraph;


public class Edge {
	
	public int id;
	public Vertex front;
	public Vertex next;
	private int w;
	
	public Edge(){
		this.id = -1;
		this.front = null;
		this.next = null;
		this.w = 1;
	}
	
	public int getWeight(){
		return this.w;
	}
	
	
}
