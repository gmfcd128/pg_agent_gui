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
import model.LoginCredential;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController {
    private ObservableList<LoginCredential> serverList;
    private int currentSelectedServer;
    private boolean unsavedEdit;
    private Stage stage;
    public LoginController(Stage stage) {
        this.stage = stage;
    }


    @FXML
    private ListView<LoginCredential> serverListView;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonDelete;

    @FXML
    private Button buttonLogin;

    @FXML
    private TextField databaseNameTextField;

    @FXML
    private TextField postgresPasswordTextField;

    @FXML
    private TextField postgresUsernameTextField;

    @FXML
    private TextField serverIPTextField;

    @FXML
    private TextField serverNameTextField;

    @FXML
    private TextField sshPasswordTextField;

    @FXML
    private TextField sshUsernameTextField;

    public void initialize() {
        unsavedEdit = false;
        serverList = FXCollections.observableArrayList();
        serverList.addAll(LocalStorage.getInstance().getLoginCredentials());
        serverListView.setItems(serverList);
        serverListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<LoginCredential>() {

            @Override
            public void changed(ObservableValue<? extends LoginCredential> observable, LoginCredential oldValue, LoginCredential newValue) {
                // Your action here
                if (unsavedEdit) {
                    Alert unsavedEditAlert = new Alert(Alert.AlertType.CONFIRMATION, "是否將登入資訊變更儲存?", ButtonType.YES, ButtonType.NO);
                    unsavedEditAlert.showAndWait();
                    if (unsavedEditAlert.getResult() == ButtonType.YES) {
                        updateSelectedServer();
                        LocalStorage.getInstance().saveLoginCredentials(new ArrayList<LoginCredential>(serverList));
                    } else if (unsavedEditAlert.getResult() == ButtonType.NO) {
                        serverList.remove(serverList.size() - 1);
                        serverListView.getSelectionModel().select(serverList.size() - 1);
                    }
                    buttonAdd.setDisable(false);
                    unsavedEdit = false;
                } else {
                    serverNameTextField.setText(newValue.getServerName());
                    serverIPTextField.setText(newValue.getIp());
                    databaseNameTextField.setText(newValue.getDatabase());
                    sshUsernameTextField.setText(newValue.getSshUsername());
                    sshPasswordTextField.setText(newValue.getSshPassword());
                    postgresUsernameTextField.setText(newValue.getPostgresUsername());
                    postgresPasswordTextField.setText(newValue.getPostgresPassword());
                    currentSelectedServer = serverListView.getSelectionModel().getSelectedIndex();;
                }

            }
        });
    }

    private void updateSelectedServer() {
        serverList.get(currentSelectedServer).setServerName(serverNameTextField.getText());
        serverList.get(currentSelectedServer).setIp(serverIPTextField.getText());
        serverList.get(currentSelectedServer).setDatabase(databaseNameTextField.getText());
        serverList.get(currentSelectedServer).setSshUsername(sshUsernameTextField.getText());
        serverList.get(currentSelectedServer).setSshPassword(sshPasswordTextField.getText());
        serverList.get(currentSelectedServer).setPostgresUsername(postgresUsernameTextField.getText());
        serverList.get(currentSelectedServer).setPostgresPassword(postgresPasswordTextField.getText());
        serverListView.refresh();
    }


    @FXML
    void onAddButtonClicked(MouseEvent event) {
         serverList.add(new LoginCredential("New server","","","","","",""));
         serverListView.getSelectionModel().select(serverList.size());
         buttonAdd.setDisable(true);
         unsavedEdit = true;
    }

    @FXML
    void onSaveButtonClicked(MouseEvent event) {
        updateSelectedServer();
        LocalStorage.getInstance().saveLoginCredentials(new ArrayList<LoginCredential>(serverList));
        buttonAdd.setDisable(false);
    }

    @FXML
    void onDeleteButtonCLicked(MouseEvent event) {
        Alert unsavedEditAlert = new Alert(Alert.AlertType.CONFIRMATION, "真的要刪除" + serverList.get(currentSelectedServer) + "嗎?", ButtonType.YES, ButtonType.NO);
        unsavedEditAlert.showAndWait();
        if (unsavedEditAlert.getResult() == ButtonType.YES) {
            serverList.remove(currentSelectedServer);
            serverListView.refresh();
            serverListView.getSelectionModel().select(serverList.size());
            LocalStorage.getInstance().saveLoginCredentials(new ArrayList<LoginCredential>(serverList));
        }
    }

    @FXML
    void onLoginButtonClicked(MouseEvent event) {
        Server server = new Server(serverList.get(currentSelectedServer));
        try {
            server.createConnections();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "與主機建立SQL連線時發生錯誤.", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
        } catch (JSchException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "與主機建立SSH連線時發生錯誤.", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
        }
        // create main window
        this.stage.close();
        try {
            ViewFactory.showMainWindow(server);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
