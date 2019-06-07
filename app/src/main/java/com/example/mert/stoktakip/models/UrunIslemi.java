package com.example.mert.stoktakip.models;

/**
 * {@code UrunIslemi} sınıfı StokTakip veritabanının urun_islemi tablosunu temsil ediyor
 */

public class UrunIslemi {
    private int id;
    private String islemTuru;
    private String barkodNo;
    private String kadi;
    private int adet;
    private float alisFiyati;
    private float satisFiyati;
    private String islemTarihi;
    private String aciklama;
    private String urunAdi;

    public UrunIslemi() {
    }

    public UrunIslemi(int id, String islemTuru, String barkodNo, String kadi, int adet, float alisFiyati, float satisFiyati, String islemTarihi, String urunAdi) {
        this.id = id;
        this.islemTuru = islemTuru;
        this.barkodNo = barkodNo;
        this.kadi = kadi;
        this.adet = adet;
        this.alisFiyati = alisFiyati;
        this.satisFiyati = satisFiyati;
        this.islemTarihi = islemTarihi;
        this.urunAdi = urunAdi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIslemTuru() {
        return islemTuru;
    }

    public void setIslemTuru(String islemTuru) {
        this.islemTuru = islemTuru;
    }

    public String getBarkodNo() {
        return barkodNo;
    }

    public void setBarkodNo(String barkodNo) {
        this.barkodNo = barkodNo;
    }

    public String getKadi() {
        return kadi;
    }

    public void setKadi(String kadi) {
        this.kadi = kadi;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public float getAlisFiyati() {
        return alisFiyati;
    }

    public float getSatisFiyati() {
        return satisFiyati;
    }

    public void setSatisFiyati(float satisFiyati) {
        this.satisFiyati = satisFiyati;
    }

    public void setAlisFiyati(float alisFiyati) {
        this.alisFiyati = alisFiyati;
    }

    public String getIslemTarihi() {
        return islemTarihi;
    }

    public void setIslemTarihi(String islemTarihi) {
        this.islemTarihi = islemTarihi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }

    public String getUrunAdi() {
        return urunAdi;
    }

    public void setUrunAdi(String urunAdi) {
        this.urunAdi = urunAdi;
    }
}
