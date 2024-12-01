package com.vedruna.projectmgmt.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;

@Target({ ElementType.FIELD })//Objetivo: variables (la url es una variable String) no aplicable a métodos o clases, solo a variables
@Retention(RetentionPolicy.RUNTIME) // para que funcione mientras corre. Así puede validar la URL que le metamos
@Constraint(validatedBy = UrlValidator.class)
public @interface ValidUrl {
    String message() default "URL inválida"; //la validación de la URL falla, 
    
}
