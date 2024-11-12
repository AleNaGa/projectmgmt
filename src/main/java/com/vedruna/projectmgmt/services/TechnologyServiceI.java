package com.vedruna.projectmgmt.services;

import com.vedruna.projectmgmt.dto.CreateTechnologyDTO;

public interface TechnologyServiceI {
    
    public void saveTechnology(CreateTechnologyDTO tehnology);
    public void deleteTechnology(Integer id);

}
