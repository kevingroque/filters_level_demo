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

import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.fragments.SubcategoryFragment;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<Category> mCategoryList;
    private ArrayList<Subcategory> mSubcategoryArrayList = new ArrayList<>();

    public CategoryAdapter(List<Category> mCategoryList) {
        this.mCategoryList = mCategoryList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCategoryList.get(position).isSwitchExist()){
            return 1;
        }else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();

        if (viewType == 0){
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
            case 0:
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

            case 1:
            {
                ViewHolderSwitchType holderSwitchType = (ViewHolderSwitchType) holder;
                holderSwitchType.setIsRecyclable(false);

                holderSwitchType.setCategoriySwitchName(categoryName);
                holderSwitchType.mCategorySwitch.setChecked(category.isSelected());
            }
                break;
            default:
                break;
        }


    }

    private String getSelectedCategories(String categoryId) {
        String text = "";

        for (Category category : mCategoryList) {
            if (category.getId().compareTo(categoryId) == 0) {
                if (category.getSubcategories() != null) {
                    for (Subcategory subcategory : category.getSubcategories()) {
                        if (subcategory.isSelected()) {
                            text += subcategory.getName() + "  ";
                        }
                    }
                }
                break;
            }
        }

        text = text.trim();
        text = text.replace("  ", ", ");

        if (text.length() == 0) {
            return null;
        } else {
            return text;
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }
}


class ViewHolderNormalType extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mCategories;
    public TextView mSubcategories;
    public LinearLayout itemClick;

    public ViewHolderNormalType(@NonNull View view) {
        super(view);
        mView = view;
        mSubcategories = (TextView) mView.findViewById(R.id.txt_category_subcategoryname);
        itemClick = (LinearLayout) mView.findViewById(R.id.item_category);
    }

    public void setCategoriesName(String categoryName){
        mCategories = (TextView) mView.findViewById(R.id.txt_category_name);
        mCategories.setText(categoryName);
    }

    public void setSubcategoriesName(String subcategories){
        mSubcategories.setText(subcategories);
    }
}

class ViewHolderSwitchType extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mCategories;
    public Switch mCategorySwitch;

    public ViewHolderSwitchType(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        mCategories = (TextView) mView.findViewById(R.id.txt_categoryswitch_name);
        mCategorySwitch = (Switch) mView.findViewById(R.id.swt_categoryswitch_switch);
    }

    public void setCategoriySwitchName(String categoryName){
        mCategories.setText(categoryName);
    }
}