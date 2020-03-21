package org.school.app.service.skhoolchatbot;

import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.springframework.stereotype.Service;
import telegram.Message;
import telegram.client.TelegramClient;

import javax.transaction.Transactional;

import static org.school.app.config.DictionaryKeysConfig.UNKNOWN_COMMAND;
import static org.school.app.model.User.UserStatus.TYPE_TEST_BOX_USTATUS;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

@Service
public class CommandService {

	public interface TelegramCommands {
		public static final String START = "/start";
		public static final String START_TEST = "/starttest";
		public static final String SEND_TEST = "/sendtest";
	}
	private final TelegramClient telegramClient;
	private final TestService testService;

	public CommandService(TelegramClient telegramClient, TestService testService) {
		this.telegramClient = telegramClient;
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

			case TelegramCommands.SEND_TEST:
				testService.sendTest(message, user);
				break;

			default:
				throw new BotException(getDictionaryValue(UNKNOWN_COMMAND,
						message.getFrom().getLanguageCode()), message);
		}
	}

	private void startTest(Message message, User user) {
		testService.startTest(message, user, TYPE_TEST_BOX_USTATUS);
	}

	private void start(Message message, User user) {
		telegramClient.helloMessage(message);

	}

}
