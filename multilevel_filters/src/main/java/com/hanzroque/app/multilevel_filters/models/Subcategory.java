package com.hanzroque.app.multilevel_filters.models;

import java.io.Serializable;

public class Subcategory implements Serializable {
    private String id;
    private String categoryId;
    private String name;
    private int docCount;
    private boolean selected;

    public Subcategory() {
    }

    public Subcategory(String id, String categoryId,String name, int docCount, boolean selected) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.docCount = docCount;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDocCount() {
        return docCount;
    }

    public void setDocCount(int docCount) {
        this.docCount = docCount;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Subcategory{" +
                "id='" + id + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", docCount=" + docCount +
                ", selected=" + selected +
                '}';
    }
}
