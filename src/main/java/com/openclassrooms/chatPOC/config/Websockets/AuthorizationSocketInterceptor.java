 package com.openclassrooms.chatPOC.config.Websockets;
import com.openclassrooms.chatPOC.config.UserDetailsServiceImpl;
import com.openclassrooms.chatPOC.security.services.JwtService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class AuthorizationSocketInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    final String bearerTokenString = "Bearer ";

    public AuthorizationSocketInterceptor(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        //Authenticate user on CONNECT
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
                    accessor.setUser(authentication);
                }
            }
        }
        return message;
    }
}