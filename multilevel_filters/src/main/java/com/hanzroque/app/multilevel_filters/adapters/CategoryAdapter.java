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
import android.widget.Switch;
import android.widget.TextView;

import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.fragments.SubcategoryFragment;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_VIEW_TYPE = 0;
    private static final int SWITCH_VIEW_TYPE = 1;

    private List<Category> mCategoryList;
    private ArrayList<Subcategory> mSubcategoryArrayList = new ArrayList<>();
    private FilterListener filterListener;

    public void setFilterListener(FilterListener filterListener){
        this.filterListener = filterListener;
    }

    public CategoryAdapter(List<Category> mCategoryList) {
        this.mCategoryList = mCategoryList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCategoryList.get(position).isSwitchExist()){
            return SWITCH_VIEW_TYPE;
        }else {
            return NORMAL_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context mContext = parent.getContext();

        if (viewType == NORMAL_VIEW_TYPE){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            View view1 = inflater.inflate(R.layout.item_category, parent,false);
            return new ViewHolderNormalType(view1);
        }else {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            View view2 = layoutInflater.inflate(R.layout.item_category_switch, parent,false);
            return new ViewHolderSwitchType(view2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        final Category category = mCategoryList.get(i);

        final String categoryId = category.getId();
        final String categoryName = category.getName();

        switch (holder.getItemViewType()){
            case NORMAL_VIEW_TYPE:
            {
                ViewHolderNormalType holderNormalType = (ViewHolderNormalType)holder;
                holderNormalType.setIsRecyclable(false);

                holderNormalType.setCategoriesName(categoryName);

                final String selectedCategories = getSelectedCategories(categoryId);

                if (selectedCategories != null){
                    holderNormalType.setSubcategoriesName(selectedCategories);
                    holderNormalType.mSubcategories.setTextColor(Color.RED);
                } else {
                    holderNormalType.setSubcategoriesName(null);
                    holderNormalType.mSubcategories.setTextColor(Color.BLACK);
                }

                holderNormalType.itemClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryID", categoryId);
                        bundle.putString("categoryName", categoryName);
                        for (Category category : mCategoryList) {
                            if (category.getId().compareTo(categoryId) == 0 ) {
                                if (category.getSubcategories() != null){
                                    mSubcategoryArrayList.addAll(category.getSubcategories());
                                }
                                break;
                            }
                        }

                        bundle.putSerializable("mylist", mSubcategoryArrayList);

                        SubcategoryFragment subcategoryFragment = new SubcategoryFragment();
                        subcategoryFragment.setArguments(bundle);

                        subcategoryFragment.setFilterListener(filterListener);

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        activity.getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.layout_container, subcategoryFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

            }
                break;

            case SWITCH_VIEW_TYPE:
            {
                ViewHolderSwitchType holderSwitchType = (ViewHolderSwitchType) holder;
                holderSwitchType.setIsRecyclable(false);
                holderSwitchType.setCategorySwitchName(categoryName);
                holderSwitchType.mCategorySwitch.setChecked(category.isSelected());
            }
                break;

            default:
                break;
        }
    }

    private String getSelectedCategories(String categoryId) {
        StringBuilder text = new StringBuilder();

        for (Category category : mCategoryList) {
            if (category.getId().compareTo(categoryId) == 0) {
                if (category.getSubcategories() != null) {
                    for (Subcategory subcategory : category.getSubcategories()) {
                        if (subcategory.isSelected()) {
                            text.append(subcategory.getName()).append("  ");
                        }
                    }
                }
                break;
            }
        }

        text = new StringBuilder(text.toString().trim());
        text = new StringBuilder(text.toString().replace("  ", ", "));

        if (text.length() == 0) {
            return null;
        } else {
            return text.toString();
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    private class ViewHolderNormalType extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mCategories, mSubcategories;
        private LinearLayout itemClick;

        private ViewHolderNormalType(@NonNull View view) {
            super(view);
            mView = view;
            mCategories = mView.findViewById(R.id.txt_category_name);
            mSubcategories = mView.findViewById(R.id.txt_category_subcategoryname);
            itemClick = mView.findViewById(R.id.item_category);
        }

        private void setCategoriesName(String categoryName){
            mCategories.setText(categoryName);
        }

        private void setSubcategoriesName(String subcategories){
            mSubcategories.setText(subcategories);
        }
    }

    private class ViewHolderSwitchType extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mCategories;
        private Switch mCategorySwitch;

        private ViewHolderSwitchType(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mCategories = mView.findViewById(R.id.txt_categoryswitch_name);
            mCategorySwitch = mView.findViewById(R.id.swt_categoryswitch_switch);
        }

        private void setCategorySwitchName(String categoryName){
            mCategories.setText(categoryName);
        }
    }
}

