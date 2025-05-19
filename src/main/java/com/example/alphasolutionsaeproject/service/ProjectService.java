package com.example.alphasolutionsaeproject.service;

import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.repository.SubprojectRepository;
import com.example.alphasolutionsaeproject.repository.TaskRepository;
import com.example.alphasolutionsaeproject.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;

import java.time.LocalDate;
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

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(int id) {
        return projectRepository.findById(id);
    }

    public void addProject(Project project) {
        project.setEstDeadline(project.getDeadline());
        projectRepository.save(project);
    }

    public void updateProject(Project project, int pid) {
        project.setId(pid);
        projectRepository.update(project, pid);
        if (project.getId() == 0) {
            Project existing = projectRepository.findById(pid);
            project.setId(existing.getId());
        }
        updateEstimatedDeadline(project);
    }

    public void deleteProject(int pid) {
        projectRepository.deleteById(pid);
    }

    public List<Project> getAllProjectsByUserId(int id) {
        return projectRepository.getAllProjectsByUserId(id);
    }

    public List<Project> getSharedProjectsByUserId(int id) {
        return projectRepository.getSharedProjectsByUserId(id);
    }

    public int getProjectManagerId(String getCreatedBy) {
        return userRepository.getProjectManagerId(getCreatedBy);
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

    public void updateEstimatedDeadline(Project project) {
        int expected = project.getDuration();
        int actual = projectRepository.getWorkHours(project.getId());
        LocalDate baseDeadline = project.getDeadline();
        LocalDate estDeadline;

        if (actual > expected) {
            int extra = actual - expected;
            int extraDays = (int) Math.ceil(extra / 6.0);
            estDeadline = baseDeadline.plusDays(extraDays);
        } else {
            estDeadline = baseDeadline;
        }

        if (estDeadline.isBefore(LocalDate.now())) {
            estDeadline = LocalDate.now();
        }
        project.setEstDeadline(estDeadline);
        projectRepository.updateEstimatedDeadline(project);
    }

}

