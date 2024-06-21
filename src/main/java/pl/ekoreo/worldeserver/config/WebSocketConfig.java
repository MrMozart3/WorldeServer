package pl.ekoreo.worldeserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import pl.ekoreo.worldeserver.handlers.GameHandler;
import pl.ekoreo.worldeserver.interceptors.JoinGameInterceptor;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    GameHandler gameHandler;
    WebSocketConfig (GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(gameHandler, "/game")
                .setAllowedOrigins("*")
                .addInterceptors(new JoinGameInterceptor());
    }
}
