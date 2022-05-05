package com.example.pg_agent_gui;

import controller.LocalStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import model.PGConfigDelta;
import model.TestPlan;

import java.io.File;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.*;
import java.util.function.UnaryOperator;

public class TestPlanController {
    private Stage stage;
    private List<PGConfigDelta> availableConfigDeltas;
    private HashMap<PGConfigDelta, VBox> configSectionMap;
    private ObservableList<File> localConfigFiles;
    private ObservableList<File> localTestPlans;
    private TestPlan currentTestPlan;
    @FXML
    private ComboBox<File> testPlanComboBox;
    @FXML
    private Button newPlanButton;

    @FXML
    private Button savePlanButton;
    @FXML
    private VBox configSectionVBox;
    @FXML
    private Spinner<Integer> executionCountSpinner;
    @FXML
    private Spinner<Integer> threadCountSpinner;
    @FXML
    private ComboBox<File> localConfigComboBox;
    @FXML
    private ComboBox<PGConfigDelta> availableDeltaComboBox;
    @FXML
    private Button addSectionButton;
    @FXML
    private Label sqlDirectoryLabel;

    @FXML
    private Button selectSQLDirectoryButton;

    public TestPlanController(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        configSectionMap = new HashMap<>();
        availableConfigDeltas = new ArrayList<>();
        setupSpinners();
        reloadTestPlanDropdown();
        reloadConfigDropdown();
    }

    private void appendConfigSection(PGConfigDelta configDelta, List<String> values) {
        VBox shit = new VBox();
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_LEFT);
        Text sectionTitle = new Text(configDelta.getName());
        sectionTitle.setStyle("-fx-font: 18 arial;");
        header.getChildren().add(sectionTitle);

        FlowPane fieldsPane = new FlowPane();
        Button deleteAreaButton = new Button("移除");
        deleteAreaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                removeSectionByName(configDelta.getName());
            }
        });
        VBox.setMargin(deleteAreaButton, new Insets(5));
        header.getChildren().add(deleteAreaButton);

        fieldsPane.prefWidthProperty().bind(configSectionVBox.widthProperty());
        Button addFieldButton = new Button("+");
        FlowPane.setMargin(addFieldButton, new Insets(5, 0, 5, 5));
        Button deleteFieldButton = new Button("-");
        FlowPane.setMargin(deleteFieldButton, new Insets(5, 5, 5, 0));
        if (values != null) {
            for (String value : values) {
                TextField textField = new TextField(value);
                textField.setPadding(new Insets(5, 5, 5, 5));
                fieldsPane.getChildren().add(fieldsPane.getChildren().size(), textField);
            }
        }

        addFieldButton.setOnAction(event -> {
            TextField textField = new TextField();
            textField.setPadding(new Insets(5, 5, 5, 5));
            fieldsPane.getChildren().add(fieldsPane.getChildren().size() - 2, textField);

        });

        deleteFieldButton.setOnAction(event -> {
            fieldsPane.getChildren().remove(fieldsPane.getChildren().size() - 3);
            if (fieldsPane.getChildren().size() == 2) {
                removeSectionByName(configDelta.getName());
            }
        });
        fieldsPane.getChildren().add(addFieldButton);
        fieldsPane.getChildren().add(deleteFieldButton);
        shit.getChildren().add(header);
        shit.getChildren().add(fieldsPane);
        shit.getChildren().add(new Separator());
        configSectionMap.put(configDelta, shit);
        configSectionVBox.setPadding(new Insets(10));
        configSectionVBox.getChildren().add(shit);
        if (values == null) {
            addFieldButton.fire();
        }

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
            availableConfigDeltas = LocalStorage.getInstance().getConfigDeltaFromFile(newVal);

            loadConfigDeltaDropdown();
        });
    }

    private void reloadTestPlanDropdown() {
        File[] testPlans = LocalStorage.getInstance().getTestPlans();
        localTestPlans = FXCollections.observableList(new ArrayList<>(Arrays.asList(testPlans)));
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
        testPlanComboBox.setConverter(converter);
        testPlanComboBox.setItems(localTestPlans);
        testPlanComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentTestPlan = LocalStorage.getInstance().getTestPlanFromFile(newVal);
            populateTestPlanOptions();
        });

    }

    private void populateTestPlanOptions() {
        configSectionMap.clear();
        configSectionVBox.getChildren().clear();
        threadCountSpinner.getValueFactory().setValue(currentTestPlan.getNumberOfThreads());
        executionCountSpinner.getValueFactory().setValue(currentTestPlan.getNumberOfRuns());
        for(int i = 0;i < localConfigFiles.size(); i++) {
            if (localConfigFiles.get(i).getName().equals(currentTestPlan.getBaseConfigName())) {
                localConfigComboBox.getSelectionModel().select(i);
            }
        }

        sqlDirectoryLabel.setText(currentTestPlan.getPayloadDirectory());
        for (Map.Entry<PGConfigDelta, List<String>> entry : currentTestPlan.getValues().entrySet()) {
            appendConfigSection(entry.getKey(), entry.getValue());
        }
    }

    private void loadConfigDeltaDropdown() {
        availableDeltaComboBox.setItems(FXCollections.observableArrayList(availableConfigDeltas));
        StringConverter<PGConfigDelta> converter = new StringConverter<PGConfigDelta>() {
            @Override
            public String toString(PGConfigDelta object) {
                if (object != null) {
                    return object.getName();
                } else {
                    return null;
                }
            }

            @Override
            public PGConfigDelta fromString(String string) {
                return null;
            }
        };
        availableDeltaComboBox.setConverter(converter);
    }

    private void removeSectionByName(String name) {
        for (PGConfigDelta configDelta : configSectionMap.keySet()) {
            if (configDelta.getName().equals(name)) {
                configSectionMap.get(configDelta).getChildren().clear();
                configSectionMap.remove(configDelta);
                return;
            }
        }
    }

    private void setupSpinners() {
        NumberFormat format = NumberFormat.getIntegerInstance();
        UnaryOperator<TextFormatter.Change> filter = c -> {
            if (c.isContentChange()) {
                ParsePosition parsePosition = new ParsePosition(0);
                // NumberFormat evaluates the beginning of the text
                format.parse(c.getControlNewText(), parsePosition);
                if (parsePosition.getIndex() == 0 ||
                        parsePosition.getIndex() < c.getControlNewText().length()) {
                    // reject parsing the complete text failed
                    return null;
                }
            }
            return c;
        };

        executionCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, 10000, 1));
        executionCountSpinner.setEditable(true);
        executionCountSpinner.getEditor().setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 1, filter));
        threadCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, 10000, 1));
        threadCountSpinner.setEditable(true);
        threadCountSpinner.getEditor().setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 1, filter));
    }

    @FXML
    void onAddSectionButtonClicked(MouseEvent event) {
        PGConfigDelta selectedItem = availableDeltaComboBox.getSelectionModel().getSelectedItem();
        for (PGConfigDelta configDelta : configSectionMap.keySet()) {
            if (configDelta.getName().equals(selectedItem.getName())) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "此參數已經被選擇過了");
                alert.showAndWait();
                return;
            }
        }
        appendConfigSection(selectedItem, null);
    }

    @FXML
    void onNewPlanButtonClicked(MouseEvent event) {
        TextInputDialog td = new TextInputDialog();
        td.setHeaderText("輸入檔案名稱");
        td.showAndWait();
        String fileName = td.getEditor().getText();
        if (LocalStorage.getInstance().planFileExists(fileName)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "檔案名稱已存在，是否覆寫?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.NO) {
                currentTestPlan = new TestPlan(fileName, 1, 1);
                LocalStorage.getInstance().saveTestPlan(fileName + "(new)", currentTestPlan);
                reloadConfigDropdown();
                return;
            }
        }
        currentTestPlan = new TestPlan(fileName, 1, 1);
        LocalStorage.getInstance().saveTestPlan(fileName, currentTestPlan);
        reloadConfigDropdown();
    }

    @FXML
    void onSavePlanButtonClicked(MouseEvent event) {
        currentTestPlan.setNumberOfRuns(executionCountSpinner.getValue());
        currentTestPlan.setNumberOfThreads(threadCountSpinner.getValue());
        currentTestPlan.setBaseConfigName(localConfigComboBox.getSelectionModel().getSelectedItem().getName());
        HashMap<PGConfigDelta, List<String>> data = new HashMap<>();
        for (Map.Entry<PGConfigDelta, VBox> entry : configSectionMap.entrySet()) {
            List<String> settingsValues = new ArrayList<>();
            for (Node node:  ((FlowPane)entry.getValue().getChildren().get(1)).getChildren()) {
                if (node instanceof TextField) {
                    settingsValues.add(((TextField) node).getText());
                }
            }
            data.put(entry.getKey(), settingsValues);
        }
        currentTestPlan.setValues(data);

        if (LocalStorage.getInstance().saveTestPlan(testPlanComboBox.getSelectionModel().getSelectedItem().getName(), currentTestPlan)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "測試計畫儲存成功", ButtonType.OK);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "測試計畫儲存失敗", ButtonType.OK);
            alert.showAndWait();
        }

    }

    @FXML
    void onSelectDirectoryButtonClicked(MouseEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            //No Directory selected
        } else {
            //System.out.println(selectedDirectory.getAbsolutePath());
            sqlDirectoryLabel.setText(selectedDirectory.getAbsolutePath());
            currentTestPlan.setPayloadDirectory(selectedDirectory.getAbsolutePath());
        }

    }


}
