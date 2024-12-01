package com.vedruna.projectmgmt.services;

import org.springframework.http.ResponseEntity;

import com.vedruna.projectmgmt.dto.CreateDeveloperDTO;
import com.vedruna.projectmgmt.dto.DeveloperDTO;
import com.vedruna.projectmgmt.dto.PaginatedResponseDTO;

public interface DeveloperServiceI {

    public ResponseEntity<String> saveDeveloper(CreateDeveloperDTO developer);
    public ResponseEntity<String> deleteDeveloper(Integer id);
    public PaginatedResponseDTO<DeveloperDTO> getAll(int page, int size);

    
    
}
