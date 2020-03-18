package org.school.app.tennisapp.service;

import org.school.app.tennisapp.client.Platform;
import org.school.app.tennisapp.model.User;
import org.springframework.stereotype.Service;
import telegram.Update;

@Service
public class DirectionService {

	private final UserService userService;
	private MessageService messageService;

	public DirectionService(UserService userService, MessageService messageService) {
		this.userService = userService;
		this.messageService = messageService;
	}

	public void directUpdate(Update update) {
			if (update.getMessage() != null) {
				update.getMessage().setPlatform(Platform.COMMON);
				User user = userService.createUser(update.getMessage());
				messageService.messageFromBot(update.getMessage(), user);
			}
		}
//        } else if (update.getCallBackQuery() != null) {
//            update.getCallBackQuery().getMessage().setPlatform(Platform.COMMON);
//            User user = createUser(update.getCallBackQuery().getMessage());
//            callBackQUeryService.callBackQueryToBot(update.getCallBackQuery(), user);
//        }



}
