package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Technology;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTechnologyDTO {

    @NotNull(message = "El ID no puede ser nulo")
    private Integer techId;
    @NotNull(message = "El nombre de la tecnología no puede ser nulo")
    @NotEmpty(message = "El nombre de la tecnología no puede estar vacio")
    private String techName;

    public CreateTechnologyDTO(Technology tech) {
        this.techId = tech.getTechId();
        this.techName = tech.getTechName();
    }
    
}
