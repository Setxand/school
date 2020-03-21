package org.school.app.service.skhoolchatbot;

import org.school.app.config.DictionaryKeysConfig;
import org.school.app.exception.BotException;
import org.school.app.model.User;
import org.school.app.utils.DictionaryUtil;
import org.springframework.stereotype.Service;
import telegram.CallBackQuery;

import javax.transaction.Transactional;

@Service
public class CallBackQueryService {

	public enum CallBackQueryPayload {

	}

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
				testService.choosedTestBox(callBackQuery.getMessage(), callBackQuery.getData(), user);
				break;

			case CHOOSE_USER_USTATUS:
				testService.startTestForUser(callBackQuery, user);
				break;

			case CHOOSE_TEST_BOX_FOR_USER_USTATUS:
				testService.chooseTestBoxForUser(callBackQuery, user);
				break;

			default:
				throw new BotException(DictionaryUtil
						.getDictionaryValue(DictionaryKeysConfig.CANT_NOW), callBackQuery.getMessage());
		}

	}
}
