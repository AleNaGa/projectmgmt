package com.vedruna.projectmgmt.services;

import org.springframework.http.ResponseEntity;

import com.vedruna.projectmgmt.dto.CreateTechnologyDTO;

public interface TechnologyServiceI {
    
    public ResponseEntity<String> saveTechnology(CreateTechnologyDTO tehnology);
    public ResponseEntity<String> deleteTechnology(Integer id);

}
