package com.example.pg_agent_gui;

import com.jcraft.jsch.JSchException;
import controller.LocalStorage;
import controller.Server;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.ConnectionProfile;
import model.LoginCredential;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController {
    private ObservableList<ConnectionProfile> connectionProfileList;
    private int currentSelectedProfile;
    private boolean unsavedEdit;
    private Stage stage;
    @FXML
    private ListView<ConnectionProfile> connectionProfileListView;
    @FXML
    private Button buttonAdd;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonLogin;
    @FXML
    private TextField databaseNameTextField;
    @FXML
    private PasswordField postgresPasswordField;
    @FXML
    private TextField postgresUsernameField;
    @FXML
    private TextField serverIPTextField;
    @FXML
    private TextField serverNameTextField;
    @FXML
    private PasswordField sshPasswordField;
    @FXML
    private TextField sshUsernameTextField;

    @FXML
    private TextField postgresRestartCommandField;

    @FXML
    private TextField sqlTotalTimeCommandField;

    public LoginController(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        unsavedEdit = false;
        connectionProfileList = FXCollections.observableArrayList();
        connectionProfileList.addAll(LocalStorage.getInstance().getConnectionProfiles());
        connectionProfileListView.setItems(connectionProfileList);
        connectionProfileListView.setCellFactory(param -> new ListCell<ConnectionProfile>() {
            @Override
            protected void updateItem(ConnectionProfile item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getLoginCredential().getServerName() + " (host:" + item.getLoginCredential().getIp() + ")");
                }
            }
        });
        connectionProfileListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ConnectionProfile>() {

            @Override
            public void changed(ObservableValue<? extends ConnectionProfile> observable, ConnectionProfile oldValue, ConnectionProfile newValue) {
                if (buttonDelete.isDisabled()) {
                    buttonDelete.setDisable(false);
                }
                // Your action here
                if (unsavedEdit) {
                    Alert unsavedEditAlert = new Alert(Alert.AlertType.CONFIRMATION, "是否將登入資訊變更儲存?", ButtonType.YES, ButtonType.NO);
                    unsavedEditAlert.showAndWait();
                    if (unsavedEditAlert.getResult() == ButtonType.YES) {
                        updateSelectedServer();
                        LocalStorage.getInstance().saveConnectionProfiles(new ArrayList<ConnectionProfile>(connectionProfileList));
                    } else if (unsavedEditAlert.getResult() == ButtonType.NO) {
                        connectionProfileList.remove(connectionProfileList.size() - 1);
                        connectionProfileListView.getSelectionModel().select(connectionProfileList.size() - 1);
                    }
                    buttonAdd.setDisable(false);
                    unsavedEdit = false;
                } else {
                    serverNameTextField.setText(newValue.getLoginCredential().getServerName());
                    serverIPTextField.setText(newValue.getLoginCredential().getIp());
                    databaseNameTextField.setText(newValue.getLoginCredential().getDatabase());
                    sshUsernameTextField.setText(newValue.getLoginCredential().getSshUsername());
                    sshPasswordField.setText(newValue.getLoginCredential().getSshPassword());
                    postgresUsernameField.setText(newValue.getLoginCredential().getPostgresUsername());
                    postgresPasswordField.setText(newValue.getLoginCredential().getPostgresPassword());
                    postgresRestartCommandField.setText(newValue.getServerImplementation().getPostgresRestartCommand());
                    sqlTotalTimeCommandField.setText(newValue.getServerImplementation().getTotalSqlTimeCommand());
                    currentSelectedProfile = connectionProfileListView.getSelectionModel().getSelectedIndex();
                }
            }

        });
    }

    private void updateSelectedServer() {
        connectionProfileList.get(currentSelectedProfile).getLoginCredential().setServerName(serverNameTextField.getText());
        connectionProfileList.get(currentSelectedProfile).getLoginCredential().setIp(serverIPTextField.getText());
        connectionProfileList.get(currentSelectedProfile).getLoginCredential().setDatabase(databaseNameTextField.getText());
        connectionProfileList.get(currentSelectedProfile).getLoginCredential().setSshUsername(sshUsernameTextField.getText());
        connectionProfileList.get(currentSelectedProfile).getLoginCredential().setSshPassword(sshPasswordField.getText());
        connectionProfileList.get(currentSelectedProfile).getLoginCredential().setPostgresUsername(postgresUsernameField.getText());
        connectionProfileList.get(currentSelectedProfile).getLoginCredential().setPostgresPassword(postgresPasswordField.getText());
        connectionProfileList.get(currentSelectedProfile).getServerImplementation().setPostgresRestartCommand(postgresRestartCommandField.getText());
        connectionProfileList.get(currentSelectedProfile).getServerImplementation().setTotalSqlTimeCommand(sqlTotalTimeCommandField.getText());
        connectionProfileListView.refresh();
    }


    @FXML
    void onAddButtonClicked(MouseEvent event) {
        connectionProfileList.add(new ConnectionProfile("New Server"));
        connectionProfileListView.getSelectionModel().select(connectionProfileList.size());
        buttonAdd.setDisable(true);
        unsavedEdit = true;
    }

    @FXML
    void onSaveButtonClicked(MouseEvent event) {
        updateSelectedServer();
        LocalStorage.getInstance().saveConnectionProfiles(new ArrayList<ConnectionProfile>(connectionProfileList));
        buttonAdd.setDisable(false);
    }

    @FXML
    void onDeleteButtonCLicked(MouseEvent event) {
        Alert unsavedEditAlert = new Alert(Alert.AlertType.CONFIRMATION, "真的要刪除" + connectionProfileList.get(currentSelectedProfile) + "嗎?", ButtonType.YES, ButtonType.NO);
        unsavedEditAlert.showAndWait();
        if (unsavedEditAlert.getResult() == ButtonType.YES) {
            connectionProfileList.remove(currentSelectedProfile);
            connectionProfileListView.refresh();
            connectionProfileListView.getSelectionModel().select(connectionProfileList.size());
            LocalStorage.getInstance().saveConnectionProfiles(new ArrayList<ConnectionProfile>(connectionProfileList));
        }
    }

    @FXML
    void onLoginButtonClicked(MouseEvent event) {
        Server server = new Server(connectionProfileList.get(currentSelectedProfile));
        try {
            server.createConnections();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "與主機建立SQL連線時發生錯誤.");
            alert.showAndWait();
            return;
        } catch (JSchException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "與主機建立SSH連線時發生錯誤.");
            alert.showAndWait();
            return;
        }
        // create main window
        this.stage.close();
        try {
            ViewFactory.showMainWindow(server);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void onLogSearchButtonClicked(MouseEvent event) {
        try {
            ViewFactory.showLogSearchWindow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
