package com.vedruna.projectmgmt.services;

import java.text.ParseException;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.vedruna.projectmgmt.dto.CreateProjectDTO;
import com.vedruna.projectmgmt.dto.ProjectDTO;
import com.vedruna.projectmgmt.exceptions.IllegalDateFormatException;
import com.vedruna.projectmgmt.exceptions.ProjectAlreadyExistsException;
import com.vedruna.projectmgmt.exceptions.ProjectNotFoundException;

public interface ProjectServiceI {

    public String test();
    
    public List<ProjectDTO> getAll(int page, int size); // GET ALL 
    public List<ProjectDTO> getProjectByWord(String word, int page, int size);// GET BY WORD
    public ResponseEntity<String> saveProject(CreateProjectDTO project) throws ProjectAlreadyExistsException, IllegalDateFormatException; //POST
    public ResponseEntity<String> updateProject(CreateProjectDTO project, Integer id) throws ProjectNotFoundException, IllegalDateFormatException; //PUT
    public ResponseEntity<String> deleteProject(Integer id) throws ProjectNotFoundException;//DELETE

    //El custom path
    public List<ProjectDTO> getByTechno(String tech, int page, int size);

}
