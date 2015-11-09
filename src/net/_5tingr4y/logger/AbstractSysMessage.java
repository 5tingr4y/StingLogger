package net._5tingr4y.logger;

public class AbstractSysMessage extends AbstractMessage {

	public AbstractSysMessage(String levelTag_, String message_, boolean isError_) {
		super("System", levelTag_, message_, "[%d{HH:mm:ss} %l]%e %m", isError_);
	}

}
