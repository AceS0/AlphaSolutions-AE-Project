package com.example.alphasolutionsaeproject.model;

import java.time.LocalDate;

public class Project {

    private int id;
    private String title;
    private String description;
    private LocalDate deadline;
    private LocalDate estDeadline;
    private int duration;
    private int workHours;
    private int createdBy;
    private boolean checked;
    private String projectManager;


    public Project() {
    }


    public Project(int id, String title, String description, LocalDate deadline, LocalDate estDeadline, int duration, int workHours, int createdBy, boolean checked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.estDeadline = estDeadline;
        this.duration = duration;
        this.workHours = workHours;
        this.createdBy = createdBy;
        this.checked = checked;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
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
