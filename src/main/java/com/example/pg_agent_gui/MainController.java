package com.example.pg_agent_gui;

import controller.Server;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;


public class MainController {
    private Stage stage;
    private Server server;
    public MainController(Stage stage, Server server) {
        this.stage = stage;
        this.server = server;
    }

    @FXML
    private Label serverNameLabel;

    public void initialize() {
        serverNameLabel.setText(this.server.getName());
    }

    @FXML
    private Button settingsButton;

    @FXML
    void onSettingsButtonClicked(MouseEvent event) {
        try {
            ViewFactory.showSettingsWindow(server);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
