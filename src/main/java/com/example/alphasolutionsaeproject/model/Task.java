package com.example.alphasolutionsaeproject.model;


public class Task {
    private int id;
    private int subprojectId;
    private String title;
    private String description;
    private String deadline;
    private int duration;
    private String status;
    private String priority;
    private boolean checked;
    private String assigned;

    public Task() {
    }

    public Task(int id, int subprojectId, String title, String description, String deadline, int duration, String status, String priority, boolean checked) {
        this.id = id;
        this.subprojectId = subprojectId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.duration = duration;
        this.status = status;
        this.priority = priority;
        this.checked = checked;
    }

    // Getters og setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubprojectId() {
        return subprojectId;
    }

    public void setSubprojectId(int subprojectId) {
        this.subprojectId = subprojectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }
}
