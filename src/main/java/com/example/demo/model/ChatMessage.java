package com.example.demo.model;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class ChatMessage {
	
	    @Id
	   @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private int id;
	    private String chatId;
	    private String senderId;
	    private String recipientId;
	    private String content;
	    private Date timestamp;
	    
	}

