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

import java.io.ByteArrayOutputStream;
import java.io.IOException;

final class LoggerOutputStream extends ByteArrayOutputStream {

	String str = "";
	
	private void log() {
		str += toString();
		String[] strs = str.split("(?<=\\r?\\n)");
		str = "";
		for (int i = 0; i < strs.length; ++i) {
			if (strs[i].matches(".*\\r?\\n$")) {
//				new SysOutMessage(strs[i].replaceFirst("\\r?\\n$", ""));
				Logger.log(Logger.SYSOUT, strs[i].replaceFirst("\\r?\\n$", ""));
			} else {
				str += strs[i];
			}
		}
		reset();
	}
	
	@Override
	public void write(byte[] b) throws IOException {
		super.write(b);
		log();
	}
	
	@Override
	public synchronized void write(byte[] b, int off, int len) {
		super.write(b, off, len);
		log();
	}
	
	@Override
	public synchronized void write(int b) {
		super.write(b);
		log();
	}
	
}