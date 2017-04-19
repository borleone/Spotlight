import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

/**
 * Created by harsh on 2017-03-25.
 */
public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {

		primaryStage = GUI.getGUI().makeSpotlightGui(primaryStage);

//		Retrieve elements from stage
        GridPane grid = (GridPane) primaryStage.getScene().lookup("#grid");
        Button browseButton = (Button) grid.lookup("#browseButton");
        Label path = (Label) grid.lookup("#path");
        Button okButton = (Button) grid.lookup("#okButton");
        TextArea logArea = new TextArea();
        Tooltip tooltip = new Tooltip();

        final Stage stage = primaryStage;

        browseButton.setOnAction(e -> {
            File selectedDirectory = new DirectoryChooser().showDialog(stage);

            if (selectedDirectory != null) {
                tooltip.setText(selectedDirectory.getAbsolutePath());
                path.setTooltip(tooltip);
                path.setText(selectedDirectory.getAbsolutePath());
                okButton.setVisible(true);
                grid.add(logArea, 1, 4);
                stage.sizeToScene();
                logArea.setVisible(true);
            }
        });
//
        okButton.setOnAction(e -> copySpotlightImages(path.getText(), logArea));

        // Add scene to stage and show stage
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

    // Create a MD5 hash of the given file in order to compare 2 files
    public static String computeChecksum(File file) {

        FileInputStream fis;
        StringBuffer sb = null;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");

            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }

            byte[] mdbytes = md.digest();

            // convert the byte to hex format
            sb = new StringBuffer();
            for (int j = 0; j < mdbytes.length; j++) {
                sb.append(Integer.toString((mdbytes[j] & 0xff) + 0x100, 16).substring(1));
            }

            // System.out.println("Digest(in hex format):: " + sb.toString());
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public void copySpotlightImages(String destinationDirectory, TextArea logArea) {

        int width = 0, height = 0;
        String username = System.getProperty("user.name");

        String spotlightPath = "C:/Users/" + username
                + "/AppData/Local/Packages/Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy/LocalState/Assets/";
        File spotlightFolder = new File(spotlightPath);

        File targetFolder = new File(destinationDirectory);

        HashSet<String> hs = new HashSet<String>();

        File[] listOfFiles = targetFolder.listFiles();

        for (File child : listOfFiles) {
            // Do something with child
            // File imageFile = new File(targetPath + listOfFiles[i].getName());
            hs.add(computeChecksum(child));
        }

        System.out.println(hs);

        listOfFiles = spotlightFolder.listFiles();
        int numImages = targetFolder.list().length;
        int count = numImages;
        System.out.println(count);

        for (int i = 0; i < listOfFiles.length; i++) {

            if (listOfFiles[i].isFile()) {

                File source = new File(spotlightPath + listOfFiles[i].getName());
                Image image = Helpers.readImage(source);
                if (image == null)
                    continue;

                Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix("jpg");
                while (iter.hasNext()) {
                    ImageReader reader = iter.next();
                    try {
                        ImageInputStream stream = new FileImageInputStream(source);
                        reader.setInput(stream);
                        width = reader.getWidth(reader.getMinIndex());
                        height = reader.getHeight(reader.getMinIndex());
                        System.out.println(width + " " + height);
                    } catch (IOException e) {
                        System.out.println("Error reading: " + source.getAbsolutePath() + e);
                    } finally {
                        reader.dispose();
                    }
                }

                if (width == 1920) {

                    if (hs.contains(computeChecksum(source)))
                        System.out.println("DUPLICATE");
                    else {

                        File destination = new File(destinationDirectory + '/' + listOfFiles[i].getName());

                        if (!destination.exists())
                            Helpers.createFile(destination);

                        if (destination.exists()) {
                            Helpers.copyFile(source, destination);
                            destination.renameTo(new File(destinationDirectory + '/' + count++ + ".jpg"));
                            System.out.println(i + " " + destination.getName());
                        }
                    }
                    width = height = 0;
                }
            }
        }

        String displayResult = (count == numImages) ? "No new images detected" : count - numImages + " images were successfully copied.";

        GUI.getGUI().makeAlert(Alert.AlertType.CONFIRMATION, "Success!", "", displayResult).showAndWait();
    }
}