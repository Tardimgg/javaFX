package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
            outputText.setText(getText());
        }
    }

    @FXML
    public void clickDecryption(){
        try {
            outputText.setText(Main.decryption(inputText.getText(), key.getText()));
        }catch (Exception e){
            outputText.setText(getText());
        }
    }

    private String getText(){
        if (inputText.getText().strip().equals("")){
            if (key.getText().strip().equals("")){
                return "Write text and key";
            }else{
                return "Write text";
            }
        }
        if (key.getText().strip().equals("")){
            return "Write text";
        }
        return "Key error";
    }
}
