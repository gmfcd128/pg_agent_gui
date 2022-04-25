module com.example.pg_agent_gui {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pg_agent_gui to javafx.fxml;
    exports com.example.pg_agent_gui;
}