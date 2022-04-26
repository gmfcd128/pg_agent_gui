package com.example.pg_agent_gui;

import controller.Server;
import javafx.stage.Stage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;


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


}
