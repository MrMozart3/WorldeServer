package pl.ekoreo.worldeserver.games;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Data
@AllArgsConstructor
public class Player {
    private WebSocketSession session;
    private String nickname;
    public boolean sendText(String text){
        if(!session.isOpen()){
            return false;
        }
        try {
            session.sendMessage(new TextMessage(text));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
