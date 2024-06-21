package pl.ekoreo.worldeserver.games.wordle;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;
import pl.ekoreo.worldeserver.exceptions.handleInput.HandleInputException;
import pl.ekoreo.worldeserver.games.Player;

@Getter
@Setter
public class WordlePlayer extends Player {
    private char[] answer;
    private int currentRow;
    private boolean sentWord;
    private boolean guessedWord;
    private int points;
    private WordleBoard board;
    private WordleGame wordleGame;
    public WordlePlayer(WordleGame wordleGame, WebSocketSession session, String nickname) {
        super(session, nickname);
        this.wordleGame = wordleGame;

        sentWord = false;
        guessedWord = false;
        board = new WordleBoard();
    }
    public void ResetBoard(){
        board.clearBoard();
        sentWord = false;
        guessedWord = false;
    }
    public void AddAnswer(int row, char[] word) throws HandleInputException {
        if(!WordleUtils.VerifyWord(word)){
            throw new HandleInputException("Invalid word");
        }

        char[] tempAnswer = new char[5];
        for(int i = 0; i < 5; i++){
            tempAnswer[i] = answer[i];
        }
        if(board.AddAnswer(row, word, tempAnswer)){
            guessedWord = true;
            sentWord = true;
        }
        else{
            sentWord = true;
        }
    }
}