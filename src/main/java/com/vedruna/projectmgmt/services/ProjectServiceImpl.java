package com.vedruna.projectmgmt.services;

import java.util.Collections;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import com.vedruna.projectmgmt.dto.CreateProjectDTO;
import com.vedruna.projectmgmt.dto.PaginatedResponseDTO;
import com.vedruna.projectmgmt.dto.ProjectDTO;
import com.vedruna.projectmgmt.exceptions.DeveloperNotFound;
import com.vedruna.projectmgmt.exceptions.IllegalDateFormatException;
import com.vedruna.projectmgmt.exceptions.ProjectAlreadyExistsException;
import com.vedruna.projectmgmt.exceptions.ProjectNotFoundException;
import com.vedruna.projectmgmt.persistance.model.Developer;
import com.vedruna.projectmgmt.persistance.model.Project;
import com.vedruna.projectmgmt.persistance.model.Technology;
import com.vedruna.projectmgmt.persistance.repository.DeveloperRepositoryI;
import com.vedruna.projectmgmt.persistance.repository.ProjectRepositoryI;
import com.vedruna.projectmgmt.persistance.repository.StatusRepositoryI;
import com.vedruna.projectmgmt.persistance.repository.TechnologyRepositoryI;


@Service
public class ProjectServiceImpl implements ProjectServiceI {

    @Autowired 
    ProjectRepositoryI projectRepo;
    @Autowired
    TechnologyRepositoryI techRepo;
    @Autowired
    DeveloperRepositoryI devRepo;
    @Autowired
    StatusRepositoryI statusRepo;
    @Autowired
    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);

    @Override
    public String test(){
        return "holaMundo";
    }

    /**
     * Obtiene una lista de proyectos paginada.
     *
     * @param page   número de página a obtener
     * @param size   tamaño de la página
     * @return       un DTO de respuesta paginada con los proyectos
     */
    @Override
    public PaginatedResponseDTO<ProjectDTO> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
    
        // Obtener la página de proyectos
        Page<Project> projectPage = projectRepo.findAll(pageable);
        log.info("Se han obtenido {} proyectos", projectPage.getTotalElements());
    
        // Construir y devolver la respuesta paginada
        return new PaginatedResponseDTO<>(projectPage.map(ProjectDTO::new));
    }
    



    /**
     * Obtiene una lista paginada de proyectos que contengan la palabra clave en su nombre
     *
     * @param word   palabra clave para buscar proyectos
     * @param page   número de página a obtener
     * @param size   tamaño de la página
     * @return       un DTO de respuesta paginada con los proyectos
     */
    @Override
    public PaginatedResponseDTO<ProjectDTO> getProjectByWord(String word, int page, int size) {
        Pageable pages = PageRequest.of(page, size);
        // Método para buscar proyectos que contengan la palabra clave en su nombre
        Page<Project> projectPage = projectRepo.findByProjectNameContainingIgnoreCase(word, pages);

        log.info("Se han obtenido {} proyectos", projectPage.getTotalElements());

        // Crear el PaginatedResponseDTO con los resultados y la información de paginación
        PaginatedResponseDTO<ProjectDTO> response = new PaginatedResponseDTO<>();
        response.setContent(projectPage.getContent().stream().map(ProjectDTO::new).collect(Collectors.toList()));
        response.setCurrentPage(page);
        response.setTotalPages(projectPage.getTotalPages());
        response.setTotalItems((int) projectPage.getTotalElements());
        response.setPageSize(size);

        return response;
    }


    /**
     * Insertar proyecto.
     *
     * Verifica que no exista un proyecto con el mismo ID o nombre. Si no
     * existe, lo inserta en la base de datos.
     *
     * @param project El proyecto a insertar.
     * @return Un mensaje de confirmación de que se ha insertado el proyecto.
     * @throws ProjectAlreadyExistsException Si ya existe un proyecto con el
     *                                       mismo ID o nombre.
     */
    @Override
    public ResponseEntity<String> saveProject(CreateProjectDTO project) {
        // Verificar si ya existe un proyecto con el mismo ID
        if (projectRepo.existsById(project.getProjectId())) {
            throw new ProjectAlreadyExistsException("Ya existe el proyecto con id: " + project.getProjectId());
        }

        // Verificar si ya existe un proyecto con el mismo nombre
        if (projectRepo.existsByProjectName(project.getName())) {
            throw new ProjectAlreadyExistsException("Ya existe un proyecto con el nombre: " + project.getName());
        }

        Project proj2 = new Project();
        proj2.setProjectId(project.getProjectId());
        proj2.setProjectName(project.getName());
        proj2.setDescription(project.getDescription());
        proj2.setStartDate(convertStringToDate(project.getStartDate()));
        proj2.setEndDate(convertStringToDate(project.getEndDate()));
        proj2.setRepoUrl(project.getRepoUrl());
        proj2.setDemoUrl(project.getDemoUrl());
        proj2.setPictureUrl(project.getPictureUrl());
        proj2.setTechnologies(techRepo.findAllById(project.getTechnologiesIds()));
        proj2.setDevelopers(devRepo.findAllById(project.getDevelopersIds()));
        proj2.setStatus(statusRepo.findByStatusName("In Development"));

        try {
            projectRepo.save(proj2);
        } catch (Exception e) {
            log.error("Error al guardar el proyecto", e);
        }

        return ResponseEntity.ok("Se ha insertado el proyecto: " + project.getName());
      
    }

    /**
     * Actualiza un proyecto existente.
     *
     * Verifica que exista un proyecto con el ID pasado como parámetro y
     * actualiza los campos que sean diferentes de null o vacío. Si no
     * existe, lanza una excepción.
     *
     * @param project El proyecto con los valores a actualizar.
     * @param id      El ID del proyecto a actualizar.
     * @return Un mensaje de confirmación de que se ha actualizado el
     *         proyecto.
     */
    @Override
    public ResponseEntity<String> updateProject(CreateProjectDTO project, Integer id) {
        Project existingProject = projectRepo.findById(id).orElseThrow(() -> new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + id));
        //Comprobación de los valores a insertar
        if(!project.getName().equals(existingProject.getProjectName())) {
            existingProject.setProjectName(project.getName());
        }
        if(!project.getDescription().equals(existingProject.getDescription())) {
            existingProject.setDescription(project.getDescription());
        }
        if(!convertStringToDate(project.getStartDate()).equals(existingProject.getStartDate())) {
            existingProject.setStartDate(convertStringToDate(project.getStartDate()));
        }
        if(!convertStringToDate(project.getEndDate()).equals(existingProject.getEndDate())) {
            existingProject.setEndDate(convertStringToDate(project.getEndDate()));
        }
        if(!project.getRepoUrl().equals(existingProject.getRepoUrl())) {
            existingProject.setRepoUrl(project.getRepoUrl());
        }
        if(!project.getDemoUrl().equals(existingProject.getDemoUrl()) ) {
            existingProject.setDemoUrl(project.getDemoUrl());
        }
        if(!project.getPictureUrl().equals(existingProject.getPictureUrl()) ) {
            existingProject.setPictureUrl(project.getPictureUrl());
        }
        //comprobación de las tecnologías y los Devs a insertar
        if(!project.getTechnologiesIds().equals(existingProject.getTechnologies().stream().map(Technology::getTechId).toList())) {
            existingProject.setTechnologies(techRepo.findAllById(project.getTechnologiesIds()));
        }
        if(!project.getDevelopersIds().equals(existingProject.getDevelopers().stream().map(Developer::getDevId).toList()) ) {
            existingProject.setDevelopers(devRepo.findAllById(project.getDevelopersIds()));
        }
        try {
            projectRepo.save(existingProject);
        } catch (Exception e) {
            log.error("Error al guardar el proyecto", e);
        }
        return ResponseEntity.ok("Se ha actualizado el proyecto con id: " + id);
    }

    /**
     * Elimina un proyecto existente.
     *
     * Verifica que el proyecto exista, y si es así, lo elimina de la base de datos.
     *
     * @param id El ID del proyecto a eliminar.
     * @return Un mensaje de confirmación de que se ha eliminado el proyecto.
     */
    @Override
    public ResponseEntity<String> deleteProject(Integer id){
        if (!projectRepo.existsById(id)) {
            throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + id);
        }
        projectRepo.deleteById(id);
        return ResponseEntity.ok("Se ha borrado el proyecto con id: " + id);
      
    }

    /**
     * Obtener proyectos que contengan una tecnología.
     *
     * Utiliza un custom query para obtener proyectos que contengan la tecnología
     * especificada.
     *
     * @param tech El nombre de la tecnología que se busca.
     * @param page La página que se quiere obtener.
     * @param size El número de elementos por página.
     * @return Una lista de proyectos en formato DTO.
     */
    @Override
    public PaginatedResponseDTO<ProjectDTO> getByTechno(String tech, int page, int size) {
        Pageable pages = PageRequest.of(page, size);
        // Buscar proyectos por tecnología
        Page<Project> projectPage = projectRepo.findProjectsByTechnologyName(tech, pages);

        log.info("Se han obtenido {} proyectos", projectPage.getTotalElements());

        // Crear el PaginatedResponseDTO con los resultados y la información de paginación
        PaginatedResponseDTO<ProjectDTO> response = new PaginatedResponseDTO<>();
        response.setContent(projectPage.getContent().stream()
                                    .map(ProjectDTO::new)
                                    .collect(Collectors.toList()));
        response.setCurrentPage(page);
        response.setTotalPages(projectPage.getTotalPages());
        response.setTotalItems((int) projectPage.getTotalElements());
        response.setPageSize(size);

        return response;
    }



    /**
     * Convierte una cadena en formato AAAA-MM-DD a un objeto Date. Verifica
     * que la cadena tenga el formato correcto, y lanza una excepción
     * IllegalDateFormatException si no es así.
     *
     * @param date La cadena a convertir.
     * @return Un objeto Date con la fecha correspondiente.
     */
    private Date convertStringToDate(String date) {
        if(date == null){
            throw new IllegalDateFormatException("La fecha no puede ser nula");
        }else if(date.isEmpty()){
            throw new IllegalDateFormatException("La fecha no puede estar vacia");
        }else if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new IllegalDateFormatException();
        }else{
            return Date.valueOf(date);
        }
    }


    // SERVICIOS EXTRA 

    /**
     * Añade un desarrollador a un proyecto existente.
     * 
     * Verifica que el proyecto y el desarrollador existan, y si es así, 
     * los relaciona en la base de datos.
     * 
     * @param projectId El ID del proyecto al que se va a anadir el
     * desarrollador.
     * @param developerId El ID del desarrollador que se va a anadir.
     * @return Un mensaje de confirmación de que se ha anadido el
     *         desarrollador al proyecto.
     */
    @Override
    public ResponseEntity<String> addDeveloperToProject(Integer developerId, Integer projectId) {
        if(!projectRepo.existsById(projectId)){
            throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + projectId);
        }
        if(!devRepo.existsById(developerId)){
            throw new DeveloperNotFound(developerId);
        }
        Project pj = projectRepo.findById(projectId).get();
        Developer dev = devRepo.findById(developerId).get();
        try{
        pj.getDevelopers().add(devRepo.findById(developerId).get());
        dev.getProjects().add(projectRepo.findById(projectId).get());
        projectRepo.save(pj);
        devRepo.save(dev);
        return ResponseEntity.ok("Se ha anadido el desarrollador con id: " + developerId + " al proyecto con id: " + projectId);   
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.ok("No se ha anadido el desarrollador con id: " + developerId + " al proyecto con id: " + projectId);
        }
    }

    /**
     * Añade una tecnología a un proyecto existente.
     * 
     * Verifica que el proyecto y la tecnología existan, y si es así, 
     * los relaciona en la base de datos.
     * 
     * @param projectId El ID del proyecto al que se va a anadir la
     * tecnología.
     * @param technologyId El ID de la tecnología que se va a anadir.
     * @return Un mensaje de confirmación de que se ha anadido la
     *         tecnología al proyecto.
     */
    @Override
    public ResponseEntity<String> addTechnologyToProject(Integer technologyId, Integer projectId) {
        if(!projectRepo.existsById(projectId)){
            throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + projectId);
        }
        if(!techRepo.existsById(technologyId)){
            throw new DeveloperNotFound(technologyId);
        }
        Project pj = projectRepo.findById(projectId).get();
        Technology tech = techRepo.findById(technologyId).get();
        try{
        pj.getTechnologies().add(techRepo.findById(technologyId).get());
        tech.getProjects().add(projectRepo.findById(projectId).get());
        projectRepo.save(pj);
        techRepo.save(tech);
        return ResponseEntity.ok("Se ha anadido la tecnología con id: " + technologyId + " al proyecto con id: " + projectId);   
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.ok("No se ha anadido la tecnología con id: " + technologyId + " al proyecto con id: " + projectId);
        }
    }

    /**
     * Cambia el estado de un proyecto a Testing.
     * 
     * Verifica que el proyecto exista y que est  en desarrollo, y si es
     * así, cambia su estado a Testing.
     * 
     * @param projectId El ID del proyecto al que se va a cambiar el
     *                  estado.
     * @return Un mensaje de confirmaci n de que se ha cambiado el
     *         estado del proyecto.
     */
    @Override
    public ResponseEntity<String> projectToTest(Integer projectId) {
        if(!projectRepo.existsById(projectId)){
            throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + projectId);
        }
        Project pj = projectRepo.findById(projectId).get();
        if(pj.getStatus().getStatusId() != 1){
            throw new ProjectNotFoundException("El proyecto con id: " + projectId + " no se encuentra en development");
        }
        try{
        pj.setStatus(statusRepo.findById(2).get());
        projectRepo.save(pj);
        return ResponseEntity.ok("Se ha cambiado el estado del proyecto con id: " + projectId + " a Testing");   
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.ok("No se ha cambiado el estado del proyecto con id: " + projectId + " a Testing");
        }
    }

    /**
     * Cambia el estado de un proyecto a Production.
     * 
     * Verifica que el proyecto exista y que est  en Testing, y si es
     * así, cambia su estado a Production.
     * 
     * @param projectId El ID del proyecto al que se va a cambiar el
     *                  estado.
     * @return Un mensaje de confirmación de que se ha cambiado el
     *         estado del proyecto.
     */
    @Override
    public ResponseEntity<String> projectToProduction(Integer projectId) {
        if(!projectRepo.existsById(projectId)){
            throw new ProjectNotFoundException("No se ha encontrado el proyecto con id: " + projectId);
        }
        Project pj = projectRepo.findById(projectId).get();
        if(pj.getStatus().getStatusId() != 2){
            throw new ProjectNotFoundException("El proyecto con id: " + projectId + " no se encuentra en Testing");
        }
        try{
        pj.setStatus(statusRepo.findById(3).get());
        projectRepo.save(pj);
        return ResponseEntity.ok("Se ha cambiado el estado del proyecto con id: " + projectId + " a Production");   
        }catch(Exception e){
            log.error(e.getMessage());
            return ResponseEntity.ok("No se ha cambiado el estado del proyecto con id: " + projectId + " a Production");
        }
    }   
}
