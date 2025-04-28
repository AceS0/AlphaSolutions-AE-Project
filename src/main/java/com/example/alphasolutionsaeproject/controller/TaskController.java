package com.example.alphasolutionsaeproject.controller;
import com.example.alphasolutionsaeproject.model.Task;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.alphasolutionsaeproject.service.TaskService;

import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // 1. Vis alle tasks
    @GetMapping
    public String listTasks(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "tasks"; // Thymeleaf side: tasks.html
    }

    // 2. Vis form for at tilføje en task
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("task", new Task());
        return "addTask"; // Thymeleaf side: addTask.html
    }

    // 3. Gem ny task
    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task) {
        taskService.addTask(task);
        return "redirect:/tasks";
    }

    // 4. Vis form for at redigere en task
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "editTask"; // Thymeleaf side: editTask.html
    }

    // 5. Gem ændringer på eksisterende task
    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable int id, @ModelAttribute Task task) {
        task.setId(id);
        taskService.updateTask(task);
        return "redirect:/tasks";
    }

    // 6. Slet en task
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }
}

