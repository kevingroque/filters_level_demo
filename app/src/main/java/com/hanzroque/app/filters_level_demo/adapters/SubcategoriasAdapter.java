package com.hanzroque.app.filters_level_demo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hanzroque.app.filters_level_demo.MainActivity;
import com.hanzroque.app.filters_level_demo.R;
import com.hanzroque.app.filters_level_demo.models.Category;

import java.util.List;

public class SubcategoriasAdapter extends RecyclerView.Adapter<SubcategoriasAdapter.ViewHolder> {

    private Context mContext;
    private List<Category> categoriesList;
    public MainActivity mainActivity;

    public SubcategoriasAdapter(Context mContext ,List<Category> categoriesList) {
        this.mContext = mContext;
        this.categoriesList = categoriesList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcategory_item, parent, false);
        SubcategoriasAdapter.ViewHolder categoryHolder = new SubcategoriasAdapter.ViewHolder(itemView);
        mContext = parent.getContext();
        return categoryHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        holder.setIsRecyclable(false);

        final String categoryName = categoriesList.get(i).getName();

        holder.setDataText(categoryName);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.Name(categoryName);
                Toast.makeText(mContext, "Name " + categoryName, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mCategories;
        private RelativeLayout item;

        public ViewHolder(@NonNull View view) {
            super(view);
            mView = view;
            item = (RelativeLayout) mView.findViewById(R.id.subcategory_item);
        }

        public void setDataText(String categoryName){
            mCategories = (TextView) mView.findViewById(R.id.txt_subcategoria);
            mCategories.setText(categoryName);
        }
    }
}
