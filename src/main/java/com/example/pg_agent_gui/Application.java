package com.example.pg_agent_gui;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        Platform.setImplicitExit(true);
        ViewFactory.showLoginWindow();
    }


    public static void main(String[] args) {
        launch();
    }
}
