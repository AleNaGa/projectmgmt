package com.vedruna.projectmgmt.dto;


import lombok.NoArgsConstructor;


@NoArgsConstructor
public class SampleDTO {

    private String saludo;
    
    public SampleDTO(String saludo) {
        this.saludo = saludo;
    }
    public String getSaludo() { return saludo; }
    public void setSaludo(String saludo) { this.saludo = saludo; }
}
