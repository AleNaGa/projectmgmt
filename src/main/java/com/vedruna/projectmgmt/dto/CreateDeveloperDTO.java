package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateDeveloperDTO {
    private Integer devId;
    private String name;
    private String surname;
    private String email;
    private String linkedinUrl;
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
