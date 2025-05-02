package com.example.alphasolutionsaeproject.model;

public class Subproject {
    private int id;
    private int projectId;
    private String title;
    private int priority;
    private String deadline;
    private int duration;
    private boolean checked;

    public Subproject() {
    }


    public Subproject(int id, int projectId, String title, int priority,
                      String deadline, int duration, boolean checked) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
        this.priority = priority;
        this.deadline = deadline;
        this.duration = duration;
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

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}

