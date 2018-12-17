package com.hanzroque.app.multilevel_filters.models;

import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;

import java.io.Serializable;

public class Subcategory implements Serializable, FilterListener {
    private String id;
    private String categoryId;
    private String name;
    private boolean selected;

    public Subcategory() {
    }

    public Subcategory(String id, String categoryId,String name, boolean selected) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
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
                ", selected=" + selected +
                '}';
    }

    @Override
    public void cleanListFilters() {
        setSelected(false);
    }
}
