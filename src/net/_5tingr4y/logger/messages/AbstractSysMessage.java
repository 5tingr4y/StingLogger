package net._5tingr4y.logger.messages;

public class AbstractSysMessage extends AbstractMessage {

	public AbstractSysMessage(String levelTag_, boolean isError_) {
		super(levelTag_, "[%d{HH:mm:ss} %l]%e %m", isError_);
	}

}
