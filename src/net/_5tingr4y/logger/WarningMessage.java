package net._5tingr4y.logger;

public final class WarningMessage extends AbstractLoggerMessage {

	public WarningMessage(String sender, String message) {
		super(sender, "WARN", message, false);
	}

	public WarningMessage(Class<?> sender, String message) {
		this(sender.getName(), message);
	}

	public WarningMessage(Object sender, String message) {
		this(sender.getClass().getName(), message);
	}

}
