package com.practice.controller;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MyController {
	
	@RequestMapping(value = "/about", method = RequestMethod.GET)
	public String about(Model model) {
		
		System.out.println("Inside About Handler...");
		
		model.addAttribute("name", "Priyanshu");
		model.addAttribute("currentDate", new Date().toLocaleString());
		
		return "about";
		
	}
	
	//Handling Iteration
	@GetMapping("/example-loop")
	public String iterateHandler(Model m) {
		
		//Create a list, set collection etc...
		//This of function used in java 9 or above
		List<String> name = List.of("Priyanshu", "Sohan", "Rohan", "Mohan");
		m.addAttribute("name", name);
		
		return "iterator";
	}
	
	//Handler for conditional statements
	@GetMapping("/condition")
	public String conditionHandler(Model m) {
		
		System.out.println("Conditional Handler Executed...");
		m.addAttribute("isActive", true);
		m.addAttribute("gender", "F");
		
		List<Integer> list = List.of(5,7,3,9,4,2);
		m.addAttribute("mylist", list);
		
		return "condition";
	}
	
	//Handler for including fragments
	@GetMapping("/service")
	public String serviceHandler(Model m) {
		
		m.addAttribute("title", "My name is Priyanshu");
		m.addAttribute("subtitle", LocalDateTime.now().toString());
		
		return "service";
	}
	
	//For New About
	@GetMapping("/newabout")
	public String newAbout() {
		return "aboutnew";
	}
	
	@GetMapping("/contact")
	public String contact() {
		return "contact";
	}

}
