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

    private Integer projectId;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String repoUrl;
    private String demoUrl;
    private String pictureUrl;
    private List<Integer> developersIds;
    private List<Integer> technologiesIds;

    public CreateProjectDTO(Project project) {
        this.projectId = project.getProjectId();
        this.name = project.getProjectName();
        this.description = project.getDescription();
        this.startDate = project.getStartDate().toString();
        this.endDate = project.getEndDate().toString();
        this.repoUrl = project.getRepoUrl();
        this.demoUrl = project.getDemoUrl();
        this.pictureUrl = project.getPictureUrl();
        this.technologiesIds = project.getTechnologies().stream().map(Technology::getTechId).toList();
        this.developersIds = project.getDevelopers().stream().map(Developer::getDevId).toList();
    }
  
}
