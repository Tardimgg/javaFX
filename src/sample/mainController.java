package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class mainController {

    @FXML
    public void initialize() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String checkPassive = prefs.get("autoPassiveMod", null);
        String checkDefault = prefs.get("autoDefaultMod", null);
        checkPassiveEncryption.setSelected(Boolean.parseBoolean(checkPassive));
        getCheckPassiveEncryption();
        if (Boolean.parseBoolean(checkDefault)) {
            String valueDefault = prefs.get("keyForDefaultMod", null);
            key.appendText(valueDefault);
        }
    }

    @FXML
    TextArea inputText;

    @FXML
    TextArea outputText;

    @FXML
    TextField key;

    @FXML
    CheckBox checkPassiveEncryption;

    private Thread stream;
    private static String keyPassiveMod;
    private static String checkAutoPassive;
    private static String keyDefaultMod;
    private static String checkAutoDefault;
    private static Stage stage;

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

    @FXML
    public void getCheckPassiveEncryption() {
        Main.isPassiveEncryption(checkPassiveEncryption.isSelected());
    }

    @FXML
    public void clickSettingsPassiveMode() throws Exception {
        stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("windows/settingsPassiveModeWindow.fxml"));
        stage.setTitle("Settings");
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    exit();
                } catch (Exception e) {
                }

            }
        });
    }

    @FXML
    public void clickSettingsDefaultMode() throws Exception {
        stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("windows/settingsDefaultModeWindow.fxml"));
        stage.setTitle("Settings");
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void clickHelpPassiveMode() throws Exception{
        stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("windows/helpPassiveModeWindow.fxml"));
        stage.setTitle("Help");
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void clickHelpDefaultMode() throws Exception{
        stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("windows/helpDefaultModeWindow.fxml"));
        stage.setTitle("Help");
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void clickDefaultSettings() {
        Preferences prefs = Preferences.userNodeForPackage(mainController.class);
        try {
            prefs.clear();
        } catch (BackingStoreException e) {
        }
        key.clear();
        checkPassiveEncryption.setSelected(false);
    }

    protected static void setPassiveSettings(TextField key, CheckBox check) {
        keyPassiveMod = key.getText();
        checkAutoPassive = Boolean.toString(check.isSelected());
        exit();
        Stage stage = (Stage) mainController.stage.getScene().getWindow();
        stage.close();
    }

    public static void setDefaultSettings(TextField key, CheckBox check) {
        keyDefaultMod = key.getText();
        checkAutoDefault = Boolean.toString(check.isSelected());
        exit();
        Stage stage = (Stage) mainController.stage.getScene().getWindow();
        stage.close();
    }

    private static void exit() {
        Preferences prefs = Preferences.userNodeForPackage(mainController.class);
        prefs.put("keyForPassiveMod", keyPassiveMod == null ? Main.INIT_TEXT : keyPassiveMod);
        prefs.put("autoPassiveMod", checkAutoPassive == null ? "false" : "true");
        prefs.put("keyForDefaultMod", keyDefaultMod == null ? Main.INIT_TEXT : keyDefaultMod);
        prefs.put("autoDefaultMod", checkAutoDefault == null ? "false" : "true");
    }
}
