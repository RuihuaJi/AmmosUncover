package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives.util;

import java.util.ArrayList;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.ValueSet.ValueSet;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.Objectives.util.ValueSetConstraint;

public class VarCalMethods {
	
	
	public static double variableCalculateConstraintDistance(double Vv, ValueSet BPc) {
		
		List<ValueSetConstraint> BPCList = new ArrayList<ValueSetConstraint>();
		if( BPc == null){
			System.out.println("[++++++++++]{[BPc == null]}");
		}
		if( BPc.getValueSet().isEmpty()){
			System.out.println("[++++++++++]{[BPc.getValueSet().isEmpty()]}");
		}
		int l = BPc.getValueSet().size();
		
		for( int i = 0; i < l; i ++){
			String[] srcs = BPc.getValueSet().get(i).split(" ");
			ValueSetConstraint temp = new ValueSetConstraint();
			temp.varLeft = srcs[0];
			temp.operator = srcs[1];
			temp.varRight = srcs[2];
			BPCList.add(temp);
			
		}
		
		int numOfLessThan = 0;
		int numOfGreaterThan = 0;
		int numOfEqualTo = 0;
		
		for( int i = 0; i < l; i ++){
			
			if( BPCList.get(i).operator.equals("==")){
				
				numOfEqualTo ++;
			}
			else if( BPCList.get(i).operator.equals("<=") || BPCList.get(i).operator.equals("<")){
				numOfLessThan ++;
				
			}
			else if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
				numOfGreaterThan ++;
				
			}
			else {
				System.out.println("#################### {[Error][Wrong operator!][VarCalMethods]} ####################");
				return -1;
			}
			
		}
		
		if( ( numOfLessThan >= 2) || ( numOfGreaterThan >= 2) || ( numOfLessThan == 1 && numOfGreaterThan >= 2)
				|| ( numOfLessThan >= 2 && numOfGreaterThan == 1)){
			System.out.println("#################### {[Warning][Unsolved situation 1!][VarCalMethods]}*****");
			return -1;
		}
		
		double distance = 0;
		
		//Normalization
		Vv = normalization(Vv);
		
		if( numOfLessThan == 0 && numOfGreaterThan == 0){
			if( numOfEqualTo == 1){
				distance = Vv - normalization(Double.parseDouble( BPCList.get(0).varRight));
				if( distance < 0){
					distance = distance * (-1);
				}
			}
			else {
				System.out.println("#################### {[Warning][Some Impossible situation!][VarCalMethods]} ####################");
				return -1;
			}
		}
		else if( numOfLessThan == 0 && numOfGreaterThan == 1){
			double scrop = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
					scrop = Double.parseDouble( BPCList.get(i).varRight);
				}
			}
			if( Vv >= scrop){
				distance = 0;
			}
			else {
				distance = normalization(scrop) - Vv;
			}
		}
		else if( numOfLessThan == 1 && numOfGreaterThan == 0){
			double scrop = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals("<") || BPCList.get(i).operator.equals("<=")){
					scrop = Double.parseDouble( BPCList.get(i).varRight);
				}
			}
			if( Vv <= scrop){
				distance = 0;
			}
			else {
				distance = Vv - normalization(scrop);
			}
		}
		else if( numOfLessThan == 1 && numOfGreaterThan == 1){
			double scropG = 0;
			double scropL = 0;
			for(int i = 0; i < l; i ++){
				if( BPCList.get(i).operator.equals(">") || BPCList.get(i).operator.equals(">=")){
					scropG = Double.parseDouble( BPCList.get(i).varRight);
				}
				if( BPCList.get(i).operator.equals("<") || BPCList.get(i).operator.equals("<=")){
					scropL = Double.parseDouble( BPCList.get(i).varRight);	
				}
			}
			if( scropG <= scropL){
				if( Vv <= scropL && Vv >= scropG){
					distance = 0;
				}
				else if( Vv <= scropL && Vv <= scropG){
					distance = normalization(scropG) - Vv;
				}
				else if( Vv >= scropL && Vv >= scropG){
					distance = Vv - normalization(scropL);
				}
				else{
					System.out.println("#################### {[Warning][Impossible situation!][VarCalMethods]} ####################");
					return -1;
				}
			}
			else {
				if( Vv >= scropG || Vv <= scropL){
					distance = 0;
				}
				else {
					double t1 = normalization(scropG) - Vv;
					double t2 = Vv - normalization(scropL);
					if( t1 >= t2){
						distance = t2;
					}
					else {
						distance = t1;
					}
				}
			}
			
			
		}
		else {
			System.out.println("#################### {[Warning][Unsolved situation 2!][VarCalMethods]} ####################");
			return -1;
		}
		
		
		return distance;
	}
	
	
	public static double variableCalculateConstraintDistance(ValueSet Vv, ValueSet BPc){
		
		List<ValueSetConstraint> BPCList_v = new ArrayList<ValueSetConstraint>();
		List<ValueSetConstraint> BPCList_bp = new ArrayList<ValueSetConstraint>();
		
		int l_v = Vv.getValueSet().size();
		int l_bp = BPc.getValueSet().size();
		
		for( int i = 0; i < l_v; i ++){
			String[] srcs = Vv.getValueSet().get(i).split(" ");
			ValueSetConstraint temp = new ValueSetConstraint();
			temp.varLeft = srcs[0];
			temp.operator = srcs[1];
			temp.varRight = srcs[2];
			BPCList_v.add(temp);
		}
		for( int i = 0; i < l_bp; i ++){
			String[] srcs = BPc.getValueSet().get(i).split(" ");
			ValueSetConstraint temp = new ValueSetConstraint();
			temp.varLeft = srcs[0];
			temp.operator = srcs[1];
			temp.varRight = srcs[2];
			BPCList_bp.add(temp);
		}
		
		int numOfLessThan_v = 0;
		int numOfGreaterThan_v = 0;
		int numOfEqualTo_v = 0;
		int numOfLessThan_bp = 0;
		int numOfGreaterThan_bp = 0;
		int numOfEqualTo_bp = 0;
		
		for( int i = 0; i < l_v; i ++){
			if( BPCList_v.get(i).operator.equals("==")){
				numOfEqualTo_v ++;
			}
			else if( BPCList_v.get(i).operator.equals("<=") || BPCList_v.get(i).operator.equals("<")){
				numOfLessThan_v ++;
			}
			else if( BPCList_v.get(i).operator.equals(">=") || BPCList_v.get(i).operator.equals(">")){
				numOfGreaterThan_v ++;
			}
			else {
				System.out.println("#################### {[Error][Wrong operator1!][VarCalMethods]} ####################");
				return -1;
			}
		}
		for( int i = 0; i < l_bp; i ++){
			if( BPCList_bp.get(i).operator.equals("==")){
				numOfEqualTo_bp ++;
			}
			else if( BPCList_bp.get(i).operator.equals("<=") || BPCList_bp.get(i).operator.equals("<")){
				numOfLessThan_bp ++;
			}
			else if( BPCList_bp.get(i).operator.equals(">=") || BPCList_bp.get(i).operator.equals(">")){
				numOfGreaterThan_bp ++;
			}
			else {
				System.out.println("#################### {[Error][Wrong operator2!][VarCalMethods]} ####################");
				return -1;
			}
		}
		
		
		
		if( ( numOfLessThan_v >= 2) || ( numOfGreaterThan_v >= 2) || ( numOfLessThan_bp >= 2)
				|| ( numOfGreaterThan_bp >= 2)){
			System.out.println("#################### {[Warning][Unsolved situation 3!][VarCalMethods]} ####################");
			return -1;
		}
		if( l_v != l_bp){
			return 1;
			
		}
		
		double distance = 0;
		
		
		if( numOfLessThan_v == numOfLessThan_bp && numOfGreaterThan_v == numOfGreaterThan_bp){
			if( numOfLessThan_v == 0 && numOfGreaterThan_v == 0){
				if( numOfEqualTo_v == 1 && numOfEqualTo_bp == 1){
					if( Double.parseDouble( BPCList_v.get(0).varRight)
							== Double.parseDouble( BPCList_bp.get(0).varRight)){
						distance = 0;
					}
					else {
						distance = 1;
					}
				}
				else {
					System.out.println("#################### {[Warning][Unsolved situation 5!][VarCalMethods]} ####################");
					return -1;
				}
			}
			else if( numOfLessThan_v == 1 && numOfGreaterThan_v == 0){
				double scrop_v = 0;
				double scrop_bp = 0;
				for( int i = 0; i < l_v; i ++){
					if( BPCList_v.get(i).operator.equals("<") || BPCList_v.get(i).operator.equals("<=")){
						scrop_v = Double.parseDouble( BPCList_v.get(i).varRight);
					}
				}
				for( int i = 0; i < l_bp; i ++){
					if( BPCList_bp.get(i).operator.equals("<") || BPCList_bp.get(i).operator.equals("<=")){
						scrop_bp = Double.parseDouble( BPCList_bp.get(i).varRight);
					}
				}
				if( scrop_v == scrop_bp){
					distance = 0;
				}
				else {
					distance = 1;
				}
			}
			else if( numOfLessThan_v == 0 && numOfGreaterThan_v == 1){
				double scrop_v = 0;
				double scrop_bp = 0;
				for( int i = 0; i < l_v; i ++){
					if( BPCList_v.get(i).operator.equals(">") || BPCList_v.get(i).operator.equals(">=")){
						scrop_v = Double.parseDouble( BPCList_v.get(i).varRight);
					}
				}
				for( int i = 0; i < l_bp; i ++){
					if( BPCList_bp.get(i).operator.equals(">") || BPCList_bp.get(i).operator.equals(">=")){
						scrop_bp = Double.parseDouble( BPCList_bp.get(i).varRight);
					}
				}
				if( scrop_v == scrop_bp){
					distance = 0;
				}
				else {
					distance = 1;
				}
			}
			else if( numOfLessThan_v == 1 && numOfGreaterThan_v == 1){
				double scropG_v = 0;
				double scropL_v = 0;
				double scropG_bp = 0;
				double scropL_bp = 0;
				for( int i = 0; i < l_v; i ++){
					if( BPCList_v.get(i).operator.equals("<") || BPCList_v.get(i).operator.equals("<=")){
						scropL_v = Double.parseDouble( BPCList_v.get(i).varRight);
					}
					if( BPCList_v.get(i).operator.equals(">") || BPCList_v.get(i).operator.equals(">=")){
						scropG_v = Double.parseDouble( BPCList_v.get(i).varRight);
					}
					
				}
				for( int i = 0; i < l_bp; i ++){
					if( BPCList_bp.get(i).operator.equals("<") || BPCList_bp.get(i).operator.equals("<=")){
						scropL_bp = Double.parseDouble( BPCList_bp.get(i).varRight);
					}
					if( BPCList_bp.get(i).operator.equals(">") || BPCList_bp.get(i).operator.equals(">=")){
						scropG_bp = Double.parseDouble( BPCList_bp.get(i).varRight);
					}
				}
				if( scropL_v == scropL_bp && scropG_v == scropG_bp){
					distance = 0;
				}
				else {
					distance = 1;
				}
			}
			else {
				System.out.println("#################### {[Warning][Unsolved situation 6!][VarCalMethods]} ####################");
				return -1;
			}
			
			
		}
		else {
			System.out.println("#################### {[Warning][Unsolved situation 7!][VarCalMethods]} ####################");
			return -1;
		}
		
		
		return distance;
	}
	
	
	
	public static double normalization(double x){
		double nor = x/(x+1);
		return nor;
	}
	public static double max_min_normalization(double x, double min, double max){
		double nor = (x-min)/(max-min);
		return nor;
	}
	
}
