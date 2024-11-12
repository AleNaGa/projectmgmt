package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Technology;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateTechnologyDTO {

    private String techName;

    public CreateTechnologyDTO(Technology tech) {
        this.techName = tech.getTechName();
    }
    
}
