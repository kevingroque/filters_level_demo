package com.hanzroque.app.multilevel_filters.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.CategoryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    private String url_api_categorias = "https://api.mercadolibre.com/sites/MLA/categories";

    private String id_category;

    private FragmentActivity mContext;

    //Lista CAtegorias Subcategorias
    private RecyclerView catRaRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<Category> categoryList,subcategorias;
    private CategoryAdapter mCategoryAdapter;
    private DividerItemDecoration dividerItemDecoration;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);

        catRaRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_categories);
        categoryList = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getActivity(),categoryList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(catRaRecyclerView.getContext(), linearLayoutManager.getOrientation());

        catRaRecyclerView.setHasFixedSize(true);
        catRaRecyclerView.setLayoutManager(linearLayoutManager);
        catRaRecyclerView.addItemDecoration(dividerItemDecoration);
        catRaRecyclerView.setAdapter(mCategoryAdapter);

        getCategoriesData();

        return view;
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
                        id_category = jsonObject.getString("id");

                        cate.setName(jsonObject.getString("name"));
                        cate.setId(id_category);

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

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonArrayRequest);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        mContext = (FragmentActivity) activity;
        super.onAttach(activity);
    }
}
