package org.school.app.tennisapp.service;

import org.school.app.tennisapp.exception.BotException;
import org.school.app.tennisapp.model.User;
import org.school.app.tennisapp.util.DictionaryUtil;
import org.school.app.tennisapp.validator.CommandValidator;
import org.springframework.stereotype.Service;
import telegram.Message;
import telegram.client.TelegramClient;

import javax.transaction.Transactional;

import static org.school.app.tennisapp.config.DictionaryKeysConfig.*;

@Service
public class CommandService {

	public static final String LOGIN = "/login";
	public static final String START = "/start";
	public static final String BOOK_TABLE = "/booktable";
	public static final String RESET_CREDS = "/reentercredentials";
	public static final String CANCEL_GAME = "/cancelgame";
	public static final String CHECK_ORDER = "/checkorder";
	public static final String START_TEST = "/starttest";

	private final TelegramClient telegramClient;
	private final TennisService tennisService;
	private final CommandValidator validator;
	private final TestService testService;

	public CommandService(TelegramClient telegramClient, TennisService tennisService,
						  CommandValidator validator, TestService testService) {
		this.telegramClient = telegramClient;
		this.tennisService = tennisService;
		this.validator = validator;
		this.testService = testService;
	}

	@Transactional
	public void commandToBot(Message message, User user) {
		user.setStatus(null);
		String command = message.getText();

		switch (command) {
			case START:
				start(message, user);
				break;

			case LOGIN:
				login(message, user);
				break;

			case BOOK_TABLE:
				bookTable(message, user);
				break;

			case RESET_CREDS:
				reenterCredentials(message, user);
				break;

			case CANCEL_GAME:
				cancelGame(message, user);
				break;

			case CHECK_ORDER:
				checkOrder(message, user);
				break;

			case START_TEST:
				startTest(message, user);
				break;


			default:
				throw new BotException("I don`t know such command", message);
		}
	}

	private void startTest(Message message, User user) {
		testService.startTest(message, user);
	}

	private void checkOrder(Message message, User user) {
		validator.validateLogin(message, user);
		tennisService.sendQueueList(message, user);
	}

	private void bookTable(Message message, User user) {
		validator.validateLogin(message, user);
		tennisService.bookTable(message, user);
	}

	private void start(Message message, User user) {
		telegramClient.helloMessage(message);
		login(message, user);
	}

	private void cancelGame(Message message, User user) {
		validator.validateLogin(message, user);
		tennisService.cancelGame(message, user);
		telegramClient.simpleMessage(DictionaryUtil.getDictionaryValue(GAME_CANCELLED), message);
	}

	public void login(Message message, User user) {

		if (user.getLogin() != null && user.getPassword() != null) {
			tennisService.login(user);
			telegramClient
					.simpleMessage(DictionaryUtil.getDictionaryValue(LOGIN_SUCCESS), message);

		} else {
			telegramClient
					.simpleMessage(DictionaryUtil.getDictionaryValue(LOGIN_PROCESS), message);

			user.setStatus(User.UserStatus.LOGIN);
		}

	}

	private void reenterCredentials(Message message, User user) {
		validator.validateLogin(message, user);
		user.setLogin(null);
		user.setPassword(null);
		login(message, user);
	}
}
