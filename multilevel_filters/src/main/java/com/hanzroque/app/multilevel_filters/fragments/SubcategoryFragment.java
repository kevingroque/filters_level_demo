package com.hanzroque.app.multilevel_filters.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.MainActivity;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.SubcategoryAdapter;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubcategoryFragment extends Fragment {

    private FragmentActivity mContext;

    private Subcategory mSubcategory;
    private ListView mListView;
    private ArrayList<Subcategory> mSubcategoryArrayList;
    private SubcategoryAdapter mSubcategoryAdapter;

    private ImageButton btnBack;
    private TextView mTitulo;
    private Button mBtnDone;

    private FilterListener filterListener;

    private String mCategoryId, mCategoryname;

    public void setFilterListener(FilterListener filterListener){
        this.filterListener = filterListener;
    }

    public SubcategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subcategory, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mCategoryId = (String) bundle.get("categoryID");
            mCategoryname = (String) bundle.get("categoryName");
            mSubcategoryArrayList = (ArrayList<Subcategory>) bundle.get("mySubcategoriesSelected");
            if(mSubcategoryArrayList == null){
                mSubcategoryArrayList.clear();
                mSubcategoryArrayList = new ArrayList<>();
            }
        }

        mListView = (ListView) view.findViewById(R.id.listview_subcategories);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        mBtnDone = (Button) view.findViewById(R.id.btn_subcategory_done);
        mTitulo = (TextView) view.findViewById(R.id.txt_subcategoria_name);
        mTitulo.setSelected(true);

        loadData();

        for (Category category : MainActivity.INSTANCE.getCategoryList()) {
            if (category.getId().compareTo(mCategoryId) == 0) {
                category.setSubcategories(mSubcategoryArrayList);
                break;
            }
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSubcategory = mSubcategoryArrayList.get(position);

                if (mSubcategory.isSelected()) {
                    mSubcategory.setSelected(false);
                } else {
                    mSubcategory.setSelected(true);
                }
                mSubcategoryArrayList.set(position, mSubcategory);
                mSubcategoryAdapter.updateRecords(mSubcategoryArrayList);
            }
        });

        //Button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDone();
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDone();
                DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });
        return view;
    }


    private void getSubcategorias(){
        JSONObject json = new JSONObject();
        try{
            json.put("string_search", filterListener.wordToSearch());
            json.put("num_pag", 1);
        }catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, filterListener.URL_API_PRODUCTS,
                json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray jsonArrayContadoresFiltros = null;

                try {
                    if (mCategoryname.compareTo("precios_rango_fijo") != 0) {
                        jsonArrayContadoresFiltros = response.getJSONObject("contadores_filtros").getJSONArray(mCategoryname);
                        for (int cont = 0; cont < jsonArrayContadoresFiltros.length(); cont++) {
                            JSONObject jSubcategories = (JSONObject) jsonArrayContadoresFiltros.get(cont);

                            Subcategory subcategory = new Subcategory();
                            subcategory.setCategoryId(mCategoryId);
                            subcategory.setName(jSubcategories.getString("key"));
                            subcategory.setDocCount(jSubcategories.getInt("doc_count"));
                            mSubcategoryArrayList.add(subcategory);
                        }
                    }else {
                        JSONArray rangosPrecios = response.getJSONObject("contadores_filtros").getJSONObject("precios_rango_fijo").names();

                        for (int i = 0; i < rangosPrecios.length(); i++) {
                            String rangoPrecio = rangosPrecios.getString(i);

                            JSONObject jsonRangoPrecio = response.getJSONObject("contadores_filtros").getJSONObject("precios_rango_fijo").getJSONObject(rangoPrecio);

                            int from = jsonRangoPrecio.getInt("from");
                            int to;
                            int doc_count = jsonRangoPrecio.getInt("doc_count");

                            if (jsonRangoPrecio.has("to")) {
                                to = jsonRangoPrecio.getInt("to");
                            } else {
                                to = 0;
                            }

                            String nombreCategoria;

                            if (from == 0) {
                                nombreCategoria = "S/.0";
                            } else {
                                nombreCategoria = "S/." + from;
                            }

                            if (to == 0) {
                                nombreCategoria += " En adelante";
                            } else {
                                nombreCategoria += " - S/. " + to;
                            }

                            Log.i("alertahorro", nombreCategoria);

                            Subcategory subcategory = new Subcategory();
                            subcategory.setCategoryId(mCategoryId);
                            subcategory.setName(nombreCategoria);
                            subcategory.setDocCount(doc_count);
                            mSubcategoryArrayList.add(subcategory);

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mSubcategoryAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(objectRequest);
    }

    private void backDone() {
        CategoryFragment fragment = CategoryFragment.newInstance(MainActivity.INSTANCE.getCategoryList());
        FragmentManager fragmentManager = mContext.getSupportFragmentManager();

        fragment.setFilterListener(filterListener);
        fragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void loadData() {
        mSubcategoryAdapter = new SubcategoryAdapter(Objects.requireNonNull(getActivity()), mSubcategoryArrayList);
        mListView.setAdapter(mSubcategoryAdapter);
        getSubcategorias();
        mTitulo.setText(mCategoryname);
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
}
