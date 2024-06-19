package pl.ekoreo.worldeserver;

import org.springframework.web.socket.WebSocketSession;
import lombok.Getter;

public class Player {
    private WebSocketSession session;

    @Getter
    private String name;

    Player (WebSocketSession session, String name){
        this.session = session;
        this.name = name;
    }
    public String getSessionId(){
        return session.getId();
    }
}
