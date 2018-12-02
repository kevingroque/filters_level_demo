package com.hanzroque.app.multilevel_filters.models;

import java.util.ArrayList;

public class Category {

    private String id;
    private String name;
    private ArrayList<Subcategory> subcategories;

    public Category() {
    }

    public Category(String id, String name, ArrayList<Subcategory> subcategories) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
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

    public ArrayList<Subcategory> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<Subcategory> subcategories) {
        this.subcategories = subcategories;
    }
}
