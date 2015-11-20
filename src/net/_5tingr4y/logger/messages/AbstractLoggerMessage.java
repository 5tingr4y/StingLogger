package net._5tingr4y.logger.messages;

public class AbstractLoggerMessage extends AbstractMessage {
	
	public AbstractLoggerMessage(String levelTag_, boolean isError_) {
		super(levelTag_, "[%d{HH:mm:ss} %l]%e %s: %m", isError_);
	}
}
