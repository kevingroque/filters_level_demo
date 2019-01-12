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

import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.adapters.CategoryAdapter;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    public static final String ARG_CATEGORIES = "categories";

    private RecyclerView mCategoryRecyclerView;
    private ArrayList<Category> mCategoryArrayList;
    private CategoryAdapter mCategoryAdapter;
    private FilterListener filterListener;

    List<Category> categoriesFilteredList = new ArrayList<>();
    List<Subcategory> subcategoriesFilteredList = new ArrayList<>();

    private Button mClearBtn, mDoneFiltersBtn;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public void setFilterListener(FilterListener listener) {
        this.filterListener = listener;
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
        }else {
            mCategoryArrayList = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        mCategoryRecyclerView = view.findViewById(R.id.recyclerview_categories);
        mClearBtn = view.findViewById(R.id.btn_category_clear);

        mDoneFiltersBtn = view.findViewById(R.id.btn_category_done);

        loadDataCategories();

        mDoneFiltersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Category> categoriesSelected = new ArrayList<>();
                List<Subcategory> subcategoriesSelected = new ArrayList<>();

                for (Category category : mCategoryArrayList) {
                    if (category.getSubcategories() != null) {
                        for (Subcategory subcategory : category.getSubcategories()) {
                            if (subcategory.isSelected()) {
                                if (!categoriesSelected.contains(category)) {
                                    categoriesSelected.add(category);
                                }
                                subcategoriesSelected.add(subcategory);
                            }
                        }
                    }
                }

                for (Subcategory subcategory : subcategoriesSelected){
                    Subcategory subcategoryFiltered = new Subcategory();
                    subcategoryFiltered.setId(subcategory.getId());
                    subcategoryFiltered.setName(subcategory.getName());
                    subcategoryFiltered.setCategoryId(subcategory.getCategoryId());
                    subcategoryFiltered.setSelected(subcategory.isSelected());

                    subcategoriesFilteredList.add(subcategoryFiltered);
                }

                for (Category category : categoriesSelected){
                    Category categoryFiltered = new Category();
                    categoryFiltered.setId(category.getId());
                    categoryFiltered.setName(category.getName());
                    categoryFiltered.setSubcategories(subcategoriesFilteredList);

                    categoriesFilteredList.add(categoryFiltered);
                }

                filterListener.filtrar(categoriesFilteredList);
            }
        });

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterListener.cleanFilters();
                mCategoryAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    //Load all categories to listview
    private void loadDataCategories(){
        mCategoryAdapter = new CategoryAdapter(mCategoryArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mCategoryRecyclerView.getContext(), linearLayoutManager.getOrientation());

        mCategoryRecyclerView.setHasFixedSize(true);
        mCategoryRecyclerView.setLayoutManager(linearLayoutManager);
        mCategoryRecyclerView.addItemDecoration(dividerItemDecoration);
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);

        mCategoryAdapter.notifyDataSetChanged();
        mCategoryAdapter.setFilterListener(filterListener);
    }
}
