package com.example.pg_agent_gui;

import controller.LocalStorage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import model.LoginCredential;

public class LoginController {
    private ObservableList<LoginCredential> serverList;
    private int currentSelectedServer;
    private boolean unsavedEdit;

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

                    } else if (unsavedEditAlert.getResult() == ButtonType.NO) {

                    } else {
                        return;
                    }
                } else {
                    serverNameTextField.setText(newValue.getServerName());
                    serverIPTextField.setText(newValue.getIp());
                    databaseNameTextField.setText(newValue.getDatabase());
                    sshUsernameTextField.setText(newValue.getSshUsername());
                    sshPasswordTextField.setText(newValue.getSshPassword());
                    postgresUsernameTextField.setText(newValue.getPostgresUsername());
                    postgresPasswordTextField.setText(newValue.getPostgresPassword());
                    currentSelectedServer = serverListView.getEditingIndex();
                }

            }
        });
    }

    @FXML
    void onAddButtonClicked(MouseEvent event) {
         serverList.add(new LoginCredential("New server","","","","","",""));
         serverListView.getSelectionModel().select(serverList.size() - 1);
         unsavedEdit = true;
    }
}
