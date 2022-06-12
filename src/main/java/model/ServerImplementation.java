package model;

import java.io.Serializable;

public class ServerImplementation implements Serializable {
    public void setPostgresRestartCommand(String postgresRestartCommand) {
        this.postgresRestartCommand = postgresRestartCommand;
    }

    public void setTotalSqlTimeCommand(String totalSqlTimeCommand) {
        this.totalSqlTimeCommand = totalSqlTimeCommand;
    }

    private String postgresRestartCommand;
    private String totalSqlTimeCommand;

    public String getPostgresRestartCommand() {
        return postgresRestartCommand;
    }

    public String getTotalSqlTimeCommand() {
        return totalSqlTimeCommand;
    }
}
