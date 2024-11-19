package com.vedruna.projectmgmt.controllers;

import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vedruna.projectmgmt.dto.CreateDeveloperDTO;
import com.vedruna.projectmgmt.dto.ResponseDTO;
import com.vedruna.projectmgmt.dto.SampleDTO;

import com.vedruna.projectmgmt.services.DeveloperServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperController {

    private final ResponseDTO<String> responseDTO = new ResponseDTO<>();

    @Autowired
    private DeveloperServiceI developerServ;

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(DeveloperController.class);


    // TEST
     @Operation(
    summary = "Test para recibir y devolver un saludo",
    description = "Este endpoint es un test para verificar que los datos enviados en el request body se manejan correctamente y se devuelven como respuesta."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "El saludo ha sido procesado y devuelto correctamente."),
        @ApiResponse(responseCode = "400", description = "El formato del saludo no es válido.")
    })
    @PostMapping("/test")
    public String test(@RequestBody SampleDTO test) {
        log.info("Test: {}", test.getSaludo());
        return test.getSaludo();
    }



    //INSERTAR

    @Operation(
    summary = "Inserta un nuevo desarrollador",
    description = "Este endpoint permite insertar un nuevo desarrollador en la base de datos. Valida los datos antes de guardarlos."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Desarrollador insertado correctamente."),
        @ApiResponse(responseCode = "400", description = "Errores de validación en los datos proporcionados."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    @PostMapping("/insert")
    public ResponseEntity<ResponseDTO<String>> insert(@Valid @RequestBody CreateDeveloperDTO developer, BindingResult bindingResult) {
        // Recoger los errores de validación que haya
        if (bindingResult.hasErrors()) {
            // Listado de errores
            String errorMessages = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            
            // ResponseDTO con un mensaje de error
            responseDTO.setMessage("Errores de validación: " + errorMessages);
            return ResponseEntity.badRequest().body(responseDTO);
        }

        try {
            log.info("Insertando desarrollador: {}", developer.getName());

            // guardado a través del servicio
            ResponseEntity<String> response = developerServ.saveDeveloper(developer);

            //  ResponseDTO con mensaje de éxito y el cuerpo de la respuesta
            ResponseDTO<String> responseDTO = new ResponseDTO<>();
            responseDTO.setMessage("Desarrollador insertado correctamente.");
            responseDTO.setData(response.getBody());

            // 201
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception e) {
            log.error("Error al insertar desarrollador: ", e);
            
            // ResponseDTO con mensaje de error
            ResponseDTO<String> responseDTO = new ResponseDTO<>();
            responseDTO.setMessage("Error al insertar el desarrollador: " + e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }




    //DELETE
    @Operation(summary = "Eliminar un desarrollador por ID", description = "Elimina un desarrollador existente de la base de datos utilizando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Desarrollador eliminado correctamente."),
        @ApiResponse(responseCode = "404", description = "No se encontró un desarrollador con el ID especificado."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar eliminar el desarrollador.")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Integer id) {
        try {
            log.info("Borrando developer con id: {}", id);

            // servicio para eliminar el desarrollador
            ResponseEntity<String> response = developerServ.deleteDeveloper(id);

            //  ResponseDTO de éxito;
            responseDTO.setMessage("Desarrollador eliminado correctamente.");
            responseDTO.setData(response.getBody());

            return ResponseEntity.ok(responseDTO);  // codigo 200!
        } catch (Exception e) {
            log.error("Error al borrar Developer: ", e);

            //  ResponseDTO de error
            responseDTO.setMessage("Error al borrar el developer: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);  // 500 Error
        }
    }
}
