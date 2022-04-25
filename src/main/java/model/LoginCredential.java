package model;

public class LoginCredential {
    private String database;
    private String sshUsername;
    private String sshPassword;
    private String postgresUsername;
    private String postgresPassword;
    private String serverName;
    private String ip;

    public LoginCredential(String serverName, String ip, String database, String sshUsername, String sshPassword, String postgresUsername, String postgresPassword) {
        this.serverName = serverName;
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

    @Override
    public String toString() {
        return this.serverName + "( ip: " + this.ip + ")";
    }
}