package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Technology;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TechnologyDTO {
    private Integer id;
    private String name;

    public TechnologyDTO(Technology tech) {
        this.id = tech.getTechId();
        this.name = tech.getTechName();
    }
}
