package net._5tingr4y.test.logger;

import java.awt.Color;

import net._5tingr4y.logger.DefaultLoggerWindow;
import net._5tingr4y.logger.Logger;
import net._5tingr4y.logger.Message;

public class LoggerTest2 {
	
	private final int WAITTIME = 1000;
	
	private LoggerTest2() throws InterruptedException {
		Logger.catchUncaughtExceptions(true);
		Logger.catchSysOut(true);
		Logger.catchSysErr(true);
		Logger.setLoggerWindow(new DefaultLoggerWindow("Logger: Test"));
		Logger.setLogFile("test/testLog2.log");
		Logger.startLogging();
		
		Logger.log(this, Logger.INFO, "Starting Test");
		Thread.sleep(WAITTIME);
		
		Logger.addLevel("Test", new Message("Test00", Logger.PATTERN_LOGGER, false));
		Logger.log(this, "Test", "Test Add");
		Thread.sleep(WAITTIME);
		
		Logger.setColor("Test", new Color(0, 0, 255));
		Logger.setColor(Logger.SYSOUT, new Color(0, 200, 200));
		Logger.log(this, "Test", "Test SetColor 1");
		Thread.sleep(WAITTIME);
		System.out.println("Test SetColor 2");
		Thread.sleep(WAITTIME);
		
		Logger.removeLevel(Logger.DEBUG);
		Logger.log(this, Logger.DEBUG, "Test Remove");
		Thread.sleep(WAITTIME);
		
		Logger.addLevel("Long", new Message("Testlong", Logger.PATTERN_LOGGER, false));
		Logger.log(this, "Long", "Test Long");
		Thread.sleep(WAITTIME);
		Logger.log(this, "Test", "Test Long 2");
		Thread.sleep(WAITTIME);
		
		Logger.removeLevel("Long");
		Logger.log(this, Logger.INFO, "Test shorten");
	}
	
	public static void main(String[] args) {
		try {
			new LoggerTest2();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
