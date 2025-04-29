package com.example.alphasolutionsaeproject.model;

public class Project {

    private int id;
    private String title;
    private String description;
    private String deadline;
    private int duration;
    private int createdBy;
    private boolean checked;


    public Project() {
    }

    public Project(int id, String title, String description, String deadline, int duration, int createdBy, boolean checked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.duration = duration;
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
}
