package org.school.app.service.skhoolchatbot;

import org.school.app.client.TelegramClient;
import org.school.app.config.DictionaryKeysConfig;
import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.school.app.model.UserGroup;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;
import telegram.Message;

import static org.school.app.config.DictionaryKeysConfig.CHOOSE_USER_GROUP;
import static org.school.app.config.DictionaryKeysConfig.NAME_OF_UGROUP;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

@Service
public class SendActionService {

	private final UserGroupService userGroupService;
	private final TelegramClient telegramClient;

	public SendActionService(UserGroupService userGroupService, TelegramClient telegramClient) {
		this.userGroupService = userGroupService;
		this.telegramClient = telegramClient;
	}

	public void sendGroupNamesForUsers(Message message, User user, User.UserStatus status) {//todo fix method names
		Page<UserGroup> userGroups = userGroupService.findByName(message.getText());

		if (userGroups.isEmpty()) {
			throw new BotException(DictionaryKeysConfig.NO_SUCH_GROUPS, message);
		}

		telegramClient.sendUserGroupNames(userGroups, getTextMessage(CHOOSE_USER_GROUP, message), message);
		user.setStatus(status);
	}

	public void saveUserGroupInfoAndSendMessage(
			CallBackQuery callBackQuery, User user, User.UserStatus status, DictionaryKeysConfig dictionaryMessage) {
		user.setMetaInf(callBackQuery.getData());
		Message message = callBackQuery.getMessage();
		telegramClient.simpleMessage(dictionaryMessage, message);
		user.setStatus(status);
	}

	public void sendTypeName(Message message, User user, User.UserStatus status) {
		telegramClient.simpleMessage(NAME_OF_UGROUP, message);
		user.setStatus(status);
	}

	public void typeGroupName(Message message, User user, User.UserStatus status) {
		telegramClient.simpleMessage(getDictionaryValue(NAME_OF_UGROUP, message.getFrom().getLanguageCode()), message);
		user.setStatus(status);
	}


	private String getTextMessage(DictionaryKeysConfig key, Message message) {
		return getDictionaryValue(key, message.getFrom().getLanguageCode());
	}
}
