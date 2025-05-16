package com.example.alphasolutionsaeproject.model;

import java.time.LocalDate;

public class Subproject {
    private int id;
    private int projectId;
    private String title;
    private int priority;
    private LocalDate deadline;
    private LocalDate estDeadline;
    private int duration;
    private int workHours;
    private boolean checked;

    public Subproject() {
    }

    public Subproject(int id, int projectId, String title, int priority,
                      LocalDate deadline, LocalDate estDeadline, int duration, int workHours, boolean checked) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.priority = priority;
        this.deadline = deadline;
        this.estDeadline = estDeadline;
        this.duration = duration;
        this.workHours = workHours;
        this.checked = checked;
    }

    // Getters og setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDate getEstDeadline() {
        return estDeadline;
    }

    public void setEstDeadline(LocalDate estDeadline) {
        this.estDeadline = estDeadline;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getWorkHours() {
        return workHours;
    }

    public void setWorkHours(int workHours) {
        this.workHours = workHours;
    }

    public int getDifferenceInHours() {
        return duration - workHours;
    }

    public boolean isOverWorked() {
        return workHours > duration;
    }
}

