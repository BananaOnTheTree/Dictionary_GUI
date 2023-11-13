package Dictionary.Dictionary;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import static Dictionary.Dictionary.dictionaryApp.*;

public class gameSelectionController extends baseMenu implements Initializable {
  @FXML
  private Button anagramBtn = new Button();
  @FXML
  private Button quizBtn = new Button();

  public void initialize(URL url, ResourceBundle rb) {
    super.initialize(url, rb);
    anagramBtn.setGraphic(getImage("anagram", 225, 225, PNG));
    quizBtn.setGraphic(getImage("Quiz", 225, 225, PNG));
    setStyle(anagramBtn, "toHandCursor");
    setStyle(quizBtn, "toHandCursor");
  }

  @FXML
  void menuAPI(ActionEvent event) {
    mainMenu();
    switchToAPI();
    dictionaryControl.switchToAPI();
    translateControl.switchToAPI();
  }

  @FXML
  void menuBookmark(ActionEvent event) {
    mainMenu();
    switchToBookmark();
    dictionaryControl.switchToBookmark();
    translateControl.switchToBookmark();
  }

  @FXML
  void menuHistory(ActionEvent event) {
    mainMenu();
    switchToHistory();
    dictionaryControl.switchToHistory();
    translateControl.switchToHistory();
  }

  @FXML
  void menuSearch(ActionEvent event) {
    mainMenu();
    switchToSearch();
    dictionaryControl.switchToSearch();
    translateControl.switchToSearch();
  }

  @FXML
  void menuTranslate(ActionEvent event) {
    translateMenu();
    switchToTranslate();
    dictionaryControl.switchToTranslate();
    translateControl.switchToTranslate();
  }

  @FXML
  void menuGame(ActionEvent event) {
    switchToGameSelection();
    dictionaryControl.switchToGameSelection();
    translateControl.switchToGameSelection();
  }

  @FXML
  void anagram(ActionEvent event) {
    Stage stage = (Stage) anagramBtn.getScene().getWindow();
    stage.setScene(anagramMainMenuScene);
  }

  @FXML
  void quiz(ActionEvent event) {
    Stage stage = (Stage) quizBtn.getScene().getWindow();
    stage.setScene(quizGameScene);
  }
}
