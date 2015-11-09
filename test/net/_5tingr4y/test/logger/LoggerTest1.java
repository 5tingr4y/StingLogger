package net._5tingr4y.test.logger;

import net._5tingr4y.logger.DebugMessage;
import net._5tingr4y.logger.DefaultLoggerWindow;
import net._5tingr4y.logger.ErrorMessage;
import net._5tingr4y.logger.ExceptionMessage;
import net._5tingr4y.logger.InfoMessage;
import net._5tingr4y.logger.Logger;
import net._5tingr4y.logger.SysErrMessage;
import net._5tingr4y.logger.SysOutMessage;
import net._5tingr4y.logger.WarningMessage;

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
		new DebugMessage(this, "test debug");
		Thread.sleep(WAITTIME);
		new InfoMessage(this, "test information");
		Thread.sleep(WAITTIME);
		new WarningMessage(this, "test warning");
		Thread.sleep(WAITTIME);
		new ErrorMessage(this, "test error");
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
		new SysOutMessage("test sysout 3");
		Thread.sleep(WAITTIME);
		new SysErrMessage("test syserr 3");
		Thread.sleep(WAITTIME);
		Logger.catchSysOut(true);
		Logger.catchSysErr(true);
		Logger.stopLogging();
		System.out.println("test sysout 4");
		Thread.sleep(WAITTIME);
		System.err.println("test syserr 4");
		Thread.sleep(WAITTIME);
		Logger.startLogging();
		Logger.mute(DebugMessage.class);
		new DebugMessage(this, "test mute 1");
		Thread.sleep(WAITTIME);
		new InfoMessage(this, "test mute 2");
		Thread.sleep(WAITTIME);
		try {
			throw new Exception("test exception");
		} catch (Exception e) {
			new ExceptionMessage(e);
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
