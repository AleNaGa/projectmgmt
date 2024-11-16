package com.vedruna.projectmgmt.dto;

import java.util.List;

import com.vedruna.projectmgmt.persistance.model.Technology;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTechnologyDTO {


    private Integer techId;
    @NotNull(message = "El nombre de la tecnología no puede ser nulo")
    @NotEmpty(message = "El nombre de la tecnología no puede estar vacio")
    private String techName;
    private List<Integer> projectsIds;

    public CreateTechnologyDTO(Technology tech) {
        this.techId = tech.getTechId();
        this.techName = tech.getTechName();
        tech.getProjects().forEach(project -> this.projectsIds.add(project.getProjectId()));
    }
    
}
