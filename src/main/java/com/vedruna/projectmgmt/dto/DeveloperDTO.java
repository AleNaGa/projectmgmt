package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DeveloperDTO {

    private Integer developerId;
    private String developerName;
    private String developerSurname;
    private String email;
    private String linkedinUrl;
    private String githubUrl;
    private int[] projects;

    public DeveloperDTO(Developer developer) {
        this.developerId = developer.getDevId();
        this.developerName = developer.getDevName();
        this.developerSurname = developer.getDevSurname();
        this.email = developer.getEmail();
        this.linkedinUrl = developer.getLinkedinUrl();
        this.githubUrl = developer.getGithubUrl();
        this.projects = developer.getProjects().stream().mapToInt(Project::getProjectId).toArray();
    }
    
}
