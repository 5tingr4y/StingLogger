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
