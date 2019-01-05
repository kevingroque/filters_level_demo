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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.adapters.ProductAdapter;
import com.hanzroque.app.multilevel_filters.fragments.CategoryFragment;
import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.CategoryFiltered;
import com.hanzroque.app.multilevel_filters.models.Product;
import com.hanzroque.app.multilevel_filters.models.Subcategory;
import com.hanzroque.app.multilevel_filters.models.SubcategoryFiltered;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterListener {

    private ArrayList<Category> mCategoryList = new ArrayList<>();
    private Category mCategory;

    private RecyclerView mProductsRecycler;
    private List<Product> mProductList;
    private ProductAdapter mProductAdapter;

    private EditText mSearchString;
    private ImageButton mSearchBtn;
    private TextView mCounterProducts;
    private String wordToSearch = null;

    private DrawerLayout mDrawer;

    private JSONObject jsonObjectSearch;
    private JSONObject jsonObjectFilter = new JSONObject();

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
        mProductsRecycler = (RecyclerView) findViewById(R.id.rv_main_products);

        mCategoryList = new ArrayList<>();

        mProductList = new ArrayList<>();
        mProductAdapter = new ProductAdapter(this, mProductList);
        mProductsRecycler.setHasFixedSize(true);
        mProductsRecycler.setLayoutManager(new GridLayoutManager(this, 2));
        mProductsRecycler.setAdapter(mProductAdapter);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProductList.clear();
                mCategoryList.clear();
                mProductAdapter.notifyDataSetChanged();

                jsonObjectSearch = new JSONObject();
                wordToSearch = mSearchString.getText().toString();

                try {
                    if (!TextUtils.isEmpty(mSearchString.getText())) {
                        jsonObjectSearch.put("string_search", wordToSearch);
                        jsonObjectSearch.put("num_pag", 1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                getProducts(jsonObjectSearch);

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

    @Override
    public void filtrar(List<CategoryFiltered> categoriesFilteredList) {

        mProductList.clear();
        mCategoryList.clear();
        mProductAdapter.notifyDataSetChanged();

        JSONArray jShops = jSonQueryText(categoriesFilteredList, "contador_tiendas" );
        JSONArray jBrands = jSonQueryText(categoriesFilteredList, "contador_marcas");

        try {
            jsonObjectFilter.put("string_search", wordToSearch);
            jsonObjectFilter.put("num_pag", 1);
            jsonObjectFilter.put("orden_busqueda", "precio_menor");
            jsonObjectFilter.put("precio_minimo", 0);
            jsonObjectFilter.put("precio_maximo", 5000);
            if (jShops.length() != 0)
                jsonObjectFilter.put("tienda", jShops.toString());

            if (jBrands.length() != 0)
                jsonObjectFilter.put("marca", jBrands.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        getProducts(jsonObjectFilter);

        closeDrawer();
    }

    //Obteber productos
    private void getProducts(JSONObject json) {

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                FilterListener.URL_API_PRODUCTS,
                json, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArrayProducts = null;
                try {
                    jsonArrayProducts = response.getJSONArray("lista_productos");
                    for (int i = 0; i < jsonArrayProducts.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArrayProducts.get(i);
                        Product product = new Product();
                        product.setTitleProduct(jsonObject.getString("titulo"));
                        product.setMarca(jsonObject.getString("marca"));
                        product.setShop(jsonObject.getString("tienda"));
                        product.setCoverPhoto(jsonObject.getJSONArray("foto_portada"));

                        mProductList.add(product);
                    }

                    JSONObject jCategories = response.getJSONObject("contadores_filtros");
                    for (int c = 0; c < jCategories.length(); c++){
                        mCategory = new Category();
                        mCategory.setId("AA00"+c);
                        mCategory.setName(jCategories.names().get(c).toString());
                        mCategory.setCategoryType("NORMAL_TYPE");

                        mCategoryList.add(mCategory);
                    }

                    mCounterProducts.setText(response.getString("cant") + " productos");

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


    private JSONArray jSonQueryText(List<CategoryFiltered> categoriesFiltered, String categoryName){
        JSONArray jsonArray = new JSONArray();

        for (CategoryFiltered categoryFiltered : categoriesFiltered) {
            for (SubcategoryFiltered subcategoryFiltered : categoryFiltered.getSubcategoriesSelected()) {
                if (categoryFiltered.getCategoryId().compareTo(subcategoryFiltered.getCategoryId()) == 0) {
                    if (categoryFiltered.getCategoryName().equals(categoryName)){
                        jsonArray.put(subcategoryFiltered.getSubcategoryName());
                    }
                }
            }
        }

        return jsonArray;
    }


    @Override
    public String wordToSearch() {
        return wordToSearch;
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
