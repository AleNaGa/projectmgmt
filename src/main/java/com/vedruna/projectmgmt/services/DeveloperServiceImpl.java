package com.vedruna.projectmgmt.services;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vedruna.projectmgmt.dto.CreateDeveloperDTO;
import com.vedruna.projectmgmt.exceptions.DeveloperAlreadyExists;
import com.vedruna.projectmgmt.exceptions.DeveloperNotFound;
import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.repository.DeveloperRepositoryI;

@Service
public class DeveloperServiceImpl implements DeveloperServiceI {

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(DeveloperServiceImpl.class);
    @Autowired
    DeveloperRepositoryI developerRepo;

/**
 * Guarda un desarrollador en la base de datos.
 *
 * Este método crea un nuevo desarrollador utilizando los datos proporcionados
 * en el objeto CreateDeveloperDTO y lo guarda en la base de datos. Si ocurre
 * un error durante el proceso de guardado, el error se registra en el log.
 *
 * @param developer El objeto CreateDeveloperDTO que contiene la información
 *                  del desarrollador a guardar.
 */
    @Override
    public void saveDeveloper(CreateDeveloperDTO developer) {
       Developer dev = new Developer();
       if(developerRepo.existsByEmail(developer.getEmail())){
           throw new DeveloperAlreadyExists(developer.getName());
       }
            dev.setDevName(developer.getName());
            dev.setDevSurname(developer.getSurname());
            dev.setEmail(developer.getEmail());
            dev.setLinkedinUrl(developer.getLinkedinUrl());
            dev.setGithubUrl(developer.getGithubUrl());
            dev.setProjects(Collections.emptyList());
            developerRepo.save(dev);
       
    }

/**
 * Elimina un desarrollador de la base de datos.
 *
 * Este método elimina un desarrollador de la base de datos cuyo id es el
 * proporcionado como parámetro. Si ocurre un error al eliminar el
 * desarrollador, imprime el error en el log.
 *
 * @param id El id del desarrollador a eliminar.
 */
    @Override
    public void deleteDeveloper(Integer id) {
        if(!developerRepo.existsById(id)){
            throw new DeveloperNotFound(id);
        }
        developerRepo.deleteById(id);
    }
    
}
