package com.example.alphasolutionsaeproject.service;

import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;
import com.example.alphasolutionsaeproject.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.SubprojectRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class SubprojectService {

    private final SubprojectRepository subprojectRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public SubprojectService(SubprojectRepository subprojectRepository, TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.subprojectRepository = subprojectRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public Subproject getSubprojectById(int id) {
        return subprojectRepository.findById(id);
    }

    public void addSubproject(Subproject subproject) {
        subproject.setEstDeadline(subproject.getDeadline());
        subprojectRepository.save(subproject);
    }

    public void updateSubproject(Subproject subproject, int spid) {
        subproject.setId(spid);
        subprojectRepository.update(subproject, spid);
        if (subproject.getProjectId() == 0) {
            Subproject existing = subprojectRepository.findById(spid); // Fetch from DB
            subproject.setProjectId(existing.getProjectId());
        }
        updateEstimatedDeadline(subproject);
    }


    public void deleteSubproject(int spid) {
        subprojectRepository.delete(spid);
    }


    public List<Subproject> getAllSubprojectsByProjectId(int id) {
        return subprojectRepository.getAllProjectsByProjectId(id);
    }

    public void toggleCheckedAndCascade(int spid) {
        Subproject subproject = subprojectRepository.findById(spid);
        if (subproject == null) return;

        boolean newChecked = !subproject.getChecked();
        subprojectRepository.updateChecked(spid, newChecked);

        List<Task> tasks = taskRepository.getAllTasksBySpid(spid);
        for (Task task : tasks) {
            taskRepository.updateChecked(task.getId(), newChecked);
        }

        if (!newChecked) {
            int projectId = subproject.getProjectId();
            Project project = projectRepository.findById(projectId);
            if (project.getChecked()) {
                projectRepository.updateChecked(projectId, false);
            }
        }
    }

    public void updateEstimatedDeadline(Subproject subproject) {
        int expected = subproject.getDuration();
        int actual = subprojectRepository.getWorkHours(subproject.getId());
        LocalDate baseDeadline = subproject.getDeadline();
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
        subproject.setEstDeadline(estDeadline);
        subprojectRepository.updateEstimatedDeadline(subproject);
    }


}
