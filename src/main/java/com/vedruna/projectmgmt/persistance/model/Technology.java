package com.vedruna.projectmgmt.persistance.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "technologies")
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_id", unique = true)
    @NotNull(message="El ID no puede ser nulo")
    private Integer techId;

    @Column(name="tech_name", unique = true, length=45)
    @NotNull(message="El nombre de la tecnología no puede ser nulo")
    private String techName;

    @ManyToMany(mappedBy = "technologies") //Map en porj
    private List<Project> projects;
}
