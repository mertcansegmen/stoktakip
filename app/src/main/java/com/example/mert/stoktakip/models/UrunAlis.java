package com.example.mert.stoktakip.models;

public class UrunAlis {
    private int id;
    private String barkodNo;
    private String kadi;
    private int adet;
    private float alisFiyati;
    private String alisTarihi;
    private String aciklama;

    public UrunAlis() {}

    public UrunAlis(String barkodNo, String kadi, int adet, float alisFiyati, String alisTarihi, String aciklama) {
        this.barkodNo = barkodNo;
        this.kadi = kadi;
        this.adet = adet;
        this.alisFiyati = alisFiyati;
        this.alisTarihi = alisTarihi;
        this.aciklama = aciklama;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setAlisFiyati(float alisFiyati) {
        this.alisFiyati = alisFiyati;
    }

    public String getAlisTarihi() {
        return alisTarihi;
    }

    public void setAlisTarihi(String alisTarihi) {
        this.alisTarihi = alisTarihi;
    }

    public String getAciklama() {
        return aciklama;
    }

    public void setAciklama(String aciklama) {
        this.aciklama = aciklama;
    }
}
