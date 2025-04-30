package com.example.alphasolutionsaeproject.service;
import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    // Hent alle projekter
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Hent projekt ud fra ID
    public Project getProjectById(int id) {
        return projectRepository.findById(id);
    }

    // Gem et nyt projekt
    public void addProject(Project project) {
        projectRepository.save(project);
    }

    // Opdater eksisterende projekt
    public void updateProject(Project project) {
        projectRepository.update(project);
    }

    // Slet projekt
    public void deleteProject(int id) {
        projectRepository.deleteById(id);
    }

    public List<Project> getAllProjectsByUserId(int id){
        return projectRepository.getAllProjectsByUserId(id);
    }

    public List<Project> getSharedProjectsByUserId(int id){
        return projectRepository.getSharedProjectsByUserId(id);
    }

    public void toggleChecked(int id) {
        Project project = projectRepository.findById(id);
        if (project != null) {
            projectRepository.updateChecked(id, !project.getChecked());
        }
    }

}

