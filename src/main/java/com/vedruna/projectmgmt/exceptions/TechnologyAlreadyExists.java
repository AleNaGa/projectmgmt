package com.vedruna.projectmgmt.exceptions;


public class TechnologyAlreadyExists extends RuntimeException {
    public TechnologyAlreadyExists(Integer id) {
        super("Ya existe la tecnologia con id: " + id);
    }
    
    public TechnologyAlreadyExists(String message) {
        super(message);
    }
    
    public TechnologyAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
    
}
