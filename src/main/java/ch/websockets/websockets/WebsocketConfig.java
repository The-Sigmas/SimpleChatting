package ch.websockets.websockets;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(), "/time").setAllowedOrigins("*");
        registry.addHandler(new ChatHandler(), "/chat").setAllowedOrigins("*");
    }
}
