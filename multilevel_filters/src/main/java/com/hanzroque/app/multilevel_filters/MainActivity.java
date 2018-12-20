package com.hanzroque.app.multilevel_filters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hanzroque.app.multilevel_filters.fragments.CategoryFragment;
import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.localdata.CategoryRepository;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FilterListener {


    private String mIdCategory;
    private ArrayList<Category> mCategoryList = new ArrayList<>();
    private ArrayList<Subcategory> subcategories = new ArrayList<>();

    private Category mCategory;

    private TextView textView;

    public static MainActivity INSTANCE;

    public MainActivity() {
        INSTANCE = this;
    }

    public ArrayList<Category> getCategoryList() {
        return mCategoryList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView = (TextView) findViewById(R.id.txt_main_result) ;

        mCategoryList = (ArrayList<Category>) CategoryRepository.getCategories();
        //getCategoriesData();

        //Initialize fragment
        CategoryFragment fragment = CategoryFragment.newInstance(mCategoryList);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment)
                .commit();

        fragment.setFilterListener(this);
    }

    //Obtener las categorias
    /*private void getCategoriesData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(FilterListener.URL_API_CATEGORIES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        mCategory = new Category();
                        mIdCategory = jsonObject.getString("id");

                        mCategory.setName(jsonObject.getString("name"));
                        mCategory.setId(mIdCategory);

                        mCategoryList.add(mCategory);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    } */

    @Override
    public void filtrar() {

        for (Category category : mCategoryList) {
            if (category.getSubcategories() != null) {
                for (Subcategory subcategory : category.getSubcategories()) {
                    if (subcategory.isSelected()) {
                        subcategories.add(subcategory);
                    }else {
                        subcategories.remove(subcategory);
                    }
                }
            }
        }

        if (subcategories.size() == 0){
            textView.setText("No filters");
        }else {
            textView.setText(subcategories.toString());
        }


        Log.d("FILTROS S", "Filtros: "+ subcategories);
        closeDrawer();
    }

    public void closeDrawer(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
