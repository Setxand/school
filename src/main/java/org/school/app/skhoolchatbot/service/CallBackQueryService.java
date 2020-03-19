package org.school.app.skhoolchatbot.service;

import org.school.app.skhoolchatbot.exception.BotException;
import org.school.app.skhoolchatbot.model.User;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;

import javax.transaction.Transactional;

@Service
public class CallBackQueryService {

	private final TestService testService;

	public CallBackQueryService(TestService testService) {
		this.testService = testService;
	}

	@Transactional
	public void callBackQueryToBot(CallBackQuery callBackQuery, User user) {

		if (user.getStatus() != null) {
			checkByStatus(callBackQuery, user);
		} else
			throw new BotException(callBackQuery.getMessage());
	}

	private void checkByStatus(CallBackQuery callBackQuery, User user) {

		switch (user.getStatus()) {
			case CHOOSE_TEST_BOX_USTATUS:
				testService.choosedTestBox(callBackQuery, user);
				break;

			default:
				throw new BotException(callBackQuery.getMessage());
		}

	}

	public enum CallBackQueryPayload {

	}
}
