package pl.ekoreo.worldeserver.interceptors;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class JoinGameInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        try {
            String query = request.getURI().getQuery();
            if (query != null) {
                String[] queryParams = query.split("&");
                for (String param : queryParams) {
                    String[] keyValue = param.split("=");
                    if(keyValue.length != 2){
                        return false;
                    }
                    attributes.put(keyValue[0], keyValue[1]);
                }
                return true;
            }
            return attributes.containsKey("gameId") && attributes.containsKey("nickname");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}
