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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
	
	public final String levelTag;
	/**
	 * <li>%l: levelTag
	 * <li>%e: spaces to align after the level tag
	 * <li>%s: the sender
	 * <li>%m: the actual message
	 */
	public final String pattern;
	
	public final boolean isError;
	
	public Message(String levelTag_, String pattern_, boolean isError_) {
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
