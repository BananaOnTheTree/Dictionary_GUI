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

public class anagramScoreController implements Initializable {
  @FXML private Label lblScore = new Label();
  @FXML private ImageView retryBtn = new ImageView();
  @FXML private ImageView exitBtn = new ImageView();

  private void setButtonAnimation(ImageView btn) {
    btn.setOnMouseEntered(event -> {
      btn.setFitHeight(95 * 1.2);
      btn.setFitWidth(95 * 1.2);
    });
    btn.setOnMouseExited(event -> {
      btn.setFitHeight(95);
      btn.setFitWidth(95);
    });
  }

  public void initialize(URL url, ResourceBundle rb) {
    setFinalScore("0");
    setButtonAnimation(retryBtn);
    setButtonAnimation(exitBtn);
  }

  public void setFinalScore(String x) {
    lblScore.setText(x);
  }

  @FXML
  void retry(MouseEvent event) {
    Stage stage = (Stage) lblScore.getScene().getWindow();
    stage.setScene(anagramGameScene);
    anagramGameControl.startGame(
        anagramGameControl.getTimeLimit(), anagramGameControl.getMinLength(), anagramGameControl.getMaxLength());
  }

  @FXML
  void toMenu(MouseEvent event) {
    Stage stage = (Stage) lblScore.getScene().getWindow();
    stage.setScene(anagramMainMenuScene);
  }
}
