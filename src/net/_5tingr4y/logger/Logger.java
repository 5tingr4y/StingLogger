package net._5tingr4y.logger;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashSet;
import java.util.Hashtable;

import net._5tingr4y.logger.messages.AbstractMessage;
import net._5tingr4y.logger.messages.DebugMessage;
import net._5tingr4y.logger.messages.ErrorMessage;
import net._5tingr4y.logger.messages.ExceptionMessage;
import net._5tingr4y.logger.messages.InfoMessage;
import net._5tingr4y.logger.messages.SysErrMessage;
import net._5tingr4y.logger.messages.SysOutMessage;
import net._5tingr4y.logger.messages.UncaughtExceptionMessage;
import net._5tingr4y.logger.messages.WarningMessage;

public class Logger {
	
	public static final String SYSOUT = "sysout";
	public static final String SYSERR = "syserr";
	public static final String DEBUG = "debug";
	public static final String INFO = "info";
	public static final String WARN = "warn";
	public static final String ERROR = "error";
	public static final String EXCEPT = "except";
	public static final String UNCEXC = "uncexc";
	
	
	private static final Logger instance = new Logger();
	
	private static int maxTagLength = 6;
	
	private boolean running = false;
	
	private UncaughtExceptionHandler logger_unc;
	
	private ByteArrayOutputStream sys_out;
	private ByteArrayOutputStream sys_err;
	
	private PrintStream logger_out;
	private PrintStream logger_err;
	private PrintStream logger_file;
	
	private LoggerWindow window = null;
	
//	private HashSet<Class<? extends AbstractMessage>> mutedTypes = new HashSet<>();
	
//	private Hashtable<Class<? extends AbstractMessage>, Color> messageColors; //TODO
	
	private HashSet<String> mutedTypes = new HashSet<>();
	
	private Hashtable<String, Color> messageColors = new Hashtable<>();
	
	private Hashtable<String, AbstractMessage> messages = new Hashtable<>();
	
	static {
		//messages
		instance.messages.put(SYSOUT, new SysOutMessage());
		instance.messages.put(SYSERR, new SysErrMessage());
		
		instance.messages.put(DEBUG, new DebugMessage());
		instance.messages.put(INFO, new InfoMessage());
		instance.messages.put(WARN, new WarningMessage());
		instance.messages.put(ERROR, new ErrorMessage());
		instance.messages.put(EXCEPT, new ExceptionMessage());
		instance.messages.put(UNCEXC, new UncaughtExceptionMessage());
		
		
		//message colors
		instance.messageColors.put(SYSOUT, new Color(0, 0, 0));
		instance.messageColors.put(SYSERR, new Color(255, 0, 0));
		
		instance.messageColors.put(DEBUG, new Color(100, 100, 150));
		instance.messageColors.put(INFO, new Color(0, 100, 0));
		instance.messageColors.put(WARN, new Color(255, 100, 0));
		instance.messageColors.put(ERROR, new Color(200, 50, 50));
		instance.messageColors.put(EXCEPT, new Color(200, 0, 0));
		instance.messageColors.put(UNCEXC, new Color(100, 0, 0));
	}
	
	private Logger() {
		logger_unc = Thread.getDefaultUncaughtExceptionHandler();
		logger_out = System.out;// new PrintStream(new FileOutputStream(FileDescriptor.out));
		logger_err = System.err; //new PrintStream(new FileOutputStream(FileDescriptor.err));
	}
	
	//getters
	public static boolean isRunning() {
		return instance.running;
	}
	
	public static int getMaxTagLength() {
		return maxTagLength;
	}
	
	public static LoggerWindow getWindow() {
		return instance.window;
	}
	
	public static Color getColor(String level) {
		AbstractMessage message = instance.messages.get(level);
		return instance.messageColors.getOrDefault(level, message == null ? Color.DARK_GRAY : message.isError ? Color.RED : Color.BLACK);
	}
	
	//setters
	public static void catchUncaughtExceptions(boolean b) {
		if (b) {
			if (Thread.getDefaultUncaughtExceptionHandler() != instance.logger_unc) return;
//			instance.logger_unc = (Thread t, Throwable e) -> new UncaughtExceptionMessage(t, e);
			instance.logger_unc = (Thread t, Throwable e) -> log("<Thread: " + t.getName() + "> - " + e.getClass().getName(), UNCEXC, AbstractMessage.formatThrowable(e));
			if (instance.running) Thread.setDefaultUncaughtExceptionHandler(instance.logger_unc);
		} else {
			Thread.setDefaultUncaughtExceptionHandler(instance.logger_unc);
		}
	}
	
	/**
	 * If this is set to true, the logger will catch sysout messages (sent via {@code System.out}).
	 * This means that sysout messages will be converted to conform the standard logger message form.
	 * 
	 * <p>If this is set to false, the logger will not catch sysout messages.
	 * This means that the output given to sysout will be logged unedited.
	 * @param b
	 */
	public static void catchSysOut(boolean b) {
		if (b) {
			if (instance.sys_out != null) return;
			instance.sys_out = new LoggerOutputStream();// ByteArrayOutputStream();
			if (instance.running) System.setOut(new PrintStream(instance.sys_out));	
		} else {
			instance.sys_out = null;
			System.setOut(instance.logger_out);
		}
	}
	
	public static void catchSysErr(boolean b) {
		if (b) {
			if (instance.sys_err != null) return;
			instance.sys_err = new LoggerErrorStream(); //ByteArrayOutputStream();
			if (instance.running) System.setErr(new PrintStream(instance.sys_err));	
		} else {
			instance.sys_err = null;
			System.setErr(instance.logger_err);
		}
	}
	
	public static void setLogFile(String fileName) {
		if (instance.logger_file != null)
			unsetLogFile();
		try {
			File file = new File(fileName);
			File folder = file.getParentFile();
			if (folder != null) folder.mkdirs();
			instance.logger_file = new PrintStream(file);
		} catch (FileNotFoundException e) {
//			new ExceptionMessage(e);
			if(instance.running)
				log(e);
			else
				e.printStackTrace();
		}
	}
	
	public static void unsetLogFile() {
		instance.logger_file.close();
		instance.logger_file = null;
	}
	
	public static void mute(String level) {
		instance.mutedTypes.add(level);
	}
	
	public static void unmute(String level) {
		instance.mutedTypes.remove(level);
	}
	
//	public static void setTagMaxLength(int length) {
//		MAX_TAG_LENGTH = Math.max(1, length);
//	}
	
	public static LoggerWindow setLoggerWindow(LoggerWindow window_) {
		LoggerWindow buf = instance.window;
		instance.window = window_;
		return buf;
	}
	
	public static Color setColor(String level, Color color) {
		if(level == null) return null;
		Color previous = instance.messageColors.get(level);
		if(color != null)
			instance.messageColors.put(level, color);
		return previous;
	}
	
	//starting/stopping
	public static void startLogging() {
		if(instance.running) return;
		//TODO: incatcher
		if(instance.sys_out != null) System.setOut(new PrintStream(instance.sys_out));
		if(instance.sys_err != null) System.setErr(new PrintStream(instance.sys_err));
		if(instance.logger_unc != null) Thread.setDefaultUncaughtExceptionHandler(instance.logger_unc);
		instance.running = true;
	}
	
	public static void stopLogging() {
		if(!instance.running) return;
		instance.running = false;
		System.setOut(instance.logger_out);
		System.setErr(instance.logger_err);
		Thread.setDefaultUncaughtExceptionHandler(instance.logger_unc);
	}
	
	//msg management	
	public static void log(Throwable e) {
		log(e.getClass().getName(), EXCEPT, AbstractMessage.formatThrowable(e));
	}
	
	public static void log(String level, String message) {
		log("", level, message);
	}
	
	public static void log(Object sender, String level, String message) {
		log(sender.getClass().getName(), level, message);
	}
	
	public static void log(String sender, String level, String message) {
		if(sender == null || level == null || message == null) {
			//TODO: add LoggerMessage and use that here (to also log the message)
			log("Logger", WARN, "Given invalid Logger input:\nSender: " + sender + "\nLevel: " + level + "\nMessage: " + message);
			return;
		}
		
		if(!instance.running || instance.mutedTypes.contains(level)) return;
		AbstractMessage msg = instance.messages.get(level);
		
		if(msg == null) {
			//TODO: add LoggerMessage and use that here (to also log the message)
			log("Logger", WARN, "Level \"" + level + "\" does not exist; unable to log message");
		}
		
		String text = msg.formatMessage(sender, message);
		if (msg.isError) {
			instance.logger_err.println(text);
		} else {
			instance.logger_out.println(text);
		}
		if (instance.logger_file != null)
			instance.logger_file.println(text);
		if (instance.window != null)
			instance.window.addMessage(getColor(level), text);
	}
}
