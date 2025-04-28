package com.example.alphasolutionsaeproject.service;
import com.example.alphasolutionsaeproject.model.Project;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
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
}

