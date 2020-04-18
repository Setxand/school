package org.school.app.service.skhoolchatbot;

import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.springframework.stereotype.Service;
import telegram.Message;

import javax.transaction.Transactional;

import static org.school.app.model.User.UserStatus.*;

@Service
public class MessageService {

	private final CommandService commandService;
	private final TestService testService;
	private final UserGroupService userGroupService;
	private final SendActionService sendActionService;

	public MessageService(CommandService commandService, TestService testService, UserGroupService userGroupService,
						  SendActionService sendActionService) {
		this.commandService = commandService;
		this.testService = testService;
		this.userGroupService = userGroupService;
		this.sendActionService = sendActionService;
	}

	@Transactional
	public void messageFromBot(Message message, User user) {
		if (message.getText().contains("/")) {
			user.setStatus(null);
			commandService.commandToBot(message, user);

		} else if (user.getStatus() != null) {
			checkByStatus(message, user);
		}
	}

	private void checkByStatus(Message message, User user) {

		switch (user.getStatus()) {
			case TYPE_TEST_BOX_USTATUS:
				testService.chooseTestBox(message, user, CHOOSE_TEST_BOX_USTATUS);
				break;

			case TYPE_NAME_USTATUS:
				typeNameSendTest(message, user);
				break;

			case TYPE_TEST_BOX_FOR_USER_USTATUS:
				testService.chooseTestBox(message, user, CHOOSE_TEST_BOX_FOR_USER_USTATUS);
				break;

			case TYPE_GROUP_NAME:
				userGroupService.createUserGroupStep1(message, user);
				break;

			case REMOVE_CLASS_STATUS:
				userGroupService.removeUserGroupStep1(message, user);
				break;

			case ADD_TO_CLASS_STATUS2:
				userGroupService.addUserGroupStep2(message, user);
				break;

			case ADD_TO_CLASS_STATUS:
				sendActionService.sendGroupNamesForUsers(message, user, ADD_TO_CLASS_STATUS1);
				break;

			case REMOVE_FROM_CLASS_STATUS1:
				sendActionService.sendGroupNamesForUsers(message, user, REMOVE_FROM_CLASS_STATUS2);
				break;

			case REMOVE_FROM_CLASS_STATUS3:
				userGroupService.removeUserFromGroupStep3(message, user);
				break;

			case SEND_TEST_TO_CLASS_STATUS1:
				prepareToStatus1(message, user);
				break;

			case SEND_TEST_TO_CLASS_STATUS3:
				testService.chooseTestBox(message, user, SEND_TEST_TO_CLASS_STATUS4);
				break;

			default:
				throw new BotException(message);
		}

	}

	private void prepareToStatus1(Message message, User user) {
		user.setMessageIdToEdit(null);
		user.setStatus(null);
		user.setAssigneeTestChatId(null);
		sendActionService.sendGroupNamesForUsers(message, user, SEND_TEST_TO_CLASS_STATUS2);
	}

	private void typeNameSendTest(Message message, User user) {
		userGroupService.sendUserNamesByName(message, user);
	}

}
