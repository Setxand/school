package org.school.app.tennisapp.validator;

import org.school.app.tennisapp.exception.BotException;
import org.school.app.tennisapp.model.User;
import org.springframework.stereotype.Component;
import telegram.Message;

@Component
public class CommandValidator {

	public void validateLogin(Message message, User user) {
		if (user.getLoginCookie() == null)
			throw new BotException("Login first", message);
	}
}
