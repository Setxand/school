package org.school.app.service.skhoolchatbot;

import org.school.app.client.TelegramClient;
import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.school.app.utils.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.Message;

import java.util.List;

import static org.school.app.config.DictionaryKeysConfig.CHOOSE_USER_DICTIONARY;
import static org.school.app.config.DictionaryKeysConfig.USER_ID_INVALID;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

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
			throw new BotException(DictionaryUtil.getDictionaryValue(USER_ID_INVALID,
					message.getFrom().getLanguageCode()), message);
		}

		telegramClient.sendUsersAsButtons(users,
				getDictionaryValue(CHOOSE_USER_DICTIONARY, message.getFrom().getLanguageCode()), message);
		user.setStatus(User.UserStatus.CHOOSE_USER_USTATUS);
	}
}
