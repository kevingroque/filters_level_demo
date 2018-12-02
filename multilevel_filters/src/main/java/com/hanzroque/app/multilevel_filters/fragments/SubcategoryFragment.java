package com.hanzroque.app.multilevel_filters.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.MainActivity;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.SubcategoryAdapter;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubcategoryFragment extends Fragment {

    public static final String ARG_CAT_SELECCIONADA_2 = "CategoriID";
    public static final String ARG_SUB_CAT_SELECCIONADAS_2 = "Sucategorias Seleccionadas";

    private FragmentActivity mContext;

    private Subcategory subcategory;

    //Listar Subcategorias
    private ListView listView;
    private ArrayList<Subcategory> subcategories;
    private SubcategoryAdapter mAdapter;

    private ImageButton btnBack;
    private String categoryID, categoryname;

    private TextView txtTitulo;

    private String url_api_subcatergories = "https://api.mercadolibre.com/categories/";

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
            categoryID = bundle.get("categoryID").toString();
            categoryname = bundle.get("categoryName").toString();
        }

        listView = (ListView) view.findViewById(R.id.listview_subcategories);
        btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        txtTitulo = (TextView) view.findViewById(R.id.txt_subcategoria_name);
        txtTitulo.setSelected(true);

        loadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subcategory = subcategories.get(position);

                if (subcategory.isSelected()) {
                    subcategory.setSelected(false);
                } else {
                    subcategory.setSelected(true);
                }

                subcategories.set(position, subcategory);
                mAdapter.updateRecords(subcategories);
            }
        });

        //Button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryFragment fragment = CategoryFragment.newInstance(MainActivity.INSTANCE.getCategoryList());
                FragmentManager fragmentManager = mContext.getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.layout_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    public void getSubCategories(String cat_id) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url_api_subcatergories + cat_id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("children_categories");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                Log.d("RESULTADO", jsonObject.getString("name"));

                                Subcategory subcate = new Subcategory();
                                subcate.setName(jsonObject.getString("name"));

                                subcategories.add(subcate);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESULTADO", "Error: " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(req);
    }

    public void loadData(){
        subcategories = new ArrayList<>();
        mAdapter = new SubcategoryAdapter(getActivity(), subcategories);
        listView.setAdapter(mAdapter);
        getSubCategories(categoryID);
        txtTitulo.setText(categoryname);
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }

}
