package net._5tingr4y.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractMessage {
	
	protected static int MAX_TAG_LENGTH = 6;
	
	public final String sender;
	public final String levelTag;
	public final String message;
	/**
	 * <li>%l: levelTag
	 * <li>%e: spaces to align after the level tag
	 * <li>%s: the sender
	 * <li>%m: the actual message
	 */
	public final String pattern;
	
	public final boolean isError;
	
	public AbstractMessage(String sender_, String levelTag_, String message_, String pattern_, boolean isError_) {
		sender = sender_;
		levelTag = levelTag_;
		message = message_;
		pattern = pattern_;
		isError = isError_;
		Logger.log(this);
	}
	
	public final AbstractMessage send() {
		Logger.log(this);
		return this;
	}
	
	@Override
	public final String toString() {
		String ret = pattern.
				replace("%l", levelTag).
				replace("%e", fill(levelTag, MAX_TAG_LENGTH)).
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
	
	protected static String fill(String str, int length) {
		if (str.length() < length) {
			StringBuilder sB = new StringBuilder();
			while (sB.length() < length - str.length()) {
				sB.append(" ");
			}
			return sB.toString();
		}
		return "";
	}
}
