package com.vedruna.projectmgmt.exceptions;

public class DeveloperNotFound extends RuntimeException{
    public DeveloperNotFound(Integer id ) {
        super("No se ha encontrado el desarrollador con id: " + id);
    } 
    public DeveloperNotFound(String message, Throwable cause) {
        super(message, cause);  
    }
}
