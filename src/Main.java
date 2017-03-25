import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Light;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.security.NoSuchAlgorithmException;

/**
 * Created by harsh on 2017-03-25.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {

        GUI gui = new GUI();
        Spotlight spotlight = new Spotlight();

//         Create a GridPane for UI elements
        GridPane grid = gui.addGridPane(Pos.CENTER, 10, 10, new Insets(25, 25, 25, 25));

//         Create UI elements
        Text scenetitle = gui.addText("Select a location to copy images", Font.font("Tahoma", FontWeight.NORMAL, 18));
        Button browseButton = gui.addButton("...", true);
        Label path = new Label();
        Tooltip tooltip = new Tooltip(); // tooltip to show path (in case part of a long path is hidden)
        Button okButton = gui.addButton("Go", false);

//        Add listeners
        browseButton.setOnAction(e -> {
            File selectedDirectory = new DirectoryChooser().showDialog(primaryStage);

            if (selectedDirectory != null) {
                tooltip.setText(selectedDirectory.getAbsolutePath());
                path.setText(selectedDirectory.getAbsolutePath());
                path.setTooltip(tooltip);
                okButton.setVisible(true);
            }
        });

        okButton.setOnAction(e -> spotlight.copySpotlightImages(path.getText()));

//        Add UI elements to grid
        grid.add(scenetitle, 0, 0, 2, 1);
        grid.add(browseButton, 0, 1);
        grid.add(path, 1, 1);
        grid.add(okButton, 0, 3);

//        Initialize Scene
        Scene scene = new Scene(grid);

//        Add scene to stage and show stage
        primaryStage.setTitle("Spotlight");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
