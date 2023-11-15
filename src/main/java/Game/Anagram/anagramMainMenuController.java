package Game.Anagram;

import static Dictionary.Dictionary.dictionaryApp.anagramModeScene;
import static Dictionary.Dictionary.dictionaryApp.gameSelectionScene;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class anagramMainMenuController implements Initializable {
  @FXML
  private ImageView exitBtn = new ImageView();

  @FXML
  private ImageView newAnagramGameBtn = new ImageView();

  private void setButtonAnimation(ImageView btn) {
    btn.setOnMouseEntered(event -> {
      btn.setFitHeight(47 * 1.2);
      btn.setFitWidth(206 * 1.2);
    });
    btn.setOnMouseExited(event -> {
      btn.setFitHeight(47);
      btn.setFitWidth(206);
    });
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    setButtonAnimation(newAnagramGameBtn);
    setButtonAnimation(exitBtn);
  }

  @FXML
  void exitGame(MouseEvent event) {
    Stage stage = (Stage) exitBtn.getScene().getWindow();
    stage.setScene(gameSelectionScene);
  }

  @FXML
  void newGame(MouseEvent event) {
    Stage stage = (Stage) exitBtn.getScene().getWindow();
    stage.setScene(anagramModeScene);
  }

}
