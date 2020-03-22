package org.school.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
		CHOOSE_TEST_BOX_FOR_USER_USTATUS,
		TYPE_GROUP_NAME,
		REMOVE_CLASS_STATUS,
		REMOVE_CLASS_STATUS1

	}

	@OneToMany(cascade = CascadeType.ALL)
	private List<TestProcess> testProcesses = new ArrayList<>();

	@Id
	private Integer chatId;
	private String name;
	private Integer assigneeTestChatId;

	@Enumerated(EnumType.STRING)
	private UserStatus status;

}
