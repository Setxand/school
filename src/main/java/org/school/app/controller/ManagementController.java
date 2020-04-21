package org.school.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.school.app.dto.TestBoxDTO;
import org.school.app.dto.TestProcessDTO;
import org.school.app.dto.UserDataDTO;
import org.school.app.dto.UserGroupDto;
import org.school.app.model.TestBox;
import org.school.app.model.TestProcess;
import org.school.app.service.TestBoxService;
import org.school.app.service.TestProcessService;
import org.school.app.service.skhoolchatbot.UserGroupService;
import org.school.app.service.skhoolchatbot.UserService;
import org.school.app.utils.DtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ManagementController {

	@Autowired TestBoxService testBoxService;
	@Autowired ObjectMapper objectMapper;
	@Autowired UserGroupService userGroupService;
	@Autowired TestProcessService testProcessService;
	@Autowired UserService userService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/v1/tests")
	public void createTestBox(@RequestBody TestBoxDTO dto){
		testBoxService.createTestBox(dto);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PatchMapping("/v1/tests")
	public void updateTestBox(@RequestBody Map<String, Object> body) {
		TestBoxDTO dto = objectMapper.convertValue(body, TestBoxDTO.class);
		dto.keys = body.keySet();
		testBoxService.updateTestBox(dto);
	}

	@GetMapping("/v1/sync")
	public void synchronize(@RequestParam String query) {
		testBoxService.synchronize(query);
	}

	@GetMapping("/v1/tests")
	public Page<TestBoxDTO> getTestBoxes(@SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
													 												Pageable pageable) {
		return testBoxService.getTestBoxes(pageable).map(DtoUtil::testBox);
	}

	@GetMapping("/v1/tests/{testId}")
	public TestBoxDTO getTestBox(@PathVariable String testId) {
		return DtoUtil.testBox(testBoxService.getTestBox(testId));
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/v1/tests/{testId}")
	public void deleteTestBot(@PathVariable String testId) {
		testBoxService.deleteTestBox(testId);
	}

	@GetMapping("/v1/user-groups")
	public Page<UserGroupDto> userGroups(Pageable pageable) {
		return userGroupService.getUserGroups(pageable).map(DtoUtil::userGroup);
	}

	@GetMapping("/v1/test-processes/{userId}")
	public Page<TestProcessDTO> getTestProcesses(@PathVariable Integer userId, Pageable pageable) {
		Page<TestProcess> testProcessesModels = testProcessService.getAllProcessesByUserId(userId, pageable);

		List<String> testIds = testProcessesModels.map(TestProcess::getCurrentTestId).getContent();
		Map<String, String> testBoxMap = testBoxService.getTestBoxesByIds(testIds)
				.stream().collect(Collectors.toMap(TestBox::getId, TestBox::getName));

		return testProcessesModels.map(tp -> {
			TestProcessDTO testProcessDTO = DtoUtil.testProcess(tp);
			testProcessDTO.testName = testBoxMap.get(tp.getCurrentTestId());
			return testProcessDTO;
		});
	}

	@PatchMapping("/v1/users")
	public void updateUser(@RequestBody Map<String, Object> body) {
		UserDataDTO userDataDTO = objectMapper.convertValue(body, UserDataDTO.class);
		userDataDTO.keys = body.keySet();

		userService.updateUser(userDataDTO);
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
		return new ErrorResponse("INVALID_ID", e.getMessage());
	}
}
