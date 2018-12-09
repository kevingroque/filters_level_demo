package com.hanzroque.app.multilevel_filters.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.models.Subcategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubcategoryAdapter extends BaseAdapter {

    private Activity mActivity;
    private ArrayList<Subcategory> mSubcategories;
    private LayoutInflater mInflater;

    public SubcategoryAdapter(Activity activity) {
        this.mActivity = activity;
    }

    public SubcategoryAdapter(Activity activity, ArrayList<Subcategory> subcategories) {
        this.mActivity = activity;
        this.mSubcategories = subcategories;
        mInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return mSubcategories.size();
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
            view = mInflater.inflate(R.layout.item_subcategory,parent, false);
            holder = new ViewHolder();
            holder.txtSubcategoryName = (TextView) view.findViewById(R.id.txt_subcategoria);
            holder.ivCheckBox = (ImageView) view.findViewById(R.id.img_checkbox);

            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        Subcategory subcategory = mSubcategories.get(position);

        holder.txtSubcategoryName.setText(subcategory.getName());
        holder.txtSubcategoryName.setSelected(true);


        if (subcategory.isSelected()){
            holder.ivCheckBox.setBackgroundResource(R.drawable.ic_checked_circle);
            holder.txtSubcategoryName.setTextColor(Color.RED);
        }else {
            holder.ivCheckBox.setBackgroundResource(R.drawable.ic_check);
            holder.txtSubcategoryName.setTextColor(Color.BLACK);
        }

        return view;
    }

    class ViewHolder{
        TextView txtSubcategoryName;
        ImageView ivCheckBox;
    }

    public void updateRecords(ArrayList<Subcategory> subcategories){
        this.mSubcategories = subcategories;
        notifyDataSetChanged();
    }

}
