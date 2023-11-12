package com.ruanbekker.cargarage;


import java.io.Serializable;
import java.util.List;


public class TestItem implements Serializable {
    private String shopId;
    private String id;

    private List<Double> embedding;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
