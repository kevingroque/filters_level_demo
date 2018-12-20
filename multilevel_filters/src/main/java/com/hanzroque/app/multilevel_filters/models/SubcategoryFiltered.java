package com.hanzroque.app.multilevel_filters.models;

public class SubcategoryFiltered {

    private String subcategoryId;
    private String subcategoryName;
    private String categoryId;

    public SubcategoryFiltered() {
    }

    public SubcategoryFiltered(String subcategoryId, String subcategoryName, String categoryId) {
        this.subcategoryId = subcategoryId;
        this.subcategoryName = subcategoryName;
        this.categoryId = categoryId;
    }

    public String getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(String subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
