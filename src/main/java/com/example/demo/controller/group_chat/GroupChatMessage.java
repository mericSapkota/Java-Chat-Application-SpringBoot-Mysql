package com.example.demo.controller.group_chat;

import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class GroupChatMessage {
    private String content;
    private String sender;
    private GroupMessageType type;

    
    }


