package com.vedruna.projectmgmt.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {

    /**
     * Valida si una cadena cumple con la estructura de una URL (https?, ftp)
     * @param value Cadena a validar
     * @param context
     * @return true si la cadena cumple con la estructura de una URL, false en caso contrario
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        String urlRegex = "^(https?|ftp)://[^\s/$.?#].[^\s]*$"; //Reglas de la URL para la validación
        if(value !=null){// si es null se encarga el validador @NotNull, por lo que no queremos que intente vgalidar con la URL porque petaría
            return value.matches(urlRegex);
        }else{
            return true;
        }
    }
    
}
