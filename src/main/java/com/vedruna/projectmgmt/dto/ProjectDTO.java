package com.vedruna.projectmgmt.dto;


import java.util.List;

import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;
import com.vedruna.projectmgmt.persistance.model.Technology;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDTO {
    private Integer projectId;
    private String name;
    private String description;
    private String startDate; 
    private String endDate;
    private String repoUrl;
    private String demoUrl;
    private String pictureUrl;
    private String statusName;
    private List<String> developersNames;
    private List<String> technologiesNames;

    public ProjectDTO(Project project) {
        this.projectId = project.getProjectId();
        this.name = project.getProjectName();
        this.description = project.getDescription();
        this.startDate = project.getStartDate().toString();
        this.endDate = project.getEndDate().toString();
        this.repoUrl = project.getRepoUrl();
        this.demoUrl = project.getDemoUrl();
        this.pictureUrl = project.getPictureUrl();
        this.statusName = project.getStatus().getStatusName();
        this.developersNames = project.getDevelopers().stream().map(Developer::getDevName).toList();
        this.technologiesNames = project.getTechnologies().stream().map(Technology::getTechName).toList();
    }
}
