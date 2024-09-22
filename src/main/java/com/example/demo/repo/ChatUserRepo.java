package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ChatUser;
import com.example.demo.model.Status;

public interface ChatUserRepo extends JpaRepository<ChatUser, String>  {
	public List<ChatUser> findAllByStatus(Status status);
}
