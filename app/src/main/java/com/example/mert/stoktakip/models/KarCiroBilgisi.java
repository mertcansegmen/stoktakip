package com.example.mert.stoktakip.models;

/**
 * {@code KarCiroBilgisi} sınıfı bir zaman aralığı içinde yapılan ciro ve kar miktarını tutuyor.
 * Örneğin:
 * Zaman - Kar(TL) - Ciro(TL)
 * 15 Haz - 728.30 - 140.95
 * Aralık - 12573.15 - 1128.00
 * 2019 - 95833.45 - 7985.20
 */
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