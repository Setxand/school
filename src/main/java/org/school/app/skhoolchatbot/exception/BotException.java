package org.school.app.skhoolchatbot.exception;

import lombok.Getter;
import org.school.app.skhoolchatbot.util.DictionaryUtil;
import telegram.Message;

import static org.school.app.skhoolchatbot.config.DictionaryKeysConfig.SYSTEM_ERROR;

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
