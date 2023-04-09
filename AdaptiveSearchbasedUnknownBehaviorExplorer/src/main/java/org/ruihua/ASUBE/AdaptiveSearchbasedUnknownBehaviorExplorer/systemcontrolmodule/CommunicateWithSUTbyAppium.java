package org.ruihua.ASUBE.AdaptiveSearchbasedUnknownBehaviorExplorer.systemcontrolmodule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;

public class CommunicateWithSUTbyAppium {
	
	public String app;
	
	HashMap<String, String> CommonDeviceSetting;
	
	public CommunicateWithSUTbyAppium() {
		this.app = "jitsi apk location";
		this.CommonDeviceSetting = new HashMap<String, String>();
		this.CommonDeviceSetting.put("unicodeKeyboard", "True");
		this.CommonDeviceSetting.put("resetKeyboard", "True");
		this.CommonDeviceSetting.put("noReset", "True");
		this.CommonDeviceSetting.put("newCommandTimeout", "1800");
	}
	
	public CommunicateWithSUTbyAppium(String appLocation) {
		this.app = appLocation;
		this.CommonDeviceSetting = new HashMap<String, String>();
		this.CommonDeviceSetting.put("unicodeKeyboard", "True");
		this.CommonDeviceSetting.put("resetKeyboard", "True");
		this.CommonDeviceSetting.put("noReset", "True");
		this.CommonDeviceSetting.put("newCommandTimeout", "1800");
	}
	
	public AndroidDriver setupSingleAppiumConnectionWithApp(HashMap<String, String> device, String appLocation) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		String DriverUrl = null;
		for(Map.Entry<String, String> entry: device.entrySet() ) {
			System.out.println("" + entry.getKey() + ", " + entry.getValue());
			if(entry.getKey().equals("DriverUrl")) {
				DriverUrl = entry.getValue();
			}
			else {
				capabilities.setCapability(entry.getKey(), entry.getValue());
			}
		}
		capabilities.setCapability("app", appLocation);
		for(Map.Entry<String, String> entry: this.CommonDeviceSetting.entrySet() ) {
			System.out.println("" + entry.getKey() + ", " + entry.getValue());
			capabilities.setCapability(entry.getKey(), entry.getValue());
		}
		
		AndroidDriver driver = null;
		try {
			driver = new AndroidDriver(new URL(DriverUrl), capabilities);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		
		return driver;
	}
	
	//SCH-N719, G750T01
	public void startVideoConference_A(AndroidDriver driver, int conferenceID) {
		WebElement focus = (WebElement)driver.findElementsByClassName("android.widget.EditText").get(0);
		focus.sendKeys("" + conferenceID);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		focus = (WebElement)driver.findElementsByClassName("android.view.View").get(2);
		focus.click();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//Che-TL00M, G621-TL00 (Android Platform version related)
	public void startVideoConference_B(AndroidDriver driver, int conferenceID) {
		WebElement focus = (WebElement)driver.findElementsByClassName("android.widget.EditText").get(0);
		focus.sendKeys("" + conferenceID);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		focus = (WebElement)driver.findElementsByClassName("android.view.View").get(5);
		focus.click();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void shutDownVoice(AndroidDriver driver) {
		int screen_x = driver.manage().window().getSize().width;
		int screen_y = driver.manage().window().getSize().height;
		
		TouchAction tAction = new TouchAction(driver);
		tAction.longPress(PointOption.point(screen_x/2, screen_y/2)).release().perform();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		WebElement focus = null;
		List<WebElement> listEle = driver.findElementsByClassName("android.view.View");
		if(listEle.size() < 10) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			tAction.longPress(PointOption.point(screen_x/2, screen_y/2)).release().perform();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			focus = (WebElement)driver.findElementsByClassName("android.view.View").get(9);
		}
		else {
			focus = listEle.get(9);
		}
		
		
		focus.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void shutDownVideoCall(AndroidDriver driver) {
		int screen_x = driver.manage().window().getSize().width;
		int screen_y = driver.manage().window().getSize().height;
		
		TouchAction tAction = new TouchAction(driver);
		tAction.longPress(PointOption.point(screen_x/2, screen_y/2)).release().perform();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement focus = (WebElement)driver.findElementsByClassName("android.view.View").get(10);
		focus.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	public void shutDownLocalVideo(AndroidDriver driver) {
		int screen_x = driver.manage().window().getSize().width;
		int screen_y = driver.manage().window().getSize().height;
		
		TouchAction tAction = new TouchAction(driver);
		tAction.longPress(PointOption.point(screen_x/2, screen_y/2)).release().perform();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement focus = (WebElement)driver.findElementsByClassName("android.view.View").get(11);
		focus.click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
	
	
	public void takeScreenShot(AndroidDriver driver, String picName) {
		File srcFile = driver.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File("" + picName + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void takeScreenPageSource(AndroidDriver driver, String layoutFileName) {
		String pageSource = driver.getPageSource();
		File file = new File("" + layoutFileName + ".xml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		FileWriter fileWriter = null;
		BufferedWriter bufferWriter = null;
		try {
			fileWriter = new FileWriter("" + layoutFileName + ".xml", true);
			bufferWriter = new BufferedWriter(fileWriter);
			bufferWriter.write(pageSource);
			bufferWriter.flush();
			bufferWriter.close();
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void resetApp(List<AndroidDriver> drivers) {
		if(drivers != null) {
			int l = drivers.size();
			for(int i = 0; i < l; i ++) {
				try {
					drivers.get(i).quit();
				} catch(Exception e) {System.out.println("----- Exception occurs when driver NO." + i + "quits -----");}
			}
		}
	}
	
	
	//===================
	
	
	
	
}
