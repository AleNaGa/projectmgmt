package com.vedruna.projectmgmt.services;

import com.vedruna.projectmgmt.dto.CreateDeveloperDTO;

public interface DeveloperServiceI {

    public void saveDeveloper(CreateDeveloperDTO developer);
    public void deleteDeveloper(Integer id);

    
    
}
