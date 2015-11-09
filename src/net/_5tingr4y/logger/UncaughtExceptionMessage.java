package net._5tingr4y.logger;

final class UncaughtExceptionMessage extends AbstractLoggerMessage {

	public UncaughtExceptionMessage(Thread t, Throwable e) {
		super("<Thread: " + t.getName() + "> - " + e.getClass().getName(), "UNCEXC", formatMsg(e), true);
	}

}
