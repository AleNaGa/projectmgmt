package com.vedruna.projectmgmt.dto;

import java.util.List;

import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;
import com.vedruna.projectmgmt.persistance.model.Technology;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateProjectDTO {
    private String name;
    private String description;
    //private String startDate;
    //private String endDate;
    private String repoUrl;
    private String demoUrl;
    private String pictureUrl;
    private List<Integer> developersIds;
    private List<Integer> technologiesIds;

    public CreateProjectDTO(Project project) {
        this.name = project.getProjectName();
        this.description = project.getDescription();
        this.repoUrl = project.getRepoUrl();
        this.demoUrl = project.getDemoUrl();
        this.pictureUrl = project.getPictureUrl();
        this.technologiesIds = project.getTechnologies().stream().map(Technology::getTechId).toList();
        this.developersIds = project.getDevelopers().stream().map(Developer::getDevId).toList();
    }
}
