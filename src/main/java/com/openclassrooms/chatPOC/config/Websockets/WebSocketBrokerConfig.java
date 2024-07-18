package com.openclassrooms.chatPOC.config.Websockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

    private final AuthorizationSocketInterceptor authorizationSocketInterceptor;

    public WebSocketBrokerConfig(AuthorizationSocketInterceptor authorizationSocketInterceptor){
        this.authorizationSocketInterceptor = authorizationSocketInterceptor;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // We have to register an authorizationInterceptor that will be responsible for
        // validating access-token and user access rights for any incoming CONNECTION
        // attempts.
        registration.interceptors(authorizationSocketInterceptor);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("topic", "/specific");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
