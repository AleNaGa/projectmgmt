package com.vedruna.projectmgmt.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.vedruna.projectmgmt.dto.CreateTechnologyDTO;
import com.vedruna.projectmgmt.dto.TechnologyDTO;

public interface TechnologyServiceI {

    public List<TechnologyDTO> getAllTechnologies();
    public ResponseEntity<String> saveTechnology(CreateTechnologyDTO tehnology);
    public ResponseEntity<String> deleteTechnology(Integer id);

}
