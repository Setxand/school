package org.school.app.service.skhoolchatbot;

import org.school.app.client.TelegramClient;
import org.school.app.config.DictionaryKeysConfig;
import org.school.app.exception.BotException;
import org.school.app.model.Question;
import org.school.app.model.TestBox;
import org.school.app.model.TestProcess;
import org.school.app.model.User;
import org.school.app.service.TestBoxService;
import org.school.app.service.TestProcessService;
import org.school.app.utils.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;
import telegram.Message;

import java.time.LocalDateTime;
import java.util.List;

import static org.school.app.config.DictionaryKeysConfig.*;
import static org.school.app.model.User.UserStatus.TYPE_NAME_USTATUS;
import static org.school.app.model.User.UserStatus.TYPE_TEST_BOX_FOR_USER_USTATUS;
import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

@Service
public class TestService {

	private final TelegramClient telegramClient;
	private final TestBoxService testBoxService;
	private final UserService userService;
	private final TestProcessService testProcessService;

	public TestService(TelegramClient telegramClient, TestBoxService testBoxService, UserService userService,
					   TestProcessService testProcessService) {
		this.telegramClient = telegramClient;
		this.testBoxService = testBoxService;
		this.userService = userService;
		this.testProcessService = testProcessService;
	}

	public void startTest(Message message, User user, User.UserStatus userStatus) {
		telegramClient.simpleMessage(getDictionaryValue(DictionaryKeysConfig.TYPE_TEST_BOX_DICTIONARY,
				message.getFrom().getLanguageCode()), message);
		user.setStatus(userStatus);
	}

	public void chooseTestBox(Message message, User user, User.UserStatus status) {
		List<TestBox> testBoxes = testBoxService.getTestBoxesByName(message.getText());

		if (testBoxes.isEmpty()) {
			throw new BotException("There is not such test boxes", message);
		}

		telegramClient.sendTextBoxesAsButtons(testBoxes,
				DictionaryUtil.getDictionaryValue(TYPE_TEST_BOX_DICTIONARY,
						message.getFrom().getLanguageCode()), message);

		user.setStatus(status);
	}

	/**
	 *
	 * @param user - user, which is a receiver of the message (can be used for '/starttest' and '/sendtest')
	 *             in case of '/sendtest' user - receiver of thmessage, and it's redirected in previous method
	 *             'public void chooseTestBoxForUser(CallBackQuery callBackQuery, User user)' in the @param message.
	 *             in this case chatId of sender is held till method 'ediInlineButtons(message, user)'
	 */
	public void choosedTestBox(Message message, String testBoxId, User user) { //todo rename all the methods related to this action
		testProcessService.createTestProcess(testBoxId, user.getChatId(), message);

		ediInlineButtons(message, user);
		user.setStatus(User.UserStatus.NEXT_QUESTION);

		nextQuestion(message, user);
	}

	public void sendTest(Message message, User user) {
		telegramClient.simpleMessage(DictionaryUtil
				.getDictionaryValue(TYPE_NAME, message.getFrom().getLanguageCode()), message);
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
		TestProcess testProcess = testProcessService.getActiveTestProcess(user.getChatId());

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
		if (user.getMessageIdToEdit() != null) {
			telegramClient.editInlineButtons(null, message);
		}
		message.getChat().setId(user.getChatId()); // Destination to user, that is set to the method
	}

	private void nextQuestion(Message message, User user) {
		TestProcess testProcess = testProcessService.getActiveTestProcess(user.getChatId());///todo send to method "testproc" and testbox
		TestBox testBox = testBoxService.getTestBox(testProcess.getCurrentTestId());

		if (testProcess.getCurrentTestStep() == testBox.getQuestions().size()) {
			testEnded(message, testProcess, user.getName());
			user.setStatus(null);
			return;
		}

		Question question = testBox.getQuestions().get(testProcess.getCurrentTestStep());
		telegramClient.sendQuestion(question, message, user.getMessageIdToEdit());
		user.setMessageIdToEdit(null);
	}

	private String getAnswerVariant(String text) {
		return text.substring(0, 2);
	}

	private void testEnded(Message message, TestProcess testProcess, String userName) {
		String testEndedMessage = String.format(DictionaryUtil
				.getDictionaryValue(TEST_ENDED, message.getFrom().getLanguageCode()), testProcess.getMark());

		telegramClient.editMessageText(null, message, testEndedMessage);

		testProcess.setActive(false);
		testProcess.setEndTime(LocalDateTime.now());

		message.getChat().setId(testProcess.getSenderChatId());
		telegramClient.simpleMessage(DictionaryUtil
				.createTestConclusion(testProcess, userName, message.getFrom().getLanguageCode()), message);
	}
}
