package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.systemcontrolmodule;

import java.io.IOException;
import java.io.InputStream;

public class NetworkConditionController {
	
	
	
	public NetworkConditionController(){
		
	}
	
	//Warning. Before this, we need to create the network file first(use 'add', not 'change')
	public static void initialLocalEquipmentNetworkEnvironment(){
		String command = "echo \"'\" | sudo -S tc qdisc change dev enp0s31f6 root netem loss 0% delay 0ms duplicate 0% corrupt 0%";
		
		System.out.println(command);
		try {
			
			String[] commands = {"/bin/sh", "-c", command};
			Process child = Runtime.getRuntime().exec(commands);
			InputStream in = child.getInputStream();
			int c;
			while( ( c = in.read()) != -1){
				System.out.print( (char)c);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void set_local_equipment_network_environment( double packetLoss, double packetDelay, double packetDuplication, double packetCorruption){
		
		String command = "echo \"'\" | sudo -S tc qdisc change dev enp0s31f6 root netem loss " + packetLoss + 
				"% delay " + packetDelay + 
				"ms duplicate " + packetDuplication + 
				"% corrupt " + packetCorruption +
				"%";
		System.out.println(command);
		try {
			
			String[] commands = {"/bin/sh", "-c", command};
			Process child = Runtime.getRuntime().exec(commands);
			InputStream in = child.getInputStream();
			int c;
			while( ( c = in.read()) != -1){
				System.out.print( (char)c);
			}
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	
	
	public static void main(String[] args){
		System.out.print("Test");
		//NetworkEnvironmentController.initialLocalEquipmentNetworkEnvironment();
		
		
	}
}
