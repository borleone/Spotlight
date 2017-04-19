import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.imageio.ImageIO;

/**
 * Created by harsh on 2017-03-25.
 */
public final class Helpers {

    // Read file using ImageIO
    public static Image readImage(File file) {
        Image image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            ErrorLogger.log("Error! File " + file.getName() + " could not be read. Skipping...");
        }
        return image;
    }

    // Create a new file
    public static void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Copy file from one folder to another
    public static void copyFile(File file1, File file2) {
        try {
            Files.copy(file1.toPath(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageDigest isChecksumValid(String s)
    {
        try{
            return MessageDigest.getInstance(s);}
        catch(NoSuchAlgorithmException x)
        {
            return null;
        }
    }

}
