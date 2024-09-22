package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;


@Controller
public class InfoController {

	@Autowired
	private UserRepository uRepo;
	
	@PostMapping("/changeSettings")
	public String postMethodName(User u, HttpServletRequest req, RedirectAttributes redirectAttribute) {
		
		HttpSession session = req.getSession();
		int id =(int) session.getAttribute("id");
		Optional<User> user = uRepo.findById(id);
		
	
		User us = user.get();
		us.setEmail(u.getEmail());
		us.setFullName(u.getFullName());
        BCrypt.Result result = BCrypt.verifyer().verify(u.getPassword().toCharArray(), us.getPassword());

		
		
		if(result.verified ) {
			redirectAttribute.addFlashAttribute("success", "You have succesfull changed your details");
			uRepo.save(us);
			return "redirect:/chatUI";
		}
		else {
			redirectAttribute.addFlashAttribute("failed","Sorry your password is incorrect");
			return "redirect:/chatUI";
		}
		
		
		
		
		
		
		
		
	}
	
}
