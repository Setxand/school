package org.school.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.school.app.dto.TestBoxDTO;
import org.school.app.dto.UserGroupDto;
import org.school.app.service.TestBoxService;
import org.school.app.service.skhoolchatbot.UserGroupService;
import org.school.app.utils.DtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class ManagementController {

	@Autowired TestBoxService testBoxService;
	@Autowired ObjectMapper objectMapper;
	@Autowired private UserGroupService userGroupService;

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

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(IllegalArgumentException.class)
	public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
		return new ErrorResponse("INVALID_ID", e.getMessage());
	}
}
