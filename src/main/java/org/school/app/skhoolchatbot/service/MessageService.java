package org.school.app.skhoolchatbot.service;

import org.school.app.skhoolchatbot.exception.BotException;
import org.school.app.skhoolchatbot.model.User;
import org.springframework.stereotype.Service;
import telegram.Message;

import javax.transaction.Transactional;

@Service
public class MessageService {

	private final CommandService commandService;
	private final TestService testService;

	public MessageService(CommandService commandService, TestService testService) {
		this.commandService = commandService;
		this.testService = testService;
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
				typeTestBox(message, user);
				break;

			case NEXT_QUESTION:
				nextQuestion(message, user);
				break;

			default:
				throw new BotException(message);
		}

	}

	private void nextQuestion(Message message, User user) {
		testService.answer(message, user);
	}

	private void typeTestBox(Message message, User user) {
		testService.chooseTestBoxByStatus(message, user);
	}
}
