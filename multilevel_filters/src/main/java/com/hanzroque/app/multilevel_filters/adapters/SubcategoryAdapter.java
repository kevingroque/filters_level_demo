package com.hanzroque.app.multilevel_filters.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.util.List;

public class SubcategoryAdapter extends BaseAdapter {

    Activity activity;
    List<Subcategory> subcategories;
    LayoutInflater inflater;

    public SubcategoryAdapter(Activity activity) {
        this.activity = activity;
    }

    public SubcategoryAdapter(Activity activity, List<Subcategory> subcategories) {
        this.activity = activity;
        this.subcategories = subcategories;
        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return subcategories.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = null;

        if (view == null){
            view = inflater.inflate(R.layout.subcategory_item,parent, false);
            holder = new ViewHolder();
            holder.txtSubcategoryName = (TextView) view.findViewById(R.id.txt_subcategoria);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.img_checkbox);

            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        Subcategory subcategory = subcategories.get(position);

        holder.txtSubcategoryName.setText(subcategory.getName());

        if (subcategory.isSelected()){
            holder.ivCheckBox.setBackgroundResource(R.drawable.ic_checked_circle);
        }else {
            holder.ivCheckBox.setBackgroundResource(R.drawable.ic_check);
        }

        return view;
    }


    public void updateRecords(List<Subcategory> subcategories){
        this.subcategories = subcategories;
        notifyDataSetChanged();
    }

    class ViewHolder{
        TextView txtSubcategoryName;
        ImageView ivCheckBox;
    }
}
