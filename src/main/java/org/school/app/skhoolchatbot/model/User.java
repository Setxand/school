package org.school.app.skhoolchatbot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

	public enum UserStatus {

		CHOOSE_TEST_BOX_USTATUS,
		TYPE_TEST_BOX_USTATUS,
		NEXT_QUESTION,
		TYPE_NAME_USTATUS,
		CHOOSE_USER_USTATUS,
		TYPE_TEST_BOX_FOR_USER_USTATUS,
		CHOOSE_TEST_BOX_FOR_USER_USTATUS

	}

	@OneToOne(cascade = CascadeType.ALL)
	private TestProcess testProcess = new TestProcess();

	@Id
	private Integer chatId;
	private String name;
	private Integer assigneeTestChatId;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

}
