package com.vedruna.projectmgmt.services;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.vedruna.projectmgmt.dto.CreateTechnologyDTO;
import com.vedruna.projectmgmt.persistance.model.Technology;
import com.vedruna.projectmgmt.persistance.repository.TechnologyRepositoryI;

public class TechnologyServiceImpl implements TechnologyServiceI {

    @Autowired
    private TechnologyRepositoryI technologyRepo;
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(TechnologyServiceImpl.class);
    @Override
    public void saveTechnology(CreateTechnologyDTO tehnology) {
       Technology tech = new Technology();
       try{
        tech.setTechName(tehnology.getTechName());
        tech.setProjects(Collections.emptyList());
        technologyRepo.save(tech);
       }catch(Exception e){
           log.error(e.getMessage());
       }
    }

    @Override
    public void deleteTechnology(Integer id) {
        try{
        technologyRepo.deleteById(id);
        }catch(Exception e){
            log.error(e.getMessage());
        }
    }
    
}
