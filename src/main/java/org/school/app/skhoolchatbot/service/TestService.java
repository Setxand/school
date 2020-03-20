package org.school.app.skhoolchatbot.service;

import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.service.TestBoxService;
import org.school.app.skhoolchatbot.client.TelegramClient;
import org.school.app.skhoolchatbot.config.DictionaryKeysConfig;
import org.school.app.skhoolchatbot.exception.BotException;
import org.school.app.skhoolchatbot.model.TestProcess;
import org.school.app.skhoolchatbot.model.User;
import org.school.app.skhoolchatbot.util.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;
import telegram.Message;

import java.util.List;

import static org.school.app.skhoolchatbot.config.DictionaryKeysConfig.*;
import static org.school.app.skhoolchatbot.model.User.UserStatus.*;
import static org.school.app.skhoolchatbot.util.DictionaryUtil.getDictionaryValue;

@Service
public class TestService {

	private final TelegramClient telegramClient;
	private final TestBoxService testBoxService;
	private final UserService userService;

	public TestService(TelegramClient telegramClient, TestBoxService testBoxService, UserService userService) {
		this.telegramClient = telegramClient;
		this.testBoxService = testBoxService;
		this.userService = userService;
	}

	public void startTest(Message message, User user, User.UserStatus userStatus) {
		telegramClient.simpleMessage(getDictionaryValue(DictionaryKeysConfig.TYPE_TEST_BOX_DICTIONARY), message);
		user.setStatus(userStatus);
	}

	public void chooseTestBoxByStatus(Message message, User user) {
		List<TestBox> testBoxes = testBoxService.getTestBoxesByName(message.getText());

		if (testBoxes.isEmpty()) {
			throw new BotException("There is not such test boxes", message);
		}

		telegramClient.sendTextBoxesAsButtons(testBoxes,
				DictionaryUtil.getDictionaryValue(TYPE_TEST_BOX_DICTIONARY), message);

		user.setStatus(user.getStatus() == TYPE_TEST_BOX_FOR_USER_USTATUS ?
				CHOOSE_TEST_BOX_FOR_USER_USTATUS : CHOOSE_TEST_BOX_USTATUS);
	}

	public void choosedTestBox(Message message, String testBoxId, User user) {

		ediInlineButtons(message, user);

		TestProcess testProcess = user.getTestProcess();

		testProcess.setCurrentTestId(testBoxId);
		testProcess.setCurrentTestStep(0);
		testProcess.setMark(0);
//		testProcess.getTestHistory().add(testBoxId);

		user.setStatus(User.UserStatus.NEXT_QUESTION);
		nextQuestion(message, user);
	}

	public void sendTest(Message message, User user) {
		telegramClient.simpleMessage(DictionaryUtil.getDictionaryValue(TYPE_NAME), message);
		user.setStatus(TYPE_NAME_USTATUS);
	}

	public void startTestForUser(CallBackQuery callBackQuery, User user) {
		Message message = callBackQuery.getMessage();
		user.setAssigneeTestChatId(Integer.valueOf(callBackQuery.getData()));

		startTest(message, user, TYPE_TEST_BOX_FOR_USER_USTATUS);
	}

	public void chooseTestBoxForUser(CallBackQuery callBackQuery, User user) {
		User assigneeUser = userService.getUser(user.getAssigneeTestChatId());
		Message message = callBackQuery.getMessage();

		choosedTestBox(message, callBackQuery.getData(), assigneeUser);
	}

	public void answer(Message message, User user) {
		TestProcess testProcess = user.getTestProcess();
		TestBox testBox = testBoxService.getTestBox(testProcess.getCurrentTestId());
		Question question = testBox.getQuestions().get(testProcess.getCurrentTestStep());

		if (user.getStatus() == User.UserStatus.NEXT_QUESTION &&
				getAnswerVariant(question.getCorrectAnswer()).equals(getAnswerVariant(message.getText()))) {
			testProcess.setMark(testProcess.getMark() + 1);
		}

		testProcess.setCurrentTestStep(testProcess.getCurrentTestStep() + 1);
		nextQuestion(message, user);
	}

	private void ediInlineButtons(Message message, User user) {
		telegramClient.editInlineButtons(null, message);

		if (user.getStatus() == CHOOSE_TEST_BOX_FOR_USER_USTATUS) { //Redirect message to assignee user
			message.getChat().setId(user.getChatId());
		}
	}

	private void nextQuestion(Message message, User user) {
		TestProcess testProcess = user.getTestProcess();///todo send to method "testproc" and testbox
		TestBox testBox = testBoxService.getTestBox(testProcess.getCurrentTestId());

		if (testProcess.getCurrentTestStep() == testBox.getQuestions().size()) {
			testEnded(message, user, testProcess);
			user.setStatus(null);
			return;
		}

		Question question = testBox.getQuestions().get(testProcess.getCurrentTestStep());
		telegramClient.sendQuestion(question, message);
	}

	private String getAnswerVariant(String text) {
		return text.substring(0, 2);
	}

	private void testEnded(Message message, User user, TestProcess testProcess) {
		String text = String.format(DictionaryUtil.getDictionaryValue(TEST_ENDED), testProcess.getMark());
		telegramClient.simpleMessage(text, message);
		telegramClient.removeKeyboardButtons(message);
	}
}
