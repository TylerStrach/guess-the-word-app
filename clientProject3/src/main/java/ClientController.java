import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    // data to pass between loaders/controllers/scenes
    GameData data;
    Client client;

    //Start screen
    @FXML
    TextField title_directions;

    //ClientStartingScreen
    @FXML
    Button topic1;
    @FXML
    Button topic2;
    @FXML
    Button topic3;

    //Guessing
    @FXML
    TextField category;
    @FXML
    TextField letter_guess;
    @FXML
    TextField lives;
    @FXML
    Button serverResponse;
    @FXML
    TextField letter0;
    @FXML
    TextField letter1;
    @FXML
    TextField letter2;
    @FXML
    TextField letter3;
    @FXML
    TextField letter4;

    //EndScreen
    @FXML
    Text results;


    //StartScreen to ClientStartingGUI
    public void portInput(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            try{
                Socket clientSocket = new Socket("127.0.0.1", Integer.parseInt(title_directions.getText()));
                Client client = new Client(clientSocket);
                this.client = client;
                client.start();
            } catch(Exception e){ // if the port isnt correct
                // update title directions
                title_directions.clear();
                title_directions.setPromptText("port not valid...try again");
                return;
            }

            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ClientStartingGUI.fxml"));
                Parent root = loader.load();

                // connect data within this controller
                ClientController c = loader.getController();
                c.client = this.client;
                c.data = new GameData();

                Scene clientMain = new Scene(root);
                clientMain.getStylesheets().add("/Styles/ClientStartingGUI.css");
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setTitle("ClientMainScreen");
                stage.setScene(clientMain);
                stage.show();
            }
            catch(Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    //ClientStartingGUI to Guessing (sport)
    public void guessGame1(ActionEvent e){
        this.data.setCategory("sports");
        this.data.setWord(null);
        this.data.resetWord();
        this.data.setGuesses(6);

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Guessing.fxml"));
            Parent root = loader.load();

            // connect data within this controller
            ClientController c = loader.getController();
            c.client = this.client;
            c.data = this.data;

            Scene clientMain = new Scene(root);
            clientMain.getStylesheets().add("/Styles/Guessing.css");
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setTitle("ClientGuessGame");
            c.category.setText("Sports");
            stage.setScene(clientMain);
            stage.show();
        }
        catch(Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    //ClientStartingGUI to Guessing (computer)
    public void guessGame2(ActionEvent e){
        this.data.setCategory("computers");
        this.data.setWord(null);
        this.data.resetWord();
        this.data.setGuesses(6);

        data.currentWord.replaceAll(ignored -> 0);
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Guessing.fxml"));
            Parent root = loader.load();

            // connect data within this controller
            ClientController c = loader.getController();
            c.client = this.client;
            c.data = this.data;

            Scene clientMain = new Scene(root);
            clientMain.getStylesheets().add("/Styles/Guessing.css");
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setTitle("ClientGuessGame");
            c.category.setText("Computers");
            stage.setScene(clientMain);
            stage.show();
        }
        catch(Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }

    }

    //ClientStartingGUI to Guessing (food)
    public void guessGame3(ActionEvent e){
        this.data.setCategory("foods");
        this.data.setWord(null);
        this.data.resetWord();
        this.data.setGuesses(6);

        data.currentWord.replaceAll(ignored -> 0);
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Guessing.fxml"));
            Parent root = loader.load();

            // connect data within this controller
            ClientController c = loader.getController();
            c.client = this.client;
            c.data = this.data;

            Scene clientMain = new Scene(root);
            clientMain.getStylesheets().add("/Styles/Guessing.css");
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setTitle("ClientGuessGame");
            c.category.setText("Foods");
            stage.setScene(clientMain);
            stage.show();
        }
        catch(Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    //Updates word based on server response
    public void updateBoard(GameData data, int tile){
        switch(tile){
            case(0):
                letter0.setText(String.valueOf(data.word.charAt(tile)));
                break;
            case(1):
                letter1.setText(String.valueOf(data.word.charAt(tile)));
                break;
            case(2):
                letter2.setText(String.valueOf(data.word.charAt(tile)));
                break;
            case(3):
                letter3.setText(String.valueOf(data.word.charAt(tile)));
                break;
            case(4):
                letter4.setText(String.valueOf(data.word.charAt(tile)));
                break;
        }
    }

    //Sends client guess to server and handles guess
    public void userGuess(ActionEvent e){
        data.letterGuess = letter_guess.getText();
        letter_guess.setPromptText("enter 1 letter guess");
        if(data.letterGuess.length() != 1){
            return;
        }

        try{
            client.out.writeObject(data); // send guess
            data = (GameData)client.in.readObject(); // wait for result from server
        } catch(Exception exception){
            System.out.println("i could not send data to the server!");
        }

        // update the server response button
        if(data.correctPosition == -1){
            serverResponse.setStyle("-fx-background-color: maroon");
        }
        else{
            serverResponse.setStyle("-fx-background-color: green");
            int i = 0;
            for(int tile : data.currentWord){
                if(tile == 1){
                    updateBoard(data, i);
                }
                i++;
            }
        }

        if(this.data.sportCorrect && this.data.computerCorrect && this.data.foodCorrect){
            endScreen(e);
            return;
        }

        if(data.finished){
            returnToMenu(e);
            return;
        }

        // update the lives counter
        lives.setText(String.valueOf(data.guessesLeft));

        // if failed to guess in time
        if(Objects.equals(lives.getText(), "0")){
            if(data.incorrectGuessesStreak == 3 || data.incorrectSportGuesses == 3|| data.incorrectComputerGuesses == 3 || data.incorrectFoodGuesses == 3){
                endScreen(e);
                return;
            }
            returnToMenu(e);
            return;
        }

    }

    // Guessing to ClientStartingGUI
    public void returnToMenu(ActionEvent e){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ClientStartingGUI.fxml"));
            Parent root = loader.load();

            // connect data within this controller
            ClientController c = loader.getController();
            c.client = this.client;
            c.data = this.data;

            Scene clientMain = new Scene(root);
            clientMain.getStylesheets().add("/Styles/ClientStartingGUI.css");
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setScene(clientMain);
            stage.show();

            // checks which categories were correct
            if(this.data.sportCorrect){
                c.topic1.setDisable(true);
                c.topic1.setStyle("-fx-background-color: green");
            }
            else if(this.data.incorrectSportGuesses == 1){
                c.topic1.setStyle("-fx-background-color: yellow");
            }
            else if(this.data.incorrectSportGuesses == 2){
                c.topic1.setStyle("-fx-background-color: orange");
            }

            if(this.data.computerCorrect){
                c.topic2.setDisable(true);
                c.topic2.setStyle("-fx-background-color: green");
            }
            else if(this.data.incorrectComputerGuesses == 1){
                c.topic2.setStyle("-fx-background-color: yellow");
            }
            else if(this.data.incorrectComputerGuesses == 2){
                c.topic2.setStyle("-fx-background-color: orange");
            }

            if(this.data.foodCorrect){
                c.topic3.setDisable(true);
                c.topic3.setStyle("-fx-background-color: green");
            }
            else if(this.data.incorrectFoodGuesses == 1){
                c.topic3.setStyle("-fx-background-color: yellow");
            }
            else if(this.data.incorrectFoodGuesses == 2){
                c.topic3.setStyle("-fx-background-color: orange");
            }
        }
        catch(Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    // Guessing to EndScreen
    public void endScreen(ActionEvent e){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EndScreen.fxml"));
            Parent root = loader.load();

            // connect data within this controller
            ClientController c = loader.getController();
            c.client = this.client;
            c.data = this.data;

            Scene clientMain = new Scene(root);
            clientMain.getStylesheets().add("/Styles/EndScreen.css");
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setScene(clientMain);
            stage.show();

            if(this.data.sportCorrect && this.data.computerCorrect && this.data.foodCorrect){
                c.results.setText("You have won the game congratulations!");
            }
            else{
                c.results.setText("You have lost the game...");
            }
        }
        catch(Exception exception) {
            exception.printStackTrace();
            System.exit(1);
        }
    }

    //Exit game button
    public void exitGame(ActionEvent event){
        System.exit(1);
    }

    //Creates new GameData object and goes to ClientStartingGUI
    public void newGame(ActionEvent event){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ClientStartingGUI.fxml"));
            Parent root = loader.load();

            // connect data within this controller
            ClientController c = loader.getController();
            c.client = this.client;
            c.data = new GameData(); // new game!

            Scene clientMain = new Scene(root);
            clientMain.getStylesheets().add("/Styles/ClientStartingGUI.css");
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(clientMain);
            stage.show();
        } catch(Exception exception){
            exception.printStackTrace();
            System.exit(1);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) { }
}
