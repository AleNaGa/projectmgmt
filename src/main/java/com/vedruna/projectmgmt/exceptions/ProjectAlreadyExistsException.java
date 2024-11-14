package com.vedruna.projectmgmt.exceptions;

public class ProjectAlreadyExistsException extends RuntimeException {

    public ProjectAlreadyExistsException(String message) {
        super(message);
    }
    public ProjectAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
