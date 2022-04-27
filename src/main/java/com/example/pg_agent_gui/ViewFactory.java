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
        stage.setMinWidth(592);
        stage.setMinHeight(587);
        stage.setTitle("Postgres auto experiment GUI - 登入");
        stage.setScene(scene);
        stage.show();
    }

    public static void showMainWindow(Server server) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("main.fxml"));
        fxmlLoader.setController(new MainController(stage, server));
        Scene scene = new Scene(fxmlLoader.load(), 418, 309);
        stage.setMinWidth(418);
        stage.setMinHeight(309);
        stage.setMaxWidth(418);
        stage.setMaxHeight(309);
        stage.setTitle("Postgres auto experiment GUI - 主畫面");
        stage.setScene(scene);
        stage.show();
    }

    public static void showSettingsWindow(Server server) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("database_config.fxml"));
        fxmlLoader.setController(new SettingsController(stage, server));
        Scene scene = new Scene(fxmlLoader.load(), 600, 854);
        stage.setMinWidth(600);
        stage.setMinHeight(854);
        stage.setTitle("Postgres auto experiment GUI - 設定管理");
        stage.setScene(scene);
        stage.show();
    }
}
