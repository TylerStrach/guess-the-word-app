import java.io.Serializable;
import java.util.ArrayList;

public class GameData implements Serializable {
    int incorrectGuessesStreak = 0;
    String category;
    String word;

    int incorrectSportGuesses = 0;
    boolean sportCorrect = false;

    int incorrectComputerGuesses = 0;
    boolean computerCorrect = false;

    int incorrectFoodGuesses = 0;
    boolean foodCorrect = false;

    String letterGuess; // guess from client
    int correctPosition; // -1 if incorrect guess
    ArrayList<Integer> currentWord = new ArrayList<>();

    int guessesLeft;
    boolean finished;

    public GameData(){
        // add the five places of correct guesses
        currentWord.add(0);
        currentWord.add(0);
        currentWord.add(0);
        currentWord.add(0);
        currentWord.add(0);
    }

    public void setWord(String s){
        this.word = s;
    }
    public void setCategory(String c){
        this.category = c;
    }

    public void setGuesses(int i){
        this.guessesLeft = i;
    }

    public void resetWord(){
        this.currentWord.replaceAll(ignored -> 0);
    }
}
