package org.school.app.service.skhoolchatbot;

import org.school.app.client.TelegramClient;
import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.school.app.model.UserGroup;
import org.school.app.repository.UserGroupRepository;
import org.school.app.utils.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;
import telegram.Message;

import java.util.List;

import static org.school.app.config.DictionaryKeysConfig.*;
import static org.school.app.model.User.UserStatus.*;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

@Service
public class UserGroupService implements GroupServiceConstants {

	private final UserService userService;
	private final TelegramClient telegramClient;
	private final UserGroupRepository userGroupRepo;
	private final UserGroupHelper userGroupHelper;

	public UserGroupService(UserService userService, TelegramClient telegramClient, UserGroupRepository userGroupRepo) {
		this.userService = userService;
		this.telegramClient = telegramClient;
		this.userGroupRepo = userGroupRepo;
		this.userGroupHelper = new UserGroupHelper(userService, telegramClient, userGroupRepo);
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

	public void createUserGroupStep1(Message message, User user) {
		UserGroup userGroup = new UserGroup();
		userGroup.setName(message.getText());
		userGroupRepo.save(userGroup);

		telegramClient.simpleMessage(DictionaryUtil
				.getDictionaryValueWithParams(UGROUP_CREATED,
						message.getFrom().getLanguageCode(), userGroup.getName()), message);

		user.setStatus(null);
	}

	public void removeUserFromGroup(Message message, User user) {
		userGroupHelper.sendTypeName(message, user, REMOVE_FROM_CLASS_STATUS1);
	}

	public void removeUserGroup(Message message, User user) {
		userGroupHelper.removeUserGroup(message, user);
	}

	public void removeUserGroupStep1(Message message, User user) {
		userGroupHelper.removeUserGroupStep1(message, user);
	}

	public void addUserGroupStep2(CallBackQuery callBackQuery, User user) {
		userGroupHelper.removeUserGroupStep2(callBackQuery, user);
	}

	public void addToUserGroup(Message message, User user) {
		userGroupHelper.sendTypeName(message, user, ADD_TO_CLASS_STATUS);
	}

	public void addToUserGroupStep1(CallBackQuery callBackQuery, User user) {
		userGroupHelper.saveUserGroupInfoAndSendTypeNameMessage(callBackQuery, user, ADD_TO_CLASS_STATUS2);
	}

	public void addUserGroupStep2(Message message, User user) {
		userGroupHelper.userGroupActionsSendUsersAsButtons(message, user, ADD_TO_CLASS_STATUS3);
	}

	public void addToUserGroupStep3(CallBackQuery callBackQuery, User user) {
		userGroupHelper.addToUserGroupFinalStep(callBackQuery, user);
	}

	public void addToUserGroupStep0(Message message, User user) {
		userGroupHelper.sendGroupNamesForUsers(message, user, ADD_TO_CLASS_STATUS1);
	}

	public void removeUserFromGroupStep1(Message message, User user) {
		userGroupHelper.sendGroupNamesForUsers(message, user, REMOVE_FROM_CLASS_STATUS2);
	}

	public void removeUserFromGroupStep2(CallBackQuery callBackQuery, User user) {
		userGroupHelper.saveUserGroupInfoAndSendTypeNameMessage(callBackQuery, user, REMOVE_FROM_CLASS_STATUS3);
	}

	public void removeUserFromGroupStep3(Message message, User user) {
		userGroupHelper.userGroupActionsSendUsersAsButtons(message, user, REMOVE_FROM_CLASS_STATUS4);
	}

	public void removeUserFromGroupStep4(CallBackQuery callBackQuery, User user) {
		userGroupHelper.removeUserGroupFinalStep(callBackQuery, user);
	}
}
