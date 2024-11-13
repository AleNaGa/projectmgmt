package com.vedruna.projectmgmt.persistance.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vedruna.projectmgmt.persistance.model.Project;

@Repository
public interface ProjectRepositoryI extends JpaRepository<Project, Integer>{

    // Custom query
    @Query("SELECT p FROM Project p JOIN p.technologies t WHERE t.techName = :tech")
    Page<Project> findProjectsByTechnologyName(String tech,Pageable pages);

    //Un Find para encontrarlo por nombre de proyecto
    List<Project> findByProjectName(String projectName);


    //Controla que no haya proyectos duplicados
    boolean existsByProjectName(String projectName);

    Page<Project> findByProjectNameContainingIgnoreCase(String word, Pageable pages);
    
}
