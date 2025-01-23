package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	
	//Method to adding common data to response
	@ModelAttribute
	public void addCommonData(Model m, Principal principal) {
		
		String userName = principal.getName();
		System.out.println("USERNAME: " + userName);
		
		//Get the user using username(email)
		User user = userRepository.getUserByUserName(userName);
		System.out.println("USER: " + user);
		
		m.addAttribute("user", user);
		
	}
	
	//Dashboard Home
	@GetMapping("/index")
	public String dashboard(Model model, Principal principal) {
		
		model.addAttribute("title", "User Dashboard");
		
		return "normal/user_dashboard";
	}
	
	//Open and add form handler
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model) {
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
		
	}
	
	//Processing add contact form
	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute Contact contact, BindingResult bindingResult,
								  @RequestParam("image") MultipartFile file,
								  Principal principal, HttpSession session) {
		
		try {
			String name = principal.getName();
			User user = this.userRepository.getUserByUserName(name);
			
			//Processing and uploading file
			if (file.isEmpty()) {
				
				//if the file is empty
				System.out.println("File is empty");
				contact.setImage("contact.png");
				
			} else {

				//upload file and update the name to contact
				contact.setImage(file.getOriginalFilename());
				
				File saveFile = new ClassPathResource("static/img").getFile();
				
				//This is the path where we want to save the image
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); //Here copy option replace means it replace the existing image if present 
				
				System.out.println("Image is uploaded");
				
			}
			
			user.getContacts().add(contact);	//We write this because we have to set the contact one by one in the List
			//user.setContacts((List<Contact>) contact);
			contact.setUser(user);	//This is bidirectional mapping that's why first we set user in contact then we get contacts from the user.
			
			
			
			this.userRepository.save(user);
			
			System.out.println("Added to database");
			
			//Success Message
			//we use session here because we this message for small interval of time
			session.setAttribute("message", new Message("Your contact is added !! ", "success"));
			
			System.out.println("DATA : " + contact);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ERROR : " + e.getMessage());
			e.printStackTrace();
			//Error Message
			session.setAttribute("message", new Message("Something went wrong !! ", "danger"));
		}
		
		return "normal/add_contact_form";
		
	}
	
	//Show contacts handler
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model m, Principal principal) {
		
		m.addAttribute("title", "Show User Contacts");
		
		//Sending the list of contacts
		//First we get the userName then from this name we get the user
		String userName = principal.getName(); 
		User user = this.userRepository.getUserByUserName(userName); 
		//user.getContacts();
		
		//Contacts per page = 5[n]
		//Current page = 0[page]
		PageRequest pageable = PageRequest.of(page, 5);
		//In this we get the contacts from the contact table but fetching the user id before doing this we first get the user
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);
		
		m.addAttribute("contacts", contacts);
		m.addAttribute("date", new Date());
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		
		return "normal/show_contacts";
	}
	
	//Showing particular contact details
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		
		System.out.println("CID " + cId);
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		String userName = principal.getName();
		User user = this.userRepository.getUserByUserName(userName);
		
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}
		
		return "normal/contact_detail";
		
	}
	
	//Delete contact handler
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") Integer cid, Model model, HttpSession session) {
		
		Optional<Contact> contacOptional = this.contactRepository.findById(cid);
		Contact contact = contacOptional.get();
		
		System.out.println("Contact: " + contact.getcId());
		
		//contact.setUser(null);
		
		this.contactRepository.deleteContactById(contact.getcId());
		
		System.out.println("DELETED");
		
		session.setAttribute("message", new Message("Contact deleted successfully...", "success"));
		
		return "redirect:/user/show-contacts/0";
		
	}
	
	//Open update handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model model) {
		
		model.addAttribute("title", "Update Contact");
		
		Contact contact = this.contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);
		
		return "normal/update_form";
		
	}
	
	//Update contact handler
	@PostMapping("/process-update")
	public String updateHandler(@Valid @ModelAttribute Contact contact, BindingResult bindingResult,
								@RequestParam("image") MultipartFile file,
								Model m, Principal principal, HttpSession session) {

		try {
			
			//Old contact details
			Contact oldContactDetail = this.contactRepository.findById(contact.getcId()).get();
			System.out.println(oldContactDetail.getImage());
			
			//delete old photo
			if (oldContactDetail.getImage() == "contact.png") {
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContactDetail.getImage());
				file1.delete();
			}
			System.out.println(oldContactDetail.getImage());
			System.out.println(file.getOriginalFilename());
			//image
			if (!file.isEmpty()) {
				//file work, rewrite
				
				//update new photo
				File saveFile = new ClassPathResource("static/img").getFile();
				
				//This is the path where we want to save the image
				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
				
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING); //Here copy option replace means it replace the existing image if present
				
				contact.setImage(file.getOriginalFilename());
				
			} else {
				contact.setImage(oldContactDetail.getImage());
			}
			
			String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);
			
			contact.setUser(user);
			
			this.contactRepository.save(contact);
			
			session.setAttribute("message", new Message("Your contact is updated...", "success"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "redirect:/user/"+contact.getcId()+"/contact";
		
	}
	
	//Your Profile Handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		
		model.addAttribute("title", "Profile Page");
		
		return "normal/profile";
		
	}
	
	//Open Setting Handler
	@GetMapping("/settings")
	public String openSettings(Model model) {
		
		model.addAttribute("title", "Setting Page");
		
		return "normal/settings";
		
	}
	
	//Change Password Handler
	@PostMapping("change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal, HttpSession session) {
		
		System.out.println("OLD PASSWORD: " + oldPassword);
		System.out.println("NEW PASSWORD: " + newPassword);
		
		String userName = principal.getName();
		User currentUser = this.userRepository.getUserByUserName(userName);
		System.out.println(currentUser.getPassword());
		
		if (this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			//Change the Password
			if (this.bCryptPasswordEncoder.matches(newPassword, currentUser.getPassword())) {
				//Error...
				session.setAttribute("message", new Message("New Password can not be same as old password!! ", "danger"));
				return "redirect:/user/settings";
			}else {
				currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
				this.userRepository.save(currentUser);
				session.setAttribute("message", new Message("Your Password has successfully changed ", "success"));
			}
			
		} else {
			//Error...
			session.setAttribute("message", new Message("You have entered the wrong old password!! ", "danger"));
			return "redirect:/user/settings";
		}
		
		return "redirect:/user/index";
	}

}
