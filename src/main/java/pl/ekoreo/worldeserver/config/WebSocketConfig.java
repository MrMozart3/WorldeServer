package pl.ekoreo.worldeserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import pl.ekoreo.worldeserver.handlers.SocketsHandler;
import pl.ekoreo.worldeserver.interceptors.JoinGameInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    SocketsHandler socketsHandler;
    WebSocketConfig (SocketsHandler socketsHandler) {
        this.socketsHandler = socketsHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(socketsHandler, "/game")
                .setAllowedOrigins("*")
                .addInterceptors(new JoinGameInterceptor());
    }
}
