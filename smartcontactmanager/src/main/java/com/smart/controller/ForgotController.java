package com.smart.controller;

import java.security.Principal;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.service.EmailService;

import jakarta.mail.Session;
import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {
	
	Random random = new Random();
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//Email-id form open hander
	@RequestMapping("/forgot")
	public String openEmailForm(Model model) {
		
		model.addAttribute("title", "Forget Password Page");
		
		return "forgot_email_form";
	}
	
	@PostMapping("/send-otp")
	public String sendOTP(@RequestParam("email") String email, Model model, HttpSession session) {
		
		System.out.println("EMAIL: " + email);
		
		//Generating otp of 4 digit
		int otp = random.nextInt(10000 - 1000 + 1) + 1000;
		
		model.addAttribute("title", "Email Verification Page");
		
		System.out.println("OTP: " + otp);
		
		//Write a code for sending otp to email...
		String subject = "OTP to verify SmartContactManager account";
		String message = ""
				+ "<div>"
				+ "<p> Dear SmartContactManager user, <br>"
				+ "Your SmartContactManager Account One Time PIN is: " + otp + ", to verify your email"
				+ "</p>"
				+ "</div>";
		String to = email;
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		if (flag) {
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			
			session.setAttribute("message", new Message("OTP has been send to your registered email id...", "success"));
			
			return "verify_otp";
		} else {
			
			session.setAttribute("message", new Message("Check your email id!!", "danger"));
			
			return "forgot_email_form";
		}
		
	}
	
	//Verify OTP
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session, Model model){
		
		int myOtp = (int)session.getAttribute("myotp");
		String email = (String)session.getAttribute("email");
		
		if (myOtp == otp) {
			//Password change form
			User user = this.userRepository.getUserByUserName(email);
			model.addAttribute("title", "Change Password Page");
			
			if (user == null) {
				//send error message
				session.setAttribute("message", new Message("User does not exists!!", "danger"));
				model.addAttribute("title", "Email verification Page");
				
				return "forgot_email_form";	
			} else {
				//send change password form
				return "password_change_form";
			}
			/* return "password_change_form"; */
		} else {
			session.setAttribute("message", new Message("OTP is not matching!!", "danger"));
			model.addAttribute("title", "OTP verificatioon Page");
			return "verify_otp";
		}
		
	}
	
	//Change Password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session, Principal principal) {
		
		String email = (String)session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		System.out.println(user.getPassword());
		
		if (this.bCryptPasswordEncoder.matches(newpassword, user.getPassword())) {
			session.setAttribute("message", new Message("New Password can not be same as old password!! ", "danger"));
			return "password_change_form";
		} else {
			user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
			this.userRepository.save(user);
			//session.setAttribute("message", new Message("Your Password has successfully changed ", "success"));
			//redirect is used for redirecting to url
			return "redirect:/signin?change=Your Password has Successfully Changed";
		}
	}
	
	@PostMapping("/resend-otp")
	public String resendOTP(Model model, HttpSession session) {
		
		String email = (String)session.getAttribute("email");
		
		//Generating otp of 4 digit
		int otp = random.nextInt(10000 - 1000 + 1) + 1000;
		
		model.addAttribute("title", "Email Verification Page");
		
		System.out.println("OTP: " + otp);
		
		//Write a code for sending otp to email...
		String subject = "OTP to verify SmartContactManager account";
		String message = ""
				+ "<div>"
				+ "<p> Dear SmartContactManager user, <br>"
				+ "Your SmartContactManager Account One Time PIN is: " + otp + ", to verify your email"
				+ "</p>"
				+ "</div>";
		String to = email;
		boolean flag = this.emailService.sendEmail(subject, message, to);
		
		session.setAttribute("myotp", otp);
		session.setAttribute("message", new Message("OTP has been send to your registered email id...", "success"));
			
		return "verify_otp";
		
	}

	
}
