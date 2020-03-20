package org.school.app.skhoolchatbot.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TestProcess {

	@Id
	private Integer chatId;
	private String currentTestId;
	private int currentTestStep;
	private int mark;
	private Integer assignerChatId;

	@ElementCollection
	private Set<String> testHistory = new HashSet<>();

	public TestProcess(Integer chatId) {
		this.chatId = chatId;
	}
}
