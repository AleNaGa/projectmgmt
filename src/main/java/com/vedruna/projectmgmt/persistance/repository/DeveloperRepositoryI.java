package com.vedruna.projectmgmt.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vedruna.projectmgmt.persistance.model.Developer;

@Repository
public interface DeveloperRepositoryI extends JpaRepository<Developer,Integer>{
    
}
