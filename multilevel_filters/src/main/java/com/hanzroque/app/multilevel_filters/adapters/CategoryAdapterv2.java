package com.hanzroque.app.multilevel_filters.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.interfaces.BaseViewHolder;
import com.hanzroque.app.multilevel_filters.interfaces.Constants;
import com.hanzroque.app.multilevel_filters.interfaces.ItemType;
import com.hanzroque.app.multilevel_filters.models.Category;

import java.util.List;

public class CategoryAdapterv2 extends RecyclerView.Adapter<BaseViewHolder> {

    private List<? extends ItemType> mList;
    private LayoutInflater mInflator;

    public CategoryAdapterv2(List<? extends ItemType> mList, Context context) {
        this.mList = mList;
        this.mInflator = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constants.ViewType.NORMAL_TYPE:
                return new CategoryHolder(mInflator.inflate(R.layout.item_category,parent,false));
            case Constants.ViewType.SWITCH_TYPE:
                return new CategorySwitchHolder(mInflator.inflate(R.layout.item_category_switch,parent,false));

        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.bind(mList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class CategoryHolder extends BaseViewHolder<Category>{

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Category object) {

        }
    }

    public static class CategorySwitchHolder extends BaseViewHolder<Category> {

        public CategorySwitchHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bind(Category object) {

        }
    }
}
