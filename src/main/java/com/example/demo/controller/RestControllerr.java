package com.example.demo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;


@RestController
public class RestControllerr {
	
	@Autowired
	private UserRepository uRepo;
	
	@GetMapping("/api/getUser/{userName}")
	public Optional<User> getUser(@PathVariable String userName) {
		return uRepo.findByUserName(userName);	
		}
	
}
