package com.example.demo.controller;

import java.io.IOException;

import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ImageController {
	
	@Autowired
	private UserRepository uRepo;
	
	@PostMapping("/changeProfile")
	public String changeProfile(@RequestParam("profile") MultipartFile file, Model m, HttpServletRequest request ) {
		
		byte[] imgByte;
		HttpSession session = request.getSession();
		try {
			System.out.println("hi");
			int id =(int) session.getAttribute("id");
			imgByte = file.getBytes();
			String imgString = Base64.getEncoder().encodeToString(imgByte);
			Optional<User> u = uRepo.findById(id);
			User us = u.get();
			us.setImg(imgString);
			uRepo.save(us);
			return "redirect:/chatUI";
		} 
		catch (IOException e) {
			
			e.printStackTrace();
		}
		return "chatUI";
	}
	
}
