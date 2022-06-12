package model;

import java.io.Serializable;

public class ConnectionProfile implements Serializable {
    private String name;
    private LoginCredential loginCredential;
    private ServerImplementation serverImplementation;

    public ConnectionProfile(String name) {
        this.name = name;
        this.loginCredential = new LoginCredential("", "", "", "", "", "");
        this.serverImplementation = new ServerImplementation();
    }

    public LoginCredential getLoginCredential() {
        return this.loginCredential;
    }

    public ServerImplementation getServerImplementation() {
        return this.serverImplementation;
    }



}
