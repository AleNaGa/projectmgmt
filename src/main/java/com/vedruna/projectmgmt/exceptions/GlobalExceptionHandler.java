package com.vedruna.projectmgmt.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * Clase que maneja las excepciones personalizadas
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo IllegalArgumentException
     * @param exception
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.badRequest().body("Error: " + exception.getMessage());
    }
    /**
     * Maneja las excepciones de tipo Exception
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor: " + exception.getMessage());
    }


    // LAS EXCEPCIONES PERSONALIZADAS NECESARIAS
    /**
     * Maneja las excepciones de tipo IllegalDateFormatException
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalDateFormatException.class)
    public ResponseEntity<String> handleIllegalDateFormatException(IllegalDateFormatException ex) {
        //Error 400 due to bad request Se aclara la forma de resolverlo.
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La fecha debe tener el formato AAAA-MM-DD: " + ex.getMessage()); 
    }
    /**
     * Maneja las excepciones de tipo ProjectAlreadyExistsException
     * @param ex
     * @return
     */
    @ExceptionHandler(ProjectAlreadyExistsException.class)
    public ResponseEntity<String> handleProjectAlreadyExistsException(ProjectAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    /**
     * Maneja las excepciones de tipo ProjectNotFoundException
     * @param ex
     * @return
     */
    @ExceptionHandler(ProjectNotFoundException.class)
    public ResponseEntity<String> handleProjectNotFoundException(ProjectNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }



    /**
     * Maneja las excepciones de tipo TechnologyAlreadyExists
     * Para cuando una tecnologia ya exista en la base de datos y se esté intentando guardar
     * @param ex La excepción a manejar
     * @return Un objeto ResponseEntity con el código de estado 400 y el mensaje de la excepción
     */
    @ExceptionHandler(TechnologyAlreadyExists.class)
    public ResponseEntity<String> handleTechnologyAlreadyExistsException(TechnologyAlreadyExists ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Maneja las excepciones de tipo TechnologyNotFound
     * Para cuando una tecnologia no exista en la base de datos y se esté intentando
     * recuperar o utilizar.
     * @param ex La excepción a manejar
     * @return Un objeto ResponseEntity con el código de estado 400 y el mensaje de la excepción
     */
    @ExceptionHandler(TechnologyNotFound.class)
    public ResponseEntity<String> handleTechnologyNotFoundException(TechnologyNotFound ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    /**
     * Maneja las excepciones de tipo DeveloperAlreadyExists
     * Para cuando un desarrollador ya existe en la base de datos y se está intentando guardar
     * @param ex La excepción a manejar
     * @return Un objeto ResponseEntity con el código de estado 409 y el mensaje de la excepción
     */
    @ExceptionHandler(DeveloperAlreadyExists.class)
    public ResponseEntity<String> handleDeveloperAlreadyExistsException(DeveloperAlreadyExists ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    /**
     * Maneja las excepciones de tipo DeveloperNotFound
     * Para cuando un desarrollador no existe en la base de datos y se est  intentando
     * recuperar o utilizar.
     * @param ex La excepción a manejar
     * @return Un objeto ResponseEntity con el código de estado 409 y el mensaje de la excepción
     */
    @ExceptionHandler(DeveloperNotFound.class)
    public ResponseEntity<String> handleDeveloperNotFoundException(DeveloperNotFound ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }



}
