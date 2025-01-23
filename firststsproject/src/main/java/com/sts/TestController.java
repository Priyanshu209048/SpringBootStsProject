package com.sts;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
	
	@RequestMapping("/testing")
	@ResponseBody
	public String handleTest() {
		return "This is the project using sts";
	}
	
	@GetMapping("/home")
	public String message(Model model) {
		model.addAttribute("data", "Hello <b>Spring</b>!");
		model.addAttribute("name", "Priyanshu Baral");
		model.addAttribute("course", "SpringBoot");
		return "home";
	}

}
