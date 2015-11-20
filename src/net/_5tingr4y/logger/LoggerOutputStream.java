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