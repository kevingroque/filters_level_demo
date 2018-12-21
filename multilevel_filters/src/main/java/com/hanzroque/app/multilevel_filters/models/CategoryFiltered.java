package com.hanzroque.app.multilevel_filters.models;

import java.util.List;

public class CategoryFiltered {

    private String categoryId;
    private String categoryName;
    private List<SubcategoryFiltered> subcategoriesSelected;
    private boolean switchStatus;

    public CategoryFiltered() {}

    public CategoryFiltered(String categoryId, String categoryName, List<SubcategoryFiltered> subcategoriesSelected, boolean switchStatus) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.subcategoriesSelected = subcategoriesSelected;
        this.switchStatus = switchStatus;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public List<SubcategoryFiltered> getSubcategoriesSelected() {
        return subcategoriesSelected;
    }

    public void setSubcategoriesSelected(List<SubcategoryFiltered> subcategoriesSelected) {
        this.subcategoriesSelected = subcategoriesSelected;
    }

    public boolean isSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(boolean switchStatus) {
        this.switchStatus = switchStatus;
    }
}


