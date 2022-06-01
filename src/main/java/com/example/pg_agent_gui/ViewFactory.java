package com.example.pg_agent_gui;

import controller.Server;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.TestPlan;

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
        Scene scene = new Scene(fxmlLoader.load(), 700, 754);
        stage.setMinWidth(700);
        stage.setMinHeight(754);
        stage.setTitle("Postgres auto experiment GUI - 設定管理");
        stage.setScene(scene);
        stage.show();
    }

    public static void showTestPlanWindow(Server server) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("testplan.fxml"));
        fxmlLoader.setController(new TestPlanController(stage, server));
        Scene scene = new Scene(fxmlLoader.load(), 899, 639);
        stage.setMinWidth(899);
        stage.setMinHeight(639);
        stage.setTitle("Postgres auto experiment GUI - 設定管理");
        stage.setScene(scene);
        stage.show();
    }

    public static void showTestRunnerWindow(TestPlan testPlan, Server server) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("testrun.fxml"));
        fxmlLoader.setController(new TestExecController(stage, testPlan, server));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setTitle("Postgres auto experiment GUI - 設定管理");
        stage.setScene(scene);
        stage.show();
    }

    public static void showLogSearchWindow() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("logsearch.fxml"));
        fxmlLoader.setController(new LogSearchController(stage));
        Scene scene = new Scene(fxmlLoader.load(), 977, 678);
        stage.setMinWidth(977);
        stage.setMinHeight(678);
        stage.setTitle("Postgres auto experiment GUI - 記錄檔查詢工具");
        stage.setScene(scene);
        stage.show();
    }
}
