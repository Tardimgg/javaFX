package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {


    @FXML
    TextArea inputText;

    @FXML
    protected TextArea outputText;

    @FXML
    TextField key;

    private static Thread stream;

    @FXML
    public void clickEncryption() {
        if (stream != null && stream.isAlive()) {
            outputText.setText("Wait");
            return;
        }
        stream = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputText.clear();
                    outputText.setText(Main.encryption(inputText.getText(), key.getText()));
                } catch (Exception e) {
                    outputText.clear();
                    outputText.setText(getText(e));
                }
            }
        });
        stream.start();
    }

    @FXML
    public void clickDecryption() {
        if (stream != null && stream.isAlive()) {
            outputText.setText("Wait");
            return;
        }
        stream = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputText.clear();
                    outputText.setText(Main.decryption(inputText.getText(), key.getText()));
                } catch (Exception e) {
                    outputText.clear();
                    outputText.setText(getText(e));
                }
            }
        });
        stream.start();
    }

    private String getText(Exception e) {
        if (inputText.getText().length() == 0) {
            if (key.getText().length() == 0) {
                return "Write text and key";
            } else {
                return "Write text";
            }
        }
        if (key.getText().length() == 0) {
            return "Write key";
        }
        if (e.getMessage().substring(0, 16).equals("For input string")) {
            return "Text error";
        }
        return "Key error\n" + e.getMessage();
    }
}

