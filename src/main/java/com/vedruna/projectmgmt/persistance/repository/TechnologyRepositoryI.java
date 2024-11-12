package com.vedruna.projectmgmt.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vedruna.projectmgmt.persistance.model.Project;
import com.vedruna.projectmgmt.persistance.model.Technology;

public interface TechnologyRepositoryI extends JpaRepository<Technology, Integer>{

    
}
