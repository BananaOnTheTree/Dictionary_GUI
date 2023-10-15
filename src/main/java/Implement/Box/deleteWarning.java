package Implement.Box;

import GUI_App.dictionaryApp;
import Implement.MutableBoolean;
import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class deleteWarning {
  static Stage stage;
  public static MutableBoolean deleted;
  public static String Word;
  public static void start(Stage curStage, MutableBoolean curDel, String word) throws IOException {
    Word = word;
    FXMLLoader fxmlLoader = new FXMLLoader(dictionaryApp.class.getResource("deleteWarning.fxml"));
    Scene scene = new Scene(fxmlLoader.load(), 340, 150);
    stage = curStage;
    deleted = curDel;
    deleted.setValue(false);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage
        .getIcons()
        .add(
            new Image(
                new File("src/main/resources/Images/minusIcon.png").toURI().toString()));
    stage.setResizable(false);
    stage.setTitle("Xóa từ");
    stage.setScene(scene);
    stage.showAndWait();
  }

  public static void closeStage(boolean type) {
    deleted.setValue(type);
    stage.close();
  }
}