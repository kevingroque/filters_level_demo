package com.hanzroque.app.multilevel_filters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.adapters.CategoryAdapter;
import com.hanzroque.app.multilevel_filters.fragments.CategoryFragment;
import com.hanzroque.app.multilevel_filters.fragments.SubcategoryFragment;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private static final String URL_API_CATEGORIES = "https://api.mercadolibre.com/sites/MLA/categories";

    private String mIdCategory;
    private ArrayList<Category> mCategoryList = new ArrayList<>();

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

        getCategoriesData();

        CategoryFragment fragment = CategoryFragment.newInstance(mCategoryList);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment)
                .commit();
    }

    //Obtener las categorias
    private void getCategoriesData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL_API_CATEGORIES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Category category = new Category();
                        mIdCategory = jsonObject.getString("id");

                        category.setName(jsonObject.getString("name"));
                        category.setId(mIdCategory);

                        mCategoryList.add(category);

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
    }

    //Cerrar filtros -----------------------------------------------------------------
    public void Done(View view){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void Clear(View view){
        mCategoryList.clear();
        getCategoriesData();
    }
}
