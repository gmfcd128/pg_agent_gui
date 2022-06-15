package com.example.pg_agent_gui;

import controller.LocalStorage;
import controller.Server;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Shortcut;

import java.io.IOException;
import java.util.List;

public class MacrosController {
    private Stage stage;
    private Server server;
    private List<Shortcut> macros;
    public MacrosController(Stage stage, Server server) {
        this.stage = stage;
        this.server = server;
    }
    @FXML
    private GridPane macrosGridPane;

    @FXML
    private Button newMacroButton;

    @FXML
    void onNewMacroButtonClick(MouseEvent event) {
        this.macros.add(new Shortcut());
        try {
            Shortcut result = ViewFactory.showMacroEditWindow(macros.get(macros.size() - 1));
            if (result != null) {
                macros.set(macros.size() - 1, result);
                LocalStorage.getInstance().saveMacros(macros);
                initialize();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        macros = LocalStorage.getInstance().getMacros();
        macrosGridPane.getChildren().clear();
        for (int i = 0; i < macros.size(); i++) {
            int index = i;
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER_LEFT);
            Label label = new Label(macros.get(i).name);
            label.setFont(new Font("Ariel", 18));
            label.setMaxWidth(Double.MAX_VALUE);
            Button editButton = new Button("編輯");
            editButton.setOnMouseClicked(event -> {
                try {
                    Shortcut result = ViewFactory.showMacroEditWindow(macros.get(index));
                    if (result != null) {
                        macros.set(index, result);
                        LocalStorage.getInstance().saveMacros(macros);
                        initialize();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            Button deleteButton = new Button("刪除");
            deleteButton.setOnMouseClicked(event -> {
                macros.remove(index);
                LocalStorage.getInstance().saveMacros(macros);
                initialize();
            });
            Button execButton = new Button("執行");
            execButton.setOnMouseClicked(event -> {
                try {
                    ViewFactory.showMacroExecWindow(server, macros.get(index));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            hbox.getChildren().add(label);
            hbox.getChildren().add(editButton);
            hbox.getChildren().add(deleteButton);
            hbox.getChildren().add(execButton);
            HBox.setMargin(label, new Insets(5));
            macrosGridPane.add(hbox, 0, i);
        }
    }
}
