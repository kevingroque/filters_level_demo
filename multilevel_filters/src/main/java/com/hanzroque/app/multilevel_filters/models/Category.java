package com.hanzroque.app.multilevel_filters.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable{

    private String id;
    private String name;
    private List<Subcategory> subcategories;
    private String categoryType;

    public Category() {}

    public Category(String id, String name, List<Subcategory> subcategories, String categoryType) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
        this.categoryType = categoryType;
    }

    public Category(String id, String name, ArrayList<Subcategory> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
    }

    public Category(String id, String name, String categoryType) {
        this.id = id;
        this.name = name;
        this.categoryType = categoryType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }
}
