package org.school.app.service.skhoolchatbot;

import org.school.app.client.TelegramClient;
import org.school.app.config.DictionaryKeysConfig;
import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.school.app.model.UserGroup;
import org.school.app.repository.UserGroupRepository;
import org.school.app.utils.DictionaryUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;
import telegram.Message;

import java.util.List;

import static org.school.app.config.DictionaryKeysConfig.*;
import static org.school.app.model.User.UserStatus.REMOVE_CLASS_STATUS;
import static org.school.app.model.User.UserStatus.REMOVE_CLASS_STATUS1;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

@Service
public class UserGroupService {

	private static final String INVALID_UGROUP_ID = "Invalid User group ID";

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
			throw new BotException(getDictionaryValue(USER_ID_INVALID,
					message.getFrom().getLanguageCode()), message);
		}

		telegramClient.sendUsersAsButtons(users, getTextMessage(CHOOSE_USER_DICTIONARY, message), message);
		user.setStatus(User.UserStatus.CHOOSE_USER_USTATUS);
	}

	public void createUserGroup(Message message, User user) {
		telegramClient.simpleMessage(getDictionaryValue(NAME_OF_UGROUP, message.getFrom().getLanguageCode()), message);
		user.setStatus(User.UserStatus.TYPE_GROUP_NAME);
	}

	public void typeGroupName(Message message, User user) {
		UserGroup userGroup = new UserGroup();
		userGroup.setName(message.getText());
		userGroupRepo.save(userGroup);

		telegramClient.simpleMessage(DictionaryUtil
				.getDictionaryValueWithParams(UGROUP_CREATED,
						message.getFrom().getLanguageCode(), userGroup.getName()), message);

		user.setStatus(null);
	}

	public void removeClass(Message message, User user) {
		telegramClient.simpleMessage(getTextMessage(NAME_OF_UGROUP, message), message);
		user.setStatus(REMOVE_CLASS_STATUS);
	}

	public void removeClassEnterName(Message message, User user) {
		Page<UserGroup> classNames = userGroupRepo.findByNames(message.getText(), new PageRequest(0, 4));
		telegramClient.sendUserGroupNames(classNames, getTextMessage(CHOOSE_USER_GROUP_REMOVE, message), message);
		user.setStatus(REMOVE_CLASS_STATUS1);
	}

	public void removeUserGroupStep1(CallBackQuery callBackQuery, User user) {
		String userGroupId = callBackQuery.getData();
		userGroupRepo.deleteById(userGroupId);

		Message message = callBackQuery.getMessage();
		String languageCode = message.getFrom().getLanguageCode();
		telegramClient.simpleMessage(UGROUP_REMOVE_SUCCESS, languageCode, message);
	}

	public void addToUserGroup(Message message, User user) {

	}

	private String getTextMessage(DictionaryKeysConfig key, Message message) {
		return getDictionaryValue(key, message.getFrom().getLanguageCode());
	}
}
