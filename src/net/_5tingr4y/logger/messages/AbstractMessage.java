package net._5tingr4y.logger.messages;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net._5tingr4y.logger.Logger;

public abstract class AbstractMessage {
	
	public final String levelTag;
	/**
	 * <li>%l: levelTag
	 * <li>%e: spaces to align after the level tag
	 * <li>%s: the sender
	 * <li>%m: the actual message
	 */
	public final String pattern;
	
	public final boolean isError;
	
	public AbstractMessage(String levelTag_, String pattern_, boolean isError_) {
		levelTag = levelTag_;
		pattern = pattern_;
		isError = isError_;
	}
	
	public final String formatMessage(String sender, String message) {
		String ret = pattern.
				replace("%l", levelTag).
				replace("%e", fill(levelTag, Logger.getMaxTagLength())).
				replace("%s", sender).
				replace("%m", message);
		Pattern pattern = Pattern.compile("%d\\{.*\\}");
		Matcher matcher = pattern.matcher(ret);
		if (matcher.find()) {
			for (int i = 0; i <= matcher.groupCount(); i++) {
				String found = matcher.group(i);
				DateFormat dateFormat = new SimpleDateFormat(found.substring(3,
						found.length() - 1));
				Calendar cal = Calendar.getInstance();
				String date = dateFormat.format(cal.getTime());
				ret = ret.replace(found, date);
			}
		}
		
		return ret;
	}
	
	public final static String formatThrowable(Throwable e) {
		StringBuilder result = new StringBuilder(String.valueOf(e.getMessage()));
		for (StackTraceElement s : e.getStackTrace())
			result.append(System.getProperty("line.separator")
					+ fill("", 16 + Logger.getMaxTagLength()) + "at " + s);
		return result.toString();
	}
	
	protected static String fill(String str, int length) {
		if (str.length() < length) {
			StringBuilder sb = new StringBuilder();
			while (sb.length() < length - str.length()) {
				sb.append(" ");
			}
			return sb.toString();
		}
		return "";
	}
}
