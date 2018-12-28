package com.hanzroque.app.multilevel_filters.interfaces;

import com.hanzroque.app.multilevel_filters.models.CategoryFiltered;
import com.hanzroque.app.multilevel_filters.models.SubcategoryFiltered;

import java.util.List;

public interface FilterListener {
    String URL_API_CATEGORIES = "https://api.mercadolibre.com/sites/MLA/categories";
    String URL_API_SUBCATEGORIES = "https://api.mercadolibre.com/categories/";

    String URL_API_PRODUCTS = "http://www.alertahorro.com/ws/api/buscar_productos";

    public void filtrar(List<CategoryFiltered> categoriesFilteredList) ;
}