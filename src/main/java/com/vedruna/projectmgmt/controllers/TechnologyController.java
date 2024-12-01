package com.vedruna.projectmgmt.controllers;

import java.util.List;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vedruna.projectmgmt.dto.CreateTechnologyDTO;
import com.vedruna.projectmgmt.dto.ResponseDTO;
import com.vedruna.projectmgmt.dto.SampleDTO;
import com.vedruna.projectmgmt.dto.TechnologyDTO;
import com.vedruna.projectmgmt.services.TechnologyServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/technologies")
public class TechnologyController {

    private final ResponseDTO<String> responseDTO = new ResponseDTO<>();


    @Autowired
    private static final Logger log = LoggerFactory.getLogger(TechnologyController.class);
    @Autowired
    private TechnologyServiceI technologyServ;


    //TEST para el funcionamiento del controlador
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

    @Operation(
    summary = "Obtener todas las tecnologías",
    description = "Este endpoint obtiene una lista de todas las tecnologías disponibles."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tecnologías obtenidas exitosamente.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TechnologyDTO.class)))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al obtener las tecnologías.")
    })
    @GetMapping
    public ResponseEntity<List<TechnologyDTO>> getAll() {
        try{
            log.info("Obteniendo todas las tecnologias");
            List<TechnologyDTO> technologies = technologyServ.getAllTechnologies();
            return ResponseEntity.ok(technologies);
        }
        catch(Exception e){
            log.error("Error al obtener las tecnologias: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    //Insertar nueva tecnologia
   @Operation(
    summary = "Insertar nueva tecnología",
    description = "Permite insertar una nueva tecnología en el sistema validando los datos proporcionados."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tecnología insertada correctamente."),
        @ApiResponse(responseCode = "400", description = "Errores de validación en los datos proporcionados."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar insertar la tecnología.")
    })
    @PostMapping("/insert")
    public ResponseEntity<ResponseDTO<String>> insert(@Valid @RequestBody CreateTechnologyDTO technology, BindingResult bindingResult) {
        // validación del DTO
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            
            //ResponseDTO con el mensaje de error
            responseDTO.setMessage("Errores de validación: " + errorMessages);
            
            return ResponseEntity.badRequest().body(responseDTO); // 400 Bad Request
        }
        
        //  guardar la tecnología
        try {
            log.info("Insertando tecnología: {}", technology.getTechName());

            // Guardar la tecnología (suponiendo que el servicio devuelve un String o mensaje)
            ResponseEntity<String> response = technologyServ.saveTechnology(technology);

            //ResponseDTO con el mensaje de éxito
            responseDTO.setMessage("Tecnología insertada correctamente.");
            responseDTO.setData(response.getBody());

            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO); // 201 Created
        } catch (Exception e) {
            log.error("Error al insertar tecnología: ", e);

            // ResponseDTO con el mensaje de error
            ResponseDTO<String> responseDTO = new ResponseDTO<>();
            responseDTO.setMessage("Error al insertar la tecnología: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO); // 500 Internal Server Error
        }
    }




    //Borrar tecnologia
    @Operation(
    summary = "Eliminar una tecnología",
    description = "Permite eliminar una tecnología existente del sistema utilizando su ID."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tecnología eliminada correctamente."),
        @ApiResponse(responseCode = "404", description = "No se encontró la tecnología con el ID especificado."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar borrar la tecnología.")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Integer id) {
        try {
            log.info("Borrando tecnología con id: {}", id);

            // Llamada al servicio para eliminar la tecnología
            ResponseEntity<String> response = technologyServ.deleteTechnology(id);

            //ResponseDTO con el mensaje de éxito
           
            responseDTO.setMessage("Tecnología eliminada correctamente.");
            responseDTO.setData(response.getBody());

            return ResponseEntity.ok(responseDTO); // 200 OK
        } catch (Exception e) {
            log.error("Error al borrar tecnología: ", e);

            //ResponseDTO con el mensaje de error
            responseDTO.setMessage("Error al borrar la tecnología: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO); // 500 Internal Server Error
        }
    }


    
    
}
