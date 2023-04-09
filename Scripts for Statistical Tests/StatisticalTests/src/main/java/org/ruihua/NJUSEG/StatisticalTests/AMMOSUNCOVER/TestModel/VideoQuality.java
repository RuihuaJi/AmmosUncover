package org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel;

public class VideoQuality {
	private String label;
	private int value;
	private int rawvalue;
	
	public VideoQuality() {
		this.label = "videoquality";
		this.value = -1;
		this.rawvalue = -1000;
	}
	
	public static int VQValueTransformRV2V(int rv) {
		int result = -100;
		if(rv < 5) {
			result = 0;
		}
		else if(rv >= 5 && rv < 10) {
			result = 1;
		}
		else if(rv >= 10 && rv < 25) {
			result = 2;
		}
		else {
			result = 3;
		}
		
		return result;
	}
	
	
	public String getLabel() { return this.label; }
	public void setLabel(String l) { this.label = l; }
	public int getValue() { return this.value; }
	public void setValue(int v) { this.value = v; }
	public int getRawvalue() { return this.rawvalue; }
	public void setRawvalue(int rv) { this.rawvalue = rv; }
}
