package org.school.app.service.skhoolchatbot;

import org.school.app.client.TelegramClient;
import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.school.app.model.UserGroup;
import org.school.app.repository.UserGroupRepository;
import org.school.app.utils.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.Message;

import java.util.List;

import static org.school.app.config.DictionaryKeysConfig.*;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

@Service
public class UserGroupService {

	private final UserService userService;
	private final TelegramClient telegramClient;
	private final UserGroupRepository userGroupRepo;

	public UserGroupService(UserService userService, TelegramClient telegramClient, UserGroupRepository userGroupRepo) {
		this.userService = userService;
		this.telegramClient = telegramClient;
		this.userGroupRepo = userGroupRepo;
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

	public void createUserGroup(Message message, User user) {
		telegramClient.simpleMessage(DictionaryUtil
				.getDictionaryValue(NAME_OF_UGROUP, message.getFrom().getLanguageCode()), message);
		user.setStatus(User.UserStatus.TYPE_GROUP_NAME);
	}

	public void typeGroupName(Message message, User user) {
		UserGroup userGroup = new UserGroup();
		userGroup.setName(message.getText());
		userGroupRepo.save(userGroup);

		telegramClient.simpleMessage(DictionaryUtil
				.getDictionaryValue(UGROUP_CREATED, message.getFrom().getLanguageCode()), message);

		user.setStatus(null);
	}
}
