package org.school.app.service;

import org.apache.log4j.Logger;
import org.school.app.model.TestProcess;
import org.school.app.repository.TestProcessRepository;
import org.springframework.stereotype.Service;
import telegram.Message;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TestProcessService {

	private static final String INVALID_TEST_PROCESS = "Invalid Test Process";
	private static final Logger logger = Logger.getLogger(TestProcessService.class);
	private static final String DEACTIVATING_LOG =
			"THERE ARE NO ONE ACTIVE TEST PROCESSES, THE OLD PROCESSES ARE DEACTIVATING...";
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
		testProcess.setActive(true);
		testProcessRepo.save(testProcess);
	}

	public TestProcess getActiveTestProcess(Integer chatId) {
		List<TestProcess> testProcesses = testProcessRepo.findByUserChatIdAndActiveIsTrueOrderByCreationTimeDesc(chatId);

		if (testProcesses.size() > 1) {
			logger.warn(DEACTIVATING_LOG);
			for (int i = 1; i < testProcesses.size(); i++) {
				testProcesses.get(i).setActive(false);
			}
		}

		return testProcesses.get(0);
	}
}
