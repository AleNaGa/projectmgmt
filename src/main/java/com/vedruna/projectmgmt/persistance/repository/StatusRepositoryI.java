package com.vedruna.projectmgmt.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vedruna.projectmgmt.persistance.model.Status;


@Repository
public interface StatusRepositoryI extends JpaRepository<Status,Integer>{
    public Status findByStatusName(String name);
}
