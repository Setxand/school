package org.school.app.skhoolchatbot.service;

import org.school.app.skhoolchatbot.client.TelegramClient;
import org.school.app.skhoolchatbot.exception.BotException;
import org.school.app.skhoolchatbot.model.User;
import org.school.app.skhoolchatbot.util.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.Message;

import java.util.List;

import static org.school.app.skhoolchatbot.config.DictionaryKeysConfig.CHOOSE_USER_DICTIONARY;
import static org.school.app.skhoolchatbot.config.DictionaryKeysConfig.USER_ID_INVALID;
import static org.school.app.skhoolchatbot.util.DictionaryUtil.getDictionaryValue;

@Service
public class UserGroupService {

	private final UserService userService;
	private final TelegramClient telegramClient;

	public UserGroupService(UserService userService, TelegramClient telegramClient) {
		this.userService = userService;
		this.telegramClient = telegramClient;
	}

	public void sendUserNamesByName(Message message, User user) {
		user.setAssigneeTestChatId(null); //Clear for new user assignment

		List<User> users = userService.searchUsersByName(message.getText()).getContent();

		if (users.isEmpty()) {
			throw new BotException(DictionaryUtil.getDictionaryValue(USER_ID_INVALID), message);
		}

		telegramClient.sendUsersAsButtons(users, getDictionaryValue(CHOOSE_USER_DICTIONARY), message);
		user.setStatus(User.UserStatus.CHOOSE_USER_USTATUS);
	}
}
