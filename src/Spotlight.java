import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

public class Spotlight {

	// Read file using ImageIO
	private static Image readImage(File file) {

		Image image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	// Create a new file
	private static void createFile(File file) {
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Copy file from one folder to another
	private static void copyFile(File file1, File file2) {
		try {
			Files.copy(file1.toPath(), file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Create a MD5 hash of the given file in order to compare 2 files
	public static String computeChecksum(File file) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		FileInputStream fis;
		StringBuffer sb = null;
		try {
			fis = new FileInputStream(file);

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
		}

		return sb.toString();
	}

	public static void main(String[] argv) throws NoSuchAlgorithmException {

		int width = 0, height = 0;
		String username = System.getProperty("user.name");

		String spotlightPath = "C:/Users/" + username
				+ "/AppData/Local/Packages/Microsoft.Windows.ContentDeliveryManager_cw5n1h2txyewy/LocalState/Assets/";
		File spotlightFolder = new File(spotlightPath);
		String targetPath = "D:/Desktop Wallpapers/"; // target folder
		File targetFolder = new File(targetPath);

		HashSet<String> hs = new HashSet<String>();

		if (!targetFolder.exists())
			targetFolder.mkdir();
		else {
			File[] listOfFiles = targetFolder.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {

				File imageFile = new File(targetPath + listOfFiles[i].getName());

				hs.add(computeChecksum(imageFile));
			}
		}

		System.out.println(hs);

		File[] listOfFiles = spotlightFolder.listFiles();
		int cnt = targetFolder.list().length;
		System.out.println(cnt);

		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {

				File source = new File(spotlightPath + listOfFiles[i].getName());
				Image image = readImage(source);
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

						File destination = new File(targetPath + listOfFiles[i].getName());

						if (!destination.exists())
							createFile(destination);

						copyFile(source, destination);

						destination.renameTo(new File(targetPath + cnt++ + ".jpg"));
						System.out.println(i + " " + destination.getName());
					}
					width = height = 0;
				}
			}
		}

		System.out.println("transfer complete");
	}

}
