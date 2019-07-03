package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class settingsDefaultController {

    @FXML
    public TextField keyDefault;

    @FXML
    public CheckBox autoClickBox;

    @FXML
    public void saveExit() {
        mainController.setDefaultSettings(keyDefault, autoClickBox);
    }
}
