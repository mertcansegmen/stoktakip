package com.example.mert.stoktakip.models;

/**
 * {@code UrunGetirisi} sınıfı bir ürünün şuana kadar kazandırdığı getiriyi tutuyor.
 */

public class UrunGetirisi {
    private String urunAdi;
    private float getiri;

    public UrunGetirisi(String urunAdi, float getiri) {
        this.getiri = getiri;
        this.urunAdi = urunAdi;
    }

    public float getGetiri() {
        return getiri;
    }

    public void setGetiri(float getiri) {
        this.getiri = getiri;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }
}
