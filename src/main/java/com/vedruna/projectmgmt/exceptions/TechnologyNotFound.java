package com.vedruna.projectmgmt.exceptions;

public class TechnologyNotFound extends RuntimeException{
    public TechnologyNotFound(Integer id) {
        super("No se ha encontrado la tecnologia: " + id);
    }

    public TechnologyNotFound(String message, Throwable cause) {
        super(message, cause);
    }
    
}
