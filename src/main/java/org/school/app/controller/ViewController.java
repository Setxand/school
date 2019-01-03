package org.school.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

	@GetMapping("/create-test")
	public String createTest(Model model) {
		return "createTestBox";
	}

}
