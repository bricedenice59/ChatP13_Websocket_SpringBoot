package com.openclassrooms.chatPOC.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public MessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatmessage){
        messagingTemplate.convertAndSendToUser(chatmessage.getReceiverName(),"/specific", chatmessage);
    }

    @MessageMapping("/join")
    public void addUser(Principal principal){
        String joinMessage = principal.getName() + " has joined the chat";
        messagingTemplate.convertAndSend("/topic/join", joinMessage);
    }
}
