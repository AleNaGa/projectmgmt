package com.vedruna.projectmgmt.persistance.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="developers")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dev_id", nullable = false, unique = true)
    private Integer devId;

    @Column(name="dev_name", nullable = false, length = 45)
    private String devName;

    @Column(name="dev_surname", length = 45)
    private String devSurname;

    @Column(name="email", length = 200)
    private String email;

    @Column(name="linkedin_url", length = 255)
    private String linkedinUrl;

    @Column(name="github_url", length = 255)
    private String githubUrl;

    @ManyToMany(mappedBy = "developers") //mapeado en Project
    private List<Project> projects;


  
}
