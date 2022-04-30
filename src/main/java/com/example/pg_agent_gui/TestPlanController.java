package com.example.pg_agent_gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.PGConfigDelta;
import model.ValueType;

import java.util.HashMap;
import java.util.concurrent.Flow;

public class TestPlanController {
    @FXML
    private Pane configDeltaPanel;


    HashMap<PGConfigDelta, VBox> area;

    public void initialize() {
        area = new HashMap<>();
        PGConfigDelta test = new PGConfigDelta("work_mem", "16384", "kb", "integer");
        VBox shit = new VBox();
        HBox header = new HBox();
        header.getChildren().add(new Label("work_mem"));

        FlowPane fuck = new FlowPane();
        Button deleteAreaButton = new Button("移除");
        deleteAreaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                area.get("work_mem").getChildren().clear();
                area.remove("work_mem");
            }
        });
        header.getChildren().add(deleteAreaButton);

        fuck.setHgap(10);
        fuck.prefWidthProperty().bind(configDeltaPanel.widthProperty());
        Button addFieldButton = new Button("+");
        Button deleteFieldButton = new Button("-");
        addFieldButton.setOnMouseClicked(event -> fuck.getChildren().add(fuck.getChildren().size() - 2, new TextField()));
        deleteFieldButton.setOnMouseClicked(event -> fuck.getChildren().remove(fuck.getChildren().size() - 2));
        fuck.getChildren().add(addFieldButton);
        fuck.getChildren().add(deleteFieldButton);
        shit.getChildren().add(header);
        shit.getChildren().add(fuck);
        area.put(test, shit);
        configDeltaPanel.getChildren().add(new Label(test.getName()));
        configDeltaPanel.getChildren().add(fuck);
        configDeltaPanel.getChildren().add(new Separator());
    }




}
