package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.systemcontrolmodule;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.model.TestCase.*;
import org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.systemcontrolmodule.SVMetrics.SVMetrics;

import io.appium.java_client.android.AndroidDriver;

public class TestCaseElement2AppiumScripts {
	
	private CommunicateWithSUTbyAppium ComSUT = null;
	private SVMetrics svm = null;
	
	
	
	
	public TestCaseElement2AppiumScripts(){
		this.ComSUT = new CommunicateWithSUTbyAppium();
		this.svm = new SVMetrics();
	}
	
	public CommunicateWithSUTbyAppium getCommunicateWithSUT(){
		return this.ComSUT;
	}
	public SVMetrics getSVMetrics() {
		return this.svm;
	}
	
	
	public List<AndroidDriver> setUpAppiumDriver(HashMap<String, String> host_device_0, HashMap<String, String> client_device_1, HashMap<String, String> client_device_2, HashMap<String, String> client_device_3) {
		AndroidDriver t0 = this.ComSUT.setupSingleAppiumConnectionWithApp(host_device_0, "jitsi apk location");
		AndroidDriver t1 = this.ComSUT.setupSingleAppiumConnectionWithApp(client_device_1, "jitsi apk location");
		AndroidDriver t2 = this.ComSUT.setupSingleAppiumConnectionWithApp(client_device_2, "jitsi apk location");
		AndroidDriver t3 = this.ComSUT.setupSingleAppiumConnectionWithApp(client_device_3, "jitsi apk location");
		List<AndroidDriver> r = new ArrayList<AndroidDriver>();
		r.add(t0); r.add(t1); r.add(t2); r.add(t3);
		return r;
	}
	
	public void setUpIdleState(AndroidDriver host_driver_0, String deviceName, int conferenceID) {
		System.out.println("=====--------------- In setUpIdleState Func, start. ---------------=====");
		
		System.out.println("[//////////]{[Dial command in appium script. (setUpIdleState)]}");
		if(deviceName.equals("SCH-N719") || deviceName.equals("G750T01")) {
			System.out.println("[//////////]{[deviceName][" + deviceName + "]}");
			this.ComSUT.startVideoConference_A(host_driver_0, conferenceID);
		}
		else {
			System.out.println("[//////////]{[deviceName][" + deviceName + "]}");
			this.ComSUT.startVideoConference_B(host_driver_0, conferenceID);
		}
		
		System.out.println("=====--------------- End of setUpIdleState Func. ---------------=====");
	}
	
	public void setUpBenchmarkforAll(HashMap<String, String> host_device_0, HashMap<String, String> client_device_1, HashMap<String, String> client_device_2, HashMap<String, String> client_device_3, int conferenceID, String executeResultFile) {
		
		System.out.println("=====--------------- In setUpBenchmarkforAll Func, start. ---------------=====");
		System.out.println("[//////////]{[Set up drivers (Benchmark)]}");
		List<AndroidDriver> drivers = null;
		
		int try_label = 0;
		boolean willTry = false;
		do {
		
		try {
		drivers = setUpAppiumDriver(host_device_0, client_device_1, client_device_2, client_device_3);
		
		//First, set up the Idle state (the host or the first participate of the video conference)
		System.out.println("-------------------- Dial to set up Idle state (Benchmark:nullTo0) --------------------");
		if(host_device_0.get("deviceName").equals("SCH-N719") || host_device_0.get("deviceName").equals("G750T01")) {
			System.out.println("[//////////]{[deviceName][" + host_device_0.get("deviceName") + "]}");
			this.ComSUT.startVideoConference_A(drivers.get(0), conferenceID);
		}
		else {
			System.out.println("[//////////]{[deviceName][" + host_device_0.get("deviceName") + "]}");
			this.ComSUT.startVideoConference_B(drivers.get(0), conferenceID);
		}
		System.out.println("[//////////]{[Wait dial command result. (Benchmark:nullTo0)]}");
		Thread.sleep(15000);
		System.out.println("[//////////]{[Take the screenshot. (Benchmark:nullTo0)]}");
		this.ComSUT.takeScreenShot(drivers.get(0), executeResultFile + "_AC_0");
		System.out.println("[//////////]{[Take the screen source. (Benchmark:nullTo0)]}");
		this.ComSUT.takeScreenPageSource(drivers.get(0), executeResultFile + "_AC_0");
		Thread.sleep(1000);
		
		System.out.println("-------------------- Dial AC 0To1 (Benchmark:0To1) --------------------");
		if(client_device_1.get("deviceName").equals("SCH-N719") || client_device_1.get("deviceName").equals("G750T01")) {
			System.out.println("[//////////]{[deviceName][" + client_device_1.get("deviceName") + "]}");
			this.ComSUT.startVideoConference_A(drivers.get(1), conferenceID);
		}
		else {
			System.out.println("[//////////]{[deviceName][" + client_device_1.get("deviceName") + "]}");
			this.ComSUT.startVideoConference_B(drivers.get(1), conferenceID);
		}
		System.out.println("[//////////]{[Wait dial command result. (Benchmark:0To1)]}");
		Thread.sleep(15000);
		System.out.println("[//////////]{[Take the screenshot. (Benchmark:0To1)]}");
		this.ComSUT.takeScreenShot(drivers.get(0), executeResultFile + "_AC_1");
		System.out.println("[//////////]{[Take the screen source. (Benchmark:0To1)]}");
		this.ComSUT.takeScreenPageSource(drivers.get(0), executeResultFile + "_AC_1");
		Thread.sleep(1000);
		
		System.out.println("-------------------- Dial AC 1To2 (Benchmark:1To2) --------------------");
		if(client_device_2.get("deviceName").equals("SCH-N719") || client_device_2.get("deviceName").equals("G750T01")) {
			System.out.println("[//////////]{[deviceName][" + client_device_2.get("deviceName") + "]}");
			this.ComSUT.startVideoConference_A(drivers.get(2), conferenceID);
		}
		else {
			System.out.println("[//////////]{[deviceName][" + client_device_2.get("deviceName") + "]}");
			this.ComSUT.startVideoConference_B(drivers.get(2), conferenceID);
		}
		System.out.println("[//////////]{[Wait dial command result. (Benchmark:1To2)]}");
		Thread.sleep(15000);
		System.out.println("[//////////]{[Take the screenshot. (Benchmark:1To2)]}");
		this.ComSUT.takeScreenShot(drivers.get(0), executeResultFile + "_AC_2");
		System.out.println("[//////////]{[Take the screen source. (Benchmark:1To2)]}");
		this.ComSUT.takeScreenPageSource(drivers.get(0), executeResultFile + "_AC_2");
		Thread.sleep(1000);
		
		System.out.println("-------------------- Dial AC 2To3 (Benchmark:2To3) --------------------");
		if(client_device_3.get("deviceName").equals("SCH-N719") || client_device_3.get("deviceName").equals("G750T01")) {
			System.out.println("[//////////]{[deviceName][" + client_device_3.get("deviceName") + "]}");
			this.ComSUT.startVideoConference_A(drivers.get(3), conferenceID);
		}
		else {
			System.out.println("[//////////]{[deviceName][" + client_device_3.get("deviceName") + "]}");
			this.ComSUT.startVideoConference_B(drivers.get(3), conferenceID);
		}
		System.out.println("[//////////]{[Wait dial command result. (Benchmark:2To3)]}");
		Thread.sleep(15000);
		System.out.println("[//////////]{[Take the screenshot. (Benchmark:2To3)]}");
		this.ComSUT.takeScreenShot(drivers.get(0), executeResultFile + "_AC_3");
		System.out.println("[//////////]{[Take the screen source. (Benchmark:2To3)]}");
		this.ComSUT.takeScreenPageSource(drivers.get(0), executeResultFile + "_AC_3");
		Thread.sleep(1000);
		
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {}
			System.out.println("#################### {[Exception][try label][" + try_label + "]} ####################");
			
			this.ComSUT.resetApp(drivers);//same as Execute Single Test Case Func in the manager_Appium()
			
			if(try_label < 2) {
				try_label ++;
				willTry = true;
			}
			else {
				System.out.println("#################### {[Error when dealing with Exception][Failed Execution (try_label beggier than 2)]} ####################");
			}
		}
		
		
		} while(willTry);//retry when exception occurs (maximum is 3)
		
		this.ComSUT.resetApp(drivers);
		
		
		
		System.out.println("=====--------------- End of setUpBenchmarkforAll Func. ---------------=====");
		
	}
	
	public SystemVariables_0 inputPath_Dial(AndroidDriver host_driver_0, AndroidDriver client_driver, String deviceName, int conferenceID, EdgeVertex ev, String executeResultFile, String benchmarkFile) {
		
		System.out.println("=====--------------- In inputPath_Dial Func, start. ---------------=====");
		
		System.out.println("[//////////]{[Set up Network Environment. (inputPath_Dial)]}");
		System.out.println("[//////////]{[PacketLoss: " + ev.getPacketLoss()
				+ ", PacketDelay: " + ev.getPacketDelay()
				+ ", PacketDuplication: " + ev.getPacketDuplication()
				+ ", PacketCorruption: " + ev.getPacketCorruption() + "]}");
		NetworkConditionController.set_local_equipment_network_environment(ev.PacketLoss, ev.PakcetDelay, ev.PacketDuplication, ev.PacketCorruption);
		System.out.println("[//////////]{[End.]}");
		
		System.out.println("[//////////]{[Dial command in appium script. (inputPath_Dial)]}");
		if(deviceName.equals("SCH-N719") || deviceName.equals("G750T01")) {
			System.out.println("[//////////]{[deviceName][" + deviceName + "]}");
			this.ComSUT.startVideoConference_A(client_driver, conferenceID);
		}
		else {
			System.out.println("[//////////]{[deviceName][" + deviceName + "]}");
			this.ComSUT.startVideoConference_B(client_driver, conferenceID);
		}
		System.out.println("[//////////]{[Wait dial command result. (inputPath_Dial)]}");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("[//////////]{[Cancel the Network Condition setting. (inputPath_Dial)]}");
		NetworkConditionController.initialLocalEquipmentNetworkEnvironment();
		System.out.println("[//////////]{[End.]}");
		
		System.out.println("[//////////]{[Take the screenshot. (inputPath_Dial)]}");
		this.ComSUT.takeScreenShot(host_driver_0, executeResultFile);
		System.out.println("[//////////]{[Take the screen source. (inputPath_Dial)]}");
		this.ComSUT.takeScreenPageSource(host_driver_0, executeResultFile);
		
		
		System.out.println("[//////////]{[Wait the cancelled Network Condition stable. (inputPath_Dial)]}");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("[//////////]{[Apply SV Metrics to calculate]}");
		SystemVariables_0 svTemp = this.svm.getSystemVariableValues(executeResultFile + ".xml", executeResultFile + ".png", benchmarkFile + ".png");
		
		System.out.println("=====--------------- End of inputPath_Dial Func. ---------------=====");
		
		return svTemp;
	}
	
	public SystemVariables_0 inputPath_Disconnect(AndroidDriver host_driver_0, AndroidDriver client_driver, String deviceName, int conferenceID, EdgeVertex ev, String executeResultFile, String benchmarkFile) {
		
		System.out.println("=====--------------- In inputPath_Disconnect Func, start. ---------------=====");
		
		System.out.println("[//////////]{[Set up Network Environment. (inputPath_Disconnect)]}");
		System.out.println("[//////////]{[PacketLoss: " + ev.getPacketLoss()
				+ ", PacketDelay: " + ev.getPacketDelay()
				+ ", PacketDuplication: " + ev.getPacketDuplication()
				+ ", PacketCorruption: " + ev.getPacketCorruption() + "]}");
		NetworkConditionController.set_local_equipment_network_environment(ev.PacketLoss, ev.PakcetDelay, ev.PacketDuplication, ev.PacketCorruption);
		System.out.println("[//////////]{[End.]}");
		
		
		System.out.println("[//////////]{[Disconnect command in appium script. (inputPath_Disconnect)]}");
		this.ComSUT.shutDownVideoCall(client_driver);
		
		System.out.println("[//////////]{[Wait Disconnect command result. (inputPath_Disconnect)]}");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("[//////////]{[Cancel the Network Condition setting. (inputPath_Disconnect)]}");
		NetworkConditionController.initialLocalEquipmentNetworkEnvironment();
		System.out.println("[//////////]{[End.]}");
		
		System.out.println("[//////////]{[Take the screenshot. (inputPath_Disconnect)]}");
		this.ComSUT.takeScreenShot(host_driver_0, executeResultFile);
		System.out.println("[//////////]{[Take the screen source. (inputPath_Disconnect)]}");
		this.ComSUT.takeScreenPageSource(host_driver_0, executeResultFile);
		
		
		System.out.println("[//////////]{[Wait the cancelled Network Condition stable. (inputPath_Disconnect)]}");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("[//////////]{[Apply SV Metrics to calculate]}");
		SystemVariables_0 svTemp = this.svm.getSystemVariableValues(executeResultFile + ".xml", executeResultFile + ".png", benchmarkFile + ".png");
		
		
		System.out.println("=====--------------- End of inputPath_Disconnect Func. ---------------=====");
		
		return svTemp;
	}
	
	public SystemVariables_0 inputPath_Null(AndroidDriver host_driver_0, String deviceName, int conferenceID, EdgeVertex ev, String executeResultFile, String benchmarkFile) {
		
		System.out.println("=====--------------- In inputPath_Null Func, start. ---------------=====");
		
		System.out.println("[//////////]{[Set up Network Environment. (inputPath_Null)]}");
		System.out.println("[//////////]{[PacketLoss: " + ev.getPacketLoss()
				+ ", PacketDelay: " + ev.getPacketDelay()
				+ ", PacketDuplication: " + ev.getPacketDuplication()
				+ ", PacketCorruption: " + ev.getPacketCorruption() + "]}");
		NetworkConditionController.set_local_equipment_network_environment(ev.PacketLoss, ev.PakcetDelay, ev.PacketDuplication, ev.PacketCorruption);
		System.out.println("[//////////]{[End.]}");
		
		System.out.println("[//////////]{[Wait the Network Condition stable. (inputPath_Null)]}");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("[//////////]{[Wait Null command result. (inputPath_Null)]}");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("[//////////]{[Cancel the Network Condition setting. (inputPath_Null)]}");
		NetworkConditionController.initialLocalEquipmentNetworkEnvironment();
		System.out.println("[//////////]{[End.]}");
		
		System.out.println("[//////////]{[Take the screenshot. (inputPath_Null)]}");
		this.ComSUT.takeScreenShot(host_driver_0, executeResultFile);
		System.out.println("[//////////]{[Take the screen source. (inputPath_Null)]}");
		this.ComSUT.takeScreenPageSource(host_driver_0, executeResultFile);
		
		
		System.out.println("[//////////]{[Wait the cancelled Network Condition stable. (inputPath_Null)]}");
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("[//////////]{[Apply SV Metrics to calculate]}");
		SystemVariables_0 svTemp = this.svm.getSystemVariableValues(executeResultFile + ".xml", executeResultFile + ".png", benchmarkFile + ".png");
		
		System.out.println("=====--------------- End of inputPath_Null Func. ---------------=====");
		
		return svTemp;
	}
	
	public void inputPath_ShutDownLocalVideo(AndroidDriver host_driver_0, AndroidDriver client_driver, String deviceName, int conferenceID, EdgeVertex ev, String executeResultFile) {
		
		System.out.println("=====--------------- In inputPath_ShutDownLocalVideo Func, start. ---------------=====");
		
		System.out.println("[//////////]{[Set up Network Environment. (inputPath_ShutDownLocalVideo)]}");
		System.out.println("[//////////]{[PacketLoss: " + ev.getPacketLoss()
				+ ", PacketDelay: " + ev.getPacketDelay()
				+ ", PacketDuplication: " + ev.getPacketDuplication()
				+ ", PacketCorruption: " + ev.getPacketCorruption() + "]}");
		//NetworkConditionController.set_local_equipment_network_environment(ev.PacketLoss, ev.PakcetDelay, ev.PacketDuplication, ev.PacketCorruption);
		System.out.println("[//////////]{[End.]}");
		
		System.out.println("[//////////]{[Wait the Network Condition stable. (inputPath_ShutDownLocalVideo)]}");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("[//////////]{[ShutDownLocalVideo command in appium script. (inputPath_ShutDownLocalVideo)]}");
		this.ComSUT.shutDownLocalVideo(client_driver);
		
		System.out.println("[//////////]{[Wait ShutDownLocalVideo command result. (inputPath_ShutDownLocalVideo)]}");
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("[//////////]{[Take the screenshot. (inputPath_ShutDownLocalVideo)]}");
		this.ComSUT.takeScreenShot(host_driver_0, executeResultFile);
		System.out.println("[//////////]{[Take the screen source. (inputPath_ShutDownLocalVideo)]}");
		this.ComSUT.takeScreenPageSource(host_driver_0, executeResultFile);
		
		System.out.println("[//////////]{[Cancel the Network Condition setting. (inputPath_ShutDownLocalVideo)]}");
		//NetworkConditionController.initialLocalEquipmentNetworkEnvironment();
		System.out.println("[//////////]{[End.]}");
		
		System.out.println("[//////////]{[Wait the cancelled Network Condition stable. (inputPath_ShutDownLocalVideo)]}");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("=====--------------- End of inputPath_ShutDownLocalVideo Func. ---------------=====");
		
	}
	
	//public void 
	
	
	
	
	
	
	public static void main(String[] args){
		
	}
}
