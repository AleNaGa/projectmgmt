package com.vedruna.projectmgmt.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vedruna.projectmgmt.persistance.model.Status;

public interface StatusRepositoryI extends JpaRepository<Status,Integer>{
    public Status findByStatusName(String name);
}
