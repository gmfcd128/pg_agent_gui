package com.example.pg_agent_gui;

import Interface.ServerStateChangeListener;
import controller.Server;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Shortcut;
import model.ShortcutType;

import java.sql.Connection;
import java.sql.ResultSet;

public class MacroExecController {
    private Stage stage;
    private Server server;
    private Shortcut shortcut;
    @FXML
    private Label macroNameLabel;

    @FXML
    private TableView macroResultTableDisplay;

    @FXML
    private TextArea macroResultTextDisplay;

    private ObservableList<ObservableList> data;


    public MacroExecController(Server server, Stage stage, Shortcut shortcut) {
        this.server = server;
        this.stage = stage;
        this.shortcut = shortcut;
    }

    public void initialize() {
        data = FXCollections.observableArrayList();
        ServerStateChangeListener listener = new ServerStateChangeListener() {
            @Override
            public void updateText(String text) {
                macroResultTextDisplay.setText(text);
            }
        };
        macroResultTextDisplay.setVisible(false);
        macroResultTableDisplay.setVisible(false);
        if (shortcut.type == ShortcutType.SHELL) {
            macroResultTextDisplay.setVisible(true);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    server.executeScript(shortcut.command, listener);
                }
            });
            thread.start();
        } else if (shortcut.type == ShortcutType.SQL) {
            macroResultTableDisplay.setVisible(true);
            Connection connection = server.getJdbcConnection();
            String query = shortcut.command;
            try {
                ResultSet rs = connection.createStatement().executeQuery(query);

                /**
                 * ********************************
                 * TABLE COLUMN ADDED DYNAMICALLY *
                 *********************************
                 */
                for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {

                    final int j = i;
                    TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

                    macroResultTableDisplay.getColumns().addAll(col);
                    System.out.println("Column [" + i + "] ");
                }


                while (rs.next()) {
                    //Iterate Row
                    ObservableList<String> row = FXCollections.observableArrayList();
                    for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                        //Iterate Column
                        row.add(rs.getString(i));
                    }

                    data.add(row);

                }

                //FINALLY ADDED TO TableView
                macroResultTableDisplay.setItems(data);
                System.out.println(data);
            } catch (Exception e) {
                e.printStackTrace();

            }


        }

    }
}
