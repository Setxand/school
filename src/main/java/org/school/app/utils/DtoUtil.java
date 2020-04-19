package org.school.app.utils;

import org.school.app.dto.*;
import org.school.app.model.*;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class DtoUtil {

	public static TestBoxDTO testBox(TestBox testBox) {
		TestBoxDTO dto = new TestBoxDTO();
		dto.id = testBox.getId();
		dto.name = testBox.getName();
		dto.questions = testBox.getQuestions().stream().map(DtoUtil::question).collect(toList());
		return dto;
	}

	private static QuestionDTO question(Question question) {
		QuestionDTO dto = new QuestionDTO();
		dto.id = question.getId();
		dto.name = question.getName();
		dto.answers = question.getAnswers();
		dto.correctAnswer = question.getCorrectAnswer();
		dto.answers = question.getAnswers();
		return dto;
	}

	public static UserGroupDto userGroup(UserGroup userGroup) {
		UserGroupDto dto = new UserGroupDto();
		dto.id = userGroup.getId();
		dto.name = userGroup.getName();
		dto.users = userGroup.getUsers().stream().map(DtoUtil::user).collect(Collectors.toList());
		return dto;
	}

	private static UserDataDTO user(User user) {
		UserDataDTO dto = new UserDataDTO();
		dto.id = user.getChatId();
		dto.name = user.getName();
		dto.testProcesses = user.getTestProcesses().stream().map(DtoUtil::testProcess).collect(toList());
		return dto;
	}

	public static TestProcessDTO testProcess(TestProcess testProcess) {
		TestProcessDTO dto = new TestProcessDTO();
		dto.id = testProcess.getId();
		dto.currentTestStep = testProcess.getCurrentTestStep();
		dto.isActive = testProcess.getActive();
		dto.startTime = testProcess.getCreationTime();
		dto.endTime = testProcess.getEndTime();
		dto.mark = testProcess.getMark();
		return dto;
	}
}
