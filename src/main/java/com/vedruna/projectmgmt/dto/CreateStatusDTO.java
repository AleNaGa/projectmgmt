package com.vedruna.projectmgmt.dto;

import com.vedruna.projectmgmt.persistance.model.Status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateStatusDTO {
    private Integer statusId;
    private String statusName;

    public CreateStatusDTO(Status status) {
        this.statusId = status.getStatusId();
        this.statusName = status.getStatusName();
    }
    
}
