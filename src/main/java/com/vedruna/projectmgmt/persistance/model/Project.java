package com.vedruna.projectmgmt.persistance.model;

import java.sql.Date;

import java.util.List;


import com.vedruna.projectmgmt.validation.ValidUrl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id",  unique = true)
    @NotNull(message = "El ID no puede ser nulo")
    private Integer projectId;

    @Column(name = "project_name", unique = true, length = 45)
    @NotNull(message = "El nombre del proyecto no puede ser nulo")
    private String projectName;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "start_date", updatable = false) // No se puede cambiar una vez creado
    private Date startDate;

    @Column(name="end_date")
    private Date endDate;

    @Column(name="repository_url", length = 255)
    @NotNull(message = "La URL no puede ser nula")
    @ValidUrl(message = "La URL proporcionada no es válida")
    private String repoUrl;

    @Column(name="demo_url", length=255)
    @NotNull(message = "La URL no puede ser nula")
    @ValidUrl(message = "La URL proporcionada no es válida")
    private String demoUrl;

    @Column(name="picture", length=255)
    @NotNull(message = "La URL no puede ser nula")
    @ValidUrl(message = "La URL proporcionada no es válida")
    private String pictureUrl;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "status_status_id")
    @NotNull(message="El estado no puede ser nulo")
    private Status status;

    @ManyToMany
    @JoinTable(
        name = "technologies_used_in_projects",//tabla
        joinColumns = @JoinColumn(name = "projects_project_id"), // Columna de projects
        inverseJoinColumns = @JoinColumn(name = "technologies_tech_id") // Columna de technology
    )
    private List<Technology> technologies;

    @ManyToMany
    @JoinTable(
        name = "developers_worked_on_projects",//tabla
        joinColumns = @JoinColumn(name = "projects_project_id"), // Columna de projects
        inverseJoinColumns = @JoinColumn(name = "developers_dev_id") // Columna de technology
    )
    private List<Developer> developers;
}
