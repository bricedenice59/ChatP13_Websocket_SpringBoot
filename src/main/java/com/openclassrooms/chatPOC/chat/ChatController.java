package com.openclassrooms.chatPOC.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
public class ChatController {

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatmessage){
        return chatmessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatmessage,
                               SimpMessageHeaderAccessor headerAccessor){
        //Adds username in websocket session
        Objects.requireNonNull(headerAccessor.getSessionAttributes()).put("username", chatmessage.getSenderName());
        return chatmessage;
    }
}
