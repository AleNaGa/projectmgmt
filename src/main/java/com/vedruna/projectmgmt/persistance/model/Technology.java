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

@Data
@Entity
@NoArgsConstructor
@Table(name = "technologies")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_id", nullable = false, unique = true)
    private Integer techId;

    @Column(name="tech_name", nullable = false, unique = true, length=45)
    private String techName;

    @ManyToMany(mappedBy = "technologies") //Map en porj
    private List<Project> projects;
}
