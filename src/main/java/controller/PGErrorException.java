package controller;

public class PGErrorException extends Exception{
    private String message;
    public PGErrorException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
