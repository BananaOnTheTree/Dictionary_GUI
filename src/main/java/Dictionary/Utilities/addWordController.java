package Dictionary.Utilities;

import Implement.History;
import Implement.WordFormatter;
import Implement.WordStorage.DictionaryMap;
import Implement.WordStorage.Trie.Trie;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class addWordController {

  @FXML
  private TextField tfWord;
  @FXML
  TextField tfPhonetic;
  @FXML
  TextArea tfMeaning;
  @FXML
  Button btnAudio = new Button();

  String audio = "";

  public void setWord(String WORD) {
    tfWord.setText(WORD);
  }

  @FXML
  public void clickAdd(ActionEvent e) {
    String word = WordFormatter.normalize(tfWord.getText());
    String phonetic = WordFormatter.normalize(tfPhonetic.getText());
    String meaning = WordFormatter.normalize(tfMeaning.getText());
    if (word.isBlank() || phonetic.isBlank() || meaning.isBlank()) {
      return;
    }
    if (meaning.charAt(meaning.length() - 1) != '\n') {
      meaning += '\n';
    }
    Trie.add(word, phonetic, meaning, audio);
    DictionaryMap.add(word, meaning);
    History.add(word);
    Stage stage = (Stage) tfWord.getScene().getWindow();
    stage.close();
    tfMeaning.setText(""); tfPhonetic.setText("");
    audio = ""; btnAudio.setText("Chọn file");
  }

  @FXML
  void getAudio(ActionEvent event) {
    Stage stage =  (Stage) tfWord.getScene().getWindow();
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select file...");
    File initLocation = new File("src/main/resources/Audio");
    fileChooser.setInitialDirectory(initLocation);
    File selected = fileChooser.showOpenDialog(stage);
    if (selected != null) {
      audio = selected.toURI().toString();
      btnAudio.setText(selected.getName());
    } else {
      audio = "";
      btnAudio.setText("Chọn file");
    }
  }
}