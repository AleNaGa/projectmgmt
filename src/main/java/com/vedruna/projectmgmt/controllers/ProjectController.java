package com.vedruna.projectmgmt.controllers;
import com.vedruna.projectmgmt.dto.CreateProjectDTO;
import com.vedruna.projectmgmt.dto.ProjectDTO;
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

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectServiceI projectServ;

    @PostMapping("/test")
    public String test(@RequestBody SampleDTO test) {
        log.info("Test: {}", test.getSaludo());
        return test.getSaludo();
    }

    @GetMapping()
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

    @GetMapping("/bytech/{name}")
    public ResponseEntity<List<ProjectDTO>> findByTech(@PathVariable String name, @RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
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


    @PostMapping("/insert")
    public ResponseEntity<String> insert(@Valid @RequestBody CreateProjectDTO project, BindingResult bindingResult) {
            // Verifica si hay errores de validación
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body("Errores de validación: " + errorMessages);
        }
        try {
            log.info("Insertando proyecto: {}", project.getName());
            return projectServ.saveProject(project);
        } catch (Exception e) {
            log.error("Error al insertar proyecto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar el proyecto: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            log.info("Borrando proyecto con id: {}", id);
            return projectServ.deleteProject(id);
        } catch (Exception e) {
            log.error("Error al borrar proyecto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al borrar el proyecto: " + e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @Valid @RequestBody CreateProjectDTO project, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body("Errores de validación: " + errorMessages);
        }
        try {
            log.info("Actualizando proyecto con id: {}", id);
            return projectServ.updateProject(project,id);
        } catch (Exception e) {
            log.error("Error al actualizar proyecto: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el proyecto: " + e.getMessage());
        }
    }


    //EXTRAS


    @PostMapping("/asigntech/{techid}/toproject/{projectid}")
    public ResponseEntity<String> assignTech(@PathVariable Integer techid, @PathVariable Integer projectid) {
        try {
            log.info("Asignando tecnologia con id: {} al proyecto con id: {}", techid, projectid);
            return projectServ.addTechnologyToProject(techid, projectid);
        } catch (Exception e) {
            log.error("Error al asignar tecnologia: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar la tecnologia: " + e.getMessage());
        }
    }

    
    @PostMapping("/asigndev/{devid}/toproject/{projectid}")
    public ResponseEntity<String> assingDev(@PathVariable Integer devid, @PathVariable Integer projectid) {
        try {
            log.info("Asignando developer con id: {} al proyecto con id: {}", devid, projectid);
            return projectServ.addDeveloperToProject(devid, projectid);
        } catch (Exception e) {
            log.error("Error al asignar developer: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar del desarrollador: " + e.getMessage());
        }
    }

    @PatchMapping("/totesting/{id}")
    public ResponseEntity<String> toTest(@PathVariable Integer id) {
        try {
            log.info("Cambiando estado del  proyecto con id: {} a test", id);
            return projectServ.projectToTest(id);
        } catch (Exception e) {
            log.error("Error al asignar proyecto a test: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar el proyecto a test: " + e.getMessage());
        }
    }

    @PatchMapping("/toprod/{id}")
    public ResponseEntity<String> toProduction(@PathVariable Integer id) {
        try {
            log.info("Cambiando estado del  proyecto con id: {} a production", id);
            return projectServ.projectToProduction(id);
        } catch (Exception e) {
            log.error("Error al asignar proyecto a production: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al asignar el proyecto a production: " + e.getMessage());
        }
    }




}
