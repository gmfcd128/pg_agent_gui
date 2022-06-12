package com.example.pg_agent_gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Shortcut;
import model.ShortcutType;

public class MacroEditController {
    private Stage stage;
    private Shortcut editing;
    private Shortcut result;
    public MacroEditController(Shortcut shortcut, Stage stage) {
        this.editing = shortcut;
        this.stage = stage;
    }


    @FXML
    private RadioButton shortcutTypeIsSQL;

    @FXML
    private TextField shortcutNameTextField;

    @FXML
    private Button saveButton;

    @FXML
    private TextArea shortcutContentTextarea;

    @FXML
    private RadioButton shortcutTypeIsShell;

    @FXML
    void onSaveButtonClick(MouseEvent event) {
        result = new Shortcut();
        result.command = shortcutContentTextarea.getText();
        result.name = shortcutNameTextField.getText();
        if (shortcutTypeIsSQL.isSelected()) {
            result.type = ShortcutType.SQL;
        } else if (shortcutTypeIsShell.isSelected()) {
            result.type = ShortcutType.SHELL;
        }
        stage.close();
    }

    public void initialize() {
        ToggleGroup toggleGroup = new ToggleGroup();

        shortcutTypeIsSQL.setToggleGroup(toggleGroup);
        shortcutTypeIsShell.setToggleGroup(toggleGroup);
        shortcutNameTextField.setText(editing.name);
        shortcutContentTextarea.setText(editing.command);
        if (editing.type == ShortcutType.SHELL) {
            shortcutTypeIsShell.setSelected(true);
        } else if (editing.type == ShortcutType.SQL) {
            shortcutTypeIsSQL.setSelected(true);
        }
    }

    public Shortcut getResult() {
        return this.result;
    }
}
