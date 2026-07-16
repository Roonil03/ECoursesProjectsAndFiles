package com.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // TODO 10: replace "primary" with "vboxscene" and change window size to 320x240 [source: 26]
        scene = new Scene(loadFXML("vboxscene"), 320, 240);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
 
    public static void main(String[] args)  {
        try {
            // TODO 13: Call Database.getConnection() method before calling launch() [source: 26]
            Connection conn = Database.getConnection();
            if (conn != null) {
                System.out.println("connected");
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("not connected");
            e.printStackTrace();
        }
        launch(args);
    }
}