package com.holidaydessert.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.holidaydessert.constant.AllowedOrigin;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// /topic：聊天室廣播；/queue：個人訊息（例如 /user/queue/errors 錯誤通知）
		config.enableSimpleBroker("/topic", "/queue");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-chat")
        .setAllowedOrigins(AllowedOrigin.OFFICAIL_ARRAY) // 安全起見 明確指定來源 
        // .setAllowedOriginPatterns("*")
        .withSockJS();
	}
	
}