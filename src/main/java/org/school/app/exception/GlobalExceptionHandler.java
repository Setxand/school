package org.school.app.exception;

import org.apache.log4j.Logger;
import org.school.app.client.TelegramClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.school.app.config.DictionaryKeysConfig.SYSTEM_ERROR;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

@EnableWebMvc
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final String ERROR_MESSAGE = getDictionaryValue(SYSTEM_ERROR);

	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	@Autowired TelegramClient telegramClient;

	@ExceptionHandler({BotException.class})
	public void handleBotException(final BotException ex) {
		telegramClient.simpleMessage(ex.getMessage(), ex.getTelegramSystemMessage());
	}

	@ExceptionHandler({Exception.class})
	public void handleBotException(final Exception ex) {
		logger.warn(ex.getMessage(), ex);
	}

}
