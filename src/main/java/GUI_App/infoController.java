package GUI_App;

import Implement.OpenBox.infoBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class infoController implements Initializable {
  @FXML
  Label msg;
  @FXML
  Button btnOK;
  @FXML
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    msg.setText(infoBox.message);
  }
  @FXML
  public void clickOK(ActionEvent e) {
    infoBox.closeStage();
  }
}
