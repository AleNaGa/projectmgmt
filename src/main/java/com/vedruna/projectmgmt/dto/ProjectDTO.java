package com.vedruna.projectmgmt.dto;


import java.util.List;

import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;
import com.vedruna.projectmgmt.persistance.model.Technology;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDTO {
    private Integer projectId;
    @NotNull(message="El nombre no puede ser nulo")
    @NotEmpty(message="El nombre no puede estar vacio")
    private String name;
    private String description;
    @NotNull(message="La fecha de inicio no puede ser nula")
    private String startDate; 
    private String endDate;
    @NotNull(message="La URL no puede ser nula")
    private String repoUrl;
    @NotNull(message="La URL no puede ser nula")
    private String demoUrl;
    @NotNull(message="La URL no puede ser nula")
    private String pictureUrl;
    @NotNull(message="El estado no puede ser nulo")
    private String statusName;
    @NotNull(message="Los desarrolladores no pueden ser nulos")
    @NotEmpty(message="Los desarrolladores no pueden estar vacios")
    private List<String> developersNames;
    @NotNull(message="Las tecnologías no pueden ser nulas")
    @NotEmpty(message="Las tecnologías no pueden estar vacias")
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
