package com.vedruna.projectmgmt.exceptions;

public class DeveloperAlreadyExists extends RuntimeException {
    public DeveloperAlreadyExists(String name) {
        super("Ya existe un desarrollador con el nombre: " + name);
    }
    public DeveloperAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
    
}
