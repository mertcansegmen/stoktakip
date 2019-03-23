package com.example.mert.stoktakip.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VeritabaniYonetici extends SQLiteOpenHelper {

    private static final int VERITABANI_VERSION = 1;

    private static final String VERITABANI_ADI = "StokTakip.db";

    private static final String TABLO_ADI = "kullanici";

    private static final String SUTUN_KULLANICI_ID = "id";
    private static final String SUTUN_KULLANICI_ADI = "kadi";
    private static final String SUTUN_KULLANICI_SIFRE = "sifre";

    private String KULLANICI_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_ADI + "(" + SUTUN_KULLANICI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                               SUTUN_KULLANICI_ADI + " TEXT, " + SUTUN_KULLANICI_SIFRE + " TEXT" + ")";

    private String KULLANICI_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_ADI;

    //Yapıcı Metot
    public VeritabaniYonetici(Context context) {
        super(context, VERITABANI_ADI, null, VERITABANI_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(KULLANICI_TABLOSU_OLUSTUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(KULLANICI_TABLOSU_SIL);
        onCreate(db);
    }

    public long kullaniciEkle(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_KULLANICI_ADI, kullanici.getKadi());
        values.put(SUTUN_KULLANICI_SIFRE, kullanici.getSifre());

        // degisenSatir -1 ise hata oluşmuştur.
        long degisenSatir = db.insert(TABLO_ADI, null, values);
        db.close();
        return degisenSatir;

    }

    public void kullaniciSil(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLO_ADI, SUTUN_KULLANICI_ID + " = ?", new String[]{String.valueOf(kullanici.getId())});
        db.close();
    }

    public int kullaniciGuncelle(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_KULLANICI_ADI, kullanici.getKadi());
        values.put(SUTUN_KULLANICI_SIFRE, kullanici.getSifre());

        int degisenSatir = db.update(TABLO_ADI, values, SUTUN_KULLANICI_ID + " = ?",
                           new String[]{String.valueOf(kullanici.getId())});
        db.close();
        return degisenSatir;
    }

    // Kullanıcı adının var olup olmadığını kontrol eden metot
    public boolean kullaniciKontrolEt(String kadi){
        String[] sutunlar = {SUTUN_KULLANICI_ID};

        SQLiteDatabase db = this.getReadableDatabase();

        String secim = SUTUN_KULLANICI_ADI + " = ?";
        String[] secimOlcutleri = {kadi};

        Cursor cursor = db.query(TABLO_ADI,     // işlem için kullanılacak tablo
                sutunlar,                       // geri dönecek sütunlar
                secim,                          // WHERE için sütunlar
                secimOlcutleri,                 // WHERE için değerler
                null,
                null,
                null);
        int cursorSayisi = cursor.getCount();
        cursor.close();
        db.close();

        if(cursorSayisi > 0){
            return true;
        }
        return false;
    }


    public boolean kullaniciKontrolEt(String kadi, String sifre) {

        String[] sutunlar = {SUTUN_KULLANICI_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String  secim = SUTUN_KULLANICI_ADI + " = ?" + " AND " + SUTUN_KULLANICI_SIFRE + " = ?";

        String[] secimOlcutleri = {kadi, sifre};

        Cursor cursor = db.query(TABLO_ADI,     // işlem için kullanılacak tablo
                sutunlar,                       // geri dönecek sütunlar
                secim,                          // WHERE için sütunlar
                secimOlcutleri,                 // WHERE için değerler
                null,
                null,
                null);

        int cursorSayisi = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorSayisi > 0) {
            return true;
        }
        return false;
    }
}
