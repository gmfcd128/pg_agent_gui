package controller;

import com.jcraft.jsch.*;
import model.LoginCredential;
import model.PGConfigDelta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.*;
import java.util.Properties;
import java.util.regex.Pattern;

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

    public Connection getJdbcConnection() {
        return this.jdbcConnection;
    }

    public String executeCommand(String command) {
        String result = "";
        try {
            Channel channel = sshSession.openChannel("exec");
            ((ChannelExec) channel).setCommand(command);
            channel.setInputStream(null);
            OutputStream out = channel.getOutputStream();
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            ((ChannelExec) channel).setPty(true);
            channel.connect();
            out.flush();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    result += new String(tmp, 0, i);
                }
                if (channel.isClosed()) {
                    System.out.println("Exit status: " + channel.getExitStatus());
                    break;
                }
            }
            channel.disconnect();
            System.out.println("command executed: " + command);
        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with SSH: " + e.getCause());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // clean the invisible characters returned by the system
        result = result.replaceAll("\r\n", "");
        result = result.replaceAll(String.valueOf(Pattern.compile("\u001B")), "");
        System.out.print(result.toCharArray());
        return result;
    }

    public String executeCommandWithSudo(String command) {
        String result = "";
        try {
            Channel channel = sshSession.openChannel("exec");
            ((ChannelExec) channel).setCommand("sudo -S -p '' " + command);
            channel.setInputStream(null);
            OutputStream out = channel.getOutputStream();
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            ((ChannelExec) channel).setPty(true);
            channel.connect();
            out.write((loginCredential.getSshPassword() + "\n").getBytes());
            out.flush();
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    System.out.print(new String(tmp, 0, i));
                    result += new String(tmp, 0, i);
                }
                if (channel.isClosed()) {
                    System.out.println("Exit status: " + channel.getExitStatus());
                    break;
                }
            }
            channel.disconnect();
            System.out.println("SUDO command executed: " + command);
        } catch (JSchException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with SSH: " + e.getCause());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // clean the invisible characters returned by the system
        result = result.replaceAll("\r\n", "");
        result = result.replaceAll(String.valueOf(Pattern.compile("\u001B")), "");
        //get rid of the password returned
        result = result.substring(loginCredential.getSshPassword().length());
        return result;
    }

    public void applyPGConfigDelta(PGConfigDelta configDelta) {
        String result = "";
        String command = String.format("psql postgres -c \"alter system set %s to \'%s\'\"", configDelta.getName(), configDelta.getValue());
        result = executeCommand(command);
        if (!result.equals("ALTER SYSTEM")) {
            System.out.println("Error occured when adjusting PG setting: " + result);
        }
    }
}
