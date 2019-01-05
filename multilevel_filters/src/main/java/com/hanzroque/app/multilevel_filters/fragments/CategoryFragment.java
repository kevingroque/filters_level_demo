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
import com.hanzroque.app.multilevel_filters.models.CategoryFiltered;
import com.hanzroque.app.multilevel_filters.models.Subcategory;
import com.hanzroque.app.multilevel_filters.models.SubcategoryFiltered;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {

    public static final String ARG_CATEGORIES = "categories";

    private RecyclerView mCategoryRecyclerView;
    private ArrayList<Category> mCategoryArrayList;
    private Category mCategory;
    private CategoryAdapter mCategoryAdapter;
    private FilterListener filterListener;

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

                List<CategoryFiltered> categoriesFilteredList = new ArrayList<>();
                List<SubcategoryFiltered> subcategoriesFilteredList = new ArrayList<>();

                for (Subcategory subcategory : subcategoriesSelected){
                    SubcategoryFiltered subcategoryFiltered = new SubcategoryFiltered();
                    subcategoryFiltered.setSubcategoryId(subcategory.getId());
                    subcategoryFiltered.setSubcategoryName(subcategory.getName());
                    subcategoryFiltered.setCategoryId(subcategory.getCategoryId());

                    subcategoriesFilteredList.add(subcategoryFiltered);
                }

                for (Category category : categoriesSelected){
                    CategoryFiltered categoryFiltered = new CategoryFiltered();
                    categoryFiltered.setCategoryId(category.getId());
                    categoryFiltered.setCategoryName(category.getName());
                    categoryFiltered.setSubcategoriesSelected(subcategoriesFilteredList);

                    categoriesFilteredList.add(categoryFiltered);
                }


                filterListener.filtrar(categoriesFilteredList);
            }
        });

        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear Data
            }
        });

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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
