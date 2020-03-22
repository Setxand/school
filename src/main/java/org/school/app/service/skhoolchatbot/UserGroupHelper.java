package org.school.app.service.skhoolchatbot;

import org.school.app.client.TelegramClient;
import org.school.app.model.User;
import org.school.app.model.UserGroup;
import org.school.app.repository.UserGroupRepository;
import org.springframework.data.domain.Page;
import telegram.CallBackQuery;
import telegram.Message;

import java.util.List;

import static org.school.app.config.DictionaryKeysConfig.*;
import static org.school.app.model.User.UserStatus.*;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;
import static org.school.app.utils.DictionaryUtil.getDictionaryValueWithParams;

public class UserGroupHelper implements GroupServiceConstants {

	private final UserService userService;
	private final TelegramClient telegramClient;
	private final UserGroupRepository userGroupRepo;

	public UserGroupHelper(UserService userService, TelegramClient telegramClient, UserGroupRepository userGroupRepo) {
		this.userService = userService;
		this.telegramClient = telegramClient;
		this.userGroupRepo = userGroupRepo;
	}


	public void addToUserGroupStep3(CallBackQuery callBackQuery, User user) {
		String userId = callBackQuery.getData();
		User userToAdd = userService.getUser(Integer.valueOf(userId));

		UserGroup userGroup = userGroupRepo
				.findById(user.getMetaInf()).orElseThrow(() -> new IllegalArgumentException(INVALID_UGROUP_ID));

		Message message = callBackQuery.getMessage();

		String text = getDictionaryValueWithParams(ADD_TO_CLASS_SUCCESS,
				message.getFrom().getLanguageCode(), userToAdd.getName(), userGroup.getName());
		telegramClient.simpleMessage(text, message);

		userGroup.getUsers().add(userToAdd);
		user.setStatus(null);
	}

	public void addToUserGroupStep2(Message message, User user) {
		List<User> users = userService.searchUsersByName(message.getText()).getContent();
		String dictionaryValue = getDictionaryValue(CHOOSE_USER_DICTIONARY, message.getFrom().getLanguageCode());
		telegramClient.sendUsersAsButtons(users, dictionaryValue, message);
		user.setStatus(User.UserStatus.ADD_TO_CLASS_STATUS3);
	}

	public void addToUserGroupStep1(CallBackQuery callBackQuery, User user) {
		user.setMetaInf(callBackQuery.getData());
		Message message = callBackQuery.getMessage();
		telegramClient.simpleMessage(TYPE_NAME, message);
		user.setStatus(User.UserStatus.ADD_TO_CLASS_STATUS2);
	}

	public void addToUserGroupStep0(Message message, User user) {//todo fix method names
		Page<UserGroup> userGroups = userGroupRepo.findByName(message.getText(), PAGEABLE);
		telegramClient.sendUserGroupNames(userGroups, getTextMessage(CHOOSE_USER_GROUP, message), message);
		user.setStatus(ADD_TO_CLASS_STATUS1);
	}

	public void addToUserGroup(Message message, User user) {
		telegramClient.simpleMessage(NAME_OF_UGROUP, message);
		user.setStatus(ADD_TO_CLASS_STATUS);
	}

	public void removeUserGroupStep2(CallBackQuery callBackQuery, User user) {
		String userGroupId = callBackQuery.getData();
		userGroupRepo.deleteById(userGroupId);

		Message message = callBackQuery.getMessage();
		String languageCode = message.getFrom().getLanguageCode();
		telegramClient.simpleMessage(UGROUP_REMOVE_SUCCESS, message);
		user.setStatus(null);
	}

	public void removeUserGroupStep1(Message message, User user) {
		Page<UserGroup> classNames = userGroupRepo.findByName(message.getText(), PAGEABLE);
		telegramClient.sendUserGroupNames(classNames, getTextMessage(CHOOSE_USER_GROUP, message), message);
		user.setStatus(REMOVE_CLASS_STATUS1);
	}

	public void removeUserGroup(Message message, User user) {
		telegramClient.simpleMessage(getTextMessage(NAME_OF_UGROUP, message), message);
		user.setStatus(REMOVE_CLASS_STATUS);
	}
}
