package Implement;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import javafx.application.Platform;

public class FileDownloader {
  public interface Callback {
    void onSuccess(String str);
  }

  public static void download(String fileName, String fileUrl, Callback callback) {
    Thread thread = new Thread(() -> {
      String savePath = "src/main/resources/Audio/" + fileName + ".mp3";
      try {
        downloadFile(fileUrl, savePath);
        File file = new File(savePath);
        Platform.runLater(() -> callback.onSuccess(file.toURI().toString()));
      } catch (IOException ex) {
        System.err.println("Error downloading file: " + ex.getMessage());
      }
    });
    thread.start();
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
