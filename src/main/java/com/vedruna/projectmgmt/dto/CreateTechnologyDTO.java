package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Technology;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTechnologyDTO {

    private Integer techId;
    private String techName;

    public CreateTechnologyDTO(Technology tech) {
        this.techId = tech.getTechId();
        this.techName = tech.getTechName();
    }
    
}
