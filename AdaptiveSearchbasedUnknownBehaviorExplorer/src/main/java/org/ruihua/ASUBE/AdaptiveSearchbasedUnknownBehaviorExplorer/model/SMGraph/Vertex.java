package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.SMGraph;

import java.util.ArrayList;
import java.util.List;


public class Vertex {
	
	public int id;
	public List<Vertex> nextVertexs;
	public List<Edge> InEdges;
	public List<Edge> OutEdges;
	
	public Vertex(){
		this.id = -1;
		this.nextVertexs = new ArrayList<Vertex>();
		this.InEdges = new ArrayList<Edge>();
		this.OutEdges = new ArrayList<Edge>();
	}
	
	
}