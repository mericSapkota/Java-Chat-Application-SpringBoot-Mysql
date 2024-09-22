package com.example.demo.controller.group_chat;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


@Controller
public class GroupChatController {
	

	
	@MessageMapping("/chat.sendMessage")
	@SendTo("/topic/public")
	public GroupChatMessage sendMessage( @Payload GroupChatMessage chatMessage) {
		return chatMessage;
	}
	 
	@MessageMapping("/chat.addUser")
	@SendTo("/topic/public")
	public GroupChatMessage addUser(@Payload GroupChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		return chatMessage;
	}

}
