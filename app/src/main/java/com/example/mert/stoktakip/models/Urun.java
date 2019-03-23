package com.example.mert.stoktakip.models;

public class Urun {
    private String barkodNo;
    private String ad;
    private int adet;
    private float alis;
    private float satis;
    private String alisSatis;

    public Urun(){}

    public Urun(String barkodNo, String ad){
        this.barkodNo = barkodNo;
        this.ad = ad;
    }

    public Urun(String barkodNo, String ad, int adet, float alis, float satis) {
        this.barkodNo = barkodNo;
        this.ad = ad;
        this.adet = adet;
        this.alis = alis;
        this.satis = satis;
        this.alisSatis = alis + "/" + satis;
    }

    public String getBarkodNo() {
        return barkodNo;
    }

    public void setBarkodNo(String barkodNo) {
        this.barkodNo = barkodNo;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public int getAdet() {
        return adet;
    }

    public void setAdet(int adet) {
        this.adet = adet;
    }

    public float getAlis() {
        return alis;
    }

    public void setAlis(float alis) {
        this.alis = alis;
    }

    public float getSatis() {
        return satis;
    }

    public void setSatis(float satis) {
        this.satis = satis;
    }

    public String getAlisSatis() {
        return alisSatis;
    }

    public void setAlisSatis(String alisSatis) {
        this.alisSatis = alisSatis;
    }
}
