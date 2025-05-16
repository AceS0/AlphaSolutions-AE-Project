package com.example.alphasolutionsaeproject.service;


import com.example.alphasolutionsaeproject.model.*;
import com.example.alphasolutionsaeproject.repository.ProjectRepository;
import com.example.alphasolutionsaeproject.repository.SubprojectRepository;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.TaskRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final SubprojectRepository subprojectRepository;
    private final SubprojectService subprojectService;
    private final ProjectService projectService;


    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, SubprojectRepository subprojectRepository, SubprojectService subprojectService, ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.subprojectRepository = subprojectRepository;
        this.projectRepository = projectRepository;
        this.subprojectService = subprojectService;
        this.projectService = projectService;
    }

    public Task getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public void addTask(Task task, int spid) {
        taskRepository.save(task, spid);
        updateAggregatedWorkHours(task);
    }

    public void updateTask(Task task) {
        taskRepository.update(task);
        if (task.getSubprojectId() == 0) {
            Task existing = taskRepository.findById(task.getId());
            task.setSubprojectId(existing.getSubprojectId());
        }

        updateAggregatedWorkHours(task);
        updateEstimatedDeadline(task);
    }

    public void deleteTask(int tid) {
        Task task = taskRepository.findById(tid);
        taskRepository.delete(tid);
        updateAggregatedWorkHours(task);
        updateEstimatedDeadline(task);
    }

    public void assignUserToTask(int taskId, int userId) {
        taskRepository.assignUserToTask(taskId, userId);
        int projectId = taskRepository.getProjectIdByTask(taskId);
        taskRepository.assignToProjectIfNotExists(userId, projectId);
    }


    public void unassignUserFromTaskAndProject(int taskId, int userId) {
        taskRepository.removeUserFromTask(taskId, userId);
        int projectId = taskRepository.getProjectIdByTask(taskId);
        boolean stillAssigned = taskRepository.isUserStillAssignedToProject(userId, projectId);
        if (!stillAssigned) {
            taskRepository.removeUserFromProject(userId, projectId);
        }
    }

    public List<User> getUsersUnassignedTo(int taskId) {
        return taskRepository.getUsersUnassignedTo(taskId);
    }

    public List<User> getUsersAssignedTo(int taskId) {
        return taskRepository.getUsersAssignedTo(taskId);
    }

    public List<TaskUser> getTaskUser(int spid) {
        List<TaskUser> taskUserList = new ArrayList<>();
        List<Task> taskList = taskRepository.getAllTasksBySpid(spid);

        for (Task task : taskList) {
            TaskUser taskUser = new TaskUser(task,
                    taskRepository.getUsersAssignedTo(task.getId()),
                    taskRepository.getUsersUnassignedTo(task.getId()));
            taskUserList.add(taskUser);
        }
        return taskUserList;
    }


    public List<TaskUser> getAllTasksBySubProjectId(int spid) {
        return getTaskUser(spid);
    }

    public void toggleCheckedAndCascadeUp(int taskId) {
        Task task = taskRepository.findById(taskId);
        if (task == null) return;

        boolean newChecked = !task.getChecked();
        taskRepository.updateChecked(taskId, newChecked);

        if (!newChecked) {
            int subprojectId = task.getSubprojectId();
            subprojectRepository.updateChecked(subprojectId, false);

            int projectId = subprojectRepository.findById(subprojectId).getProjectId();
            projectRepository.updateChecked(projectId, false);
        }
    }

    public void updateWorkHours(int taskId, int workHours) {
        taskRepository.updateWorkHours(taskId, workHours);

        Task task = taskRepository.findById(taskId);
        updateAggregatedWorkHours(task);
        updateEstimatedDeadline(task);
    }

    public void updateAggregatedWorkHours(Task task) {
        int taskHours = taskRepository.sumWorkHoursBySubproject(task.getSubprojectId());
        subprojectRepository.updateWorkHours(task.getSubprojectId(), taskHours);

        Integer projectId = subprojectRepository.findProjectIdBySubprojectId(task.getSubprojectId());
        if (projectId != null) {
            int subprojectHours = subprojectRepository.sumWorkHoursByProject(projectId);
            projectRepository.updateWorkHours(projectId, subprojectHours);
        }
    }

    public void updateEstimatedDeadline(Task task) {
        int expected = task.getDuration();
        int actual = task.getWorkHours();
        LocalDate baseDeadline = task.getDeadline();
        LocalDate estDeadline;

        if (actual > expected) {
            int extra = actual - expected;
            int extraDays = (int) Math.ceil(extra / 6.0);
            estDeadline = baseDeadline.plusDays(extraDays);
        } else {
            estDeadline = baseDeadline;
        }

        // Prevent from being set in the past
        if (estDeadline.isBefore(LocalDate.now())) {
            estDeadline = LocalDate.now();
        }
        task.setEstDeadline(estDeadline);
        Subproject subproject = subprojectRepository.findById(task.getSubprojectId());
        Project project = projectRepository.findById(subproject.getProjectId());
        taskRepository.updateEstimatedDeadline(task.getId(), estDeadline);
        subprojectService.updateEstimatedDeadline(subproject);
        projectService.updateEstimatedDeadline(project);
    }


}

