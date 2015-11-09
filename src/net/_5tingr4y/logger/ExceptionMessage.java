package net._5tingr4y.logger;

public final class ExceptionMessage extends AbstractLoggerMessage {

	public ExceptionMessage(Throwable e) {
		super(e.getClass().getName(), "EXCEPT", formatMsg(e), true);
	}

}
