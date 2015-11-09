package net._5tingr4y.logger;

public class AbstractLoggerMessage extends AbstractMessage {
	
	public AbstractLoggerMessage(String sender_, String levelTag_, String message_, boolean isError_) {
		super(sender_, levelTag_, message_, "[%d{HH:mm:ss} %l]%e %s: %m", isError_);
	}

	protected final static String formatMsg(Throwable e) {
		StringBuilder result = new StringBuilder(String.valueOf(e.getMessage()));
		for (StackTraceElement s : e.getStackTrace())
			result.append(System.getProperty("line.separator")
					+ fill("", 16 + MAX_TAG_LENGTH) + "at " + s);
		return result.toString();
	}

}
