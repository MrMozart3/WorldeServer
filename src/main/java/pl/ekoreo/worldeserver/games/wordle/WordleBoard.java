package pl.ekoreo.worldeserver.games.wordle;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class WordleBoard {
    public static final char COLOR_WHITE = 'W';
    public static final char COLOR_ORANGE = 'O';
    public static final char COLOR_GREEN = 'G';
    //TODO change to private and make getter
    private char[][] board;
    private char[][] colors;
    public WordleBoard(){
        board = new char[6][5];
        colors = new char[6][5];
        clearBoard();
    }
    public void clearBoard(){
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 5; j++){
                board[i][j] = '-';
                colors[i][j] = 'W';
            }
        }
    }
    /**
     *
     * @param row row number(0 to 5)
     * @param word word to be added (5 characters)
     * @param answer current answer (5 characters)
     * @return returns false if word is incorrect, true if word is correct
     */
    public boolean AddAnswer(int row, char[] word, char[] answer){
        /*if(word == null || answer == null){
            return -1;
        }
        if(word.length != 5 || answer.length != 5){
            return -1;
        }*/
        //TODO check word and answer in alphabet
        //TODO check if word is in dictionary, if isn't return -1
        for(int i = 0; i < 5; i++){
            board[row][i] = word[i];
        }
        boolean isCorrect = true;
        for(int i = 0; i < 5; i++){
            if(word[i] == answer[i]) {
                colors[row][i] = COLOR_GREEN;
                word[i] = '-';
                answer[i] = '-';
            } else {
                isCorrect = false;
            }
        }
        if(isCorrect){
            return true;
        }
        for(int i = 0; i < 5; i++){
            if(word[i] == '-') continue;

            for(int j = 0; j < 5; j++){
                if(answer[j] == '-') continue;
                if(word[i] == answer[j]){
                    colors[row][i] = COLOR_ORANGE;
                    word[i] = '-';
                    answer[j] = '-';
                    break;
                }
            }
        }
        return false;
    }

}
