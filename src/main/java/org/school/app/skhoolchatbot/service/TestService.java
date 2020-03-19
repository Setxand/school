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
		testProcess.getTestHistory().add(testBoxId);

		nextQuestion(callBackQuery, user, testProcess);
	}

	public void nextQuestion(CallBackQuery callBackQuery, User user, TestProcess testProcess) {
		TestBox testBox = testBoxService.getTestBox(testProcess.getCurrentTestId());


		Question question = testBox.getQuestions().get(testProcess.getCurrentTestStep());

		Message message = callBackQuery.getMessage();

		telegramClient.sendQuestion(question, message);

		testProcess.setCurrentTestStep(testProcess.getCurrentTestStep() + 1);
	}
}
