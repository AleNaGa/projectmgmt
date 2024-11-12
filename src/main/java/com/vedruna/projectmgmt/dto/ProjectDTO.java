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
    private String name;
    private String description;
    //private String startDate;   no se si se necesita
    //private String endDate;
    private String repoUrl;
    private String demoUrl;
    private String pictureUrl;
    private String statusName;
    private List<String> developersNames;
    private List<String> technologiesNames;

    public ProjectDTO(Project project) {
        this.name = project.getProjectName();
        this.description = project.getDescription();
        this.repoUrl = project.getRepoUrl();
        this.demoUrl = project.getDemoUrl();
        this.pictureUrl = project.getPictureUrl();
        this.statusName = project.getStatus().getStatusName();
        this.developersNames = project.getDevelopers().stream().map(Developer::getDevName).toList();
        this.technologiesNames = project.getTechnologies().stream().map(Technology::getTechName).toList();
    }
}
