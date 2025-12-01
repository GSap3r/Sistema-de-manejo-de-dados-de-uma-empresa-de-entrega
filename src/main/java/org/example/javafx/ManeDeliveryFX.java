package org.example.javafx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.javafx.db.util.SingletonDB;

import javax.swing.*;
import java.io.IOException;

//b

public class ManeDeliveryFX extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ManeDeliveryFX.class.getResource("menu-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mane Delivery FX v: 0.1");
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        if (SingletonDB.conectar())
            launch();
        else
            JOptionPane.showMessageDialog(null, "Erro:" + SingletonDB.getConexao().getMensagemErro());
        Platform.exit();
    }
}