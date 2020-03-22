package org.school.app.service.skhoolchatbot;

import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.springframework.beans.factory.annotation.Value;
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
		public static final String CREATE_CLASS = "/createclass";
		public static final String ADD_TO_CLASS = "/addtoclass";
		public static final String REMOVE_FROM_CLASS = "/removefromclass";
		public static final String REMOVE_CLASS = "/removeclass";

	}

	private final TelegramClient telegramClient;
	private final TestService testService;
	private final UserGroupService userGroupService;
	@Value("${id.admin}") private String adminId;

	public CommandService(TelegramClient telegramClient, TestService testService, UserGroupService userGroupService) {
		this.telegramClient = telegramClient;
		this.testService = testService;
		this.userGroupService = userGroupService;
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
				if (message.getChat().getId().equals(Integer.valueOf(adminId))) {
					startTest(message, user);
				}

				break;

			case TelegramCommands.SEND_TEST:
				if (message.getChat().getId().equals(Integer.valueOf(adminId))) {
					testService.sendTest(message, user);
				}
				break;

			case TelegramCommands.CREATE_CLASS:
				userGroupService.createUserGroup(message, user);
				break;

			case TelegramCommands.REMOVE_CLASS:
				userGroupService.removeUserGroup(message, user);
				break;

			case TelegramCommands.ADD_TO_CLASS:
				userGroupService.addToUserGroup(message, user);
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
