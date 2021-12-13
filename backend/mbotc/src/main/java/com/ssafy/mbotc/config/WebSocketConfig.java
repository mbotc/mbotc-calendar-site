package com.ssafy.mbotc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/sub"); // enableSimpleBroker는 클라이언트로 메세지를 응답해줄 때 prefix를 정의 (받을 때)
		registry.setApplicationDestinationPrefixes("/pub"); // setApplicationDestinationPrefixes는 클라이언트에서 메세지 송신 시 붙여줄 prefix를 정의 (보낼 때)
	}
	
	@Override // 최초 소켓 연결을 하는 경우, endpoint가 되는 url이다. 추후 javascript에서 SockJS 생성자를 통해 연결
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/websocket").setAllowedOriginPatterns("*").withSockJS();
	}
	
}
