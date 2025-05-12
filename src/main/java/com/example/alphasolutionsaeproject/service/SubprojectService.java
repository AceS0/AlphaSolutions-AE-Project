package com.example.alphasolutionsaeproject.service;
import com.example.alphasolutionsaeproject.model.Project;
import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;
import com.example.alphasolutionsaeproject.repository.TaskRepository;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.SubprojectRepository;

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

    public List<Subproject> getAllSubprojects() {
        return subprojectRepository.findAll();
    }

    public Subproject getSubprojectById(int id) {
        return subprojectRepository.findById(id);
    }

    public void addSubproject(Subproject subproject) {
        subprojectRepository.save(subproject);
    }

    public void updateSubproject(Subproject subproject, int spid) {
        subprojectRepository.update(subproject, spid);
    }

    public void deleteSubproject(int spid) {
        subprojectRepository.delete(spid);
    }


    public List<Subproject> getAllSubprojectsByProjectId(int id){
        return subprojectRepository.getAllProjectsByProjectId(id);
    }

//    public void toggleChecked(int id) {
//        Subproject subproject = subprojectRepository.findById(id);
//        if (subproject != null) {
//            subprojectRepository.updateChecked(id, !subproject.getChecked());
//        }
//    }

    public void toggleCheckedAndCascade(int spid) {
        Subproject subproject = subprojectRepository.findById(spid);
        if (subproject == null) return;

        boolean newChecked = !subproject.getChecked();
        subprojectRepository.updateChecked(spid, newChecked);

        // Opdater alle tasks under subprojektet
        List<Task> tasks = taskRepository.getAllTasksBySpid(spid);
        for (Task task : tasks) {
            taskRepository.updateChecked(task.getId(), newChecked);
        }

        // Hvis subprojekt bliver unchecket, skal projektet også uncheckes
        if (!newChecked) {
            int projectId = subproject.getProjectId();
            Project project = projectRepository.findById(projectId);
            if (project.getChecked()) {
                projectRepository.updateChecked(projectId, false);
            }
        }
    }




}
