package net._5tingr4y.test.logger;

import net._5tingr4y.logger.DefaultLoggerWindow;
import net._5tingr4y.logger.Logger;

public class LoggerTest1 {
	
	private final int WAITTIME = 1000;
	
	@SuppressWarnings("null")
	private LoggerTest1() throws InterruptedException {
		Logger.catchUncaughtExceptions(true);
		Logger.catchSysOut(true);
		Logger.catchSysErr(true);
		Logger.setLoggerWindow(new DefaultLoggerWindow("Logger: Test"));
		Logger.setLogFile("testLog.log");
		Logger.startLogging();
		Logger.log(this, Logger.DEBUG, "test debug");
		Thread.sleep(WAITTIME);
		Logger.log(this, Logger.INFO, "test information");
		Thread.sleep(WAITTIME);
		Logger.log(this, Logger.WARN, "test warning");
		Thread.sleep(WAITTIME);
		Logger.log(this, Logger.ERROR, "test error");
		Thread.sleep(WAITTIME);
		System.out.println("test sysout 1");
		Thread.sleep(WAITTIME);
		System.err.println("test syserr 1");
		Thread.sleep(WAITTIME);
		Logger.catchSysOut(false);
		Logger.catchSysErr(false);
		Thread.sleep(WAITTIME);
		System.out.println("test sysout 2");
		Thread.sleep(WAITTIME);
		System.err.println("test syserr 2");
		Thread.sleep(WAITTIME);
		Logger.log(Logger.SYSOUT, "test sysout 3");
		Thread.sleep(WAITTIME);
		Logger.log(Logger.SYSERR, "test syserr 3");
		Thread.sleep(WAITTIME);
		Logger.catchSysOut(true);
		Logger.catchSysErr(true);
		Logger.stopLogging();
		System.out.println("test sysout 4");
		Thread.sleep(WAITTIME);
		System.err.println("test syserr 4");
		Thread.sleep(WAITTIME);
		Logger.startLogging();
		Logger.mute(Logger.DEBUG);
		Logger.log(this, Logger.DEBUG, "test mute 1");
		Thread.sleep(WAITTIME);
		Logger.log(this, Logger.INFO, "test mute 2");
		Thread.sleep(WAITTIME);
		try {
			throw new Exception("test exception");
		} catch (Exception e) {
			Logger.log(e);
		}
		String test = null;
		Thread.sleep(WAITTIME);
		test.length();
	}
	
	public static void main(String[] args) {
		try {
			new LoggerTest1();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
