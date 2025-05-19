package com.example.alphasolutionsaeproject.model;

import java.util.List;

public class TaskUser {

    private Task task;
    private List<User> unassigned;
    private List<User> assigned;

    public TaskUser(Task task, List<User> unassigned, List<User> assigned) {
        this.task = task;
        this.unassigned = unassigned;
        this.assigned = assigned;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<User> getUnassigned() {
        return unassigned;
    }

    public void setUnassigned(List<User> unassigned) {
        this.unassigned = unassigned;
    }

    public List<User> getAssigned() {
        return assigned;
    }

    public void setAssigned(List<User> assigned) {
        this.assigned = assigned;
    }


}
