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

public class Logger {
	
	private static final Logger instance = new Logger();
	
	private boolean running = false;
	
	private UncaughtExceptionHandler logger_unc;
	
	private ByteArrayOutputStream sys_out;
	private ByteArrayOutputStream sys_err;
	
	private PrintStream logger_out;
	private PrintStream logger_err;
	private PrintStream logger_file;
	
	private LoggerWindow window = null;
	
	private HashSet<Class<? extends AbstractMessage>> mutedTypes = new HashSet<>();
	
	private Hashtable<Class<? extends AbstractMessage>, Color> messageColors; //TODO
	
	static {
		instance.messageColors = new Hashtable<>();
		
		instance.messageColors.put(SysOutMessage.class, new Color(0, 0, 0));
		instance.messageColors.put(SysErrMessage.class, new Color(255, 0, 0));
		
		instance.messageColors.put(DebugMessage.class, new Color(100, 100, 150));
		instance.messageColors.put(InfoMessage.class, new Color(0, 100, 0));
		instance.messageColors.put(WarningMessage.class, new Color(255, 100, 0));
		instance.messageColors.put(ErrorMessage.class, new Color(200, 50, 50));
		instance.messageColors.put(ExceptionMessage.class, new Color(200, 0, 0));
		instance.messageColors.put(UncaughtExceptionMessage.class, new Color(100, 0, 0));
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
	
	public static LoggerWindow getWindow() {
		return instance.window;
	}
	
	public static Color getColor(AbstractMessage message) {
		return instance.messageColors.getOrDefault(message.getClass(), message.isError ? Color.RED : Color.BLACK);
	}
	
	//setters
	public static void catchUncaughtExceptions(boolean b) {
		if (b) {
			if (Thread.getDefaultUncaughtExceptionHandler() != instance.logger_unc) return;
			instance.logger_unc = (Thread t, Throwable e) -> new UncaughtExceptionMessage(t, e);
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
			new ExceptionMessage(e);
		}
	}
	
	public static void unsetLogFile() {
		instance.logger_file.close();
		instance.logger_file = null;
	}
	
	public static void mute(Class<? extends AbstractMessage> type) {
		instance.mutedTypes.add(type);
	}
	
	public static void unmute(Class<? extends AbstractMessage> type) {
		instance.mutedTypes.remove(type);
	}
	
	public static void setTagMaxLength(int length) {
		AbstractMessage.MAX_TAG_LENGTH = Math.max(1, length);
	}
	
	public static LoggerWindow setLoggerWindow(LoggerWindow window_) {
		LoggerWindow buf = instance.window;
		instance.window = window_;
		return buf;
	}
	
	public static Color setColor(Class<? extends AbstractMessage> msgType, Color color) {
		if(msgType == null) return null;
		Color previous = instance.messageColors.get(msgType);
		if(color != null)
			instance.messageColors.put(msgType, color);
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
	static void log(AbstractMessage msg) {
		if(!instance.running || instance.mutedTypes.contains(msg.getClass())) return;
		String text = msg.toString();
		if (msg.isError) {
			instance.logger_err.println(text);
		} else {
			instance.logger_out.println(text);
		}
		if (instance.logger_file != null)
			instance.logger_file.println(text);
		if (instance.window != null)
			instance.window.addMessage(getColor(msg), text);
	}
}
