package controller;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import model.LoginCredential;

import java.sql.*;
import java.util.Properties;

public class Server {
    private LoginCredential loginCredential;
    private Connection jdbcConnection;
    private Session sshSession;

    public Server(LoginCredential loginCredential) {
        this.loginCredential = loginCredential;
    }

    public void createConnections() throws SQLException, JSchException {
        // try to create connection to the database
        this.jdbcConnection = DriverManager.getConnection("jdbc:postgresql://" + loginCredential.getIp() + ":5432/" +
                loginCredential.getDatabase(), loginCredential.getPostgresUsername(), loginCredential.getPostgresPassword());
        // try to establish ssh connection
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        JSch jsch = new JSch();
        sshSession = jsch.getSession(loginCredential.getSshUsername(), loginCredential.getIp(), 22);
        sshSession.setPassword(loginCredential.getSshPassword());
        sshSession.setConfig(config);
        sshSession.connect();
    }

    public String getName() {
        return this.loginCredential.getServerName();
    }
}

