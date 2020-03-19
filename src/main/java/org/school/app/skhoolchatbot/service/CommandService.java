package org.school.app.skhoolchatbot.service;

import org.school.app.skhoolchatbot.exception.BotException;
import org.school.app.skhoolchatbot.model.User;
import org.school.app.skhoolchatbot.validator.CommandValidator;
import org.springframework.stereotype.Service;
import telegram.Message;
import telegram.client.TelegramClient;

import javax.transaction.Transactional;

import static org.school.app.skhoolchatbot.config.DictionaryKeysConfig.UNKNOWN_COMMAND;
import static org.school.app.skhoolchatbot.util.DictionaryUtil.getDictionaryValue;

@Service
public class CommandService {

	private final TelegramClient telegramClient;
	private final CommandValidator validator;
	private final TestService testService;

	public CommandService(TelegramClient telegramClient, CommandValidator validator, TestService testService) {
		this.telegramClient = telegramClient;
		this.validator = validator;
		this.testService = testService;
	}

	@Transactional
	public void commandToBot(Message message, User user) {
		user.setStatus(null);
		String command = message.getText();

		switch (command) {
			case TelegramCommands.START:
				start(message, user);
				break;

			case TelegramCommands.START_TEST:
				startTest(message, user);
				break;

			default:
				throw new BotException(getDictionaryValue(UNKNOWN_COMMAND), message);
		}
	}

	private void startTest(Message message, User user) {
		testService.startTest(message, user);
	}

	private void start(Message message, User user) {
		telegramClient.helloMessage(message);

	}


	public interface TelegramCommands {
		public static final String START = "/start";
		public static final String START_TEST = "/starttest";
	}

}
