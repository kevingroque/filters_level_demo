package com.hanzroque.app.filters_level_demo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.filters_level_demo.adapters.CategoryAdapter;
import com.hanzroque.app.filters_level_demo.adapters.SubcategoriasAdapter;
import com.hanzroque.app.filters_level_demo.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button btnBack, btnGone;
    private TextView subcategorianame;

    //Lista CAtegorias Subcategorias
    private RecyclerView catRaRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Category> categoryList,subcategorias;
    private CategoryAdapter mCategoryAdapter;
    private SubcategoriasAdapter mSubCatAdapter;
    private DividerItemDecoration dividerItemDecoration;

    //url de categorias
    private String url_api_categorias = "https://api.mercadolibre.com/sites/MLA/categories";
    private String url_api_subcatergories = "https://api.mercadolibre.com/categories/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        catRaRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_filtros);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnGone = (Button) findViewById(R.id.btn_done);
        subcategorianame =(TextView) findViewById(R.id.content_txt_categoryname);

        categoryList = new ArrayList<>();
        subcategorias = new ArrayList<>();

        mCategoryAdapter = new CategoryAdapter(getApplicationContext(),categoryList);
        mSubCatAdapter = new SubcategoriasAdapter(this, subcategorias);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(catRaRecyclerView.getContext(), linearLayoutManager.getOrientation());

        mCategoryAdapter.mainActivity = this;
        mSubCatAdapter.mainActivity = this;

        catRaRecyclerView.setHasFixedSize(true);
        catRaRecyclerView.setLayoutManager(linearLayoutManager);
        catRaRecyclerView.addItemDecoration(dividerItemDecoration);
        catRaRecyclerView.setAdapter(mCategoryAdapter);

        getCategoriesData();

    }

    public void BorrarListado(){

        catRaRecyclerView.setAdapter(null);
        btnGone.setVisibility(View.GONE);
        btnBack.setVisibility(View.VISIBLE);
        catRaRecyclerView.setAdapter(mSubCatAdapter);
    }

    public void RegresarCat(View view){
        catRaRecyclerView.setAdapter(null);
        subcategorias.clear();
        btnGone.setVisibility(View.VISIBLE);
        btnBack.setVisibility(View.GONE);
        catRaRecyclerView.setAdapter(mCategoryAdapter);
    }

    public void Name(String catename){
        subcategorianame.setText(catename);
    }

    //Obtener las categorias
    private void getCategoriesData() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url_api_categorias, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        Category cate = new Category();
                        cate.setName(jsonObject.getString("name"));
                        cate.setId(jsonObject.getString("id"));

                        categoryList.add(cate);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                mCategoryAdapter.notifyDataSetChanged();
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


    public void getSubCategories(String cat_id){
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url_api_subcatergories+cat_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("children_categories");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Log.d("RESULTADO", jsonObject.getString("name"));

                                Category subcate = new Category();
                                subcate.setName(jsonObject.getString("name"));

                                subcategorias.add(subcate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mSubCatAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESULTADO", "Error: " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(req);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
