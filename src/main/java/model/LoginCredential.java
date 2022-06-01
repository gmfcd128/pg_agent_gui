package model;

import java.io.Serializable;

public class LoginCredential implements Serializable {
    private String database;
    private String sshUsername;
    private String sshPassword;
    private String postgresUsername;
    private String postgresPassword;
    private String serverName;
    private String ip;

    public LoginCredential(String ip, String database, String sshUsername, String sshPassword, String postgresUsername, String postgresPassword) {
        this.ip = ip;
        this.database = database;
        this.sshUsername = sshUsername;
        this.sshPassword = sshPassword;
        this.postgresUsername = postgresUsername;
        this.postgresPassword = postgresPassword;
    }

    public String getServerName() {
        return serverName;
    }

    public String getIp() {
        return ip;
    }

    public String getDatabase() {
        return database;
    }

    public String getSshUsername() {
        return sshUsername;
    }

    public String getSshPassword() {
        return sshPassword;
    }

    public String getPostgresUsername() {
        return postgresUsername;
    }

    public String getPostgresPassword() {
        return postgresPassword;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setSshUsername(String sshUsername) {
        this.sshUsername = sshUsername;
    }

    public void setSshPassword(String sshPassword) {
        this.sshPassword = sshPassword;
    }

    public void setPostgresUsername(String postgresUsername) {
        this.postgresUsername = postgresUsername;
    }

    public void setPostgresPassword(String postgresPassword) {
        this.postgresPassword = postgresPassword;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return this.serverName + "( IP: " + this.ip + ")";
    }
}
