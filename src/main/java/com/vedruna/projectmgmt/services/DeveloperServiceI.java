package com.vedruna.projectmgmt.services;

import org.springframework.http.ResponseEntity;

import com.vedruna.projectmgmt.dto.CreateDeveloperDTO;

public interface DeveloperServiceI {

    public ResponseEntity<String> saveDeveloper(CreateDeveloperDTO developer);
    public ResponseEntity<String> deleteDeveloper(Integer id);

    
    
}
