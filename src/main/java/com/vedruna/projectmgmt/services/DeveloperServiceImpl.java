package com.vedruna.projectmgmt.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.vedruna.projectmgmt.dto.CreateDeveloperDTO;
import com.vedruna.projectmgmt.dto.DeveloperDTO;
import com.vedruna.projectmgmt.dto.PaginatedResponseDTO;
import com.vedruna.projectmgmt.exceptions.DeveloperAlreadyExists;
import com.vedruna.projectmgmt.exceptions.DeveloperNotFound;
import com.vedruna.projectmgmt.exceptions.ProjectNotFoundException;
import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;
import com.vedruna.projectmgmt.persistance.repository.DeveloperRepositoryI;
import com.vedruna.projectmgmt.persistance.repository.ProjectRepositoryI;

@Service
public class DeveloperServiceImpl implements DeveloperServiceI {

    @Autowired
    private static final Logger log = LoggerFactory.getLogger(DeveloperServiceImpl.class);
    @Autowired
    DeveloperRepositoryI developerRepo;
    @Autowired
    ProjectRepositoryI projectRepo;


    /**
     * Guarda un desarrollador en la base de datos
     * Primero, se verifica si ya existe un desarrollador con el mismo email.
     * Si existe, se lanza una excepción de tipo DeveloperAlreadyExists.
     * Si no existe, se crea un nuevo objeto de tipo Developer y se guardan sus datos
     * en la base de datos. Luego, se devuelve un objeto ResponseEntity con el
     * mensaje "Se ha guardado el desarrollador con id: " y el id del desarrollador".
     * @param developer El objeto CreateDeveloperDTO con los datos del
     * desarrollador a guardar
     * @return Un objeto ResponseEntity con el mensaje de confirmación de
     * guardado
     */
    @Override
    public ResponseEntity<String> saveDeveloper(CreateDeveloperDTO developer) {
       if(developerRepo.existsByEmail(developer.getEmail())){
           throw new DeveloperAlreadyExists(developer.getName());
       }
            Developer dev = new Developer();
            dev.setDevName(developer.getName());
            dev.setDevSurname(developer.getSurname());
            dev.setEmail(developer.getEmail());
            dev.setLinkedinUrl(developer.getLinkedinUrl());
            dev.setGithubUrl(developer.getGithubUrl());
            if (developer.getProjectsIds() == null) {
                dev.setProjects(Collections.emptyList());
            }else{
                List<Project> projects = devProjects(developer.getProjectsIds());
                dev.setProjects(projects);
            }
            try {
                developerRepo.save(dev);
                log.info("Se ha guardado el desarrollador " + dev.getDevName());
                for (Project project : dev.getProjects()) {
                    project.getDevelopers().add(dev); // Asignar el developer al proyecto por la relación Many to Many
                    projectRepo.save(project);
                }
                return ResponseEntity.ok("Se ha guardado el desarrollador " + dev.getDevName());
            } catch (Exception e) {
                log.error("Error al guardar el desarrollador " + dev.getDevName(), e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el desarrollador " + dev.getDevName());
            }
           
    }


    /**
     * Elimina un desarrollador de la base de datos.
     * 
     * Verifica que exista un desarrollador con el ID pasado como parámetro y
     * lo elimina de la base de datos. Si no existe, lanza una excepción.
     * 
     * @param id El ID del desarrollador a eliminar.
     * @return Un mensaje de confirmación de que se ha eliminado el
     *         desarrollador.
     */
    @Override
    public ResponseEntity<String> deleteDeveloper(Integer id) {
        if(!developerRepo.existsById(id)){
            throw new DeveloperNotFound(id);
        }
        developerRepo.deleteById(id);
        return ResponseEntity.ok("Se ha borrado el desarrollador con id: " + id);
    }

    /**
     * Convierte un array de id de proyectos en una lista de proyectos.
     * Busca cada id en la base de datos y si existe, lo agrega a la lista.
     * Si no existe, lanza una excepción ProjectNotFoundException.
     *
     * @param projectsIds El array con los ids de los proyectos.
     * @return La lista de proyectos correspondientes
     */
    private List<Project> devProjects(int[] projectsIds) {
       List<Project> dev = new ArrayList<>();
       for (Integer id : projectsIds) {
           if(!projectRepo.existsById(id)){
               throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + id);
           }
           Project pj = projectRepo.findById(id).get();
           log.info("añadiendo el proyecto: {}, {}, {}", pj.getProjectName(), pj.getProjectId(),pj.getStatus());
           dev.add(pj);
        }
        return dev;
    }


    @Override
    public PaginatedResponseDTO<DeveloperDTO> getAll(int page, int size) {
        if(page < 0 || size < 0){
            throw new IllegalArgumentException("Los parámetros page y size deben ser mayores o iguales a 0.");
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Developer> developerPage = developerRepo.findAll(pageable);
        log.info("Se han obtenido {} desarrolladores", developerPage.getTotalElements());
        PaginatedResponseDTO<DeveloperDTO> response = new PaginatedResponseDTO<>();
        response.setContent(developerPage.getContent().stream().map(DeveloperDTO::new).collect(Collectors.toList()));
        response.setCurrentPage(page);
        response.setTotalPages(developerPage.getTotalPages());
        response.setTotalItems((int) developerPage.getTotalElements());
        response.setPageSize(size);
        return response;
    }
    
}
