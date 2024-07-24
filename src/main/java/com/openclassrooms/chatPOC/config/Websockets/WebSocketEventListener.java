package com.openclassrooms.chatPOC.config.Websockets;

import com.openclassrooms.chatPOC.chat.ChatMessage;
import com.openclassrooms.chatPOC.chat.enums.MessageType;
import com.openclassrooms.chatPOC.chat.services.ChatSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;
    private final ChatSessionService chatSessionService;

    @EventListener
    public void onWebSocketSubscribe(SessionSubscribeEvent event) {
        log.info("Subscribing to websocket endpoint: {}", event.getMessage().getHeaders().get("simpDestination").toString());
    }

    @EventListener
    public void onWebSocketUnsubscribe(SessionUnsubscribeEvent event) {
        log.info("UnSubscribing from endpoint: {}", event.getMessage().getHeaders().get("simpDestination").toString());
    }

    @EventListener
    public void onWebSocketConnect(SessionConnectEvent event) {
        log.info("Establishing WebSocket connection...");
    }

    @EventListener
    public void onWebSocketConnected(SessionConnectedEvent event) {
        var username = Objects.requireNonNull(event.getUser()).getName();
        if(username.isEmpty()) return;

        log.info("Connected with user: {}", username);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        var username = Objects.requireNonNull(event.getUser()).getName();
        if(username.isEmpty()) return;

        log.info("Received web socket disconnect event from: {}", username);

        var chatMessage = ChatMessage.builder()
                .messageType(MessageType.LEAVE)
                .senderName(username)
                .build();
        messageTemplate.convertAndSend("/topic/join", chatMessage);

        chatSessionService.clearSession();
    }
}
