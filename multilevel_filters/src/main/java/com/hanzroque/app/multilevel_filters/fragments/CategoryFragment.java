package com.hanzroque.app.multilevel_filters.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hanzroque.app.multilevel_filters.MainActivity;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.CategoryAdapter;


import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    public static final String ARG_CATEGORIES = "categories";

    private RecyclerView mCategoryRecyclerView;
    private ArrayList<Category> mCategoryArrayList;
    private CategoryAdapter mCategoryAdapter;

    private Button mClearBtn;

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
            mCategoryArrayList = (ArrayList<Category>) getArguments().getSerializable(ARG_CATEGORIES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCategoryRecyclerView = getActivity().findViewById(R.id.recyclerview_categories);
        mClearBtn = getActivity().findViewById(R.id.btn_category_clear);
        loadDataCategories();

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear Data
                MainActivity.INSTANCE.Clear(mCategoryAdapter);
            }
        });

    }

    public void loadDataCategories(){
        mCategoryAdapter = new CategoryAdapter(getActivity(), mCategoryArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mCategoryRecyclerView.getContext(), linearLayoutManager.getOrientation());

        mCategoryRecyclerView.setHasFixedSize(true);
        mCategoryRecyclerView.setLayoutManager(linearLayoutManager);
        mCategoryRecyclerView.addItemDecoration(dividerItemDecoration);
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onAttach(Activity activity) {
        FragmentActivity fragmentActivity = (FragmentActivity) activity;
        super.onAttach(activity);
    }

}
