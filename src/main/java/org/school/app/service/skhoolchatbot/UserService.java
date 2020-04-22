package org.school.app.service.skhoolchatbot;


import org.school.app.dto.UserDataDTO;
import org.school.app.model.User;
import org.school.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import telegram.Message;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {


	private static final String INVALID_USER_ID = "Invalid user ID";

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
			user.setName(message.getFrom().getFirstName());

			if (message.getFrom().getLastName() != null) {
				user.setName(user.getName() + " " + message.getFrom().getLastName());
			}

			return userRepository.saveAndFlush(user);
		});
	}

	public Page<User> searchUsersByName(String name) {
		return userRepository.findByName(name, new PageRequest(0, 4));
	}

	public User getUser(Integer userId) {
		return userRepository.findById(userId).orElseThrow(
				() -> new IllegalArgumentException(INVALID_USER_ID));
	}

	@Transactional
	public void updateUser(UserDataDTO userDataDTO) {
		User user = getUser(userDataDTO.id);

		if (userDataDTO.keys.contains("nickName")) {
			user.setInternalNickName(userDataDTO.nickName);
		}
	}

	public Page<User> findAll(Pageable pageRequest) {
		return userRepository.findAll(pageRequest);
	}

	@Transactional
	public void createUsers(List<User> users) {
		users.forEach(u -> {
			userRepository.deleteById(u.getChatId());
			userRepository.saveAndFlush(u);
		});
	}
}
