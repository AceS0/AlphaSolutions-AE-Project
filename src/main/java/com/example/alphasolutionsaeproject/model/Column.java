package com.example.alphasolutionsaeproject.model;


public class Column {
    private int id;
    private int subprojectId;
    private String title;
    private int position;

    public Column() {
    }

    public Column(int id, int subprojectId, String title, int position) {
        this.id = id;
        this.subprojectId = subprojectId;
        this.title = title;
        this.position = position;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}


