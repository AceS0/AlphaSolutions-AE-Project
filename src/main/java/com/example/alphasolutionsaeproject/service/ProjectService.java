package com.example.alphasolutionsaeproject.service;
import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.repository.SubprojectRepository;
import com.example.alphasolutionsaeproject.repository.TaskRepository;
import com.example.alphasolutionsaeproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SubprojectRepository subprojectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, SubprojectRepository subprojectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.subprojectRepository = subprojectRepository;
        this.taskRepository = taskRepository;
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
    public void updateProject(Project project, int pid) {
        projectRepository.update(project, pid);
    }

    // Slet projekt
    public void deleteProject(int pid) {
        projectRepository.deleteById(pid);
    }

    public List<Project> getAllProjectsByUserId(int id){
        return projectRepository.getAllProjectsByUserId(id);
    }

    public List<Project> getSharedProjectsByUserId(int id){
        return projectRepository.getSharedProjectsByUserId(id);
    }

    public int getProjectManagerId(String getCreatedBy){
        return userRepository.getProjectManagerId(getCreatedBy);
    }

    public void toggleChecked(int id) {
        Project project = projectRepository.findById(id);
        if (project != null) {
            projectRepository.updateChecked(id, !project.getChecked());
        }
    }


    public void toggleCheckedAndCascade(int pid) {
        Project project = projectRepository.findById(pid);
        if (project == null) return;

        boolean newChecked = !project.getChecked();
        projectRepository.updateChecked(pid, newChecked);

        List<Subproject> subprojects = subprojectRepository.getAllProjectsByProjectId(pid);
        for (Subproject sp : subprojects) {
            subprojectRepository.updateChecked(sp.getId(), newChecked);


            List<Task> tasks = taskRepository.getAllTasksBySpid(sp.getId());
            for (Task task : tasks) {
                taskRepository.updateChecked(task.getId(), newChecked);
            }
        }
    }

    public void assignToProject(int pid, int userId){
        projectRepository.assignToProject(pid, userId);
    }

    public void unassignFromProject(int pid, int userId){
        projectRepository.unassignFromProject(pid, userId);
    }


}

