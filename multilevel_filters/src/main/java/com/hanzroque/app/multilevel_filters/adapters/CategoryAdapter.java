package com.hanzroque.app.multilevel_filters.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.MainActivity;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.fragments.CategoryFragment;
import com.hanzroque.app.multilevel_filters.fragments.SubcategoryFragment;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Category> categoriesList;
    private CategoryFragment categoryFragment;

    public CategoryAdapter(Context mContext,
                           List<Category> categoriesList) {
        this.mContext = mContext;
        this.categoriesList = categoriesList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        CategoryAdapter.ViewHolder categoryHolder = new CategoryAdapter.ViewHolder(itemView);
        mContext = parent.getContext();
        return categoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        holder.setIsRecyclable(false);
        final Category item = categoriesList.get(i);

        final String categoryId = item.getId();
        final String categoryName = item.getName();


        holder.setCategoriesName(categoryName);

        String selectedCateries = getSelectedCateries(categoryId);

        if (selectedCateries != null){
            holder.setSubcategoriesName(selectedCateries);
            holder.mSubcategories.setTextColor(Color.RED);

        } else {
            holder.setSubcategoriesName(null);
            holder.mSubcategories.setTextColor(Color.BLACK);
        }


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("categoryID", categoryId);
                bundle.putString("categoryName", categoryName);

                //SubcategoryFragment subcategoryFragment = SubcategoryFragment.newInstance(categoryId, subcategoriesSeleccionadas);
                SubcategoryFragment subcategoryFragment = new SubcategoryFragment();
                subcategoryFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.layout_container, subcategoryFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private String getSelectedCateries(String categoryId) {
        String text = "";

        for (Category category : categoriesList) {
            if (category.getId().compareTo(categoryId) == 0) {
                if (category.getSubcategories() != null) {
                    for (Subcategory subcategory : category.getSubcategories()) {
                        if (subcategory.isSelected()) {
                            text += subcategory.getName() + " ";
                        }
                    }
                }

                break;
            }
        }

        text = text.trim();

        if (text.length() == 0) {
            return null;
        } else {
            return text;
        }
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mCategories, mSubcategories;
        private LinearLayout item;

        public ViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            mSubcategories = (TextView) mView.findViewById(R.id.txt_category_subcategoryname);
            item = (LinearLayout) mView.findViewById(R.id.item_category);
        }

        public void setCategoriesName(String categoryName){
            mCategories = (TextView) mView.findViewById(R.id.txt_category_name);
            mCategories.setText(categoryName);
        }

        public void setSubcategoriesName(String subcategories){
            //mSubcategories = (TextView) mView.findViewById(R.id.txt_category_subcategoryname);
            mSubcategories.setText(subcategories);

        }
    }
}
