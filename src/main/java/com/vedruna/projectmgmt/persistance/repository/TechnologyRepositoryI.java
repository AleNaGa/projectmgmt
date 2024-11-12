package com.vedruna.projectmgmt.persistance.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.vedruna.projectmgmt.persistance.model.Technology;

public interface TechnologyRepositoryI extends JpaRepository<Technology, Integer>{

    
}
