package org.school.app.service.skhoolchatbot;

import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.school.app.utils.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;

import javax.transaction.Transactional;

import static org.school.app.config.DictionaryKeysConfig.*;
import static org.school.app.model.User.UserStatus.*;

@Service
public class CallBackQueryService {

	public enum CallBackQueryPayload {

	}

	private final TestService testService;
	private final UserGroupService userGroupService;
	private final SendActionService sendActionService;

	public CallBackQueryService(TestService testService, UserGroupService userGroupService,
								SendActionService sendActionService) {
		this.testService = testService;
		this.userGroupService = userGroupService;
		this.sendActionService = sendActionService;
	}

	@Transactional
	public void callBackQueryToBot(CallBackQuery callBackQuery, User user) {

		if (user.getStatus() != null) {
			checkByStatus(callBackQuery, user);
		} else
			throw new BotException(callBackQuery.getMessage());
	}

	private void checkByStatus(CallBackQuery callBackQuery, User user) {

		switch (user.getStatus()) {
			case CHOOSE_TEST_BOX_USTATUS:
				testService.choosedTestBox(callBackQuery.getMessage(), callBackQuery.getData(), user);
				break;

			case CHOOSE_USER_USTATUS:
				testService.startTestForUser(callBackQuery, user);
				break;

			case CHOOSE_TEST_BOX_FOR_USER_USTATUS:
				testService.chooseTestBoxForUser(callBackQuery, user);
				break;

			case REMOVE_CLASS_STATUS1:
				userGroupService.addUserGroupStep2(callBackQuery, user);
				break;

			case ADD_TO_CLASS_STATUS1:
				sendActionService.saveUserGroupInfoAndSendMessage(callBackQuery, user, ADD_TO_CLASS_STATUS2, TYPE_NAME);
				break;

			case ADD_TO_CLASS_STATUS3:
				userGroupService.addToUserGroupStep3(callBackQuery, user);
				break;

			case REMOVE_FROM_CLASS_STATUS2:
				sendActionService
						.saveUserGroupInfoAndSendMessage(callBackQuery, user, REMOVE_FROM_CLASS_STATUS3, TYPE_NAME);
				break;

			case REMOVE_FROM_CLASS_STATUS4:
				userGroupService.removeUserFromGroupStep4(callBackQuery, user);
				break;

			case SEND_TEST_TO_CLASS_STATUS2:
				sendActionService.saveUserGroupInfoAndSendMessage(
								callBackQuery, user, SEND_TEST_TO_CLASS_STATUS3, TYPE_TEST_BOX_DICTIONARY);
				break;

			case SEND_TEST_TO_CLASS_STATUS4:
				userGroupService.sendTestForUserGroupFinalStep(callBackQuery, user);
				break;

			default:
				throw new BotException(DictionaryUtil
						.getDictionaryValue(CANT_NOW,
								callBackQuery.getMessage().getFrom().getLanguageCode()), callBackQuery.getMessage());
		}

	}
}
