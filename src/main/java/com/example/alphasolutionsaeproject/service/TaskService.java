package com.example.alphasolutionsaeproject.service;


import com.example.alphasolutionsaeproject.model.Subproject;
import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.model.User;
import org.springframework.stereotype.Service;
import com.example.alphasolutionsaeproject.repository.TaskRepository;

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

    public void addTask(Task task) {
        taskRepository.save(task);
    }

    public void updateTask(Task task) {
        taskRepository.update(task);
    }

    public void deleteTask(int tid ) {
        taskRepository.delete(tid);
    }

    public List<Task> getAllTasksBySubProjectId(int id){
        return taskRepository.getAllTasksBySpid(id);
    }
}

