package com.vedruna.projectmgmt.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vedruna.projectmgmt.persistance.model.Project;

public interface ProjectRepositoryI extends JpaRepository<Project, Integer>{

    // Custom query
    @Query("SELECT p FROM Project p JOIN p.technologies t WHERE t.techName = :tech")
    List<Project> findProjectsByTechnologyName(String tech);
    
}
