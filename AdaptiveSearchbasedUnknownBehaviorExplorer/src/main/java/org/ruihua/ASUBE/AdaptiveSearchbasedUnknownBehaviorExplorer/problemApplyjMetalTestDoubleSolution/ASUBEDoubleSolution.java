package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problemApplyjMetalTestDoubleSolution;

import org.uma.jmetal.problem.DoubleProblem;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;

public class ASUBEDoubleSolution extends DefaultDoubleSolution {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3360089368717641618L;

	
	public ASUBEDoubleSolution(ASUBEDoubleSolution solution) {
		super(solution);
		// TODO Auto-generated constructor stub
	}
	
	public ASUBEDoubleSolution(DoubleProblem problem) {
		super(problem);
		// TODO Auto-generated constructor stub
	}
	
	public String getVariableValueString(int index) {
		
		return " " + getVariableValue(index);
	}
	
	
	public ASUBEDoubleSolution copy() {
		return new ASUBEDoubleSolution(this);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
