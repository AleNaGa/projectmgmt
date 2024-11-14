package com.vedruna.projectmgmt.services;

import java.util.Collections;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.vedruna.projectmgmt.dto.CreateProjectDTO;
import com.vedruna.projectmgmt.dto.ProjectDTO;
import com.vedruna.projectmgmt.exceptions.IllegalDateFormatException;
import com.vedruna.projectmgmt.exceptions.ProjectAlreadyExistsException;
import com.vedruna.projectmgmt.exceptions.ProjectNotFoundException;
import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;
import com.vedruna.projectmgmt.persistance.model.Technology;
import com.vedruna.projectmgmt.persistance.repository.DeveloperRepositoryI;
import com.vedruna.projectmgmt.persistance.repository.ProjectRepositoryI;
import com.vedruna.projectmgmt.persistance.repository.StatusRepositoryI;
import com.vedruna.projectmgmt.persistance.repository.TechnologyRepositoryI;


@Service
public class ProjectServiceImpl implements ProjectServiceI {

    @Autowired 
    ProjectRepositoryI projectRepo;
    @Autowired
    TechnologyRepositoryI techRepo;
    @Autowired
    DeveloperRepositoryI devRepo;
    @Autowired
    StatusRepositoryI statusRepo;
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Override
    public String test(){
        return "holaMundo";
    }

    /**
     * Obtener todos los proyectos paginados.
     *
     * @param page  La página que se quiere obtener.
     * @param size  El número de elementos por página.
     * @return Una lista de proyectos en formato DTO.
     */
    @Override
    public List<ProjectDTO> getAll(int page, int size) {
        Pageable pages = PageRequest.of(page, size);

        Page<Project> projectPage = projectRepo.findAll(pages);
            log.info("Se han obtenido {} proyectos", projectPage.getTotalElements());
            return projectPage.getContent().stream().map(ProjectDTO::new).collect(Collectors.toList());

    }


    /**
     * Obtener los proyectos que contengan la palabra pasada como parámetro en
     * su nombre.
     *
     * @param word La palabra a buscar en el nombre de los proyectos.
     * @param page La página que se quiere obtener.
     * @param size El número de elementos por página.
     * @return Una lista de proyectos en formato DTO que contienen la palabra
     *         pasada como parámetro en su nombre.
     */
    @Override
    public List<ProjectDTO> getProjectByWord(String word, int page, int size) {
        Pageable pages = PageRequest.of(page, size);
        //Método concreto para encontrar que contenga la palabra y devuelva pageable con las respuestas.
        Page<Project> projectPage = projectRepo.findByProjectNameContainingIgnoreCase(word, pages);
    
        log.info("Se han obtenido {} proyectos", projectPage.getTotalElements());
        return projectPage.getContent().stream().map(ProjectDTO::new).collect(Collectors.toList());
    }

    /**
     * Insertar proyecto.
     *
     * Verifica que no exista un proyecto con el mismo ID o nombre. Si no
     * existe, lo inserta en la base de datos.
     *
     * @param project El proyecto a insertar.
     * @return Un mensaje de confirmación de que se ha insertado el proyecto.
     * @throws ProjectAlreadyExistsException Si ya existe un proyecto con el
     *                                       mismo ID o nombre.
     */
    @Override
    public ResponseEntity<String> saveProject(CreateProjectDTO project) {
        // Verificar si ya existe un proyecto con el mismo ID
        if (projectRepo.existsById(project.getProjectId())) {
            throw new ProjectAlreadyExistsException("Ya existe el proyecto con id: " + project.getProjectId());
        }

        // Verificar si ya existe un proyecto con el mismo nombre
        if (projectRepo.existsByProjectName(project.getName())) {
            throw new ProjectAlreadyExistsException("Ya existe un proyecto con el nombre: " + project.getName());
        }

        Project proj2 = new Project();
        proj2.setProjectId(project.getProjectId());
        proj2.setProjectName(project.getName());
        proj2.setDescription(project.getDescription());
        proj2.setStartDate(convertStringToDate(project.getStartDate()));
        proj2.setEndDate(convertStringToDate(project.getEndDate()));
        proj2.setRepoUrl(project.getRepoUrl());
        proj2.setDemoUrl(project.getDemoUrl());
        proj2.setPictureUrl(project.getPictureUrl());
        proj2.setTechnologies(techRepo.findAllById(project.getTechnologiesIds()));
        proj2.setDevelopers(devRepo.findAllById(project.getDevelopersIds()));
        proj2.setStatus(statusRepo.findByStatusName("In Development"));

        try {
            projectRepo.save(proj2);
        } catch (Exception e) {
            log.error("Error al guardar el proyecto", e);
        }

        return ResponseEntity.ok("Se ha insertado el proyecto: " + project.getName());
      
    }

    /**
     * Actualiza un proyecto existente.
     *
     * Verifica que exista un proyecto con el ID pasado como parámetro y
     * actualiza los campos que sean diferentes de null o vacío. Si no
     * existe, lanza una excepción.
     *
     * @param project El proyecto con los valores a actualizar.
     * @param id      El ID del proyecto a actualizar.
     * @return Un mensaje de confirmación de que se ha actualizado el
     *         proyecto.
     */
    @Override
    public ResponseEntity<String> updateProject(CreateProjectDTO project, Integer id) {
        Project existingProject = projectRepo.findById(id).orElseThrow(() -> new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + id));
        //Comprobación de los valores a insertar
        if(!project.getName().equals(existingProject.getProjectName()) && project.getName() == null) {
            existingProject.setProjectName(project.getName());
        }
        if(!project.getDescription().equals(existingProject.getDescription()) && project.getDescription() != null) {
            existingProject.setDescription(project.getDescription());
        }
        if(!convertStringToDate(project.getStartDate()).equals(existingProject.getStartDate()) && project.getStartDate() != null) {
            existingProject.setStartDate(convertStringToDate(project.getStartDate()));
        }
        if(!convertStringToDate(project.getEndDate()).equals(existingProject.getEndDate()) && project.getEndDate() != null) {
            existingProject.setEndDate(convertStringToDate(project.getEndDate()));
        }
        if(!project.getRepoUrl().equals(existingProject.getRepoUrl()) && project.getRepoUrl() != null) {
            existingProject.setRepoUrl(project.getRepoUrl());
        }
        if(!project.getDemoUrl().equals(existingProject.getDemoUrl()) && project.getDemoUrl() != null) {
            existingProject.setDemoUrl(project.getDemoUrl());
        }
        if(!project.getPictureUrl().equals(existingProject.getPictureUrl()) && project.getPictureUrl() != null) {
            existingProject.setPictureUrl(project.getPictureUrl());
        }
        //comprobación de las tecnologías y los Devs a insertar
        if(!project.getTechnologiesIds().equals(existingProject.getTechnologies().stream().map(Technology::getTechId).toList()) && project.getTechnologiesIds() != null) {
            existingProject.setTechnologies(techRepo.findAllById(project.getTechnologiesIds()));
        }
        if(!project.getDevelopersIds().equals(existingProject.getDevelopers().stream().map(Developer::getDevId).toList()) && project.getDevelopersIds() != null) {
            existingProject.setDevelopers(devRepo.findAllById(project.getDevelopersIds()));
        }
        try {
            projectRepo.save(existingProject);
        } catch (Exception e) {
            log.error("Error al guardar el proyecto", e);
        }
        return ResponseEntity.ok("Se ha actualizado el proyecto con id: " + id);
    }

    /**
     * Elimina un proyecto existente.
     *
     * Verifica que el proyecto exista, y si es así, lo elimina de la base de datos.
     *
     * @param id El ID del proyecto a eliminar.
     * @return Un mensaje de confirmación de que se ha eliminado el proyecto.
     */
    @Override
    public ResponseEntity<String> deleteProject(Integer id){
        if (!projectRepo.existsById(id)) {
            throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + id);
        }
        projectRepo.deleteById(id);
        return ResponseEntity.ok("Se ha borrado el proyecto con id: " + id);
      
    }

    /**
     * Obtener proyectos que contengan una tecnología.
     *
     * Utiliza un custom query para obtener proyectos que contengan la tecnología
     * especificada.
     *
     * @param tech El nombre de la tecnología que se busca.
     * @param page La página que se quiere obtener.
     * @param size El número de elementos por página.
     * @return Una lista de proyectos en formato DTO.
     */
    @Override
    public List<ProjectDTO> getByTechno(String tech, int page, int size) {
        Pageable pages = PageRequest.of(page, size);
        Page<Project> project = projectRepo.findProjectsByTechnologyName(tech, pages);
        try{
            log.info("Se han obtenido {} proyectos", project.getSize());
            return project.stream().map(ProjectDTO::new).collect(Collectors.toList());
        }catch(Exception e){
            log.error(e.getMessage());
            return Collections.emptyList();
        }
       
    }


    /**
     * Convierte una cadena en formato AAAA-MM-DD a un objeto Date. Verifica
     * que la cadena tenga el formato correcto, y lanza una excepción
     * IllegalDateFormatException si no es así.
     *
     * @param date La cadena a convertir.
     * @return Un objeto Date con la fecha correspondiente.
     */
    private Date convertStringToDate(String date) {
        if(date == null){
            throw new IllegalDateFormatException("La fecha no puede ser nula");
        }else if(date.isEmpty()){
            throw new IllegalDateFormatException("La fecha no puede estar vacia");
        }else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalDateFormatException();
        }else{
            return Date.valueOf(date);
        }
    }   
}
