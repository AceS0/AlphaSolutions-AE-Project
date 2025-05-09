package com.example.alphasolutionsaeproject.controller;
import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.alphasolutionsaeproject.service.TaskService;

import java.util.List;

@Controller
@RequestMapping("")
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("email") != null;
    }

    // 1. Vis alle tasks
    @GetMapping("projects/{pid}/subprojects/{spid}/tasks")
    public String listTasks(@PathVariable int pid, @PathVariable int spid, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }
        String mail = (String) session.getAttribute("email");
        User user = userService.getUserByMail(mail);
        List<Task> tasks = taskService.getAllTasksBySubProjectId(spid);
        model.addAttribute("tasks", tasks);

        if (user.getRole().equals(Role.EMPLOYEE)) {
            return "Employee/tasksEmployee";
        }

        if (user.getRole().equals(Role.PM)) {
            return "PM/tasksPM";
        }

        return "Admin/tasksAdmin";
    }

    // 2. Vis form for at tilføje en task
    @GetMapping("projects/{pid}/subprojects/{spid}/tasks/add")
    public String showAddForm(@PathVariable int pid, @PathVariable int spid, Model model, HttpSession session) {
        if (!isLoggedIn(session)){
            return "redirect:/users/login";
        }

        model.addAttribute("task", new Task());
        return "CommonProjects/addTask";
    }

    // 3. Gem ny task
    @PostMapping("projects/{pid}/subprojects/{spid}/tasks/save")
    public String addTask(@ModelAttribute("task") Task task, @PathVariable int pid, @PathVariable int spid) {
        taskService.addTask(task,spid);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
    }

    // 4. Vis form for at redigere en task
    @GetMapping("projects/{pid}/subprojects/{spid}/tasks/edit/{tid}")
    public String showEditForm(@PathVariable String pid, @PathVariable String spid, @PathVariable int tid, Model model) {
        Task task = taskService.getTaskById(tid);
        model.addAttribute("task", task);
        return "editTask"; // Thymeleaf side: editTask.html
    }

    // 5. Gem ændringer på eksisterende task
    @PostMapping("projects/{pid}/subprojects/{spid}/tasks/edit/{tid}")
    public String editTask(@PathVariable String pid, @PathVariable String spid, @PathVariable int tid, @ModelAttribute Task task) {
        task.setId(tid);
        taskService.updateTask(task);
        return "redirect:/tasks";
    }

    // 6. Slet en task
    @GetMapping("projects/{pid}/subprojects/{spid}/tasks/delete/{tid}")
    public String deleteTask(@PathVariable int pid, @PathVariable int spid, @PathVariable int tid) {
        taskService.deleteTask(tid);
        return "redirect:/tasks";
    }
}

