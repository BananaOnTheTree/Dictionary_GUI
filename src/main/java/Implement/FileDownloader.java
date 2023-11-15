package Implement;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {
  public static String download(String fileName, String fileUrl) {
    String savePath = "src/main/resources/Audio/" + fileName + ".mp3";
    try {
      downloadFile(fileUrl, savePath);
      File file = new File(savePath);
      return file.toURI().toString();
    } catch (IOException ex) {
      System.err.println("Error downloading file: " + ex.getMessage());
    }
    return "";
  }

  private static void downloadFile(String fileUrl, String savePath) throws IOException {
    URL url = new URL(fileUrl);
    URLConnection connection = url.openConnection();

    try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
        FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {

      byte[] dataBuffer = new byte[1024];
      int bytesRead;

      while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
        fileOutputStream.write(dataBuffer, 0, bytesRead);
      }
    }
  }
}
