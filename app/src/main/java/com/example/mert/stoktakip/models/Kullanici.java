package com.example.mert.stoktakip.models;

public class Kullanici {
    private int id;
    private String kadi;
    private String sifre;

    public Kullanici() {}

    public Kullanici(String kadi, String sifre) {
        this.kadi = kadi;
        this.sifre = sifre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKadi() {
        return kadi;
    }

    public void setKadi(String kadi) {
        this.kadi = kadi;
    }

    public String getSifre() {
        return sifre;
    }

    public void setSifre(String sifre) {
        this.sifre = sifre;
    }
}
