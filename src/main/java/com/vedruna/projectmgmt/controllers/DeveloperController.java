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
import com.vedruna.projectmgmt.services.DeveloperServiceI;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperController {

    @Autowired
    private DeveloperServiceI developerServ;

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(DeveloperController.class);

    @GetMapping("/test")
    public String test(@RequestBody String param) {
        return param;
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insert(@Valid @RequestBody CreateDeveloperDTO developer, BindingResult bindingResult) {
         //Recoger los errores de validación que hubiera
        if (bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body("Errores de validación: " + errorMessages);
        }
        try{
            log.info("Insertando desarrollador: {}", developer.getName());
            log.info(developer.toString());
            return developerServ.saveDeveloper(developer);
        } catch (Exception e) {
            log.error("Error al insertar desarrollador: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar el desarrollador: " + e.getMessage());
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            log.info("Borrando developer con id: {}", id);
            return developerServ.deleteDeveloper(id);
        } catch (Exception e) {
            log.error("Error al borrar Developer: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al borrar el developer: " + e.getMessage());
        }
    }
    
    
    
}
