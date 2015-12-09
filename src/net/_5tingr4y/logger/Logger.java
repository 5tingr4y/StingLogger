/*
 * This file is part of StingLogger, licensed under the MIT License (MIT)
 *
 * Copyright (c) 2015 Raymond Kampmann <http://5tingr4y.net/>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net._5tingr4y.logger;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashSet;
import java.util.Hashtable;

public class Logger {
	
	public static final String SYSOUT = "sysout";
	public static final String SYSERR = "syserr";
	public static final String DEBUG = "debug";
	public static final String INFO = "info";
	public static final String WARN = "warn";
	public static final String ERROR = "error";
	public static final String EXCEPT = "except";
	public static final String UNCEXC = "uncexc";
	
	public static final String PATTERN_LOGGER = "[%d{HH:mm:ss} %l]%e %s: %m";
	public static final String PATTERN_SYSTEM = "[%d{HH:mm:ss} %l]%e %m";
	
	
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
	
	private HashSet<String> mutedTypes = new HashSet<>();
	
	private Hashtable<String, Color> messageColors = new Hashtable<>();
	
	private Hashtable<String, Message> messages = new Hashtable<>();
	
	static {
		//messages
		instance.messages.put(SYSOUT, new Message("SYSOUT", PATTERN_SYSTEM, false));
		instance.messages.put(SYSERR, new Message("SYSERR", PATTERN_SYSTEM, false));
		
		instance.messages.put(DEBUG, new Message("DEBUG", PATTERN_LOGGER, false));
		instance.messages.put(INFO, new Message("INFO", PATTERN_LOGGER, false));
		instance.messages.put(WARN, new Message("WARN", PATTERN_LOGGER, false));
		instance.messages.put(ERROR, new Message("ERROR", PATTERN_LOGGER, true));
		instance.messages.put(EXCEPT, new Message("EXCEPT", PATTERN_LOGGER, true));
		instance.messages.put(UNCEXC, new Message("UNCEXC", PATTERN_LOGGER, true));
		
		
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
		logger_out = System.out;
		logger_err = System.err;
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
		Message message = instance.messages.get(level);
		return instance.messageColors.getOrDefault(level, message == null ? Color.DARK_GRAY : message.isError ? Color.RED : Color.BLACK);
	}
	
	//setters
	public static void catchUncaughtExceptions(boolean b) {
		if (b) {
			if (Thread.getDefaultUncaughtExceptionHandler() != instance.logger_unc) return;
//			instance.logger_unc = (Thread t, Throwable e) -> new UncaughtExceptionMessage(t, e);
			instance.logger_unc = (Thread t, Throwable e) -> log("<Thread: " + t.getName() + "> - " + e.getClass().getName(), UNCEXC, Message.formatThrowable(e));
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
	
	public static LoggerWindow setLoggerWindow(LoggerWindow window_) {
		LoggerWindow buf = instance.window;
		instance.window = window_;
		return buf;
	}
	
	public static Message addLevel(String level, Message message) {
		if(level == null || message == null)
			throw new IllegalArgumentException("Level and message must not be null");
		
		Message previous = instance.messages.get(level);
		instance.messages.put(level, message);
		calculateNewMaxTagLength();
		return previous;
	}
	
	public static Message removeLevel(String level) {
		if(level != null && instance.messages.size() > 1) {
			if(level.equals(DEBUG) || level.equals(ERROR) || level.equals(EXCEPT) || level.equals(INFO)
					|| level.equals(SYSERR) || level.equals(SYSOUT) || level.equals(UNCEXC) || level.equals(WARN))
				throw new IllegalArgumentException("Level to remove must not be any of the default levels.");
			
			Message previous = instance.messages.remove(level);
			calculateNewMaxTagLength();
			return previous;
		}
		return null;
	}
	
	private static void calculateNewMaxTagLength() {
		int newMaxTagLength = 1;
		for(String level: instance.messages.keySet()) {
			newMaxTagLength = Math.max(newMaxTagLength, instance.messages.get(level).levelTag.length());
		}
		maxTagLength = newMaxTagLength;
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
	
	//logging
	public static void log(Throwable e) {
		log(e.getClass().getName(), EXCEPT, Message.formatThrowable(e));
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
		Message msg = instance.messages.get(level);
		
		if(msg == null) {
			//TODO: add LoggerMessage and use that here (to also log the message)
			log("Logger", WARN, "Level \"" + level + "\" does not exist; unable to log message");
			return;
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
