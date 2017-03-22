import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class Spotlight {

	public static void main(String[] argv) throws IOException {

		int width = 0, height = 0;
		String username = System.getProperty("user.name");

		String spotlightPath = "C:/Users/" + username + "/AppData/Local/Packages/Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy/LocalState/Assets/";
		File spotlightFolder = new File(spotlightPath);
		String targetPath = "D:/Desktop Wallpapers/"; // target folder
		File targetFolder = new File(targetPath);
		if (!targetFolder.exists())
			targetFolder.mkdir();
		File[] listOfFiles = spotlightFolder.listFiles();
		int cnt = targetFolder.list().length;
		System.out.println(cnt);

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {

				File source = new File(spotlightPath + listOfFiles[i].getName());
				Image image = ImageIO.read(source);
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

					File destination = new File(targetPath + listOfFiles[i].getName());

					if (!destination.exists())
						destination.createNewFile();

					Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);

					destination.renameTo(new File(targetPath + cnt++ + ".jpg"));
					System.out.println(i + " " + destination.getName());
					width = height = 0;

				}
			}
		}

		System.out.println("transfer complete");
	}
}
