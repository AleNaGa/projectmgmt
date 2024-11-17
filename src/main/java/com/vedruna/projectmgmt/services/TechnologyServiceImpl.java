package com.vedruna.projectmgmt.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vedruna.projectmgmt.dto.CreateTechnologyDTO;
import com.vedruna.projectmgmt.exceptions.ProjectNotFoundException;
import com.vedruna.projectmgmt.exceptions.TechnologyAlreadyExists;
import com.vedruna.projectmgmt.exceptions.TechnologyNotFound;
import com.vedruna.projectmgmt.persistance.model.Project;
import com.vedruna.projectmgmt.persistance.model.Technology;
import com.vedruna.projectmgmt.persistance.repository.ProjectRepositoryI;
import com.vedruna.projectmgmt.persistance.repository.TechnologyRepositoryI;




@Service
public class TechnologyServiceImpl implements TechnologyServiceI {

    @Autowired
    private TechnologyRepositoryI technologyRepo;
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(TechnologyServiceImpl.class);
    @Autowired
    private ProjectRepositoryI projectRepo;
    

    /**
     * Guardar tecnología.
     *
     * Convierte el objeto CreateTechnologyDTO en un objeto Technology y lo
     * guarda en la base de datos.
     *
     * @param technology El objeto CreateTechnologyDTO que se va a guardar.
     */
    @Override
    public ResponseEntity<String> saveTechnology(CreateTechnologyDTO technology) {
        if (technologyRepo.existsByTechName(technology.getTechName())) {
            log.error("La tecnología con el nombre {} ya existe", technology.getTechName());
            throw new TechnologyAlreadyExists(technology.getTechName());
        }
        Technology tech = new Technology();
        tech.setTechName(technology.getTechName());
        if(technology.getProjectsIds() == null) {
            tech.setProjects(Collections.emptyList());
        }else{
            List<Project> projects =techProjects(technology.getProjectsIds());
            tech.setProjects(projects);
        }
       try{
            log.info("Se ha guardado la tecnología con id: {}", tech.getTechId());
            technologyRepo.save(tech);
            for (Project project : tech.getProjects()) {
                project.getTechnologies().add(tech); // Asignar el developer al proyecto por la relación Many to Many
                projectRepo.save(project);
            }
            return ResponseEntity.ok("Se ha guardado la tecnología con id: " + tech.getTechId());
        }catch(Exception e){
            log.error("No se ha guardado la tecnología con id: {}", tech.getTechId());
            throw new TechnologyNotFound(tech.getTechId());
        }
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
         public ResponseEntity<String> deleteTechnology(Integer id){
        if(!technologyRepo.existsById(id)){
            log.error("La tecnología no existe");
            throw new TechnologyNotFound(id);
        }
        technologyRepo.deleteById(id);
        log.info("Se ha borrado la tecnología con id: {}", id);
        return ResponseEntity.ok("Se ha borrado la tecnología con id: " + id);
    }


    private List<Project> techProjects(List<Integer> projectsIds) {
        List<Project> tech = new ArrayList<>();
        for (Integer id : projectsIds) {
            if(!projectRepo.existsById(id)){
                throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + id);
            }
            Project pj = projectRepo.findById(id).get();
            log.info("añadiendo el proyecto: {}, {}, {}", pj.getProjectName(), pj.getProjectId(),pj.getStatus());
            tech.add(pj);
        }
        return tech;
    }
    
}
