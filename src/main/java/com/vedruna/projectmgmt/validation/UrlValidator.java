package com.vedruna.projectmgmt.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<ValidUrl, String> {

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
