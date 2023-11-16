package Dictionary.Dictionary;

import static Dictionary.Dictionary.dictionaryApp.addAPIControl;
import static Dictionary.Dictionary.dictionaryApp.addAPIScene;
import static Dictionary.Dictionary.dictionaryApp.addWordControl;
import static Dictionary.Dictionary.dictionaryApp.addWordScene;
import static Dictionary.Dictionary.dictionaryApp.deleteControl;
import static Dictionary.Dictionary.dictionaryApp.deleteScene;
import static Dictionary.Dictionary.dictionaryApp.gameSelectionControl;
import static Dictionary.Dictionary.dictionaryApp.infoControl;
import static Dictionary.Dictionary.dictionaryApp.infoScene;
import static Dictionary.Dictionary.dictionaryApp.translateControl;

import Implement.Bookmark;
import Implement.FileDownloader;
import Implement.History;
import Implement.Input.AddFromAPI;
import Implement.Input.AddFromFile;
import Implement.Input.SingleWord.Definition;
import Implement.Input.SingleWord.Meaning;
import Implement.Input.SingleWord.Phonetic;
import Implement.Output.ExportToFile;
import Implement.WordFormatter;
import Implement.WordStorage.DictionaryMap;
import Implement.WordStorage.Trie.Trie;
import Implement.WordStorage.Trie.TrieNode;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class mainController extends baseMenu implements Initializable {
  @FXML
  private TextField searchBar = new TextField();
  @FXML
  private TextField tfPhonetic = new TextField();
  @FXML
  private TextArea txtEditor = new TextArea();
  @FXML
  private Label lblWord = new Label();
  @FXML
  private Label spelling = new Label();
  @FXML
  private TextFlow meaning = new TextFlow();
  @FXML
  private ListView<String> suggestWord = new ListView<>();
  @FXML
  private ImageView bookmarkStar = new ImageView();
  @FXML
  private ImageView recycleBin = new ImageView();
  @FXML
  private ImageView imgSpeaker = new ImageView();
  @FXML
  private ImageView imgEditor = new ImageView();
  @FXML
  private ImageView imgTick = new ImageView();
  @FXML
  private ImageView miniGlass = new ImageView();
  @FXML
  private ImageView imgCross = new ImageView();
  @FXML
  private ImageView imgAdd = new ImageView();
  @FXML
  private ScrollPane scrollMeaning = new ScrollPane();
  @FXML
  private Button importBtn = new Button();
  @FXML
  private Button exportBtn = new Button();
  private String currentMenu = "Search";
  private String currentWord;
  final String IMGPath = "src/main/resources/Images/";
  private boolean noSound = true;
  String apiAudio = "";
  private MediaPlayer player;
  private final TranslateTransition transition = new TranslateTransition(Duration.millis(130));

  void setEditor(boolean type) {
    txtEditor.setVisible(type);
    tfPhonetic.setVisible(type);
    imgTick.setVisible(type);
    imgCross.setVisible(type);
  }
  void setSound() {
    imgSpeaker.setDisable(noSound);
    if (noSound) {
      imgSpeaker.setOpacity(0.3);
    } else {
      imgSpeaker.setOpacity(1);
    }
  }
  String getImgPath(String name) {
    return new File(IMGPath + name + ".png").toURI().toString();
  }
  public void getSuggestion(String[] suggestion) {
    searchBar.setText("");
    suggestWord.getItems().clear();
    suggestWord.getItems().addAll(suggestion);
  }
  public void cellFormat() {
    suggestWord.getStyleClass().add("list-cell");
    suggestWord.setCellFactory(new Callback<>() {
      @Override
      public ListCell<String> call(ListView<String> param) {
        return new ListCell<>() {
          @Override
          protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
              setText("   " + item);
            } else {
              setText(null);
            }
          }
        };
      }
    });
  }

  public void buttonFormat() {
    setStyle(bookmarkStar, "imageViewStyle"); setStyle(recycleBin, "imageViewStyle");
    setStyle(imgEditor, "imageViewStyle"); setStyle(imgAdd, "imageViewStyle");
    setStyle(imgCross, "imageViewStyle"); setStyle(imgTick, "imageViewStyle");
    setStyle(imgSpeaker, "imageViewStyle"); imgAdd.setVisible(false);
    imgSearch.toFront(); imgBookmark.toFront(); imgHistory.toFront(); imgAPI.toFront();
    setEditor(false); transition.setNode(imgToggle);
    spelling.setTextFill(Color.BLUE);
    importBtn.setGraphic(getImage("import", 23,23, PNG));
    exportBtn.setGraphic(getImage("export", 23,23, PNG));
  }

  void setMeaningOfWord(String mean) {
    String[] data = mean.split("\n");
    meaning.getChildren().clear();
    for (String tmp : data) {
      if (tmp.isEmpty()) {
        continue;
      }
      boolean example = false;
      Text text = new Text(tmp + "\n");
      text.setFont(Font.font("Times New Roman", 17));
      if (tmp.contains("• Example")) {
        Text temp = new Text("\n");
        meaning.getChildren().add(temp);
        example = true;
        text.setFont(Font.font("Times New Roman", FontPosture.ITALIC, 17));
      }
      if (tmp.charAt(0) == '☆') {
        text = new Text("\n" + tmp + "\n");
        text.setFont(Font.font("Times New Roman", FontWeight.BOLD, 25));
        text.setFill(Color.RED);
      }
      meaning.getChildren().add(text);
      if (example) {
        Text temp = new Text("\n");
        meaning.getChildren().add(temp);
      }
    }
    meaning.setPrefWidth(scrollMeaning.getPrefWidth());
    scrollMeaning.setContent(meaning);
  }

  String getMeaning() {
    StringBuilder mean = new StringBuilder();
    for (Node node : meaning.getChildren()) {
      if (node instanceof Text) {
        String add = ((Text) node).getText();
        if (add.isBlank()) {
          continue;
        }
        mean.append(add);
      }
    }
    mean.deleteCharAt(0);
    return mean.toString();
  }

  void setContent(TrieNode node) {
    apiAudio = node.getAudio(); noSound = apiAudio.isBlank(); setSound();
    searchBar.setText(currentWord);
    lblWord.setText(currentWord);
    spelling.setText(node.getSpelling());
    if (!noSound && apiAudio.contains("http")) {
      String savePath = "src/main/resources/Audio/" + node.getFullWord() + ".mp3";
      File file = new File(savePath);
      if (!file.exists()) {
        FileDownloader.download(node.getFullWord(), apiAudio, audio -> {
          if (audio != null && !audio.isBlank()) {
            apiAudio = audio;
          }
        });
      } else {
        apiAudio = file.toURI().toString();
      }
    }
    setMeaningOfWord(node.getMeaning());
    imgAdd.setVisible(false);
    if (!node.getBookmarked()) {
      bookmarkStar.setImage(new Image(getImgPath("starUntoggle")));
    } else {
      bookmarkStar.setImage(new Image(getImgPath("star")));
    }
  }

  void addWord() {
    Stage stage = new Stage();
    stage.setScene(addWordScene);
    addWordControl.setWord(searchBar.getText());
    stage.initModality(Modality.APPLICATION_MODAL);
    stage
        .getIcons()
        .add(
            new Image(
                new File("src/main/resources/Images/plusIcon.png").toURI().toString()));
    stage.setResizable(false);
    stage.setTitle("Thêm từ");
    stage.showAndWait();
  }

  public void tFieldFormat() {
    searchBar.getStyleClass().add("txtField");
  }
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    super.initialize(url, rb);
    cellFormat(); buttonFormat(); setSound();
    AddFromFile.add(null); getSuggestion(DictionaryMap.getKey());
    menuInit(false); tFieldFormat();
    suggestWord
        .getSelectionModel()
        .selectedItemProperty()
        .addListener(
            (observableValue, s, t1) -> {
              menuInit(true);
              currentWord = suggestWord.getSelectionModel().getSelectedItem();
              if (currentWord == null || currentWord.isEmpty()) {
                return;
              }
              if (currentWord.equals("Thêm...")) {
                addWord();
                return;
              }
              setContent(Trie.find(currentWord));
              if (!currentMenu.equals("History")) {
                History.add(currentWord);
              }
              setTooltip(imgAdd, "Thêm vào từ điển");
              setTooltip(imgTick, "Lưu"); setTooltip(imgCross, "Hủy");
            });
  }

  void apiSearch(String word) {
    if (word.isBlank()) {
      return;
    }
    noSound = true;
    AddFromAPI addFromAPI = new AddFromAPI();
    addFromAPI.getWord(word, entry -> {
      if (entry != null) {
        lblWord.setText(WordFormatter.normalize(word));
        for (Phonetic i : entry.getPhonetics()) {
          if (!i.getText().isBlank()) {
            spelling.setText(i.getText());
            if (!i.getAudio().isBlank()) {
              apiAudio = i.getAudio();
              noSound = false;
              break;
            }
          }
        }
        setSound(); imgAdd.setVisible(true);
        StringBuilder apiMeaning = new StringBuilder();
        for (Meaning i : entry.getMeanings()) {
          apiMeaning.append("☆   ").append(i.getPartOfSpeech()).append("\n");
          for (Definition j : i.getDefinitions()) {
            apiMeaning.append("          »   ").append(j.getDefinition()).append("\n");
            if (!j.getExample().isBlank()) {
              apiMeaning.append("\n               • Example:   ").append(j.getExample()).append("\n\n");
            }
          }
          if (!i.getSynonyms().isEmpty()) { // If i has synonyms
            apiMeaning.append("          »   Synonyms: ");
            for (String j : i.getSynonyms()) {
              apiMeaning.append(j).append(", ");
            }
            apiMeaning.delete(apiMeaning.length() - 2, apiMeaning.length() - 1);
            apiMeaning.append("\n");
          }
          if (!i.getAntonyms().isEmpty()) { // If i has antonyms
            apiMeaning.append("          »   Antonyms: ");
            for (String j : i.getAntonyms()) {
              apiMeaning.append(j).append(", ");
            }
            apiMeaning.delete(apiMeaning.length() - 2, apiMeaning.length() - 1);
            apiMeaning.append("\n");
          }
        }
        setMeaningOfWord(apiMeaning.toString());
      } else {
        infoControl.setPrompt("Không tìm thấy " + word + "trong API từ điển");
        Stage stage = new Stage();
        stage.setScene(infoScene);
        stage.showAndWait();
      }
    });

  }
  @FXML
  public void findPrefix(KeyEvent e) {
    String word = searchBar.getText();
    if (!currentMenu.equals("API")) {
      suggestWord.getItems().clear();
      if (word == null || word.isEmpty()) {
        getSuggestion(DictionaryMap.getKey());
      } else if (!word.isBlank()) {
        List<String> suggestion = Trie.getPrefix(WordFormatter.normalize(word));
        suggestion.add(0, "Thêm...");
        suggestWord.getItems().addAll(suggestion.toArray(new String[(int)suggestion.size()]));
      }
    } else {
      if (e.getCharacter().equals("\r") || e.getCode().equals(KeyCode.ENTER)) {
        apiSearch(word);
      }
    }
  }

  void menuInit(boolean active) {
    suggestWord.setVisible(true); bookmarkStar.setVisible(active);
    if (active && Bookmark.find(lblWord.getText())) {
      bookmarkStar.setImage(new Image(getImgPath("star")));
    } else {
      bookmarkStar.setImage(new Image(getImgPath("starUntoggle")));
    }
    recycleBin.setVisible(active); imgEditor.setVisible(active);
  }

  public void Switch(boolean mInit, Node node, boolean searchB, String[] lst, String cMenu) {
    menuInit(mInit);
    toggleMenu(node);
    searchBar.setVisible(searchB); miniGlass.setVisible(searchB);
    if (lst != null) getSuggestion(lst);
    currentMenu = cMenu;
  }

  @Override
  public void switchToSearch() {
    super.switchToSearch();
    Switch(true, imgSearch, true, DictionaryMap.getKey(), "Search");
  }

  public void menuSearch(ActionEvent e) {
    switchToSearch();
    translateControl.switchToSearch();
    gameSelectionControl.switchToSearch();
  }

  @Override
  public void switchToBookmark() {
    super.switchToBookmark();
    Switch(true, imgBookmark, true, Bookmark.getList(), "Bookmark");
  }

  public void menuBookmark(ActionEvent e) {
    switchToBookmark();
    translateControl.switchToBookmark();
    gameSelectionControl.switchToBookmark();
  }

  @Override
  public void switchToHistory() {
    super.switchToHistory();
    Switch(true, imgHistory, false, History.getList(), "History");
  }

  public void menuHistory(ActionEvent e) {
    switchToHistory();
    translateControl.switchToHistory();
    gameSelectionControl.switchToHistory();
  }

  @Override
  public void switchToAPI() {
    super.switchToAPI();
    Switch(true, imgAPI, true, null, "API");
    suggestWord.setVisible(false);
    bookmarkStar.setVisible(false); recycleBin.setVisible(false); imgEditor.setVisible(false);
  }

  public void menuAPI(ActionEvent e) {
    switchToAPI();
    translateControl.switchToAPI();
    gameSelectionControl.switchToAPI();
  }

  public void changeBookmarkState(MouseEvent e) {
    String word = lblWord.getText();
    if (word.equals("LingoBench") || word.isBlank()) {
      return;
    }
    TrieNode node = Trie.find(word);
    if (!node.getBookmarked()) {
      if (!DictionaryMap.exist(word)) {
        Trie.add(word, spelling.getText(), getMeaning(), apiAudio);
        DictionaryMap.add(word, getMeaning());
        History.add(word);
      }
      bookmarkStar.setImage(new Image(getImgPath("star")));
      node.setBookmarked(true);
      Bookmark.add(word);
    } else {
      bookmarkStar.setImage(new Image(getImgPath("starUntoggle")));
      node.setBookmarked(false);
      Bookmark.delete(word);
    }
    if (currentMenu.equals("Bookmark")) {
      getSuggestion(Bookmark.getList());
    }
  }

  public void deleteWord(MouseEvent e) {
    String word = lblWord.getText();
    if (word.equals("LingoBench")) {
      return;
    }
    deleteControl.setPrompt("Bạn có chắc muốn xóa " + lblWord.getText() + " khỏi từ điển?");
    Stage stage = new Stage();
    stage.setScene(deleteScene);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage
        .getIcons()
        .add(
            new Image(
                new File("src/main/resources/Images/minusIcon.png").toURI().toString()));
    stage.setResizable(false);
    stage.setTitle("Xóa từ");
    infoControl.setPrompt("Đã xóa " + word + " khỏi từ điển");
    stage.showAndWait();
    if (!deleteControl.isDeleted()) {
      return;
    }
    Trie.delete(word);
    DictionaryMap.delete(word); Bookmark.delete(word); History.delete(word);
    switch (currentMenu) {
      case "Search" -> getSuggestion(DictionaryMap.getKey());
      case "Bookmark" -> getSuggestion(Bookmark.getList());
      case "History" -> getSuggestion(History.getList());
    }
    searchBar.setText(""); lblWord.setText(""); spelling.setText(""); setMeaningOfWord("");
    bookmarkStar.setVisible(false); recycleBin.setVisible(false); imgEditor.setVisible(false);
  }

  public void playMedia(MouseEvent e) {
    Media media = new Media(apiAudio);
    player = new MediaPlayer(media);
    player.setStartTime(Duration.ZERO);
    if (player.getStatus() == Status.PLAYING) {
      player.stop();
    }
    player.seek(Duration.ZERO);
    player.play();
  }

  void iconDisable(boolean b) {
    imgSearch.setDisable(b); imgBookmark.setDisable(b); imgHistory.setDisable(b);
    imgAPI.setDisable(b); imgTranslate.setDisable(b); imgGame.setDisable(b);
    bookmarkStar.setDisable(b); recycleBin.setDisable(b);
    meaning.setOpacity(1); spelling.setOpacity(1);
    double opacity = 1;
    if (b) {
      opacity = 0.3; imgSpeaker.setDisable(true); imgSpeaker.setOpacity(opacity);
      meaning.setOpacity(0); spelling.setOpacity(0);
    } else {
      TrieNode node = Trie.find(lblWord.getText());
      apiAudio = node.getAudio(); noSound = apiAudio.isBlank(); setSound();
    }
    imgSearch.setOpacity(opacity); imgBookmark.setOpacity(opacity); imgHistory.setOpacity(opacity);
    imgAPI.setOpacity(opacity); imgTranslate.setOpacity(opacity); imgGame.setOpacity(opacity);
    bookmarkStar.setOpacity(opacity); recycleBin.setOpacity(opacity);
  }

  public void openEditor(MouseEvent e) {
    if (lblWord.getText().equals("LingoBench")) {
      return;
    }
    setEditor(true);
    txtEditor.setText(getMeaning());
    tfPhonetic.setText(spelling.getText());
    suggestWord.setDisable(true);
    iconDisable(true);
    switch (currentMenu) {
      case "Search" -> imgSearch.setOpacity(1);
      case "Bookmark" -> imgBookmark.setOpacity(1);
      case "History" -> imgHistory.setOpacity(1);
    }
  }

  void closeEditor() {
    setEditor(false);
    iconDisable(false);
    suggestWord.setDisable(false);
  }

  public void exitEditor(MouseEvent e) {
    closeEditor();
  }

  public void saveEditor(MouseEvent e) {
    String tmp = txtEditor.getText();
    if (tmp.isEmpty() || tmp.charAt(tmp.length() - 1) != '\n') {
      tmp += '\n';
    }
    setMeaningOfWord(tmp);
    String newMeaning = tmp;
    String newSpelling = tfPhonetic.getText();
    spelling.setText(newSpelling);
    Trie.add(lblWord.getText(), newSpelling, newMeaning, "");
    closeEditor();
  }

  public void addFromAPI(MouseEvent e) {
    addAPIControl.setPrompt("Bạn có chắc muốn thêm " + lblWord.getText() + " vào từ điển?");
    infoControl.setPrompt("Đã thêm " + lblWord.getText() + " vào từ điển.");
    Stage stage = new Stage();
    stage.setScene(addAPIScene);
    stage.initModality(Modality.APPLICATION_MODAL);
    stage
        .getIcons()
        .add(
            new Image(
                new File("src/main/resources/Images/plusIcon.png").toURI().toString()));
    stage.setTitle("Thêm từ");
    stage.showAndWait();
    if (addAPIControl.isAdded()) {
      Trie.add(lblWord.getText(), spelling.getText(), getMeaning(), apiAudio);
      DictionaryMap.add(lblWord.getText(), getMeaning());
    }
  }

  public void menuTranslate(ActionEvent e) {
    translateMenu();
    switchToTranslate();
    translateControl.switchToTranslate();
    gameSelectionControl.switchToTranslate();
  }

  public void menuGame(ActionEvent e) {
    gameSelectionMenu();
    switchToTranslate();
    translateControl.switchToGameSelection();
    gameSelectionControl.switchToGameSelection();
  }

  public void importFile(ActionEvent e) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select file to import from...");
    File initLocation = new File("src/main/resources");
    fileChooser.setInitialDirectory(initLocation);
    Stage stage = (Stage) imgAdd.getScene().getWindow();
    File selected = fileChooser.showOpenDialog(stage);
    if (selected == null) {
      return;
    }
    AddFromFile.add(selected);
    infoControl.setPrompt("Đã nhập file vào dữ liệu từ điển");
    Stage stage1 = new Stage();
    stage1.setScene(infoScene);
    stage1.setTitle("Thông báo");
    stage1
        .getIcons()
        .add(
            new Image(
                new File("src/main/resources/Images/infoIcon.png").toURI().toString()));
    stage1.showAndWait();
  }

  public void exportFile(ActionEvent e) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select file to export to...");
    File initLocation = new File("src/main/resources");
    fileChooser.setInitialDirectory(initLocation);
    Stage stage = (Stage) imgAdd.getScene().getWindow();
    File selected = fileChooser.showOpenDialog(stage);
    if (selected == null) {
      return;
    }
    ExportToFile.export(selected);
    infoControl.setPrompt("Đã xuất dữ liệu từ điển ra file");
    Stage stage1 = new Stage();
    stage1.setScene(infoScene);
    stage1.setTitle("Thông báo");
    stage1
        .getIcons()
        .add(
            new Image(
                new File("src/main/resources/Images/infoIcon.png").toURI().toString()));
    stage1.showAndWait();
  }

  public void autoGetAudio() {
    for (String i : DictionaryMap.getKey()) {
      setContent(Trie.find(i));
    }
  }
}