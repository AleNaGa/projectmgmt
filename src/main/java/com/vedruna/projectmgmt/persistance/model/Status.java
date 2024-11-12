package com.vedruna.projectmgmt.persistance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="status_id", unique=true)
    @NotNull(message="El statusId no puede ser nulo")
    private Integer statusId;

    @Column(name = "status_name",  unique = true, length = 255)
    @NotNull(message="El nombre nopuede ser nulo")
    private String statusName;
    
}
