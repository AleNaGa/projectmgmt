package com.vedruna.projectmgmt.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vedruna.projectmgmt.persistance.model.Developer;

public interface DeveloperRepositoryI extends JpaRepository<Developer,Integer>{
    
}
