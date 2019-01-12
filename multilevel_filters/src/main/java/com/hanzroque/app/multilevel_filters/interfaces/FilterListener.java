package com.hanzroque.app.multilevel_filters.interfaces;

import com.hanzroque.app.multilevel_filters.models.Category;

import java.util.List;

public interface FilterListener {

    String URL_API_PRODUCTS = "http://www.alertahorro.com/ws/api/buscar_productos";

    void filtrar(List<Category> categoriesFilteredList) ;

    void cleanFilters();
}