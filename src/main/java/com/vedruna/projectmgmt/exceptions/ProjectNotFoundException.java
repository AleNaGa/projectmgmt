package com.vedruna.projectmgmt.exceptions;

public class ProjectNotFoundException extends Exception {
    
    public ProjectNotFoundException(String message) {
        super(message);
    }
    
    public ProjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
