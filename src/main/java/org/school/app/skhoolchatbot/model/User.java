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

	@OneToOne(cascade = CascadeType.ALL)
	private TestProcess testProcess = new TestProcess();

	@Id
	private Integer chatId;
	private String name;

	public enum UserStatus {
		CHOOSE_TEST_BOX_USTATUS,
		TYPE_TEST_BOX_USTATUS
	}

	@Enumerated(EnumType.STRING)
	private UserStatus status;

}
