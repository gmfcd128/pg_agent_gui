module com.example.pg_agent_gui {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.jcraft.jsch;

    opens com.example.pg_agent_gui to javafx.fxml;
    exports com.example.pg_agent_gui;
}