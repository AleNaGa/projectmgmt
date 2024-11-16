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

import com.vedruna.projectmgmt.dto.CreateTechnologyDTO;
import com.vedruna.projectmgmt.services.TechnologyServiceI;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;





@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/technologies")
public class TechnologyController {
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(TechnologyController.class);
    @Autowired
    private TechnologyServiceI technologyServ;


    @GetMapping("/test")
    public String test(@RequestBody String param) {
        return param;
    }

    @PostMapping("/insert")
    public ResponseEntity<String> postMethodName(@Valid @RequestBody CreateTechnologyDTO technology, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            String errorMessages = bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body("Errores de validación: " + errorMessages);
        }
        try {
            log.info("Insertando tecnologia: {}", technology.getTechName());
            return technologyServ.saveTechnology(technology);
        } catch (Exception e) {
            log.error("Error al insertar tecnologia: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al insertar la tecnologia: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            log.info("Borrando tecnología con id: {}", id);
            return technologyServ.deleteTechnology(id);
        } catch (Exception e) {
            log.error("Error al borrar tecnología: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al borrar tecnología: " + e.getMessage());
        }
    }
    
    
}
