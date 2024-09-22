package com.example.demo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class RegisterController {
	
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private JavaMailSender jms;
	
//	@Autowired
//	private BCryptPasswordEncoder bcrypt;
	
	@PostMapping("/register")
	public String register(@ModelAttribute User u, HttpServletRequest req) {
		 
		String token = UUID.randomUUID().toString();
		
		String verificationUrl = req.getRequestURL().toString()+"/verify/token="+token;
		
		//mail
		
		  SimpleMailMessage mail = new SimpleMailMessage();
		  mail.setTo(u.getEmail());
		  mail.setSubject("Welcome to Meric's Chat App");
		  mail.setText("Welcome to my chat app. I hope You will have fun Best Regards. Please verify here "
		  +verificationUrl);
		  jms.send(mail);
		
		String hashpass =BCrypt.withDefaults().hashToString(12, u.getPassword().toCharArray());
		u.setPassword(hashpass);
		
		u.setVirificationToken(token);
		u.setVerified(false);
		
		uRepo.save(u);
		return "redirect:/";
	}
	
	@GetMapping("/verify")
	public String verify(@RequestParam ("token") String token, RedirectAttributes ra) {
		User u = uRepo.findByVirificationToken(token);
		
		if(u!=null) {
			u.setVerified(true);
			u.setVirificationToken(null);
			uRepo.save(u);
			ra.addAttribute("success","You have succesfully verified. You can login");
			return "redirect:/";
		}
		else {
			ra.addAttribute("failed","Verification failed sorry");
		return "reditect:/";
		}
	}
}
