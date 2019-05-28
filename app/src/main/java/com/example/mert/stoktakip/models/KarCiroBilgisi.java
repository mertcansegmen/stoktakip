package com.example.mert.stoktakip.models;

public class KarCiroBilgisi {
    private float ciro;
    private float kar;
    private String zaman;

    public KarCiroBilgisi(float ciro, float kar, String zaman) {
        this.ciro = ciro;
        this.kar = kar;
        this.zaman = zaman;
    }

    public float getKar() {
        return kar;
    }

    public void setKar(float kar) {
        this.kar = kar;
    }

    public float getCiro() {
        return ciro;
    }

    public void setCiro(float ciro) {
        this.ciro = ciro;
    }

    public String getZaman() {
        return zaman;
    }

    public void setZaman(String zaman) {
        this.zaman = zaman;
    }
}