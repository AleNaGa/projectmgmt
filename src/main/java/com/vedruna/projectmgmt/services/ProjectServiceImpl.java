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
import com.vedruna.projectmgmt.persistance.model.Project;
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

    @Override
    public List<ProjectDTO> getAll(int page, int size) {
        Pageable pages = PageRequest.of(page, size);

        Page<Project> projectPage = projectRepo.findAll(pages);
            log.info("Se han obtenido {} proyectos", projectPage.getTotalElements());
            return projectPage.getContent().stream().map(ProjectDTO::new).collect(Collectors.toList());

    }


    @Override
    public List<ProjectDTO> getProjectByWord(String word, int page, int size) {
        Pageable pages = PageRequest.of(page, size);
        //Método concreto para encontrar que contenga la palabra y devuelva pageable con las respuestas.
        Page<Project> projectPage = projectRepo.findByProjectNameContainingIgnoreCase(word, pages);
    
        log.info("Se han obtenido {} proyectos", projectPage.getTotalElements());
        return projectPage.getContent().stream().map(ProjectDTO::new).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<String> saveProject(CreateProjectDTO project) throws ProjectAlreadyExistsException, IllegalDateFormatException {
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

    @Override
    public ResponseEntity<String> updateProject(CreateProjectDTO project, Integer id) throws ProjectNotFoundException, IllegalDateFormatException {
        Project existingProject = projectRepo.findById(id).orElseThrow(() -> new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + id));

        existingProject.setProjectId(project.getProjectId());
        existingProject.setProjectName(project.getName());
        existingProject.setDescription(project.getDescription());
        existingProject.setStartDate(convertStringToDate(project.getStartDate()));
        existingProject.setEndDate(convertStringToDate(project.getEndDate()));
        existingProject.setRepoUrl(project.getRepoUrl());
        existingProject.setDemoUrl(project.getDemoUrl());
        existingProject.setPictureUrl(project.getPictureUrl());
        existingProject.setTechnologies(techRepo.findAllById(project.getTechnologiesIds()));
        existingProject.setDevelopers(devRepo.findAllById(project.getDevelopersIds()));

        try {
            projectRepo.save(existingProject);
        } catch (Exception e) {
            log.error("Error al guardar el proyecto", e);
        }
        return ResponseEntity.ok("Se ha actualizado el proyecto con id: " + id);
      
    }

    @Override
    public ResponseEntity<String> deleteProject(Integer id) throws ProjectNotFoundException {
        if (!projectRepo.existsById(id)) {
            throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + id);
        }
        projectRepo.deleteById(id);
        return ResponseEntity.ok("Se ha borrado el proyecto con id: " + id);
      
    }

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


    private Date convertStringToDate(String date) throws IllegalDateFormatException {
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
