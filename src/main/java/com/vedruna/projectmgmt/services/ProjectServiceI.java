package com.vedruna.projectmgmt.services;

import java.util.List;

import com.vedruna.projectmgmt.dto.CreateProjectDTO;
import com.vedruna.projectmgmt.dto.ProjectDTO;

public interface ProjectServiceI {
    
    public List<ProjectDTO> getAll(int page, int size); // GET ALL 
    public List<ProjectDTO> getProject(String word);// GET BY WORD
    public void saveProject(CreateProjectDTO project); //POST
    public void updateProject(CreateProjectDTO project); //PUT
    public void deleteProject(Integer id);//DELETE

    //El custom path
    public List<ProjectDTO> getByTechno(String tech);

}
