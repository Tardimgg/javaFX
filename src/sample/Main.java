package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.awt.datatransfer.*;
import java.util.Random;


public class Main extends Application {

    private static final String INIT_ARRAY = "ItIsOutBigSecret";
    private static boolean bufferChanged = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("noRead");
        Scene scene = new Scene(root, 808, 606);
        primaryStage.setScene(scene);
        primaryStage.show();
        listener();
    }


    public static void main(String[] args) {
        launch(args);
    }

    protected synchronized static String encryption(String word, String key) throws Exception {
        Random random = new Random();
        int quantity = random.nextInt(9) + 1;
        IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_ARRAY.getBytes("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        for (int i = 0; i < quantity; i++) {
            word = new String(Hex.encodeHex(cipher.doFinal(word.getBytes("UTF-8")), false));
        }
        return word + quantity;
    }

    protected synchronized static String decryption(String word, String key) throws Exception {
        int length = word.length();
        int quantity = Integer.parseInt(Character.toString(word.charAt(length - 1)));
        word = word.substring(0, length - 1);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(INIT_ARRAY.getBytes("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        for (int i = 0; i < quantity; i++) {
            word = new String(cipher.doFinal(Hex.decodeHex(word.toCharArray())));
        }
        return word;
    }

    protected static void listener() {
        Clipboard clipboardListener = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboardListener.addFlavorListener(new FlavorListener() {

            @Override
            public synchronized void flavorsChanged(FlavorEvent e) {
                if (bufferChanged){
                    bufferChanged = false;
                    return;
                }
                try {
                    String text = (String) clipboardListener.getContents(null).getTransferData(DataFlavor.stringFlavor);
                    text = encryption(text, "йцуйцуйц");
                    StringSelection stringSelection = new StringSelection(text);
                    clipboardListener.setContents(stringSelection, null);
                    bufferChanged = true;
                } catch (Exception e1) {
                }
            }
        });
    }
}
