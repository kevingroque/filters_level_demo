package com.hanzroque.app.multilevel_filters.localdata;

import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    private static List<Category> mCategories;
    private static ArrayList<Subcategory> mSubcategories;

    static {
        mCategories = new ArrayList<>();
        mCategories.add(new Category("MLA1403","Alimentos y bebidas", false));
        mCategories.add(new Category("MLA1071","Animales y mascotas", false));
        mCategories.add(new Category("MLA1648","Computación", false));
        mCategories.add(new Category("MLA1144","Consolas y videojuegos", false));
        mCategories.add(new Category("MLA1459","Inmuebles", false));
        mCategories.add(new Category("MLA1430","Ropa y Accesorios", false));
        mCategories.add(new Category("MMM0001", "Articulos vendidos", true));
        mCategories.add(new Category("MMM0002", "Articulos vendidos", true));
        mCategories.add(new Category("MMM0003", "Articulos vendidos", true));

        mSubcategories = new ArrayList<>();
        mSubcategories.add(new Subcategory("MLA417030","MLA1403", "Bebidas alcohólicas preparadas", false));
        mSubcategories.add(new Subcategory("MLA1423","MLA1403", "Comestibles", false));
        mSubcategories.add(new Subcategory("MLA1100","MLA1071", "Aves", false));
        mSubcategories.add(new Subcategory("MLA1117","MLA1071", "Caballos", false));
        mSubcategories.add(new Subcategory("MLA1870","MLA1648", "Apple", false));
        mSubcategories.add(new Subcategory("MLA3794","MLA1648", "Componentes de PC", false));
        mSubcategories.add(new Subcategory("MLA373768","MLA1144", "Nintendo", false));
        mSubcategories.add(new Subcategory("MLA373769","MLA1144", "PlayStation", false));
        mSubcategories.add(new Subcategory("MLA1466","MLA1459", "Casas", false));
        mSubcategories.add(new Subcategory("MLA1472","MLA1459", "Departamentos", false));
        mSubcategories.add(new Subcategory("MLA109276","MLA1430", "Bermudas y Shorts", false));
        mSubcategories.add(new Subcategory("MLA417226","MLA1430", "Blusas", false));

    }

    public static List<Category> getCategories(){
        return mCategories;
    }

    public static List<Subcategory> getAllSubcategories(){ return mSubcategories; }

    public static List<Subcategory> getSubcategoriesByCategoryId(String categoryId){
        ArrayList<Subcategory> subcategoriesByCategoryId = new ArrayList<>();
        for (Subcategory subcategory : mSubcategories){
            if (subcategory != null){
                if (subcategory.getCategoryId().equals(categoryId)){
                    subcategoriesByCategoryId.add(subcategory);
                }
            }
        }
        return subcategoriesByCategoryId;
    }

}
