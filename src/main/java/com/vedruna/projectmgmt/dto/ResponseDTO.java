package com.vedruna.projectmgmt.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseDTO<T> {
    private String message;  // Atributo para el mensaje de respuesta
    private T data;         // Atributo para los datos, con tipo genérico

    public ResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }
     // Método toString() para una mejor representación
     @Override
     public String toString() {
         return "ResponseDTO{" +
                 "message='" + message + '\'' +
                 ", data=" + data +
                 '}';
     }    
    
}
