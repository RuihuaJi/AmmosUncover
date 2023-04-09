package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.problem;


public interface Problem {
	
	/**
	 * ConstraintList: field-HashMap<String, List<Integer>>.
	 * 	String: variable's name
	 * 	ArrayList: N * 3
	 * 		0. min
	 * 		1. max
	 * 		2. type(0-numerical, 1-categorical)
	 * */
	
	public static final int DOUBLE_NUMERICAL_TYPE = 3;
	public static final int INTEGER_NUMERICAL_TYPE = 2;
	public static final int DOUBLE_CATEGORICAL_TYPE = 1;//This can't be possible.
	public static final int INTEGER_CATEGORICAL_TYPE = 0;
	
	/**
	 * matrix N*3
	 * - N is the number of variables
	 * - 3 values: min, max, type
	 * - type is 0 if integer and category
	 * - type is 1 if double and category
	 * - type is 2 if integer and numeric
	 * - type is 3 if double and numeric
	 */
	public void setConstraints();
	
	public ConstraintList getConstraints();
	
	public double getFitness(Solution v);
	
}
