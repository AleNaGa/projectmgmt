package com.vedruna.projectmgmt.services;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vedruna.projectmgmt.dto.CreateDeveloperDTO;
import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.repository.DeveloperRepositoryI;

@Service
public class DeveloperServiceImpl implements DeveloperServiceI {

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(DeveloperServiceImpl.class);
    @Autowired
    DeveloperRepositoryI developerRepo;
    @Override
    public void saveDeveloper(CreateDeveloperDTO developer) {
       Developer dev = new Developer();
       try{
            dev.setDevName(developer.getName());
            dev.setDevSurname(developer.getSurname());
            dev.setEmail(developer.getEmail());
            dev.setLinkedinUrl(developer.getLinkedinUrl());
            dev.setGithubUrl(developer.getGithubUrl());
            dev.setProjects(Collections.emptyList());
            developerRepo.save(dev);
       }catch(Exception e){
           log.error(e.getMessage());
       }
       
    }

    @Override
    public void deleteDeveloper(Integer id) {
       try{
            developerRepo.deleteById(id);
       }catch(Exception e){
           log.error(e.getMessage());
       }
    }
    
}
