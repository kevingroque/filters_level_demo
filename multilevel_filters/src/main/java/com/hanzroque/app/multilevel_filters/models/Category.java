package com.hanzroque.app.multilevel_filters.models;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Category implements Serializable{

    private String id;
    private String name;
    private ArrayList<Subcategory> subcategories;
    private boolean switchExist;

    public Category(String id, String name, ArrayList<Subcategory> subcategories, boolean switchExist) {
        this.id = id;
        this.name = name;
        this.subcategories = subcategories;
        this.switchExist = switchExist;
    }

    public Category(String id, String name, boolean switchExist) {
        this.id = id;
        this.name = name;
        this.switchExist = switchExist;
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

    public boolean isSwitchExist() {
        return switchExist;
    }

    public void setSwitchExist(boolean switchExist) {
        this.switchExist = switchExist;
    }

}
