package Game.Anagram;

import static Dictionary.Dictionary.dictionaryApp.anagramGameControl;
import static Dictionary.Dictionary.dictionaryApp.anagramGameScene;
import static Dictionary.Dictionary.dictionaryApp.anagramMainMenuScene;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class anagramModeController implements Initializable {
  @FXML
  private Label difficulty;

  @FXML
  private ImageView easyBtn = new ImageView();

  @FXML
  private ImageView mediumBtn = new ImageView();

  @FXML
  private ImageView hardBtn = new ImageView();

  @FXML
  private ImageView menuBtn = new ImageView();

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

  public void initialize(URL url, ResourceBundle rb) {
    setButtonAnimation(easyBtn);
    setButtonAnimation(mediumBtn);
    setButtonAnimation(hardBtn);
    setButtonAnimation(menuBtn);
  }

  @FXML
  void easyMode(MouseEvent event) {
    Stage stage = (Stage) difficulty.getScene().getWindow();
    stage.setScene(anagramGameScene);
    anagramGameControl.startGame(10, 2, 4);
  }

  @FXML
  void mediumMode(MouseEvent event) {
    Stage stage = (Stage) difficulty.getScene().getWindow();
    stage.setScene(anagramGameScene);
    anagramGameControl.startGame(30, 4, 5);
  }

  @FXML
  void hardMode(MouseEvent event) {
    Stage stage = (Stage) difficulty.getScene().getWindow();
    stage.setScene(anagramGameScene);
    anagramGameControl.startGame(60, 5, 7);
  }

  @FXML
  void toMenu(MouseEvent event) {
    Stage stage = (Stage) difficulty.getScene().getWindow();
    stage.setScene(anagramMainMenuScene);
  }
}
