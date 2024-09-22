package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ChatMessage;

public interface ChatMessageRepo extends JpaRepository<ChatMessage, Integer>{
	 List<ChatMessage> findByChatId(String chatId);
}
