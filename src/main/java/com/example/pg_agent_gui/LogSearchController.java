package com.example.pg_agent_gui;

import controller.LogSearchHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.PGLogEntry;

import java.io.File;
import java.io.FilenameFilter;

public class LogSearchController {
    LogSearchHandler logSearchHandler;
    private Stage stage;

    private ObservableList<String> distinctApplicationName;
    private ObservableList<String> distinctSessionId;
    private ObservableList<String> distinctUsername;
    private ObservableList<String> distinctDatabaseName;
    private ObservableList<String> distinctHost;

    @FXML
    private ListView<String> appNameFilterListView;

    @FXML
    private ListView<String> databaseFilterListView;

    @FXML
    private Button exportButton;

    @FXML
    private Spinner<String> timeRangeLowerHour;

    @FXML
    private Button folderBrowseButton;

    @FXML
    private ListView<String> hostFilterListVIew;

    @FXML
    private TableView<PGLogEntry> resultTable;

    @FXML
    private ListView<String> sessionIdFilterListView;

    @FXML
    private DatePicker timeRangeLowerData;

    @FXML
    private Spinner<?> timeRangeLowerMinute;

    @FXML
    private Spinner<?> timeRangeLowerSecond;

    @FXML
    private DatePicker timeRangeUpperDate;

    @FXML
    private Spinner<?> timeRangeUpperHour;

    @FXML
    private Spinner<?> timeRangeUpperMinute;

    @FXML
    private Spinner<?> timeRangeUpperSecond;

    @FXML
    private ListView<String> usernameFilterList;
    @FXML
    private Label logDirectoryLabel;

    public LogSearchController(Stage stage) {
        this.stage = stage;
        this.logSearchHandler = new LogSearchHandler();
    }

    @FXML
    void onExportButtonCLick(MouseEvent event) {

    }

    @FXML
    void onFolderBrowseButtonClick(MouseEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(stage);

        if (selectedDirectory == null) {
            //No Directory selected
        } else {
            //System.out.println(selectedDirectory.getAbsolutePath());
            logDirectoryLabel.setText(selectedDirectory.getAbsolutePath());
            File f = new File(selectedDirectory.getAbsolutePath());
            FilenameFilter textFilter = new FilenameFilter() {
                @Override
                public boolean accept(java.io.File file, String s) {
                    return s.toLowerCase().endsWith(".csv");
                }
            };
            File[] files = f.listFiles(textFilter);
            logSearchHandler.reset();
            for (File file : files) {
                logSearchHandler.loadFromFile(file.getAbsolutePath());
            }
            distinctApplicationName.setAll(logSearchHandler.getDistinctApplicationName());
            distinctSessionId.setAll(logSearchHandler.getDistinctSessionId());
            distinctUsername.setAll(logSearchHandler.getDistinctUsername());
            distinctDatabaseName.setAll(logSearchHandler.getDistinctDatabaseName());
            distinctHost.setAll(logSearchHandler.getDistinctHost());
        }

    }

    public void initialize() {
        distinctApplicationName = FXCollections.observableArrayList();
        distinctSessionId = FXCollections.observableArrayList();
        distinctUsername = FXCollections.observableArrayList();
        distinctDatabaseName = FXCollections.observableArrayList();
        distinctHost = FXCollections.observableArrayList();
        appNameFilterListView.setItems(distinctApplicationName);
        appNameFilterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        sessionIdFilterListView.setItems(distinctSessionId);
        sessionIdFilterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        usernameFilterList.setItems(distinctUsername);
        usernameFilterList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        databaseFilterListView.setItems(distinctDatabaseName);
        databaseFilterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        hostFilterListVIew.setItems(distinctHost);
        hostFilterListVIew.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }


}
