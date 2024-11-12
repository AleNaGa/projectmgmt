package com.vedruna.projectmgmt.controllers;
import com.vedruna.projectmgmt.dto.CreateProjectDTO;
import com.vedruna.projectmgmt.dto.ProjectDTO;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.vedruna.projectmgmt.services.ProjectServiceI;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    @Autowired
    private ProjectServiceI projectServ;

    @GetMapping("/test")
    public String test() {
        return projectServ.test();
    }

    @GetMapping()
    public List<ProjectDTO> findAll(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "10") int size) {
        // paises por página
        return projectServ.getAll(page, size);
    }

    @PostMapping("/insert")
    public String insert(@RequestBody CreateProjectDTO project) {
         projectServ.saveProject(project);
         return "Se ha insertado el proyecto";
    }    



}
