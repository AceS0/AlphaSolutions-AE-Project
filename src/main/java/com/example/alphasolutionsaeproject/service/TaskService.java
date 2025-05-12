package com.example.alphasolutionsaeproject.service;


import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.model.TaskUser;
import com.example.alphasolutionsaeproject.model.User;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(int id) {
        return taskRepository.findById(id);
    }

    public void addTask(Task task, int spid) {
        taskRepository.save(task, spid);
    }

    public void updateTask(Task task) {
        taskRepository.update(task);
    }

    public void deleteTask(int tid ) {
        taskRepository.delete(tid);
    }


    public void assignUserToTask(int taskId, int userId){
        taskRepository.assignUserToTask(taskId,userId);
    }

    public void unassignUserToTask(int taskId, int userId){
        taskRepository.unassignUserToTask(taskId,userId);
    }

    public List<User> getUsersUnassignedTo(int taskId){
        return taskRepository.getUsersUnassignedTo(taskId);
    }
    public List<User> getUsersAssignedTo(int taskId){
        return taskRepository.getUsersAssignedTo(taskId);
    }

    public List<TaskUser> getTaskUser(int spid){
        List<TaskUser> taskUserList = new ArrayList<>();
        List<Task> taskList = taskRepository.getAllTasksBySpid(spid);

        for (Task task : taskList){
            TaskUser taskUser = new TaskUser(task,
                    taskRepository.getUsersAssignedTo(task.getId()),
                    taskRepository.getUsersUnassignedTo(task.getId()));
            taskUserList.add(taskUser);
        }
        return taskUserList;
    }


    public List<TaskUser> getAllTasksBySubProjectId(int spid){
        return getTaskUser(spid);
    }
}

