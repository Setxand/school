package org.school.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {

	@GetMapping("/create-test")
	public String createTest(Model model) {
		return "createTestBox";
	}

	@GetMapping("/catalog")
	public String catalog(Model model) {
		return "catalog";
	}

	@GetMapping("/test/{testId}")
	public String catalog(Model model, @PathVariable String testId) {
		model.addAttribute("testId", testId);
		return "test";
	}

}
