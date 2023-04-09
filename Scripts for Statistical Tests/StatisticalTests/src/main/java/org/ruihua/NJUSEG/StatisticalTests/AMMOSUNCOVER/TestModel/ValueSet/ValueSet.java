package org.ruihua.NJUSEG.StatisticalTests.AMMOSUNCOVER.TestModel.ValueSet;

import java.util.ArrayList;
import java.util.List;


public class ValueSet {
	
	private List<String> values;
	
	public ValueSet(){
		this.values = new ArrayList<String>();
	}
	public ValueSet(List<String> v){
		this.values = v;
	}
	
	public boolean isequal(ValueSet vs){
		boolean checkResult = true;
		
		int l_v = this.values.size();
		int l_vs = vs.values.size();
		
		if( l_v == l_vs){
			
			for( int i = 0; i < l_v; i ++){
				int tag = 0;
				for(int j = 0; j < l_vs; j ++){
					if( this.values.get(i).equals(vs.values.get(j))){
						tag = 1;
						break;
					}
					else {
						//do nothing
					}
				}
				if( tag == 0){
					checkResult = false;
					break;
				}
			}
			
		}
		else {
			checkResult = false;
		}
		
		return checkResult;
	}
	
	
	
	
	public void AddConstriantsForValueSet(String c){
		this.values.add(c);
	}
	public void RemoveConstriantsForValueSet(int index){
		this.values.remove(index);
	}
	public void setValueSet(List<String> constraints){
		this.values = constraints;
	}
	public List<String> getValueSet(){
		return this.values;
	}
	
	public static void CalculateConstraints(){
		
	}
	
	
	
	
	
	
	public static void main(String[] args){
		
		
		
		
	}
	
}
