package org.school.app.exception;

import lombok.Getter;
import org.school.app.client.Platform;
import org.school.app.config.DictionaryKeysConfig;
import org.school.app.utils.DictionaryUtil;
import telegram.Chat;
import telegram.Message;

import static org.school.app.config.DictionaryKeysConfig.SYSTEM_ERROR;

@Getter
public class BotException extends RuntimeException {

	private final Message telegramSystemMessage;

	public BotException(String message, Message telegramSystemMessage) {
		super(message);
		this.telegramSystemMessage = telegramSystemMessage;
	}

	public BotException(DictionaryKeysConfig config, Message telegramSystemMessage) {
		super(DictionaryUtil.getDictionaryValueWithParams(config, telegramSystemMessage.getFrom().getLanguageCode()));
		this.telegramSystemMessage = telegramSystemMessage;
	}

	public BotException(String message, Integer chatId) {
		super(message);
		Message telegramSystemMessage = new Message(new Chat(chatId));
		telegramSystemMessage.setPlatform(Platform.COMMON);
		this.telegramSystemMessage = telegramSystemMessage;
	}

	public BotException(Message telegramSystemMessage) {
		super(DictionaryUtil.getDictionaryValue(SYSTEM_ERROR, telegramSystemMessage.getFrom().getLanguageCode()));
		this.telegramSystemMessage = telegramSystemMessage;
	}
}
