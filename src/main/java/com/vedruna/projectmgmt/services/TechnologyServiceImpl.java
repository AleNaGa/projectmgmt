package com.vedruna.projectmgmt.services;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vedruna.projectmgmt.dto.CreateTechnologyDTO;
import com.vedruna.projectmgmt.exceptions.TechnologyAlreadyExists;
import com.vedruna.projectmgmt.exceptions.TechnologyNotFound;
import com.vedruna.projectmgmt.persistance.model.Technology;
import com.vedruna.projectmgmt.persistance.repository.TechnologyRepositoryI;



@Service
public class TechnologyServiceImpl implements TechnologyServiceI {

    @Autowired
    private TechnologyRepositoryI technologyRepo;
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(TechnologyServiceImpl.class);
    

    /**
     * Guardar tecnología.
     *
     * Convierte el objeto CreateTechnologyDTO en un objeto Technology y lo
     * guarda en la base de datos.
     *
     * @param technology El objeto CreateTechnologyDTO que se va a guardar.
     */
    @Override
    public void saveTechnology(CreateTechnologyDTO technology) {
        if(technologyRepo.existsByTechName(technology.getTechName())){
            throw new TechnologyAlreadyExists(technology.getTechId());
        }
        Technology tech = new Technology();
        tech.setTechName(technology.getTechName());
        tech.setProjects(Collections.emptyList());
        technologyRepo.save(tech);
    }

    /**
     * Eliminar tecnología.
     *
     * Elimina una tecnología por su id. Comprueba si la tecnología existe
     * Si no es así, lanza una excepción personalizada
     *
     * @param id El id de la tecnología a eliminar.
    */
         @Override
         public void deleteTechnology(Integer id){
        if(!technologyRepo.existsById(id)){
            throw new TechnologyNotFound(id);
        }
        technologyRepo.deleteById(id);
    }
    
}
