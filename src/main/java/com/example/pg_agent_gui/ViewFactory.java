package com.example.pg_agent_gui;

import controller.Server;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    public static void showLoginWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("login.fxml"));
        fxmlLoader.setController(new LoginController(stage));
        Scene scene = null;
        scene = new Scene(fxmlLoader.load(), 592, 587);
        stage.setMinHeight(592);
        stage.setMinWidth(587);
        stage.setTitle("Postgres auto experiment GUI - 登入");
        stage.setScene(scene);
        stage.show();
    }
    public static void showMainWindow(Server server) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
        fxmlLoader.setController(new MainController(stage, server));
        Scene scene = null;
        scene = new Scene(fxmlLoader.load(), 392, 450);
        stage.setMinHeight(392);
        stage.setMinWidth(450);
        stage.setTitle("Postgres auto experiment GUI - 主畫面");
        stage.setScene(scene);
        stage.show();
    }
}
