package net._5tingr4y.logger;

public final class DebugMessage extends AbstractLoggerMessage {

	public DebugMessage(String sender, String message) {
		super(sender, "DEBUG", message, false);
	}

	public DebugMessage(Class<?> sender, String message) {
		this(sender.getName(), message);
	}

	public DebugMessage(Object sender, String message) {
		this(sender.getClass().getName(), message);
	}

}
