package com.hanzroque.app.multilevel_filters.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.hanzroque.app.multilevel_filters.MainActivity;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.CategoryAdapter;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    private FragmentActivity mContext;

    //Lista Categorias
    private RecyclerView cateRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    //private List<Category> categoryList;
    private CategoryAdapter mCategoryAdapter;
    private DividerItemDecoration dividerItemDecoration;


    public static final String ARG_CATEGORIES = "categories";

    private ArrayList<Category> categoryArrayList;



    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(ArrayList<Category> categories) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CATEGORIES, categories);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryArrayList = (ArrayList<Category>) getArguments().getSerializable(ARG_CATEGORIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cateRecyclerView = getActivity().findViewById(R.id.recyclerview_categories);
        loadDataCategories();
    }


    public void loadDataCategories(){
        //categoryList = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(getActivity(), categoryArrayList);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        dividerItemDecoration = new DividerItemDecoration(cateRecyclerView.getContext(), linearLayoutManager.getOrientation());

        cateRecyclerView.setHasFixedSize(true);
        cateRecyclerView.setLayoutManager(linearLayoutManager);
        cateRecyclerView.addItemDecoration(dividerItemDecoration);
        cateRecyclerView.setAdapter(mCategoryAdapter);
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
