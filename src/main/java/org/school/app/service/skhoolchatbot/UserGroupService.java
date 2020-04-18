package org.school.app.service.skhoolchatbot;

import org.apache.log4j.Logger;
import org.school.app.client.Platform;
import org.school.app.client.TelegramClient;
import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.school.app.model.UserGroup;
import org.school.app.repository.UserGroupRepository;
import org.school.app.utils.DictionaryUtil;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;
import telegram.Chat;
import telegram.Message;

import java.util.List;

import static org.school.app.config.DictionaryKeysConfig.*;
import static org.school.app.model.User.UserStatus.ADD_TO_CLASS_STATUS3;
import static org.school.app.model.User.UserStatus.REMOVE_FROM_CLASS_STATUS4;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

@Service
public class UserGroupService implements GroupServiceConstants {

	private final UserService userService;
	private final TelegramClient telegramClient;
	private final UserGroupRepository userGroupRepo;
	private final UserGroupHelper userGroupHelper;
	private final TestService testService;

	private static final Logger logger = Logger.getLogger(UserGroupService.class);

	public UserGroupService(UserService userService, TelegramClient telegramClient,
							UserGroupRepository userGroupRepo, TestService testService) {
		this.userService = userService;
		this.telegramClient = telegramClient;
		this.userGroupRepo = userGroupRepo;
		this.testService = testService;
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

	public void createUserGroupStep1(Message message, User user) {
		UserGroup userGroup = new UserGroup();
		userGroup.setName(message.getText());
		userGroupRepo.save(userGroup);

		telegramClient.simpleMessage(DictionaryUtil
				.getDictionaryValueWithParams(UGROUP_CREATED,
						message.getFrom().getLanguageCode(), userGroup.getName()), message);

		user.setStatus(null);
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

	public void addUserGroupStep2(Message message, User user) {
		userGroupHelper.userGroupActionsSendUsersAsButtons(message, user, ADD_TO_CLASS_STATUS3);
	}

	public void addToUserGroupStep3(CallBackQuery callBackQuery, User user) {
		userGroupHelper.addToUserGroupFinalStep(callBackQuery, user);
	}

	public void removeUserFromGroupStep3(Message message, User user) {
		userGroupHelper.userGroupActionsSendUsersAsButtons(message, user, REMOVE_FROM_CLASS_STATUS4);
	}

	public void removeUserFromGroupStep4(CallBackQuery callBackQuery, User user) {
		userGroupHelper.removeUserGroupFinalStep(callBackQuery, user);
	}

	public Page<UserGroup> findByName(String text) {
		return userGroupRepo.findByName(text, PAGEABLE);
	}

	public void sendTestForUserGroupFinalStep(CallBackQuery callBackQuery, User user) {
		String testBoxId = callBackQuery.getData();
		String userGroupId = user.getMetaInf();

		UserGroup userGroup = userGroupRepo
				.findById(userGroupId).orElseThrow(() -> new IllegalArgumentException(INVALID_UGROUP_ID));

		userGroup.getUsers().forEach(u -> {
			if (u.getMessageIdToEdit() != null) { ///TODO fix
				logger.warn("MESSAGE ID TO EDIT MUST BE CLEAR BEFORE");
				u.setMessageIdToEdit(null);
			}
			Message message = new Message(new Chat(user.getChatId()));
			message.setPlatform(Platform.COMMON);
			testService.choosedTestBox(message, testBoxId, u);
		});

		user.setMetaInf(null);
		telegramClient.deleteMessage(callBackQuery.getMessage());
	}
}
