package com.hanzroque.app.multilevel_filters.interfaces;

import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.util.ArrayList;

public interface FilterListener {
    String URL_API_CATEGORIES = "https://api.mercadolibre.com/sites/MLA/categories";
    String URL_API_SUBCATEGORIES = "https://api.mercadolibre.com/categories/";

    void cleanListFilters();
}
