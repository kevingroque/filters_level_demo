package com.hanzroque.app.filters_level_demo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanzroque.app.filters_level_demo.MainActivity;
import com.hanzroque.app.filters_level_demo.R;
import com.hanzroque.app.filters_level_demo.models.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context mContext;
    private List<Category> categoriesList;
    public MainActivity mainActivity;

    public CategoryAdapter(Context mContext ,List<Category> categoriesList) {
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

        final String categoryId = categoriesList.get(i).getId();
        final String categoryName = categoriesList.get(i).getName();

        holder.setDataText(categoryName,categoryId);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.BorrarListado();
                mainActivity.getSubCategories(categoryId);
                Toast.makeText(mContext, "ID: " + categoryId, Toast.LENGTH_SHORT).show();
            }
        });

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
            item = (LinearLayout) mView.findViewById(R.id.item_category);
        }

        public void setDataText(String categoryName, String categoryId){

            mCategories = (TextView) mView.findViewById(R.id.txt_category_name);
            mSubcategories = (TextView) mView.findViewById(R.id.txt_category_subcategoryname);

            mCategories.setText(categoryName);
            mSubcategories.setText(categoryId);

        }
    }
}
