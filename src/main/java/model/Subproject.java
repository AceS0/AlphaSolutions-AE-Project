package model;

public class Subproject {
    private int id;
    private int projectId;
    private String title;

    public Subproject() {
    }

    public Subproject(int id, int projectId, String title) {
        this.id = id;
        this.projectId = projectId;
        this.title = title;
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
}

