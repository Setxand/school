package org.school.app.tennisapp.service;

import org.school.app.tennisapp.model.User;
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
			commandService.commandToBot(message, user);

		} else if (user.getStatus() != null) {
			checkByStatus(message, user);
		}
	}

	private void checkByStatus(Message message, User user) {

		switch (user.getStatus()) {
			case LOGIN:
				loginFinalStep(message, user);
				break;

			case CHOOSE_TEXT_BOX:
				chooseTestBox(message, user);
				break;

			default:
				throw new RuntimeException();
		}

	}

	private void chooseTestBox(Message message, User user) {
		testService.chooseTestBoxByStatus(message, user);
	}

	private void loginFinalStep(Message message, User user) {
		String[] credentials = message.getText().split(" ");

		user.setLogin(credentials[0].trim());
		user.setPassword(credentials[1].trim());

		commandService.login(message, user);

		user.setStatus(null);
	}
}
