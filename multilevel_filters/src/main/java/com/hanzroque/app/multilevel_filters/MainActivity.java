package com.hanzroque.app.multilevel_filters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hanzroque.app.multilevel_filters.adapters.CategoryAdapter;
import com.hanzroque.app.multilevel_filters.fragments.CategoryFragment;
import com.hanzroque.app.multilevel_filters.localdata.CategoryRepository;
import com.hanzroque.app.multilevel_filters.models.Category;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private String mIdCategory;
    private ArrayList<Category> mCategoryList = new ArrayList<>();
    private Category mCategory;

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

        mCategoryList = (ArrayList<Category>) CategoryRepository.getCategories();
        //getCategoriesData();

        CategoryFragment fragment = CategoryFragment.newInstance(mCategoryList);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment)
                .commit();
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

    //Cerrar filtros -----------------------------------------------------------------
    public void Done(View view){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


}
