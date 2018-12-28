package com.hanzroque.app.multilevel_filters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.adapters.ProductAdapter;
import com.hanzroque.app.multilevel_filters.fragments.CategoryFragment;
import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.localdata.CategoryRepository;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.CategoryFiltered;
import com.hanzroque.app.multilevel_filters.models.Producto;
import com.hanzroque.app.multilevel_filters.models.SubcategoryFiltered;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarException;

public class MainActivity extends AppCompatActivity implements FilterListener {


    private String mIdCategory;
    private ArrayList<Category> mCategoryList = new ArrayList<>();
    private Category mCategory;

    private RecyclerView mProductsRecycler;
    private List<Producto> mProductoList;
    private ProductAdapter mProductAdapter;

    private EditText mSearchString;
    private ImageButton mSearchBtn;
    private TextView mCounterProducts;

    private DrawerLayout mDrawer;

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

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSearchString = (EditText) findViewById(R.id.edt_main_searchString);
        mSearchBtn = (ImageButton) findViewById(R.id.imgbtn_main_search);
        mCounterProducts = (TextView) findViewById(R.id.txt_main_totalproducts);


        mCategoryList = (ArrayList<Category>) CategoryRepository.getCategories();
        //getCategoriesData();

        mProductoList = new ArrayList<>();
        mProductsRecycler = (RecyclerView) findViewById(R.id.rv_main_products);

        mProductAdapter = new ProductAdapter(this, mProductoList);
        mProductsRecycler.setHasFixedSize(true);
        mProductsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mProductsRecycler.setAdapter(mProductAdapter);


        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductoList.clear();
                mProductAdapter.notifyDataSetChanged();
                getProducts();
            }
        });


        //Initialize fragment
        CategoryFragment fragment = CategoryFragment.newInstance(mCategoryList);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment)
                .commit();

        fragment.setFilterListener(this);
    }


    //Obteber productos
    private void getProducts() {
        final JSONObject json = new JSONObject();
        try {
            if (!TextUtils.isEmpty(mSearchString.getText())) {
                json.put("string_search", mSearchString.getText());
                json.put("num_pag", 1);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String URL_PRODUCTS = FilterListener.URL_API_PRODUCTS;

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL_PRODUCTS, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = response.getJSONArray("lista_productos");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        Producto producto = new Producto();
                        producto.setTitleProduct(jsonObject.getString("titulo"));
                        producto.setMarca(jsonObject.getString("marca"));
                        producto.setShop(jsonObject.getString("tienda"));
                        producto.setCoverPhoto(jsonObject.getJSONArray("foto_portada"));

                        mProductoList.add(producto);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProductAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("PRODUCTS", error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
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
    public void filtrar(List<CategoryFiltered> categoriesFilteredList) {

        JSONArray jArray = new JSONArray();

        try {
            for (CategoryFiltered categoryFiltered : categoriesFilteredList) {
                // Log.d("Categories", categoryFiltered.getCategoryName());

                for (SubcategoryFiltered subcategoryFiltered : categoryFiltered.getSubcategoriesSelected()) {
                    if (categoryFiltered.getCategoryId().compareTo(subcategoryFiltered.getCategoryId()) == 0) {
                        JSONArray jSubcategories = new JSONArray();
                        jSubcategories.put(subcategoryFiltered.getSubcategoryName());

                        JSONObject jCategories = new JSONObject();
                        jCategories.putOpt(categoryFiltered.getCategoryName(), jSubcategories);

                        jArray.put(jCategories);

                        //Log.d("Subcategories", subcategoryFiltered.getSubcategoryName());
                        Log.d("CATEGORIES", jArray.toString());
                    }
                }
            }


        } catch (JSONException e) {
        }

        closeDrawer();
    }

    public void openDrawer(View view) {
        if (!mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.openDrawer(GravityCompat.END);
        }
    }

    public void closeDrawer() {
        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
