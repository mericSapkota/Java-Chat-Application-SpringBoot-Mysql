package com.example.demo.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.example.demo.controller.group_chat.GroupChatMessage;
import com.example.demo.controller.group_chat.GroupMessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
	
	
	private final SimpMessageSendingOperations messageTemplate;
	@EventListener
	public void handleWebSocketDisconnectListener(
		
			SessionDisconnectEvent event
		)
	{
		StompHeaderAccessor ha = StompHeaderAccessor.wrap(event.getMessage());
		String username = (String) ha.getSessionAttributes().get("username");
		if(username !=null) {
			log.info("User Disconnected : {} ",username); 
			var chatMessage = GroupChatMessage.builder().type(GroupMessageType.LEAVE).sender(username).build();
			messageTemplate.convertAndSend("/topic/public",chatMessage);
		}
	}
}
