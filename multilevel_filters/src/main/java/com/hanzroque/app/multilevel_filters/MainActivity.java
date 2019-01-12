package com.hanzroque.app.multilevel_filters;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hanzroque.app.multilevel_filters.models.Product;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FilterListener {

    private ArrayList<Category> mCategoryList = new ArrayList<>();
    private ArrayList<Subcategory> mSubcategorias;
    private Category mCategory;

    private RecyclerView mProductsRecycler;
    private List<Product> mProductList;
    private ProductAdapter mProductAdapter;

    private EditText mSearchString;
    private ImageButton mSearchBtn;
    private Button mOpenFilters;
    private TextView mCounterProducts;
    private String wordToSearch = null;

    private DrawerLayout mDrawer;

    private JSONObject jsonObjectSearch;
    private JSONObject jsonObjectFilter;

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
        mOpenFilters = (Button) findViewById(R.id.btn_main_openfilters);
        mSearchString = (EditText) findViewById(R.id.edt_main_searchString);
        mSearchBtn = (ImageButton) findViewById(R.id.imgbtn_main_search);
        mCounterProducts = (TextView) findViewById(R.id.txt_main_totalproducts);
        mProductsRecycler = (RecyclerView) findViewById(R.id.rv_main_products);

        mCategoryList = new ArrayList<>();

        mProductList = new ArrayList<>();
        mProductAdapter = new ProductAdapter(this, mProductList);
        mProductsRecycler.setHasFixedSize(true);
        mProductsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mProductsRecycler.setAdapter(mProductAdapter);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoryList.clear();
                wordToSearch = mSearchString.getText().toString();
                searchProducts();
            }
        });

        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                if (mProductList.size() != 0 )
                    initializeFragment();
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        mOpenFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
    }

    //Json for the first search
    private JSONObject jSonFirstSearch(){
        jsonObjectSearch = new JSONObject();
        try {
            if (!TextUtils.isEmpty(mSearchString.getText())) {
                jsonObjectSearch.put("string_search", wordToSearch);
                jsonObjectSearch.put("num_pag", 1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObjectSearch;
    }

    @Override
    public void onBackPressed() {
        closeDrawer();
    }

    //Filter products by selected categories and subcategories
    @Override
    public void filtrar(List<Category> categoriesFilteredList) {

        jsonObjectFilter = new JSONObject();

        mProductList.clear();
        mProductAdapter.notifyDataSetChanged();

        JSONArray jShops = jSonArrayQuery(categoriesFilteredList, "contador_tiendas" );
        JSONArray jBrands = jSonArrayQuery(categoriesFilteredList, "contador_marcas");

        int lowerPrice = getLowerPrices(categoriesFilteredList, "precios_rango_fijo");
        int higherPrice = getHigherPrices(categoriesFilteredList, "precios_rango_fijo");

        try {
            jsonObjectFilter.put("string_search", wordToSearch);
            jsonObjectFilter.put("num_pag", 1);
            jsonObjectFilter.put("orden_busqueda", "precio_menor");
            jsonObjectFilter.put("precio_minimo", lowerPrice);
            if (higherPrice != 0)
                jsonObjectFilter.put("precio_maximo", higherPrice);

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

    //Clean filters
    @Override
    public void cleanFilters() {
        for (Category category : mCategoryList) {
            if (category.getSubcategories() != null) {
                for (Subcategory subcategory : category.getSubcategories()) {
                    subcategory.setSelected(false);
                }
            }
        }

        mProductList.clear();
        getProducts(jSonFirstSearch());
        mProductAdapter.notifyDataSetChanged();

    }

    //Search Products By word
    private void searchProducts(){
        mProductList.clear();
        mProductAdapter.notifyDataSetChanged();

        getProducts(jSonFirstSearch());
        getCategoriesAndSubcategories(jSonFirstSearch());
        hideSoftKeyboard(MainActivity.this);
    }

    //Get products
    private void getProducts(final JSONObject json) {

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
                        product.setLowerPrice(Double.parseDouble(jsonObject.getString("precio_menor")));
                        product.setPreviousPrice(Double.parseDouble(jsonObject.getString("precio_anterior")));
                        product.setCoverPhoto(jsonObject.getJSONArray("foto_portada"));

                        mProductList.add(product);
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

    //Get categories and subcategories
    private void getCategoriesAndSubcategories(JSONObject json){
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                FilterListener.URL_API_PRODUCTS,
                json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Category categoryOrder = new Category();
                    categoryOrder.setId("AA009");
                    categoryOrder.setName("Orden de búsqueda");
                    categoryOrder.setCategoryType("RADIO_TYPE");

                    mCategoryList.add(categoryOrder);

                    JSONObject jCategories = response.getJSONObject("contadores_filtros");

                    for (int c = 0; c < jCategories.length(); c++) {
                        mCategory = new Category();
                        mCategory.setId("AA00" + c);
                        mCategory.setName(jCategories.names().get(c).toString());
                        mCategory.setCategoryType("NORMAL_TYPE");

                        mSubcategorias = new ArrayList<>();

                        if (mCategory.getName().compareTo("precios_rango_fijo") != 0) {
                            JSONArray jsonArrayContadoresFiltros = response.getJSONObject("contadores_filtros").getJSONArray(mCategory.getName());

                            for (int cont = 0; cont < jsonArrayContadoresFiltros.length(); cont++) {
                                JSONObject jSubcategories = (JSONObject) jsonArrayContadoresFiltros.get(cont);

                                Subcategory subcategory = new Subcategory();
                                subcategory.setCategoryId(mCategory.getId());
                                subcategory.setName(jSubcategories.getString("key"));
                                subcategory.setSubcategoryType("MULTIPLESELECTION");
                                subcategory.setDocCount(jSubcategories.getInt("doc_count"));
                                mSubcategorias.add(subcategory);
                            }

                        } else {
                            JSONArray rangosPrecios = response.getJSONObject("contadores_filtros").getJSONObject("precios_rango_fijo").names();

                            for (int i = 0; i < rangosPrecios.length(); i++) {
                                String rangoPrecio = rangosPrecios.getString(i);

                                JSONObject jsonRangoPrecio = response.getJSONObject("contadores_filtros").getJSONObject("precios_rango_fijo").getJSONObject(rangoPrecio);

                                int from = jsonRangoPrecio.getInt("from");
                                int to;
                                int doc_count = jsonRangoPrecio.getInt("doc_count");

                                if (jsonRangoPrecio.has("to"))
                                    to = jsonRangoPrecio.getInt("to");
                                else
                                    to = 0;

                                String nombreCategoria;

                                if (from == 0)
                                    nombreCategoria = "S/.0";
                                else
                                    nombreCategoria = "S/." + from;

                                if (to == 0)
                                    nombreCategoria += " En adelante";
                                else
                                    nombreCategoria += " - S/. " + to;

                                Log.i("alertahorro", nombreCategoria);

                                Subcategory subcategory = new Subcategory();
                                subcategory.setCategoryId(mCategory.getId());
                                subcategory.setName(nombreCategoria);
                                subcategory.setSubcategoryType("SINGLESELECTION");
                                subcategory.setDocCount(doc_count);
                                mSubcategorias.add(subcategory);
                            }
                        }

                        mCategory.setSubcategories(mSubcategorias);

                        mCategoryList.add(mCategory);


                    }
                }catch (JSONException e){ e.printStackTrace(); }

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

    //Get JsonArray for subcategories shops and brands
    private JSONArray jSonArrayQuery(List<Category> categoriesFiltered, String categoryName){
        JSONArray jsonArray = new JSONArray();

        for (Category categoryFiltered : categoriesFiltered) {
            for (Subcategory subcategoryFiltered : categoryFiltered.getSubcategories()) {
                if (categoryFiltered.getId().compareTo(subcategoryFiltered.getCategoryId()) == 0) {
                    if (categoryFiltered.getName().equals(categoryName)){
                        jsonArray.put(subcategoryFiltered.getName());
                    }
                }
            }
        }
        return jsonArray;
    }

    //Get lower prices
    private int getLowerPrices(List<Category> categoriesFiltered, String categoryName){

        int lowerPrice = 0;

        for (Category categoryFiltered : categoriesFiltered) {
            for (Subcategory subcategoryFiltered : categoryFiltered.getSubcategories()) {
                if (categoryFiltered.getId().compareTo(subcategoryFiltered.getCategoryId()) == 0) {
                    if (categoryFiltered.getName().equals(categoryName)){

                        switch (subcategoryFiltered.getName()){
                            case "S/.0 - S/. 100":
                                lowerPrice = 0;
                                break;

                            case "S/.100 - S/. 250":
                                lowerPrice = 100;
                                break;

                            case "S/.250 - S/. 500":
                                lowerPrice = 250;
                                break;

                            case "S/.500 - S/. 1000":
                                lowerPrice = 500;
                                break;

                            case "S/.1000 - S/. 2000":
                                lowerPrice = 1000;
                                break;

                            case "S/.2000 - S/. 5000":
                                lowerPrice = 2000;
                                break;

                            case "S/.5000 En adelante":
                                lowerPrice = 5000;
                                break;
                        }

                    }
                }
            }
        }

        return lowerPrice;
    }

    //Get higher prices
    private int getHigherPrices(List<Category> categoriesFiltered, String categoryName){

        int higherPrice = 0;

        for (Category categoryFiltered : categoriesFiltered) {
            for (Subcategory subcategoryFiltered : categoryFiltered.getSubcategories()) {
                if (categoryFiltered.getId().compareTo(subcategoryFiltered.getCategoryId()) == 0) {
                    if (categoryFiltered.getName().equals(categoryName)){

                        switch (subcategoryFiltered.getName()){
                            case "S/.0 - S/. 100":
                                higherPrice = 100;
                                break;

                            case "S/.100 - S/. 250":
                                higherPrice = 250;
                                break;

                            case "S/.250 - S/. 500":
                                higherPrice = 500;
                                break;

                            case "S/.500 - S/. 1000":
                                higherPrice = 1000;
                                break;

                            case "S/.1000 - S/. 2000":
                                higherPrice = 2000;
                                break;

                            case "S/.2000 - S/. 5000":
                                higherPrice = 5000;
                                break;

                            case "S/.5000 En adelante":
                                higherPrice = 999999999;
                                break;
                        }

                    }
                }
            }
        }

        return higherPrice;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private void initializeFragment(){
        //Initialize fragment
        CategoryFragment fragment = CategoryFragment.newInstance(mCategoryList);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container,fragment)
                .commit();

        fragment.setFilterListener(this);
    }

    private void openDrawer() {
        if (!mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.openDrawer(GravityCompat.END);
        }
    }

    private void closeDrawer() {
        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }
}
