package com.example.demo.controller;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Status;
import com.example.demo.model.User;

import com.example.demo.repo.UserRepository;


import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class LoginController {

	@Autowired
	private UserRepository uRepo;
	
	
	
	@Autowired
	private JavaMailSender jms;

	

	@GetMapping("/")
	public String signin(HttpServletRequest req) {
		HttpSession session = req.getSession();
		
		if (session.getAttribute("id") != null) {
			session.invalidate();
			return "login";
		} else {
			return "login";
		}
	}

	@PostMapping("/signin")
	public String signin(@ModelAttribute User u, Model m, HttpServletRequest request) {
		String hashedPass =BCrypt.withDefaults().hashToString(12, u.getPassword().toCharArray());
		HttpSession session = request.getSession();
		
		
		if(u.getEmail().equals("admin@gmail.com") && (u.getPassword().equals("admin"))) {
			session.setAttribute("email", u.getEmail());
			System.out.println("login"+u.getEmail());
			return "redirect:/admin";
		}
		if (session != null && session.getAttribute("id") != null) {
			return "redirect:/chatUI";
		}
		else {
		if (uRepo.findByEmail(u.getEmail())!=null) {
			
			
			Optional<User> us = uRepo.findByEmail(u.getEmail());
		
			User user = us.get();
			
			
            BCrypt.Result result = BCrypt.verifyer().verify(u.getPassword().toCharArray(), user.getPassword());
            if(result.verified) {
			
			user.setStatus(Status.ONLINE);
			uRepo.save(user);
			session.setAttribute("fullname", user.getFullName());
			session.setAttribute("username", user.getUserName());
			session.setAttribute("id", user.getId());
			session.setAttribute("email",user.getEmail());
			session.setAttribute("password", hashedPass);
			
			return "redirect:/chatUI";
            }
            else {
            	return "login";
            }
		} else {
			return "login";
		}
		}
	}

	@GetMapping("/index")
	public String getIndex(HttpServletRequest req, Model m) {
		HttpSession session = req.getSession();
		if (session.getAttribute("id") != null) {
			m.addAttribute("fullname", session.getAttribute("fullname"));
			m.addAttribute("username", session.getAttribute("username"));
			return "index";
		} else {
			return "login";
		}
	}

	
	  @GetMapping("/chatUI")
	  public String getChatUI(Model m, HttpServletRequest req ) {
	
		  
	  HttpSession s = req.getSession();
		
	
	  
	  Optional<User> us = uRepo.findByEmail((String)s.getAttribute("email"));
	  
	  User user = us.get();
	  
	  
	  user.setStatus(Status.ONLINE);
	  uRepo.save(user);
	  String pp = user.getImg();
	  if(pp!=null) {
		  m.addAttribute("pp","data:image/jpeg;base64,"+pp); 	
	  }
	  else {
		  m.addAttribute("pp","/img/user_icon.png"); 
	  }
	  m.addAttribute("username",user.getUserName());
	  m.addAttribute("fullname",user.getFullName());
	  m.addAttribute("email",user.getEmail());
	  
	  return "ChatUI"; 
	  }
	  
	  @GetMapping("/settings")
	  public String setttings() {
		  return "settings";
	  }
	  
		

	  
}
