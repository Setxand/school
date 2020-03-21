package org.school.app.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TestProcess {

	private static final String DATE_FORMATTER = "dd -MM-yyyy HH:mm:ss";

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	private String id;

	private Integer userChatId;
	private Integer senderChatId;

	private String currentTestId;
	private int currentTestStep;
	private int mark;
	private Boolean active = false;

	private LocalDateTime creationTime = LocalDateTime.now();
	private LocalDateTime endTime;

	public String getCreationTime() {
		return creationTime.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
	}

	public String getEndTime() {
		return endTime.format(DateTimeFormatter.ofPattern(DATE_FORMATTER));
	}
}
