package com.vedruna.projectmgmt.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.vedruna.projectmgmt.dto.CreateProjectDTO;
import com.vedruna.projectmgmt.dto.ProjectDTO;
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
        try{
            log.info("Se han obtenido {} proyectos", projectPage.getTotalElements());
            return projectPage.getContent().stream().map(ProjectDTO::new).collect(Collectors.toList());
        }catch(Exception e){
            log.error(e.getMessage());
            return Collections.emptyList();
        }

    }

    @Override
    public List<ProjectDTO> getProject(String word, int page, int size) {
        Pageable pages = PageRequest.of(page, size);
        Page<Project> project = new PageImpl<>(projectRepo.findByProjectName(word));
        try{
            log.info("Se han obtenido {} proyectos", project.getSize());
            return project.stream().map(ProjectDTO::new).collect(Collectors.toList());
        }catch(Exception e){
            log.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void saveProject(CreateProjectDTO project) {
        Project proj2 = new Project();
        try{
            proj2.setProjectName(project.getName());
            proj2.setDescription(project.getDescription());
            proj2.setRepoUrl(project.getRepoUrl());
            proj2.setDemoUrl(project.getDemoUrl());
            proj2.setPictureUrl(project.getPictureUrl());
            proj2.setTechnologies(techRepo.findAllById(project.getTechnologiesIds()));
            proj2.setDevelopers(devRepo.findAllById(project.getDevelopersIds()));
            proj2.setStatus(statusRepo.findByStatusName("En desarrollo"));
            projectRepo.save(proj2);
        }catch(Exception e){
            log.error(e.getMessage());
        }
      
    }

    @Override
    public void updateProject(CreateProjectDTO project) {
        Project proj2 = new Project();
        try{
            proj2.setProjectName(project.getName());
            proj2.setDescription(project.getDescription());
            proj2.setRepoUrl(project.getRepoUrl());
            proj2.setDemoUrl(project.getDemoUrl());
            proj2.setPictureUrl(project.getPictureUrl());
            proj2.setTechnologies(techRepo.findAllById(project.getTechnologiesIds()));
            proj2.setDevelopers(devRepo.findAllById(project.getDevelopersIds()));
            projectRepo.save(proj2);
        }catch(Exception e){
            log.error(e.getMessage());
        }
      
    }

    @Override
    public void deleteProject(Integer id) {
        try{
            projectRepo.deleteById(id);
        }catch(Exception e){
            log.error(e.getMessage());
        }
      
    }

    @Override
    public List<ProjectDTO> getByTechno(String tech, int page, int size) {
        Pageable pages = PageRequest.of(page, size);

        Page<Project> project = new PageImpl<>(projectRepo.findProjectsByTechnologyName(tech));
        try{
            log.info("Se han obtenido {} proyectos", project.getSize());
            return project.stream().map(ProjectDTO::new).collect(Collectors.toList());
        }catch(Exception e){
            log.error(e.getMessage());
            return Collections.emptyList();
        }
       
    }
    
}
