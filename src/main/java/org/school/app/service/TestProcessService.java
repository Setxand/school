package org.school.app.service;

import org.school.app.model.TestProcess;
import org.school.app.repository.TestProcessRepository;
import org.springframework.stereotype.Service;
import telegram.Message;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class TestProcessService {

	private static final String INVALID_TEST_PROCESS = "Invalid Test Process";

	private final TestProcessRepository testProcessRepo;

	public TestProcessService(TestProcessRepository testProcessRepo) {
		this.testProcessRepo = testProcessRepo;
	}

	@Transactional
	public void createTestProcess(String testBoxId, Integer chatId, Message message) {
		TestProcess testProcess = new TestProcess();
		testProcess.setCurrentTestId(testBoxId);
		testProcess.setUserChatId(chatId);
		testProcess.setSenderChatId(message.getChat().getId());
		setActive(testProcess, chatId, message.getFrom().getLanguageCode());
		testProcessRepo.save(testProcess);
	}

	public TestProcess getActiveTestProcess(Integer chatId) {
		return testProcessRepo.findByUserChatIdAndActiveIsTrue(chatId)
				.orElseThrow(() -> new IllegalArgumentException(INVALID_TEST_PROCESS));
	}

	private void setActive(TestProcess testProcess, Integer chatId, String languageCode) {
		Optional<TestProcess> activeProcess = testProcessRepo.findByUserChatIdAndActiveIsTrue(chatId);
		activeProcess.ifPresent(process -> process.setActive(false));

		testProcess.setActive(true);
	}
}
