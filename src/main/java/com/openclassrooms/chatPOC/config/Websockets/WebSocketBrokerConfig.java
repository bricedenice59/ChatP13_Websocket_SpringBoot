package com.openclassrooms.chatPOC.config.Websockets;

import com.openclassrooms.chatPOC.config.security.UserDetailsServiceImpl;
import com.openclassrooms.chatPOC.security.services.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Slf4j
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    final String bearerTokenString = "Bearer ";

    public WebSocketBrokerConfig(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (nonNull(accessor) && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    //Extract JWT token from header, validate it and extract user authorities
                    var tokenAuthorization = accessor.getFirstNativeHeader("Authorization");
                    if (isNull(tokenAuthorization) || !tokenAuthorization.startsWith("Bearer" + " ")) {
                        // If there is no token present then we should interrupt handshake process and throw an AccessDeniedException
                        throw new AccessDeniedException("Not authorized");
                    }

                    var jwtToken = tokenAuthorization.substring(bearerTokenString.length());
                    var userEmail = jwtService.extractUserName(jwtToken);
                    if(userEmail != null && !userEmail.isEmpty()) {
                        var userDetails = userDetailsService.loadUserByUsername(userEmail);
                        if (jwtService.isTokenValid(jwtToken, userDetails)) {
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            accessor.setUser(authentication);
                        }
                    }
                }
                return message;
            }
        });
    }
}
