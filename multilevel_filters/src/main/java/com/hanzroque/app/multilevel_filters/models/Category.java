package com.hanzroque.app.multilevel_filters.models;

import com.hanzroque.app.multilevel_filters.interfaces.Constants;
import com.hanzroque.app.multilevel_filters.interfaces.ItemType;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable , ItemType {

    private String id;
    private String name;
    private ArrayList<Subcategory> subcategories;
    private boolean selected;

    public Category() {
    }

    public Category(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Category(String id, String name, boolean selected){
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    public Category(String id, String name, ArrayList<Subcategory> subcategories, boolean selected) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", subcategories=" + subcategories +
                ", selected=" + selected +
                '}';
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.NORMAL_TYPE;
    }
}
