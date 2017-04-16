import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by harsh on 2017-03-25.
 */
public final class GUI {

	private static GUI gui = null;
	private static Stage primaryStage = null;

	public static GUI getGUI() {
	    if(gui == null) {
			gui = new GUI();
		}
		return gui;
    }

	public Stage makeSpotlightGui(Stage stage) {
		// Create a GridPane for UI elements
        GridPane grid = null;
        try {
            grid = (GridPane) FXMLLoader.load(getClass().getResource("GUI.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

		// Initialize Scene
		Scene scene = new Scene(grid);

		stage.setTitle("Spotlight");
        stage.setScene(scene);
		primaryStage = stage;

        return primaryStage;
	}

    public static Alert makeAlert(AlertType typeOfAlert, String title, String headerText, String contentText) {
		Alert alert = new Alert(typeOfAlert);
		alert.setHeaderText(headerText);
		alert.setTitle(title);
		alert.setContentText(contentText);

		return alert;
	}
}