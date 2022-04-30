package com.example.pg_agent_gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import model.PGConfigDelta;
import model.ValueType;

import java.util.HashMap;
import java.util.concurrent.Flow;

public class TestPlanController {
    @FXML
    private Pane configDeltaPanel;


    HashMap<PGConfigDelta, FlowPane> area;

    public void initialize() {
        area = new HashMap<>();
        PGConfigDelta test = new PGConfigDelta("work_mem", "16384", "kb", "integer");
        FlowPane fuck = new FlowPane();
        fuck.setHgap(10);
        fuck.prefWidthProperty().bind(configDeltaPanel.widthProperty());
        Button addFieldButton = new Button("+");
        Button deleteFieldButton = new Button("-");
        addFieldButton.setOnMouseClicked(event -> fuck.getChildren().add(fuck.getChildren().size() - 2, new TextField()));
        deleteFieldButton.setOnMouseClicked(event -> fuck.getChildren().remove(fuck.getChildren().size() - 2));
        fuck.getChildren().add(addFieldButton);
        fuck.getChildren().add(deleteFieldButton);
        area.put(test, fuck);
        configDeltaPanel.getChildren().add(new Label(test.getName()));
        configDeltaPanel.getChildren().add(fuck);
        configDeltaPanel.getChildren().add(new Separator());
    }


}
