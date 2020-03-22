package org.school.app.service.skhoolchatbot;

import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.springframework.stereotype.Service;
import telegram.Message;

import javax.transaction.Transactional;

@Service
public class MessageService {

	private final CommandService commandService;
	private final TestService testService;
	private final UserGroupService userGroupService;

	public MessageService(CommandService commandService, TestService testService, UserGroupService userGroupService) {
		this.commandService = commandService;
		this.testService = testService;
		this.userGroupService = userGroupService;
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
				testService.chooseTestBoxByStatus(message, user);
				break;

			case NEXT_QUESTION:
				nextQuestion(message, user);
				break;

			case TYPE_NAME_USTATUS:
				typeNameSendTest(message, user);
				break;

			case TYPE_TEST_BOX_FOR_USER_USTATUS:
				testService.chooseTestBoxByStatus(message, user);
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
				userGroupService.addToUserGroupStep0(message, user);
				break;


			default:
				throw new BotException(message);
		}

	}

	private void typeNameSendTest(Message message, User user) {
		userGroupService.sendUserNamesByName(message, user);
	}

	private void nextQuestion(Message message, User user) {
		testService.answer(message, user);
	}

}
