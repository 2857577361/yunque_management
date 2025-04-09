package com.ruoyi.framework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.ruoyi.framework.websocket.ClimateDataWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(climateDataHandler(), "/ws/climate")
                .setAllowedOriginPatterns("*");
    }

    @Bean
    public ClimateDataWebSocketHandler climateDataHandler() {
        return new ClimateDataWebSocketHandler();
    }
}