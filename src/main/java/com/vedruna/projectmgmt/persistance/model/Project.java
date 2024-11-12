package com.vedruna.projectmgmt.persistance.model;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

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
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id", nullable = false, unique = true)
    private Integer projectId;

    @Column(name = "project_name", nullable = false, unique = true, length = 45)
    private String projectName;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", updatable = false) // No se puede cambiar una vez creado
    @CreationTimestamp //  Asigna la fecha y hora automáticamente cuando se inserta el registro
    private LocalDateTime startDate;

    @Column(name="end_date")
    private LocalDateTime endDate;

    @Column(name="repository_url", length = 255)
    private String repoUrl;

    @Column(name="demo_url", length=255)
    private String demoUrl;

    @Column(name="pricture", length=255)
    private String pictureUrl;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToMany
    @JoinTable(
        name = "technologies_used_in_projects",//tabla
        joinColumns = @JoinColumn(name = "project_id"), // Columna de projects
        inverseJoinColumns = @JoinColumn(name = "tech_id") // Columna de technology
    )
    private List<Technology> technologies;

    @ManyToMany
    @JoinTable(
        name = "developers_worked_in_projects",//tabla
        joinColumns = @JoinColumn(name = "project_id"), // Columna de projects
        inverseJoinColumns = @JoinColumn(name = "dev_id") // Columna de technology
    )
    private List<Developer> developers;

}
