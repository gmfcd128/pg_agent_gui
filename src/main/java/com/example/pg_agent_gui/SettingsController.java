package com.example.pg_agent_gui;

import controller.ConfigManager;
import controller.LocalStorage;
import controller.PGErrorException;
import controller.Server;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.PGConfigDelta;
import model.ValueType;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsController {
    private Stage stage;
    private Server server;
    private ConfigManager configManager;
    private List<PGConfigDelta> serverConfig;
    private List<PGConfigDelta> localConfig;
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

    @FXML
    private Button uploadButton;


    public void initialize() {
        System.out.println("Settings controller initialized");
        configManager = new ConfigManager();
        showServerConfig();
        reloadConfigDropdown();
        //localConfigComboBox.getSelectionModel().select(0);
    }

    private void showServerConfig() {
        serverConfig = configManager.downloadFromServer(server);
        populateServerGridPane();
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
            localConfig = LocalStorage.getInstance().getConfigurationFromFile(newVal);
            populateLocalGridPane();
        });
    }

    private void populateServerGridPane() {
        serverConfigGridPane.getChildren().clear();
        for (int i = 0; i < serverConfig.size(); i++) {
            PGConfigDelta configDelta = serverConfig.get(i);
            Label label = new Label(configDelta.getName());
            Label value = new Label(configDelta.getValue());
            serverConfigGridPane.add(value, 1, i);
            GridPane.setMargin(value, new Insets(5));

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
            serverConfigGridPane.add(label, 0, i);
            serverConfigGridPane.add(unit, 2, i);
            GridPane.setMargin(label, new Insets(5));
            GridPane.setMargin(unit, new Insets(5));
        }
    }

    private void highlightConfigDiff(PGConfigDelta configDelta, ComboBox<String> comboBox) {
        String selected = comboBox.getSelectionModel().getSelectedItem();
        String key = localConfig.get( GridPane.getRowIndex(comboBox)).getName();
        for (int j = 0; j < serverConfig.size(); j++) {
            if (serverConfig.get(j).getName().equals(key)) {
                if (serverConfig.get(j).getValue().equals(selected)) {
                    comboBox.setStyle("");
                } else {
                    comboBox.setStyle("-fx-text-base-color: red;");
                    configDelta.updateValue(comboBox.getValue());
                }
            }
        }
    }

    private void highlightConfigDiff(PGConfigDelta configDelta, TextField textField) {
        String key = localConfig.get(GridPane.getRowIndex(textField)).getName();
        for (int j = 0; j < serverConfig.size(); j++) {
            if (serverConfig.get(j).getName().equals(key)) {
                if (serverConfig.get(j).getValue().equals(textField.getText())) {
                    textField.setStyle("");
                } else {
                    textField.setStyle("-fx-control-inner-background: red");
                    configDelta.updateValue(textField.getText());
                }

            }

        }
    }

    private void populateLocalGridPane() {
        localConfigGridPane.getChildren().clear();
        for (int i = 0; i < localConfig.size(); i++) {
            PGConfigDelta configDelta = localConfig.get(i);
            Label label = new Label(configDelta.getName());
            if (configDelta.getValueType() == ValueType.ENUM) {
                String[] options = configDelta.getOptions();
                ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList(Arrays.asList(options)));

                comboBox.setOnAction((event -> {
                    String selected = comboBox.getSelectionModel().getSelectedItem();
                    String key = localConfig.get(GridPane.getRowIndex(comboBox)).getName();
                    for (int j = 0; j < serverConfig.size(); j++) {
                        if (serverConfig.get(j).getName().equals(key)) {
                            if (serverConfig.get(j).getValue().equals(selected)) {
                                comboBox.setStyle("");
                            } else {
                                comboBox.setStyle("-fx-text-base-color: red;");
                            }
                            configDelta.updateValue(comboBox.getValue());
                        }
                    }
                }));


                localConfigGridPane.add(comboBox, 1, i);
                comboBox.getSelectionModel().select(configDelta.getValue());
                highlightConfigDiff(configDelta, comboBox);
                GridPane.setMargin(comboBox, new Insets(5));
            } else if (configDelta.getValueType() == ValueType.BOOL) {
                ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("on", "off"));

                comboBox.setOnAction((event -> {
                    String selected = comboBox.getSelectionModel().getSelectedItem();
                    String key = localConfig.get(GridPane.getRowIndex(comboBox)).getName();
                    for (int j = 0; j < serverConfig.size(); j++) {
                        if (serverConfig.get(j).getName().equals(key)) {
                            if (serverConfig.get(j).getValue().equals(selected)) {
                                comboBox.setStyle("");
                            } else {
                                comboBox.setStyle("-fx-text-base-color: red;");
                            }
                            configDelta.updateValue(comboBox.getValue());
                        }
                    }

                }));
                comboBox.getSelectionModel().select(configDelta.getValue());

                localConfigGridPane.add(comboBox, 1, i);
                highlightConfigDiff(configDelta, comboBox);
                GridPane.setMargin(comboBox, new Insets(5));
            } else {
                TextField textField = new TextField();
                textField.setText(configDelta.getValue());

                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    String key = localConfig.get(GridPane.getRowIndex(textField)).getName();
                    for (int j = 0; j < serverConfig.size(); j++) {
                        if (serverConfig.get(j).getName().equals(key)) {
                            if (serverConfig.get(j).getValue().equals(textField.getText())) {
                                textField.setStyle("");
                            } else {
                                textField.setStyle("-fx-control-inner-background: red");
                            }
                            configDelta.updateValue(textField.getText());
                        }
                    }

                });

                localConfigGridPane.add(textField, 1, i);
                highlightConfigDiff(configDelta, textField);
                GridPane.setMargin(textField, new Insets(5));
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
            localConfigGridPane.add(label, 0, i);
            localConfigGridPane.add(unit, 2, i);
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

    @FXML
    void onUploadButtonClicked(MouseEvent event) {
        ArrayList<PGConfigDelta> failedToApply = new ArrayList<>();
        for (PGConfigDelta configDelta : localConfig) {
            if (configDelta.isModified()) {
                try {
                    server.applyPGConfigDelta(configDelta);
                } catch (PGErrorException e) {
                    failedToApply.add(configDelta);
                }
            }
        }
        Alert alert;
        if (failedToApply.size() > 0) {
            alert = new Alert(Alert.AlertType.ERROR, "錯誤:以下設定項目套用失敗");
            String alertContent = "";
            for (PGConfigDelta configDelta : failedToApply) {
                alertContent += configDelta.getName() + ":" + configDelta.getValue() + "\n";
            }
            alert.setContentText(alertContent);

        } else {
            alert = new Alert(Alert.AlertType.INFORMATION, "設定已成功上傳到遠端");
        }
        alert.showAndWait();
        server.restartPostgres();
        showServerConfig();
        populateLocalGridPane();
    }


}
