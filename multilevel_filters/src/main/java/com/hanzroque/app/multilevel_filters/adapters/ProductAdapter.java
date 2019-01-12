package com.hanzroque.app.multilevel_filters.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hanzroque.app.multilevel_filters.R;
import com.hanzroque.app.multilevel_filters.models.Product;

import org.json.JSONException;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context mContext;
    private List<Product> mProductsList;

    public ProductAdapter(Context mContext, List<Product> mProductsList) {
        this.mContext = mContext;
        this.mProductsList = mProductsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent,false);
        ProductAdapter.ViewHolder productHolder = new ProductAdapter.ViewHolder(v);
        mContext = parent.getContext();
        return productHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        holder.setIsRecyclable(false);

        String title = mProductsList.get(i).getTitleProduct();
        String marca = mProductsList.get(i).getMarca();
        String shop = mProductsList.get(i).getShop();
        Double lowerPrice = mProductsList.get(i).getLowerPrice();
        Double previousPrice = mProductsList.get(i).getPreviousPrice();
        String imageUrl = null;
        try {
            imageUrl = (String) mProductsList.get(i).getCoverPhoto().get(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.setData(title,marca,shop, lowerPrice, previousPrice,imageUrl);
    }

    @Override
    public int getItemCount() {
        return mProductsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView mTitle, mMarca, mPrices;
        private ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mTitle = mView.findViewById(R.id.txt_itemProduct_title);
            mMarca = mView.findViewById(R.id.txt_itemProduct_marca);
            mPrices = mView.findViewById(R.id.txt_itemProduct_price);
            mImage = mView.findViewById(R.id.img_itemproduct_image);
        }

        private void setData(String title, String marca, String shop, Double lowerPrice, Double previousPrice , String urlImage){
            mTitle.setText(title);
            mMarca.setText(marca + " en " + shop);
            mPrices.setText(String.valueOf(lowerPrice) + " - " + String.valueOf(previousPrice));
            Glide.with(mContext).load(urlImage).into(mImage);
        }
    }
}
