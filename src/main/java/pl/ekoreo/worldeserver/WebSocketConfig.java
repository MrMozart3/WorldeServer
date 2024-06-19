package pl.ekoreo.worldeserver;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import pl.ekoreo.worldeserver.handlers.CreateGameHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    CreateGameHandler createGameHandler;
    WebSocketConfig (CreateGameHandler createGameHandler) {
        this.createGameHandler = createGameHandler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(createGameHandler, "/createGame").setAllowedOrigins("*");
    }
}
