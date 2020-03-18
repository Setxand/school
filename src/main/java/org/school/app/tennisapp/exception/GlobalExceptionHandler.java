package org.school.app.tennisapp.exception;

import org.school.app.tennisapp.client.TelegramClient;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger logger = Logger.getLogger(GlobalExceptionHandler.class);
	@Autowired TelegramClient telegramClient;

	@ExceptionHandler({BotException.class})
	public void handleBotException(final BotException ex) {
		telegramClient.simpleMessage(ex.getMessage(), ex.getTelegramSystemMessage());
	}

	@ExceptionHandler({Exception.class})
	public void handleBotException(final Exception ex) {
		logger.warn("Internal error: ", ex);
	}

}
