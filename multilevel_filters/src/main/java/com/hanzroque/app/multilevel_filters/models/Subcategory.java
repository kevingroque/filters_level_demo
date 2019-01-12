package com.hanzroque.app.multilevel_filters.models;

import java.io.Serializable;

public class Subcategory implements Serializable {
    private String id;
    private String categoryId;
    private String name;
    private String subcategoryType;
    private int docCount;
    private boolean selected;

    public Subcategory() {
    }

    public Subcategory(String id, String categoryId,String name, String subcategoryType,int docCount, boolean selected) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.subcategoryType = subcategoryType;
        this.docCount = docCount;
        this.selected = selected;
    }

    public Subcategory(String id, String categoryId, String name, String subcategoryType) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.subcategoryType = subcategoryType;
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

    public String getSubcategoryType() {
        return subcategoryType;
    }

    public void setSubcategoryType(String subcategoryType) {
        this.subcategoryType = subcategoryType;
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
                ", subcategoryType='" + subcategoryType + '\'' +
                ", docCount=" + docCount +
                ", selected=" + selected +
                '}';
    }
}
