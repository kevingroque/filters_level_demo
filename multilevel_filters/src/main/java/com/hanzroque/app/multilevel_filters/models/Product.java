package com.hanzroque.app.multilevel_filters.models;

import org.json.JSONArray;

public class Product {
    private float productId;
    private String dateProduct;
    private String shop;
    private String titleProduct;
    private String marca;
    private Double lowerPrice;
    private Double previousPrice;
    private Double averagePrice;
    private Double priceDifference;
    private JSONArray coverPhoto;

    public Product() {
    }

    public Product(float productId, String dateProduct, String shop, String titleProduct, String marca, Double lowerPrice, Double previousPrice, Double averagePrice, Double priceDifference, JSONArray coverPhoto) {
        this.productId = productId;
        this.dateProduct = dateProduct;
        this.shop = shop;
        this.titleProduct = titleProduct;
        this.marca = marca;
        this.lowerPrice = lowerPrice;
        this.previousPrice = previousPrice;
        this.averagePrice = averagePrice;
        this.priceDifference = priceDifference;
        this.coverPhoto = coverPhoto;
    }

    public float getProductId() {
        return productId;
    }

    public void setProductId(float productId) {
        this.productId = productId;
    }

    public String getDateProduct() {
        return dateProduct;
    }

    public void setDateProduct(String dateProduct) {
        this.dateProduct = dateProduct;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getTitleProduct() {
        return titleProduct;
    }

    public void setTitleProduct(String titleProduct) {
        this.titleProduct = titleProduct;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Double getLowerPrice() {
        return lowerPrice;
    }

    public void setLowerPrice(Double lowerPrice) {
        this.lowerPrice = lowerPrice;
    }

    public Double getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(Double previousPrice) {
        this.previousPrice = previousPrice;
    }

    public Double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(Double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public Double getPriceDifference() {
        return priceDifference;
    }

    public void setPriceDifference(Double priceDifference) {
        this.priceDifference = priceDifference;
    }

    public JSONArray getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(JSONArray coverPhoto) {
        this.coverPhoto = coverPhoto;
    }
}
