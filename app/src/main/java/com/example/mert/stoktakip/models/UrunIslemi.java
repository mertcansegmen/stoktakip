package com.example.mert.stoktakip.models;

public class UrunIslemi {
    private int id;
    private String islemTuru;
    private String barkodNo;
    private String kadi;
    private int adet;
    private float urunFiyati;
    private String islemTarihi;
    private String aciklama;
    private String urunAdi;

    public UrunIslemi(){}

    public UrunIslemi(String islemTuru, String barkodNo, String kadi, int adet, float urunFiyati, String islemTarihi, String urunAdi) {
        this.islemTuru = islemTuru;
        this.barkodNo = barkodNo;
        this.kadi = kadi;
        this.adet = adet;
        this.urunFiyati = urunFiyati;
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

    public float getUrunFiyati() {
        return urunFiyati;
    }

    public void setUrunFiyati(float urunFiyati) {
        this.urunFiyati = urunFiyati;
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
