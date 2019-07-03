package sample;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class settingsPassiveController {

    @FXML
    public TextField keyPassive;

    @FXML
    public CheckBox autoClickBox;

    @FXML
    public void saveExit() {
        mainController.setPassiveSettings(keyPassive, autoClickBox);
    }
}
