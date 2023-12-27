import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    @FXML
    TextField title_directions;
    @FXML
    ListView<String> log;

    public void portInput(KeyEvent event){
        if(event.getCode().equals(KeyCode.ENTER)){
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainServerGUI.fxml"));
                loader.setController(this); // this saved me so much time omg
                Parent root = loader.load();
                Scene serverLog = new Scene(root);
                serverLog.getStylesheets().add("/Styles/MainServerGUI.css");
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Server Log");
                stage.setScene(serverLog);
                stage.show();
            }
            catch(Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            // set up the server and callback method to write the string to the log
            Server server = new Server((data) -> {
                Platform.runLater(() -> {
                    this.log.getItems().add(data.toString());
                });
            }, Integer.parseInt(title_directions.getText()));

            log.getItems().add("Server created on port " + Integer.parseInt(title_directions.getText()));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }
}
