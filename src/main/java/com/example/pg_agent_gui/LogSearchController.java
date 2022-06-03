package com.example.pg_agent_gui;

import controller.LogSearchHandler;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import model.PGLogEntry;

import java.io.File;
import java.io.FilenameFilter;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.UnaryOperator;

public class LogSearchController {
    private LogSearchHandler logSearchHandler;
    private Stage stage;

    private ObservableList<String> distinctApplicationName;
    private ObservableList<String> distinctSessionId;
    private ObservableList<String> distinctUsername;
    private ObservableList<String> distinctDatabaseName;
    private ObservableList<String> distinctHost;

    private ObservableList<PGLogEntry> filteredResult;

    @FXML
    private ListView<String> appNameFilterListView;

    @FXML
    private ListView<String> databaseFilterListView;

    @FXML
    private Button exportButton;

    @FXML
    private Spinner<Integer> timeRangeLowerHour;

    @FXML
    private Button folderBrowseButton;

    @FXML
    private ListView<String> hostFilterListVIew;

    @FXML
    private TableView<PGLogEntry> resultTable;

    @FXML
    private ListView<String> sessionIdFilterListView;

    @FXML
    private DatePicker timeRangeLowerDate;

    @FXML
    private Spinner<Integer> timeRangeLowerMinute;

    @FXML
    private Spinner<Integer> timeRangeLowerSecond;

    @FXML
    private DatePicker timeRangeUpperDate;

    @FXML
    private Spinner<Integer> timeRangeUpperHour;

    @FXML
    private Spinner<Integer> timeRangeUpperMinute;

    @FXML
    private Spinner<Integer> timeRangeUpperSecond;

    @FXML
    private ListView<String> usernameFilterList;
    @FXML
    private Label logDirectoryLabel;

    // columns for tableview
    @FXML
    private TableColumn<PGLogEntry, String> applicationNameTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> backendTypeTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> clientHostTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> commandTagTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> contextTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> databaseNameTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> errorMessageDetailTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> errorMessageTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> errorSeverityTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> hintTableCol;

    @FXML
    private TableColumn<PGLogEntry, Integer> internalQueryPosTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> internalQueryTableCol;

    @FXML
    private TableColumn<PGLogEntry, Integer> leaderPidTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> locationTableCol;

    @FXML
    private TableColumn<PGLogEntry, Integer> processIdTableCol;

    @FXML
    private TableColumn<PGLogEntry, Long> queryIdTableCol;

    @FXML
    private TableColumn<PGLogEntry, Integer> queryPosTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> queryTableCol;

    @FXML
    private TableColumn<PGLogEntry, Long> regularTidTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> sessionIdTableCol;

    @FXML
    private TableColumn<PGLogEntry, Long> sessionLineNumTableCol;

    @FXML
    private TableColumn<PGLogEntry, Date> sessionStartTimeTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> sqlStateTableCol;

    @FXML
    private TableColumn<PGLogEntry, Date> timestampTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> usernameTableCol;

    @FXML
    private TableColumn<PGLogEntry, String> virtualTIdTableCol;

    @FXML
    private Button appNameFilterResetButton;

    @FXML
    private Button databaseFilterResetButton;

    @FXML
    private Button hostFilterResetButton;

    @FXML
    private Button sessionIdFilterResetButton;

    @FXML
    private Button timeRangeApplyButton;

    @FXML
    private Button usernameFilterResetButton;



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
            updateFilterListview();
            populateTableView();
        }

    }

    private void updateFilterListview() {
        distinctApplicationName.setAll(logSearchHandler.getDistinctApplicationName());
        distinctSessionId.setAll(logSearchHandler.getDistinctSessionId());
        distinctUsername.setAll(logSearchHandler.getDistinctUsername());
        distinctDatabaseName.setAll(logSearchHandler.getDistinctDatabaseName());
        distinctHost.setAll(logSearchHandler.getDistinctHost());
    }

    public void initialize() {
        distinctApplicationName = FXCollections.observableArrayList();
        distinctSessionId = FXCollections.observableArrayList();
        distinctUsername = FXCollections.observableArrayList();
        distinctDatabaseName = FXCollections.observableArrayList();
        distinctHost = FXCollections.observableArrayList();
        filteredResult = FXCollections.observableArrayList();
        appNameFilterListView.setItems(distinctApplicationName);
        appNameFilterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Callback<ListView<String>, ListCell<String>> listViewStringFormat = param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item.equals("")) {
                    setText("(empty)");
                } else {
                    setText(item);
                }
            }
        };
        appNameFilterListView.setCellFactory(listViewStringFormat);
        appNameFilterListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            ObservableList<String> selectedItems = appNameFilterListView.getSelectionModel().getSelectedItems();
            List<String> appNameContains = new ArrayList<>();
            for (String name : selectedItems) {
                appNameContains.add(name);
            }
            logSearchHandler.setApplicationNameContains(appNameContains);
            populateTableView();
        });
        sessionIdFilterListView.setItems(distinctSessionId);
        sessionIdFilterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        sessionIdFilterListView.setCellFactory(listViewStringFormat);
        sessionIdFilterListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            ObservableList<String> selectedItems = sessionIdFilterListView.getSelectionModel().getSelectedItems();
            List<String> sessionIdContains = new ArrayList<>();
            for (String name : selectedItems) {
                sessionIdContains.add(name);
            }
            logSearchHandler.setSessionIdContains(sessionIdContains);
            populateTableView();
        });
        usernameFilterList.setItems(distinctUsername);
        usernameFilterList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        usernameFilterList.setCellFactory(listViewStringFormat);
        usernameFilterList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            ObservableList<String> selectedItems = usernameFilterList.getSelectionModel().getSelectedItems();
            List<String> usernameContains = new ArrayList<>();
            for (String name : selectedItems) {
                usernameContains.add(name);
            }
            logSearchHandler.setUsernameContains(usernameContains);
            populateTableView();
        });
        databaseFilterListView.setItems(distinctDatabaseName);
        databaseFilterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        databaseFilterListView.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item.equals("")) {
                    setText("(empty)");
                } else {
                    setText(item);
                }
            }
        });
        databaseFilterListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            ObservableList<String> selectedItems = databaseFilterListView.getSelectionModel().getSelectedItems();
            List<String> databaseContains = new ArrayList<>();
            for (String name : selectedItems) {
                databaseContains.add(name);
            }
            logSearchHandler.setDatabaseContains(databaseContains);
            populateTableView();
        });
        hostFilterListVIew.setItems(distinctHost);
        hostFilterListVIew.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        hostFilterListVIew.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item.equals("")) {
                    setText("(empty)");
                } else {
                    setText(item);
                }
            }
        });
        hostFilterListVIew.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
            ObservableList<String> selectedItems = hostFilterListVIew.getSelectionModel().getSelectedItems();
            List<String> hostContains = new ArrayList<>();
            for (String name : selectedItems) {
                hostContains.add(name);
            }
            logSearchHandler.setHostContains(hostContains);
            populateTableView();
        });

        // set value properties for table columns
        timestampTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Date>("Log_time"));
        usernameTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("User_name"));
        databaseNameTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Database_name"));
        processIdTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Integer>("Process_id"));
        clientHostTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Connection_from"));
        sessionIdTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Session_id"));
        sessionLineNumTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Long>("Session_line_num"));
        commandTagTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Command_tag"));
        sessionStartTimeTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Date>("Session_start_time"));
        virtualTIdTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Virtual_transaction_id"));
        regularTidTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Long>("Transaction_id"));
        errorSeverityTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Error_severity"));
        sqlStateTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Sql_state_code"));
        errorMessageTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Message"));
        errorMessageDetailTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Detail"));
        hintTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Hint"));
        internalQueryTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Internal_query"));
        internalQueryPosTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Integer>("Internal_query_pos"));
        contextTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Context"));
        queryTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Query"));
        queryPosTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Integer>("Query_pos"));
        locationTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Location"));
        applicationNameTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Application_name"));
        backendTypeTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, String>("Backend_type"));
        leaderPidTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Integer>("Leader_pid"));
        queryIdTableCol.setCellValueFactory(new PropertyValueFactory<PGLogEntry, Long>("Query_id"));
        resultTable.setItems(filteredResult);
        populateTableView();
        setupTimeFilterUI();
    }

    private void setupTimeFilterUI() {
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

        timeRangeLowerDate.setValue(LocalDate.now());

        timeRangeLowerHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 23, 0));
        timeRangeLowerHour.setEditable(true);
        timeRangeLowerHour.getEditor().setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter));
        timeRangeLowerMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 59, 0));
        timeRangeLowerMinute.setEditable(true);
        timeRangeLowerMinute.getEditor().setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter));
        timeRangeLowerSecond.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 59, 0));
        timeRangeLowerSecond.setEditable(true);
        timeRangeLowerSecond.getEditor().setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter));


        timeRangeUpperDate.setValue(LocalDate.now());
        timeRangeUpperHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 23, 0));
        timeRangeUpperHour.setEditable(true);
        timeRangeUpperHour.getEditor().setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter));
        timeRangeUpperMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 59, 0));
        timeRangeUpperMinute.setEditable(true);
        timeRangeUpperMinute.getEditor().setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter));
        timeRangeUpperSecond.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                0, 59, 0));
        timeRangeUpperSecond.setEditable(true);
        timeRangeUpperSecond.getEditor().setTextFormatter(new TextFormatter<Integer>(
                new IntegerStringConverter(), 0, filter));
    }

    private void populateTableView() {
        filteredResult.clear();
        filteredResult.addAll(logSearchHandler.calculateResult());
    }

    @FXML
    void onAppNameFilterResetButtonClick(MouseEvent event) {
        appNameFilterListView.getSelectionModel().clearSelection();
    }

    @FXML
    void onDatabaseFilterResetButtonClick(MouseEvent event) {
        databaseFilterListView.getSelectionModel().clearSelection();
    }

    @FXML
    void onHostFilterResetButtonClick(MouseEvent event) {
        hostFilterListVIew.getSelectionModel().clearSelection();
    }

    @FXML
    void onSessionIdFilterResetButtonClick(MouseEvent event) {
        sessionIdFilterListView.getSelectionModel().clearSelection();
    }

    @FXML
    void onTimeRangeApplyButtonClick(MouseEvent event) {
        LocalDate localDateLower = timeRangeLowerDate.getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, localDateLower.getYear());
        calendar.set(Calendar.MONTH, localDateLower.getMonthValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, localDateLower.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, timeRangeLowerHour.getValue());
        calendar.set(Calendar.MINUTE, timeRangeLowerMinute.getValue());
        calendar.set(Calendar.SECOND, timeRangeLowerSecond.getValue());
        Date timeRangeLower = calendar.getTime();
        LocalDate localDateUpper = timeRangeUpperDate.getValue();
        calendar.set(Calendar.YEAR, localDateUpper.getYear());
        calendar.set(Calendar.MONTH, localDateUpper.getMonthValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, localDateUpper.getDayOfMonth());
        calendar.set(Calendar.HOUR_OF_DAY, timeRangeUpperHour.getValue());
        calendar.set(Calendar.MINUTE, timeRangeUpperMinute.getValue());
        calendar.set(Calendar.SECOND, timeRangeUpperSecond.getValue());
        Date timeRangeUpper = calendar.getTime();
        if (timeRangeLower.after(timeRangeUpper)) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "開始日期不得在結束時間之後.", ButtonType.OK);
            alert.showAndWait();
        } else {
            logSearchHandler.setFilterTimeLower(timeRangeLower);
            logSearchHandler.setFilterTimeUpper(timeRangeUpper);
            logSearchHandler.calculateResult();
            updateFilterListview();
            populateTableView();
        }

    }

    @FXML
    void onUsernameFilterResetButtonClick(MouseEvent event) {
        usernameFilterList.getSelectionModel().clearSelection();
    }


}
