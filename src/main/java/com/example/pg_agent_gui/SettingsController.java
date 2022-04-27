package com.example.pg_agent_gui;

import controller.ConfigManager;
import controller.LocalStorage;
import controller.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.PGConfigDelta;
import model.ValueType;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsController {
    private Stage stage;
    private Server server;
    private ConfigManager configManager;
    private List<PGConfigDelta> serverConfig;
    private ObservableList<File> localConfigFiles;

    public SettingsController(Stage stage, Server server) {
        this.stage = stage;
        this.server = server;
    }

    @FXML
    private GridPane localConfigGridPane;

    @FXML
    private VBox parentVBox;

    @FXML
    private GridPane serverConfigGridPane;

    @FXML
    private ComboBox<File> localConfigComboBox;

    @FXML
    private Button downloadButton;


    public void initialize() {
        System.out.println("Settings controller initialized");
        configManager = new ConfigManager();
        serverConfig = configManager.downloadFromServer(server);
        System.out.println("Server configuration get!");
        System.out.println(serverConfig);
        populateGridView(serverConfigGridPane, serverConfig, true);

        reloadConfigDropdown();
    }

    private void reloadConfigDropdown() {
        File[] configFiles = LocalStorage.getInstance().getLocalConfigFiles();
        localConfigFiles = FXCollections.observableList(new ArrayList<>(Arrays.asList(configFiles)));
        StringConverter<File> converter = new StringConverter<File>() {
            @Override
            public String toString(File object) {
                if (object != null) {
                    return object.getName();
                } else {
                    return null;
                }
            }

            @Override
            public File fromString(String string) {
                return null;
            }
        };
        localConfigComboBox.setConverter(converter);
        localConfigComboBox.setItems(localConfigFiles);
        localConfigComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            List<PGConfigDelta> deltaList = LocalStorage.getInstance().getConfigurationFromFile(newVal);
            populateGridView(localConfigGridPane, deltaList, false);
        });
    }

    private void populateGridView(GridPane gridPane, List<PGConfigDelta> configDeltas, boolean readOnly) {
        for (int i = 0; i < configDeltas.size(); i++) {
            PGConfigDelta configDelta = configDeltas.get(i);
            Label label = new Label(configDelta.getName());
            if (readOnly) {
                Label value = new Label(configDelta.getValue());
                gridPane.add(value, 1, i);
                GridPane.setMargin(value, new Insets(5));
            } else {
                if (configDelta.getValueType() == ValueType.ENUM) {
                    String[] options = configDelta.getOptions();
                    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(Arrays.asList(options)));
                    comboBox.getSelectionModel().select(configDelta.getValue());
                    gridPane.add(comboBox, 1, i);
                    GridPane.setMargin(comboBox, new Insets(5));
                } else if (configDelta.getValueType() == ValueType.BOOL) {
                    ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("on", "off"));
                    comboBox.getSelectionModel().select(configDelta.getValue());
                    gridPane.add(comboBox, 1, i);
                    GridPane.setMargin(comboBox, new Insets(5));
                } else {
                    TextField textField = new TextField(configDelta.getValue());
                    gridPane.add(textField, 1, i);
                    GridPane.setMargin(textField, new Insets(5));
                }
            }

            Label unit = new Label();
            if (configDelta.getValueType() == ValueType.INTEGER || configDelta.getValueType() == ValueType.REAL) {
                if (configDelta.getUnit() != null) {
                    unit.setText(configDelta.getUnit() + " (range: " + configDelta.getAllowedMin() + "~" + configDelta.getAllowedMax() + ")");
                } else {
                    unit.setText("(range: " + configDelta.getAllowedMin() + "~" + configDelta.getAllowedMax() + ")");
                }
            } else {
                unit.setText(configDelta.getUnit());
            }
            gridPane.add(label, 0, i);
            gridPane.add(unit, 2, i);
            GridPane.setMargin(label, new Insets(5));
            GridPane.setMargin(unit, new Insets(5));
        }
    }

    @FXML
    void onDownloadButtonClick(MouseEvent event) {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("輸入檔案名稱");
        td.showAndWait();
        String fileName = td.getEditor().getText();
        LocalStorage.getInstance().saveConfigLocally(fileName, serverConfig);
        reloadConfigDropdown();
    }


}
