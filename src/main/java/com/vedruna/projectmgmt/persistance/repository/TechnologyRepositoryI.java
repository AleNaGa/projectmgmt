package com.vedruna.projectmgmt.persistance.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vedruna.projectmgmt.persistance.model.Technology;


@Repository
public interface TechnologyRepositoryI extends JpaRepository<Technology, Integer>{

    
}
