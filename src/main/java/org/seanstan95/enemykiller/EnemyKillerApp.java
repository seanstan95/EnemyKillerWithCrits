package org.seanstan95.enemykiller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EnemyKillerApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EnemyKillerApp.class.getResource("EnemyKiller.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Enemy Killer");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}