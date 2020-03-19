package org.school.app.skhoolchatbot.service;

import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.service.TestBoxService;
import org.school.app.skhoolchatbot.client.TelegramClient;
import org.school.app.skhoolchatbot.config.DictionaryKeysConfig;
import org.school.app.skhoolchatbot.model.TestProcess;
import org.school.app.skhoolchatbot.model.User;
import org.school.app.skhoolchatbot.util.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;
import telegram.Message;

import java.util.ArrayList;
import java.util.List;

import static org.school.app.skhoolchatbot.config.DictionaryKeysConfig.TEST_ENDED;
import static org.school.app.skhoolchatbot.config.DictionaryKeysConfig.TYPE_TEST_BOX_DICTIONARY;
import static org.school.app.skhoolchatbot.model.User.UserStatus.CHOOSE_TEST_BOX_USTATUS;
import static org.school.app.skhoolchatbot.model.User.UserStatus.TYPE_TEST_BOX_USTATUS;
import static org.school.app.skhoolchatbot.util.DictionaryUtil.getDictionaryValue;

@Service
public class TestService {

	private final TelegramClient telegramClient;
	private final TestBoxService testBoxService;

	public TestService(TelegramClient telegramClient, TestBoxService testBoxService) {
		this.telegramClient = telegramClient;
		this.testBoxService = testBoxService;
	}

	public void startTest(Message message, User user) {
		telegramClient.simpleMessage(getDictionaryValue(DictionaryKeysConfig.TYPE_TEST_BOX_DICTIONARY), message);
		user.setStatus(TYPE_TEST_BOX_USTATUS);
	}

	public void chooseTestBoxByStatus(Message message, User user) {
		List<TestBox> testBoxex = new ArrayList<>(testBoxService.getTestBoxesByName(message.getText()));

		telegramClient.sendTextBoxesAsButtons(testBoxex,
				DictionaryUtil.getDictionaryValue(TYPE_TEST_BOX_DICTIONARY), message);

		user.setStatus(CHOOSE_TEST_BOX_USTATUS);
	}

	public void choosedTestBox(CallBackQuery callBackQuery, User user) {
		String testBoxId = callBackQuery.getData();
		TestProcess testProcess = user.getTestProcess();

		testProcess.setCurrentTestId(testBoxId);
		testProcess.setCurrentTestStep(0);
		testProcess.setMark(0);
//		testProcess.getTestHistory().add(testBoxId);

		user.setStatus(User.UserStatus.NEXT_QUESTION);
		nextQuestion(callBackQuery.getMessage(), user);
	}

	public void nextQuestion(Message message, User user) {
		TestProcess testProcess = user.getTestProcess();
		TestBox testBox = testBoxService.getTestBox(testProcess.getCurrentTestId());

		if (testProcess.getCurrentTestStep() == testBox.getQuestions().size()) {
			testEnded(message, user, testProcess);
			user.setStatus(null);
			return;
		}

		Question question = testBox.getQuestions().get(testProcess.getCurrentTestStep());

		if (user.getStatus() == User.UserStatus.NEXT_QUESTION) {
			checkAnswer(message, testProcess, question);
		}

		telegramClient.sendQuestion(question, message);
		testProcess.setCurrentTestStep(testProcess.getCurrentTestStep() + 1);
	}

	private void checkAnswer(Message message, TestProcess testProcess, Question question) {

		if (question.getCorrectAnswer().equals(message.getText().substring(0, 2))) {
			testProcess.setMark(testProcess.getMark() + 1);
		}
	}

	private void testEnded(Message message, User user, TestProcess testProcess) {
		String text = String.format(DictionaryUtil.getDictionaryValue(TEST_ENDED), testProcess.getMark());
		telegramClient.simpleMessage(text, message);
	}
}
