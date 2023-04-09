package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.DVrules;

public class WeightedDVRule {
	private DeltaVariableRule dvrule;
	private double weight;
	
	/**Construction Methods**/
	public WeightedDVRule() {
		dvrule = new DeltaVariableRule();
		weight = 1;
	}
	public WeightedDVRule(DeltaVariableRule r) {
		dvrule = r;
		weight = 1;
	}
	public WeightedDVRule(double w) {
		dvrule = new DeltaVariableRule();
		weight = w;
	}
	public WeightedDVRule(DeltaVariableRule r, double w) {
		dvrule = r;
		weight = w;
	}
	
	/**Get and Set Methods**/
	public DeltaVariableRule getDVRule() {
		return this.dvrule;
	}
	public void setDVRule(DeltaVariableRule r) {
		this.dvrule = r;
	}
	public double getWeight() {
		return this.weight;
	}
	public void setWeight(double w) {
		this.weight = w;
	}
}
