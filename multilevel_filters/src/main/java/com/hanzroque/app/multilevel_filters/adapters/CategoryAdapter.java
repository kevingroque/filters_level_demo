package com.hanzroque.app.multilevel_filters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hanzroque.app.multilevel_filters.interfaces.FilterListener;
import com.hanzroque.app.multilevel_filters.models.Category;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.fragments.SubcategoryFragment;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int NORMAL_VIEW_TYPE = 0;
    private static final int SWITCH_VIEW_TYPE = 1;
    private static final int RADIO_VIEW_TYPE = 2;

    private List<Category> mCategoryList;
    private ArrayList<Subcategory> mSubcategoryArrayList = new ArrayList<>();
    private FilterListener filterListener;

    public void setFilterListener(FilterListener filterListener) {
        this.filterListener = filterListener;
    }

    public CategoryAdapter(List<Category> mCategoryList) {
        this.mCategoryList = mCategoryList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mCategoryList.get(position).getCategoryType().equals("NORMAL_TYPE")) {
            return NORMAL_VIEW_TYPE;
        } else if (mCategoryList.get(position).getCategoryType().equals("RADIO_TYPE")) {
            return RADIO_VIEW_TYPE;
        } else {
            return SWITCH_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        if (viewType == NORMAL_VIEW_TYPE) {
            View view1 = inflater.inflate(R.layout.item_category, parent, false);
            return new ViewHolderNormalType(view1);

        } else if (viewType == RADIO_VIEW_TYPE) {
            View view2 = inflater.inflate(R.layout.item_category_searchorder, parent, false);
            return new ViewHolderRadioButtonType(view2);

        } else {

            View view3 = inflater.inflate(R.layout.item_category_switch, parent, false);
            return new ViewHolderSwitchType(view3);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int i) {

        final Category category = mCategoryList.get(i);

        final String categoryId = category.getId();
        final String categoryName = category.getName();

        switch (holder.getItemViewType()) {
            case RADIO_VIEW_TYPE: {
                final ViewHolderRadioButtonType holderRadioButtonType = (ViewHolderRadioButtonType) holder;
                holderRadioButtonType.setIsRecyclable(false);
                holderRadioButtonType.setCategoryRadioButton(categoryName);

                holderRadioButtonType.mSearchOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton searchOptions = (RadioButton) group.findViewById(checkedId);
                        if (null != searchOptions && checkedId > -1) {
                            Log.d("SEARCHORDER", "Order Selected: " + searchOptions.getText() + " - " + checkedId);
                        }
                    }
                });
            }
            break;

            case NORMAL_VIEW_TYPE: {
                ViewHolderNormalType holderNormalType = (ViewHolderNormalType) holder;
                holderNormalType.setIsRecyclable(false);

                holderNormalType.setCategoriesName(categoryName);

                final String selectedCategories = getSelectedCategories(categoryId);

                if (selectedCategories != null) {
                    holderNormalType.setSubcategoriesName(selectedCategories);
                    holderNormalType.mSubcategories.setTextColor(Color.RED);
                } else {
                    holderNormalType.setSubcategoriesName("All");
                    holderNormalType.mSubcategories.setTextColor(Color.BLACK);
                }

                holderNormalType.itemClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("categoryID", categoryId);
                        bundle.putString("categoryName", categoryName);
                        bundle.putSerializable("subcategories", (Serializable) category.getSubcategories());
                        for (Category category : mCategoryList) {
                            if (category.getId().compareTo(categoryId) == 0) {
                                if (category.getSubcategories() != null) {
                                    mSubcategoryArrayList.addAll(category.getSubcategories());
                                }
                                break;
                            }
                        }
                        bundle.putSerializable("mySubcategoriesSelected", mSubcategoryArrayList);

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

            case SWITCH_VIEW_TYPE: {
                ViewHolderSwitchType holderSwitchType = (ViewHolderSwitchType) holder;
                holderSwitchType.setIsRecyclable(false);
                holderSwitchType.setCategorySwitchName(categoryName);

                holderSwitchType.mCategorySwitch.setChecked(false);

                holderSwitchType.mCategorySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {

                        } else {

                        }
                    }
                });
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
                            text.append(subcategory.getName() + " (" + subcategory.getDocCount() + ")").append("  ");
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

        private void setCategoriesName(String categoryName) {
            mCategories.setText(categoryName);
        }

        private void setSubcategoriesName(String subcategories) {
            mSubcategories.setText(subcategories);
        }
    }

    private class ViewHolderRadioButtonType extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mCategories;
        private RadioGroup mSearchOrder;

        public ViewHolderRadioButtonType(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mCategories = mView.findViewById(R.id.txt_categoryorder_name);
            mSearchOrder = mView.findViewById(R.id.rg_categoryorder_orderoptions);
        }

        private void setCategoryRadioButton(String categoryName) {
            mCategories.setText(categoryName);
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

        private void setCategorySwitchName(String categoryName) {
            mCategories.setText(categoryName);
        }
    }
}

