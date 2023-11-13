package Game.Quiz.utils;
import Dictionary.Dictionary.dictionaryApp;
import Game.Quiz.models.QuizModel;
import Implement.Output.ExportToFile;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class Helper {
    public static EventHandler<WindowEvent> confirmCloseEventHandler = (event) -> {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Do you wish to exit without saving?",
                ButtonType.YES, ButtonType.NO
        );

        Button noButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.NO
        );
        noButton.setText("save and exit");
        closeConfirmation.setHeaderText(null);
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (ButtonType.NO.equals(closeResponse.get())) {
            QuizModel model = dictionaryApp.getQuizModel();
            model.save();
            ExportToFile.exportBookmark(null);
            ExportToFile.export(null);
            Platform.exit();
            System.exit(0);
        }
        if (ButtonType.YES.equals(closeResponse.get())) {
            Platform.exit();
            System.exit(0);
        } else {
            event.consume();
        }
    } ;

    public static Popup createPopup(final String message) {
        final Popup popup = new Popup();
        popup.setAutoFix(true);
        popup.setAutoHide(true);
        popup.setHideOnEscape(true);
        Label label = new Label(message);
        label.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                popup.hide();
            }
        });
        label.getStylesheets().add("/css/Styles.css");
        label.getStyleClass().add("popup");
        popup.getContent().add(label);
        return popup;
    }

    public static void showPopupMessage(final String message, final Stage stage) {
        final Popup popup = createPopup(message);
        popup.setOnShown(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent e) {
                popup.setX(stage.getX());
                popup.setY(stage.getY()+30);
            }
        });
        popup.show(stage);
    }

}
