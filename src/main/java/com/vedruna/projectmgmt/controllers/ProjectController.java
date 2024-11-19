package com.vedruna.projectmgmt.controllers;
import com.vedruna.projectmgmt.dto.CreateProjectDTO;
import com.vedruna.projectmgmt.dto.ProjectDTO;
import com.vedruna.projectmgmt.dto.ResponseDTO;
import com.vedruna.projectmgmt.dto.SampleDTO;

import java.util.Collections;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vedruna.projectmgmt.services.ProjectServiceI;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ResponseDTO<String> responseDTO = new ResponseDTO<>();//crea el DTOResponse para limpiar el código

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectServiceI projectServ;

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

    //METODOS DE FIND CON GET-------------------------------------------
    @Operation(
    summary = "Obtener todos los proyectos",
    description = "Este endpoint obtiene todos los proyectos con paginación. Si no hay proyectos, devuelve un código 204 No Content.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Proyectos obtenidos exitosamente."),
        @ApiResponse(responseCode = "204", description = "No se encontraron proyectos."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    }
    )
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> findAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        try {
            List<ProjectDTO> projects = projectServ.getAll(page, size);
            if (projects.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content si no hay proyectos
            }
            return ResponseEntity.ok(projects); // 200 OK con los proyectos
        } catch (Exception e) {
            log.error("Error al obtener proyectos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList()); // 500 con lista vacía
        }
    }


    @Operation(
    summary = "Buscar proyectos por palabra clave",
    description = "Este endpoint permite buscar proyectos que contengan una palabra clave específica en su nombre o descripción. También admite paginación.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Proyectos encontrados exitosamente."),
        @ApiResponse(responseCode = "204", description = "No se encontraron proyectos que coincidan con la palabra clave."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    }
    )
    @GetMapping("/byword/{word}")
    public ResponseEntity<List<ProjectDTO>> findByWord(@PathVariable String word, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        try {
            List<ProjectDTO> projects = projectServ.getProjectByWord(word, page, size);
            if (projects.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content si no hay proyectos
            }
            return ResponseEntity.ok(projects); // 200 OK con los proyectos
        } catch (Exception e) {
            log.error("Error al obtener proyectos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList()); // 500 con lista vacía
        }
    }


    @Operation(
        summary = "Buscar proyectos por tecnología",
        description = "Este endpoint permite buscar proyectos que usan una tecnología específica, soportando paginación.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Proyectos encontrados exitosamente."),
            @ApiResponse(responseCode = "204", description = "No se encontraron proyectos asociados a la tecnología."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        }
    )
    @GetMapping("/bytech/{name}")
    public ResponseEntity<List<ProjectDTO>> findByTech(@PathVariable String name,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        try {
            List<ProjectDTO> projects = projectServ.getByTechno(name, page, size);
            if (projects.isEmpty()) {
                return ResponseEntity.noContent().build(); // 204 No Content si no hay proyectos
            }
            return ResponseEntity.ok(projects); // 200 OK con los proyectos
        } catch (Exception e) {
            log.error("Error al obtener proyectos: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList()); // 500 con lista vacía
        }
    }
    

    //METODOS DE MODIFICACIÓN
    @Operation(
        summary = "Crear un nuevo proyecto",
        description = "Este endpoint permite crear un nuevo proyecto. La solicitud debe contener un objeto con los detalles del proyecto a insertar.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Proyecto creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Errores de validación en los datos proporcionados."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor al crear el proyecto.")
        }
    )
    @PostMapping("/insert")
    public ResponseEntity<ResponseDTO<String>> insert(@Valid @RequestBody CreateProjectDTO project, BindingResult bindingResult) {
        // Verifica si hay errores de validación
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            
            responseDTO.setMessage("Errores de validación: " + errorMessages);
            responseDTO.setData(null);
            
            return ResponseEntity.badRequest().body(responseDTO); // 400 Bad Request
        }

        try {
            log.info("Insertando proyecto: {}", project.getName());
            
            // Llamar al servicio para guardar el proyecto
            String result = projectServ.saveProject(project).getBody();
            
           //inserción exitosa.
            responseDTO.setMessage("Proyecto creado exitosamente.");
            responseDTO.setData(result);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO); // 201 Created
        } catch (Exception e) {
            log.error("Error al insertar proyecto: ", e);
            // Manejo del error interno del servidor
            responseDTO.setMessage("Error al insertar el proyecto: " + e.getMessage());
            responseDTO.setData(null);
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO); // 500 Internal Server Error
        }
    }



    @Operation(
    summary = "Eliminar un proyecto",
    description = "Este endpoint permite eliminar un proyecto mediante su ID. El ID debe ser proporcionado en la ruta.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Proyecto eliminado correctamente."),
        @ApiResponse(responseCode = "400", description = "ID del proyecto no válido o no encontrado."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar eliminar el proyecto.")
    }
    )
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO<String>> delete(@PathVariable Integer id) {
        try {
            log.info("Borrando proyecto con id: {}", id);
            // Llamada al servicio que elimina el proyecto
            projectServ.deleteProject(id);

            // devolver el ResponseDTO en caso de éxito
            responseDTO.setMessage("Proyecto eliminado correctamente.");
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error al borrar proyecto: ", e);
            // Manejo del error interno del servidor
            responseDTO.setMessage("Error al borrar el proyecto: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    

    @Operation(
    summary = "Actualizar un proyecto",
    description = "Este endpoint permite actualizar los datos de un proyecto existente utilizando su ID. El ID del proyecto debe ser proporcionado en la ruta y los nuevos datos en el cuerpo de la solicitud.",
    responses = {
        @ApiResponse(responseCode = "200", description = "Proyecto actualizado correctamente."),
        @ApiResponse(responseCode = "400", description = "Errores de validación en los datos proporcionados."),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado con el ID especificado."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    }
    )
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDTO<String>> update(@PathVariable Integer id, @Valid @RequestBody CreateProjectDTO project, BindingResult bindingResult) {
        // Verifica si hay errores de validación
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
            
            // respuesta
            responseDTO.setMessage("Errores de validación: " + errorMessages);
            responseDTO.setData(null);
            return ResponseEntity.badRequest().body(responseDTO);
        }
        try {
            log.info("Actualizando proyecto con id: {}", id);

            // Llamada al servicio para actualizar el proyecto
            projectServ.updateProject(project, id);

            //  la respuesta exitosa
            responseDTO.setMessage("Proyecto actualizado correctamente.");
            responseDTO.setData(null);
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error al actualizar proyecto: ", e);

            // Manejo del error
            responseDTO.setMessage("Error al actualizar el proyecto: " + e.getMessage());
            responseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }




    //EXTRAS

    //ASSIGN TECH TO PROJECT
    @Operation(
        summary = "Asignar tecnología a un proyecto",
        description = "Este endpoint permite asignar una tecnología a un proyecto mediante sus respectivos IDs. " +
                      "El proyecto y la tecnología deben existir previamente en la base de datos.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Tecnología asignada correctamente al proyecto."),
            @ApiResponse(responseCode = "400", description = "ID de tecnología o proyecto no válido."),
            @ApiResponse(responseCode = "404", description = "Tecnología o proyecto no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        }
    )
    @PostMapping("/asigntech/{techid}/toproject/{projectid}")
    public ResponseEntity<ResponseDTO<String>> assignTech(@PathVariable Integer techid, @PathVariable Integer projectid) {
        try {
            log.info("Asignando tecnología con id: {} al proyecto con id: {}", techid, projectid);
    
            // Llamada al servicio para asignar la tecnología al proyecto
            projectServ.addTechnologyToProject(techid, projectid);
    
            // Respuesta exitosa
            responseDTO.setMessage("Tecnología asignada correctamente al proyecto.");
            responseDTO.setData(null); // No hay datos adicionales a devolver
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error al asignar tecnología: ", e);
    
            // Manejo de error
            responseDTO.setMessage("Error al asignar la tecnología: " + e.getMessage());
            responseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
    


    
    // ASIGN DEV TO PROJECT
    @Operation(
    summary = "Asignar un developer a un proyecto",
    description = "Este endpoint permite asignar un desarrollador (developer) a un proyecto específico.",
    responses = {
        @ApiResponse(responseCode = "200", description = "El desarrollador ha sido asignado correctamente al proyecto."),
        @ApiResponse(responseCode = "400", description = "Los datos proporcionados no son válidos."),
        @ApiResponse(responseCode = "404", description = "El desarrollador o el proyecto no se encuentran."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    }
    )
    @PostMapping("/asigndev/{devid}/toproject/{projectid}")
    public ResponseEntity<ResponseDTO<String>> assignDev(@PathVariable Integer devid, @PathVariable Integer projectid) {
        try {
            log.info("Asignando developer con id: {} al proyecto con id: {}", devid, projectid);

            // Llamada al servicio para asignar el developer al proyecto
            projectServ.addDeveloperToProject(devid, projectid);

            // Respuesta exitosa
            responseDTO.setMessage("El desarrollador ha sido asignado correctamente al proyecto.");
            responseDTO.setData(null); // No se devuelve información adicional
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error al asignar developer: ", e);

            // Manejo de error
            responseDTO.setMessage("Error al asignar el desarrollador: " + e.getMessage());
            responseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }

    


    @Operation(
        summary = "Cambiar el estado de un proyecto a 'test'",
        description = "Este endpoint permite cambiar el estado de un proyecto a 'test' para iniciar su fase de pruebas.",
        responses = {
            @ApiResponse(responseCode = "200", description = "El estado del proyecto se ha cambiado a 'test' exitosamente."),
            @ApiResponse(responseCode = "400", description = "El ID proporcionado no es válido."),
            @ApiResponse(responseCode = "404", description = "El proyecto no se encuentra."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
        }
    )
    @PatchMapping("/totesting/{id}")
    public ResponseEntity<ResponseDTO<String>> toTest(@PathVariable Integer id) {
        try {
            log.info("Cambiando estado del proyecto con id: {} a test", id);
    
            // Cambiar el estado del proyecto a 'test'
            projectServ.projectToTest(id);
    
            // Respuesta exitosa
            responseDTO.setMessage("El estado del proyecto se ha cambiado a 'test' exitosamente.");
            responseDTO.setData(null); // No se devuelve información adicional
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error al asignar proyecto a test: ", e);
    
            // Manejo de error
            responseDTO.setMessage("Error al asignar el proyecto a test: " + e.getMessage());
            responseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
    


    @Operation(
    summary = "Cambiar el estado de un proyecto a 'production'",
    description = "Este endpoint permite cambiar el estado de un proyecto a 'production' para moverlo a la fase de producción.",
    responses = {
        @ApiResponse(responseCode = "200", description = "El estado del proyecto se ha cambiado a 'production' exitosamente."),
        @ApiResponse(responseCode = "400", description = "El ID proporcionado no es válido."),
        @ApiResponse(responseCode = "404", description = "El proyecto no se encuentra."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    }
    )
    @PatchMapping("/toprod/{id}")
    public ResponseEntity<ResponseDTO<String>> toProduction(@PathVariable Integer id) {
        try {
            log.info("Cambiando estado del proyecto con id: {} a production", id);

            // Cambiar el estado del proyecto a 'production'
            projectServ.projectToProduction(id);

            // Respuesta exitosa
            responseDTO.setMessage("El estado del proyecto se ha cambiado a 'production' exitosamente.");
            responseDTO.setData(null); // No se devuelve información adicional
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            log.error("Error al asignar proyecto a production: ", e);

            // Manejo de error
            responseDTO.setMessage("Error al asignar el proyecto a production: " + e.getMessage());
            responseDTO.setData(null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
        }
    }
}
