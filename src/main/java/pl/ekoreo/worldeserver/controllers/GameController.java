package pl.ekoreo.worldeserver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ekoreo.worldeserver.enums.GameTypes;
import pl.ekoreo.worldeserver.services.GameManager;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameManager gameManager;
    public GameController(GameManager gameManager){
        this.gameManager = gameManager;
    }

    @GetMapping("/createGame")
    public ResponseEntity<String> createGame() {
        try {
            return new ResponseEntity<>(gameManager.CreateGame(GameTypes.WORDLE), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}