package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.awt.datatransfer.*;
import java.util.Random;
import java.util.prefs.Preferences;


public class Main extends Application {

    protected static final String INIT_TEXT = "ItIsOutBigSecret";
    private static boolean bufferChanged = false;
    private static boolean checkPassiveEncryption = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("windows/mainWindow.fxml"));
        primaryStage.setTitle("noRead");
        Scene scene = new Scene(root, 808, 606);
        primaryStage.setScene(scene);
        primaryStage.show();
        listener();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    protected synchronized static String encryption(String word, String key) throws Exception {

        // create quantity
        Random random = new Random();
        int quantity = random.nextInt(9) + 1;

        // create encryption
        key = encryptionKey(key);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_TEXT.getBytes("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // encryption text
        for (int i = 0; i < quantity; i++) {
            word = new String(Hex.encodeHex(cipher.doFinal(word.getBytes("UTF-8")), false));
        }
        return word + quantity + "/e";
    }

    protected synchronized static String decryption(String word, String key) throws Exception {

        // read quantity
        int length = word.length();
        int quantity = Integer.parseInt(Character.toString(word.charAt(length - 3)));
        word = word.substring(0, length - 3);

        // create decryption
        key = encryptionKey(key);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_TEXT.getBytes("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        // decryption text
        for (int i = 0; i < quantity; i++) {
            word = new String(cipher.doFinal(Hex.decodeHex(word.toCharArray())));
        }
        return word;
    }

    private static void listener() {
        Clipboard clipboardListener = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboardListener.addFlavorListener(new FlavorListener() {

            @Override
            public synchronized void flavorsChanged(FlavorEvent e) {
                if (checkPassiveEncryption) {
                    System.out.println(1);
                    if (bufferChanged) {
                        bufferChanged = false;
                        return;
                    }
                    try {
                        Preferences prefs = Preferences.userNodeForPackage(Main.class);
                        String key = prefs.get("keyForPassiveMod", null);
                        key = key == null ? INIT_TEXT : key;
                        String text = (String) clipboardListener.getContents(null).getTransferData(DataFlavor.stringFlavor);
                        if (text.substring(text.length() - 2).equals("/e")) {
                            text = decryption(text, key);
                        } else {
                            text = encryption(text, key);
                        }
                        StringSelection stringSelection = new StringSelection(text);
                        clipboardListener.setContents(stringSelection, null);
                        bufferChanged = true;
                    } catch (Exception e1) {
                    }
                }
            }
        });
    }

    private static String encryptionKey(String key) throws Exception {
        IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_TEXT.getBytes("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec(INIT_TEXT.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        return new String(Hex.encodeHex(cipher.doFinal(key.getBytes("UTF-8")), false)).substring(0, 32);
    }

    protected static void isPassiveEncryption(boolean answer) {
        checkPassiveEncryption = answer;
    }
}
