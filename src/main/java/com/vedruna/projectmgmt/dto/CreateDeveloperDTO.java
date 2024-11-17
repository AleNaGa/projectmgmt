package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateDeveloperDTO {
    private Integer devId;
    @NotNull(message="El nombre no puede ser nulo")
    private String name;
    private String surname;
    @NotNull(message="El email no puede ser nulo")
    @Email
    private String email;
    @NotNull(message="La URL no puede ser nula")
    private String linkedinUrl;
    @NotNull(message="La URL no puede ser nula")
    private String githubUrl;
    private int[] projectsIds;
   

    public CreateDeveloperDTO(Developer developer) {
        this.devId = developer.getDevId();
        this.name = developer.getDevName();
        this.surname = developer.getDevSurname();
        this.email = developer.getEmail();
        this.linkedinUrl = developer.getLinkedinUrl();
        this.githubUrl = developer.getGithubUrl();
        this.projectsIds = developer.getProjects().stream().mapToInt(Project::getProjectId).toArray();
    }
    
}
