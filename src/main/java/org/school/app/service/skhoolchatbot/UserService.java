package org.school.app.service.skhoolchatbot;


import org.school.app.model.User;
import org.school.app.repository.UserRepository;
import org.school.app.utils.DictionaryUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import telegram.Message;

import static org.school.app.config.DictionaryKeysConfig.USER_ID_INVALID;

@Service
public class UserService {


	private final Integer ADMIN_ID;
	private final UserRepository userRepository;

	public UserService(@Value("${id.admin}") String adminId, UserRepository userRepository) {
		ADMIN_ID = Integer.valueOf(adminId);
		this.userRepository = userRepository;
	}


	public User createUser(Message message) {
		return userRepository.findById(message.getChat().getId()).orElseGet(() -> {
			User user = new User();
			user.setChatId(message.getChat().getId());
			user.setName(message.getFrom().getFirstName() + " " + message.getFrom().getLastName());
			return userRepository.saveAndFlush(user);
		});
	}

	public Page<User> searchUsersByName(String name) {
		return userRepository.findByName(name, new PageRequest(0, 4));
	}

	public User getUser(Integer userId) {
		return userRepository.findById(userId).orElseThrow(
				() -> new IllegalArgumentException(DictionaryUtil.getDictionaryValue(USER_ID_INVALID)));
	}
}
