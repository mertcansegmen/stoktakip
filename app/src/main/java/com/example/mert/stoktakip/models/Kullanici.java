package com.example.mert.stoktakip.models;

/** {@code Kullanici} sınıfı StokTakip veritabanının kullanici tablosunu temsil ediyor */

public class Kullanici {
    private String kadi;
    private String sifre;

    public Kullanici() {}

    public Kullanici(String kadi, String sifre) {
        this.kadi = kadi;
        this.sifre = sifre;
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
