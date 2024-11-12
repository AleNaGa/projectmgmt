package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateStatusDTO {
    //Solo necesitamos el nombre para crearlo
    private String statusName;

    public CreateStatusDTO(Status status) {
        this.statusName = status.getStatusName();
    }
    
}
