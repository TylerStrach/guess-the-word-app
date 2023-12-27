import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;


public class ClientGUI extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	// start on StartScreen
	@Override
	public void start(Stage primaryStage) throws Exception {
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/StartScreen.fxml"));
			Parent root = loader.load();
			Scene startScreen = new Scene(root);
			startScreen.getStylesheets().add("/Styles/StartScreen.css");
			primaryStage.setScene(startScreen);
			primaryStage.show();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
