package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.security.PrivateKey;
import java.util.Base64;

public class Controller {


    @FXML
    TextArea inputText;

    @FXML
    TextArea outputText;

    @FXML
    TextField key;

    @FXML
    public void clickEncryption(){
        try {
            String h = Main.encryption(inputText.getText(), key.getText());
            outputText.setText(h);
        }catch (Exception e){
            //outputText.setText("Write text");
            outputText.setText(e.getMessage());
        }

    }

    @FXML
    public void clickDecryption(){
        try {
            outputText.setText(Main.decryption(inputText.getText(), key.getText()));
        }catch (Exception e){
            outputText.setText("Write text");
        }

    }
}
