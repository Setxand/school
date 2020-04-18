package org.school.app.service.skhoolchatbot;

import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import telegram.Message;
import telegram.client.TelegramClient;

import javax.transaction.Transactional;

import static org.school.app.config.DictionaryKeysConfig.ACCESS_RESTRICTED;
import static org.school.app.config.DictionaryKeysConfig.UNKNOWN_COMMAND;
import static org.school.app.model.User.UserStatus.*;
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
		public static final String SEND_TEST_TO_CLASS = "/sendtesttoclass";

	}

	private final TelegramClient telegramClient;
	private final TestService testService;
	private final UserGroupService userGroupService;
	private final SendActionService sendActionService;

	@Value("${id.admin}") private String adminId;

	public CommandService(TelegramClient telegramClient, TestService testService, UserGroupService userGroupService,
						  SendActionService sendActionService) {
		this.telegramClient = telegramClient;
		this.testService = testService;
		this.userGroupService = userGroupService;
		this.sendActionService = sendActionService;
	}

	@Transactional
	public void commandToBot(Message message, User user) {
		user.setStatus(null);
		String command = message.getText();

		if (!(message.getChat().getId().equals(388073901) ||
				message.getChat().getId().equals(968840961))  && //todo create constants
				!command.equals(TelegramCommands.START)) {
			throw new BotException(ACCESS_RESTRICTED, message);
		}

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

			case TelegramCommands.CREATE_CLASS:
				sendActionService.typeGroupName(message, user, TYPE_GROUP_NAME);
				break;

			case TelegramCommands.REMOVE_CLASS:
				userGroupService.removeUserGroup(message, user);
				break;

			case TelegramCommands.ADD_TO_CLASS:
				sendActionService.sendTypeName(message, user, ADD_TO_CLASS_STATUS);
				break;

			case TelegramCommands.REMOVE_FROM_CLASS:
				sendActionService.sendTypeName(message, user, REMOVE_FROM_CLASS_STATUS1);
				break;

			case TelegramCommands.SEND_TEST_TO_CLASS:
				sendActionService.typeGroupName(message, user, SEND_TEST_TO_CLASS_STATUS1);
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
