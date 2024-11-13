package com.vedruna.projectmgmt.exceptions;

public class IllegalDateFormatException extends Exception {
    public IllegalDateFormatException() {
        super("La fecha debe tener el formato AAAA-MM-DD");    
    }
    public IllegalDateFormatException(String message) {
        super(message);
    }
    public IllegalDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
