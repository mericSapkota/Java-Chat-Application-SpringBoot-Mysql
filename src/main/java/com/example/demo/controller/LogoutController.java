package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.demo.model.Status;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {
	
	
	@Autowired
	private UserRepository uRepo;
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		HttpSession session = req.getSession();
		int id =(int) session.getAttribute("id");
		if(id != 0) {
			Optional<User> u = uRepo.findById(id);
			User user = u.get();
			user.setStatus(Status.OFFLINE);
			uRepo.save(user);
			session.invalidate();
			
			return "login";
		}
		else {
			return "message";
		}
	}
	
}
