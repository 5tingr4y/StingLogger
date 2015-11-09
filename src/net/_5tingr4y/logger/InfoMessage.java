package net._5tingr4y.logger;

public final class InfoMessage extends AbstractLoggerMessage {

	public InfoMessage(String sender, String message) {
		super(sender, "INFO", message, false);
	}

	public InfoMessage(Class<?> sender, String message) {
		this(sender.getName(), message);
	}

	public InfoMessage(Object sender, String message) {
		this(sender.getClass().getName(), message);
	}

}
