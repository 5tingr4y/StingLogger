package net._5tingr4y.logger;

public final class ErrorMessage extends AbstractLoggerMessage {

	public ErrorMessage(String sender, String message) {
		super(sender, "ERROR", message, true);
	}

	public ErrorMessage(Class<?> sender, String message) {
		this(sender.getName(), message);
	}

	public ErrorMessage(Object sender, String message) {
		this(sender.getClass().getName(), message);
	}

}
