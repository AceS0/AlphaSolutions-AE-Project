package com.example.alphasolutionsaeproject.controller;

import com.example.alphasolutionsaeproject.model.Role;
import com.example.alphasolutionsaeproject.model.Task;
import com.example.alphasolutionsaeproject.model.TaskUser;
import com.example.alphasolutionsaeproject.model.User;
import com.example.alphasolutionsaeproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.alphasolutionsaeproject.service.TaskService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private boolean redirectEmployee(HttpSession session){
        String mail = (String) session.getAttribute("email");
        User me = userService.getUserByMail(mail);
        return me.getRole().equals(Role.EMPLOYEE);
    }

    @GetMapping("/projects/{pid}/subprojects/{spid}/tasks")
    public String listTasks(@PathVariable int pid, @PathVariable int spid,
                            @RequestParam(value = "subprojectName", required = false) String subprojectName,
                            @RequestParam(value = "projectName", required = false) String projectName,
                            Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }

        if (projectName != null) {
            session.setAttribute("projectName", projectName);
        }
        if (subprojectName != null) {
            session.setAttribute("subprojectName", subprojectName);
        }
        String mail = (String) session.getAttribute("email");
        User user = userService.getUserByMail(mail);
        List<TaskUser> tasks = taskService.getAllTasksBySubProjectId(spid);

        Map<Integer, List<User>> assignedUsersMap = new HashMap<>();
        for (TaskUser taskUser : tasks) {
            int taskId = taskUser.getTask().getId();
            List<User> assignedUsers = taskService.getUsersAssignedTo(taskId);
            assignedUsersMap.put(taskId, assignedUsers);
        }

        Map<Integer, List<Integer>> assignedUserIdsMap = new HashMap<>();
        for (Map.Entry<Integer, List<User>> entry : assignedUsersMap.entrySet()) {
            List<Integer> userIds = entry.getValue().stream()
                    .map(User::getId)
                    .toList();
            assignedUserIdsMap.put(entry.getKey(), userIds);
        }

        model.addAttribute("projectName", session.getAttribute("projectName"));
        model.addAttribute("subprojectName", session.getAttribute("subprojectName"));
        model.addAttribute("assignedUserIdsMap", assignedUserIdsMap);
        model.addAttribute("currentUser", user);
        model.addAttribute("assignedUsersMap", assignedUsersMap);
        model.addAttribute("tasks", tasks);

        if (user.getRole().equals(Role.EMPLOYEE)) {
            return "Employee/tasksEmployee";
        }

        return "CommonProjects/tasks";
    }

    @GetMapping("/projects/{pid}/subprojects/{spid}/tasks/add")
    public String showAddForm(@PathVariable int pid, @PathVariable int spid, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }
        if (redirectEmployee(session)) {
            return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
        }

        model.addAttribute("task", new Task());
        return "CommonProjects/addTask";
    }

    @PostMapping("/projects/{pid}/subprojects/{spid}/tasks/save")
    public String addTask(@ModelAttribute("task") Task task, @PathVariable int pid, @PathVariable int spid) {
        taskService.addTask(task, spid);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
    }

    @GetMapping("/projects/{pid}/subprojects/{spid}/tasks/edit/{tid}")
    public String showEditForm(@PathVariable String pid, @PathVariable String spid, @PathVariable int tid, Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }
        if (redirectEmployee(session)) {
            return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
        }
        Task task = taskService.getTaskById(tid);
        model.addAttribute("task", task);
        return "CommonProjects/editTask";
    }

    @PostMapping("/projects/{pid}/subprojects/{spid}/tasks/edit/{tid}")
    public String editTask(@PathVariable String pid, @PathVariable String spid, @PathVariable int tid, @ModelAttribute Task task) {
        task.setId(tid);
        task.setChecked(false);
        taskService.updateTask(task);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
    }

    @PostMapping("/projects/{pid}/subprojects/{spid}/tasks/delete/{tid}")
    public String deleteTask(@PathVariable int pid, @PathVariable int spid, @PathVariable int tid) {
        taskService.deleteTask(tid);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
    }

    @GetMapping("/projects/{pid}/subprojects/{spid}/tasks/{tid}/assign")
    public String showAssignUsersForm(@PathVariable int pid, @PathVariable int spid,
                                      @PathVariable int tid,
                                      Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }
        if (redirectEmployee(session)) {
            return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
        }

        List<User> users = taskService.getUsersUnassignedTo(tid);
        model.addAttribute("users", users);
        return "CommonProjects/assignUserTask";
    }

    @PostMapping("/projects/{pid}/subprojects/{spid}/tasks/{tid}/assign/{userId}")
    public String assignTask(@PathVariable int pid, @PathVariable int spid,
                             @PathVariable("userId") int userId,
                             @PathVariable("tid") int taskId) {
        taskService.assignUserToTask(taskId, userId);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks/{tid}/assign";
    }

    @GetMapping("/projects/{pid}/subprojects/{spid}/tasks/{tid}/unassign")
    public String showUnassignUsersForm(@PathVariable int pid, @PathVariable int spid,
                                        @PathVariable int tid,
                                        Model model, HttpSession session) {
        if (!isLoggedIn(session)) {
            return "redirect:/users/login";
        }

        if (redirectEmployee(session)) {
            return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
        }

        List<User> users = taskService.getUsersAssignedTo(tid);
        model.addAttribute("users", users);
        return "CommonProjects/unassignUserTask";
    }

    @PostMapping("/projects/{pid}/subprojects/{spid}/tasks/{tid}/unassign/{userId}")
    public String unassignTask(@PathVariable int pid, @PathVariable int spid,
                               @PathVariable("userId") int userId,
                               @PathVariable("tid") int taskId) {
        taskService.unassignUserFromTaskAndProject(taskId, userId);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks/{tid}/unassign";
    }

    @PostMapping("/projects/{pid}/subprojects/{spid}/tasks/toggleChecked/{tid}")
    public String toggleTasksChecked(@PathVariable int pid, @PathVariable int spid, @PathVariable int tid) {
        taskService.toggleCheckedAndCascadeUp(tid);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
    }

    @PostMapping("/projects/{pid}/subprojects/{spid}/tasks/updateWorkHours")
    public String updateWorkHours(@PathVariable int pid, @PathVariable int spid,
                                  @RequestParam int taskId,
                                  @RequestParam int workHours) {
        taskService.updateWorkHours(taskId, workHours);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
    }

    @PostMapping("/projects/{pid}/subprojects/{spid}/tasks/updateStatus")
    public String updateStatus(@PathVariable int pid, @PathVariable int spid,
                                  @RequestParam int taskId,
                                  @RequestParam String status) {
        taskService.updateStatus(taskId, status);
        return "redirect:/projects/{pid}/subprojects/{spid}/tasks";
    }
}

