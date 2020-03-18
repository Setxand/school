package org.school.app.tennisapp.exception;

import org.school.app.tennisapp.util.DictionaryUtil;
import lombok.Getter;
import telegram.Message;

import static org.school.app.tennisapp.config.DictionaryKeysConfig.SYSTEM_ERROR;

@Getter
public class BotException extends RuntimeException {

	private final Message telegramSystemMessage;

	public BotException(String message, Message telegramSystemMessage) {
		super(message);
		this.telegramSystemMessage = telegramSystemMessage;
	}

	public BotException(Message telegramSystemMessage) {
		super(DictionaryUtil.getDictionaryValue(SYSTEM_ERROR));
		this.telegramSystemMessage = telegramSystemMessage;
	}
}
