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
		Logger.setLogFile("test/testLog1.log");
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
			e.printStackTrace();
		}
	}
}
