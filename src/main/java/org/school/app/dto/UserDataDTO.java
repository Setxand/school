package org.school.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Set;

public class UserDataDTO {

	public Integer id;
	public String name;
	public String nickName;

	public List<TestProcessDTO> testProcesses;

	@JsonIgnore
	public Set<String> keys;

}
