package com.vedruna.projectmgmt.persistance.model;

import java.util.List;

import com.vedruna.projectmgmt.validation.ValidUrl;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="developers")
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dev_id",  unique = true)//Validación de único
    @NotNull(message = "El ID no puede ser nulo")//Validaciones de null
    @Positive(message = "El ID debe ser un número positivo")
    private Integer devId;

    @Column(name="dev_name", length = 45)
    @NotNull(message = "El nombre del desarrollador no puede ser nulo")
    private String devName;

    @Column(name="dev_surname", length = 45)
    private String devSurname;

    @Column(name="email", length = 200, unique = true)
    @Email(message = "Este valor debe ser un email válido")
    private String email;

    @Column(name="linkedin_url", length = 255)
     @NotNull(message = "La URL no puede ser nula")
    @ValidUrl(message = "La URL proporcionada no es válida")
    private String linkedinUrl;

    @Column(name="github_url", length = 255)
    @NotNull(message = "La URL no puede ser nula")
    @ValidUrl(message = "La URL proporcionada no es válida")
    private String githubUrl;

    @ManyToMany(mappedBy = "developers") //mapeado en Project
    private List<Project> projects; 
}
