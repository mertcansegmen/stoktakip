package com.example.mert.stoktakip.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;

public class VeritabaniIslemleri extends SQLiteOpenHelper {

    // Veritabanı versiyonu
    private static final int VERITABANI_VERSION = 2;

    // Veritabanı ismi
    private static final String VERITABANI_ADI = "StokTakip.db";

    // Tablo isimleri
    private static final String TABLO_KULLANICI = "kullanici";
    private static final String TABLO_URUN = "urun";
    private static final String TABLO_URUN_ALIS = "urun_alis";
    private static final String TABLO_URUN_SATIS = "urun_satis";

    // kullanici tablosu sütun isimleri
    private static final String SUTUN_KULLANICI_ID = "id";
    private static final String SUTUN_KULLANICI_KADI = "kadi";
    private static final String SUTUN_KULLANICI_SIFRE = "sifre";

    // urun tablosu sütun isimleri
    private static final String SUTUN_URUN_ID = "barkod_no";
    private static final String SUTUN_URUN_AD = "ad";
    private static final String SUTUN_URUN_KALAN_ADET = "kalan_adet";
    private static final String SUTUN_URUN_FIYAT_ALIS = "alis_fiyati";
    private static final String SUTUN_URUN_FIYAT_SATIS = "satis_fiyati";

    // urun_alis tablosu sütun isimleri
    private static final String SUTUN_URUN_ALIS_ID = "id";
    private static final String SUTUN_URUN_ALIS_URUN_ID = "urun_id";
    private static final String SUTUN_URUN_ALIS_ADET = "adet";
    private static final String SUTUN_URUN_ALIS_FIYAT = "alis_fiyati";
    private static final String SUTUN_URUN_ALIS_TARIH = "alis_tarihi";
    private static final String SUTUN_URUN_ALIS_ACIKLAMA = "aciklama";

    // urun_satis tablosu sütun isimleri
    private static final String SUTUN_URUN_SATIS_ID = "id";
    private static final String SUTUN_URUN_SATIS_URUN_ID = "urun_id";
    private static final String SUTUN_URUN_SATIS_ADET = "adet";
    private static final String SUTUN_URUN_SATIS_FIYAT = "alis_fiyati";
    private static final String SUTUN_URUN_SATIS_TARIH = "alis_tarihi";
    private static final String SUTUN_URUN_SATIS_ACIKLAMA = "aciklama";

    // Tablo oluşturma sorguları

    // kullanici tablosunun oluşturma sorgusu
    private static final String KULLANICI_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_KULLANICI + "(" + SUTUN_KULLANICI_ID +
                                                            " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUTUN_KULLANICI_KADI + " TEXT, " +
                                                            SUTUN_KULLANICI_SIFRE + " TEXT" + ")";

    // urun tablosunun oluşturma sorgusu
    private static final String URUN_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_URUN + "(" + SUTUN_URUN_ID + " TEXT, " + SUTUN_URUN_AD + " TEXT, " +
                                                      SUTUN_URUN_KALAN_ADET + " INTEGER, " + SUTUN_URUN_FIYAT_ALIS + " INTEGER, " +
                                                      SUTUN_URUN_FIYAT_SATIS + " INTEGER" + ")";

    // urun_alis tablosunun olusturma sorgusu
    private static final String URUN_ALIS_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_URUN_ALIS + "(" + SUTUN_URUN_ALIS_ID +
                                                            " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUTUN_URUN_ALIS_URUN_ID +
                                                            " TEXT, " + SUTUN_URUN_ALIS_ADET + " INTEGER, " + SUTUN_URUN_ALIS_FIYAT +
                                                            " INTEGER, " + SUTUN_URUN_ALIS_TARIH + " TEXT, " + SUTUN_URUN_ALIS_ACIKLAMA + " TEXT" + ")";

    // urun_satis tablosunun olusturma sorgusu
    private static final String URUN_SATIS_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_URUN_SATIS + "(" + SUTUN_URUN_SATIS_ID +
                                                             " INTEGER PRIMARY KEY AUTOINCREMENT, " + SUTUN_URUN_SATIS_URUN_ID +
                                                             " TEXT, " + SUTUN_URUN_SATIS_ADET + " INTEGER, " + SUTUN_URUN_SATIS_FIYAT +
                                                             " INTEGER, " + SUTUN_URUN_SATIS_TARIH + " TEXT, " + SUTUN_URUN_SATIS_ACIKLAMA + " TEXT" + ")";

    // kullanici tablosunun silme sorgusu
    private static final String KULLANICI_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_KULLANICI;

    // urun tablosunun silme sorgusu
    private static final String URUN_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_URUN;

    // urun_alis tablosunun silme sorgusu
    private static final String URUN_ALIS_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_URUN_ALIS;

    // urun_satis tablosunun silme sorgusu
    private static final String URUN_SATIS_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_URUN_SATIS;

    //Yapıcı Metot
    public VeritabaniIslemleri(Context context) {
        super(context, VERITABANI_ADI, null, VERITABANI_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(KULLANICI_TABLOSU_OLUSTUR);
        db.execSQL(URUN_TABLOSU_OLUSTUR);
        db.execSQL(URUN_ALIS_TABLOSU_OLUSTUR);
        db.execSQL(URUN_SATIS_TABLOSU_OLUSTUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(KULLANICI_TABLOSU_SIL);
        db.execSQL(URUN_TABLOSU_SIL);
        db.execSQL(URUN_ALIS_TABLOSU_SIL);
        db.execSQL(URUN_SATIS_TABLOSU_SIL);
        onCreate(db);
    }

    /**
     *
     * kullanici tablosuyla ilgili metotlar
     *
     */

    public long kullaniciEkle(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_KULLANICI_KADI, kullanici.getKadi());
        values.put(SUTUN_KULLANICI_SIFRE, kullanici.getSifre());

        // degisenSatir -1 ise hata oluşmuştur.
        long degisenSatir = db.insert(TABLO_KULLANICI, null, values);
        db.close();
        return degisenSatir;

    }

    public void kullaniciSil(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLO_KULLANICI, SUTUN_KULLANICI_ID + " = ?", new String[]{String.valueOf(kullanici.getId())});
        db.close();
    }

    public int kullaniciGuncelle(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_KULLANICI_KADI, kullanici.getKadi());
        values.put(SUTUN_KULLANICI_SIFRE, kullanici.getSifre());

        int degisenSatir = db.update(TABLO_KULLANICI, values, SUTUN_KULLANICI_ID + " = ?",
                           new String[]{String.valueOf(kullanici.getId())});
        db.close();
        return degisenSatir;
    }

    // Kullanıcı adının var olup olmadığını kontrol eden metot
    public boolean kullaniciAdiniKontrolEt(String kadi){
        String[] sutunlar = {SUTUN_KULLANICI_ID};

        SQLiteDatabase db = this.getReadableDatabase();

        String secim = SUTUN_KULLANICI_KADI + " = ?";
        String[] secimOlcutleri = {kadi};

        Cursor cursor = db.query(TABLO_KULLANICI,       // işlem için kullanılacak tablo
                sutunlar,                               // geri dönecek sütunlar
                secim,                                  // WHERE için sütunlar
                secimOlcutleri,                         // WHERE için değerler
                null,
                null,
                null);
        int cursorSayisi = cursor.getCount();
        cursor.close();
        db.close();

        return cursorSayisi > 0;
    }

    // Kullanıcı adı ve şifrenin doğruluğunu kontrol eden metot
    public boolean girisBilgileriniKontrolEt(String kadi, String sifre) {

        String[] sutunlar = {SUTUN_KULLANICI_ID};
        SQLiteDatabase db = this.getReadableDatabase();
        String  secim = SUTUN_KULLANICI_KADI + " = ?" + " AND " + SUTUN_KULLANICI_SIFRE + " = ?";

        String[] secimOlcutleri = {kadi, sifre};

        Cursor cursor = db.query(TABLO_KULLANICI,       // işlem için kullanılacak tablo
                sutunlar,                               // geri dönecek sütunlar
                secim,                                  // WHERE için sütunlar
                secimOlcutleri,                         // WHERE için değerler
                null,
                null,
                null);

        int cursorSayisi = cursor.getCount();
        cursor.close();
        db.close();

        return cursorSayisi > 0;
    }

    /**
     *
     * urun tablosuyla ilgili metotlar
     *
     */

    public long urunEkle(Urun urun){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SUTUN_URUN_ID, urun.getBarkodNo());
        values.put(SUTUN_URUN_AD, urun.getAd());
        values.put(SUTUN_URUN_KALAN_ADET, urun.getAdet());
        // gelen değerler float, kuruş şekline çevrilip integer'a dönüştürülmesi gerekiyor. Veritabanında o şekilde saklanacak
        values.put(SUTUN_URUN_FIYAT_ALIS, Math.round(urun.getAlis()*100));
        values.put(SUTUN_URUN_FIYAT_SATIS, Math.round(urun.getSatis()*100));

        // degisenSatir -1 ise hata oluşmuştur
        long degisenSatir = db.insert(TABLO_URUN, null, values);
        db.close();
        return degisenSatir;
    }

    public void urunSil(String barkod){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLO_URUN, SUTUN_URUN_ID + " = ?", new String[]{String.valueOf(barkod)});
        db.close();
    }

    public int urunGuncelle(Urun urun){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_URUN_AD, urun.getAd());
        values.put(SUTUN_URUN_FIYAT_ALIS, urun.getAlis());
        values.put(SUTUN_URUN_FIYAT_SATIS, urun.getSatis());

        int degisenSatir = db.update(TABLO_URUN, values, SUTUN_URUN_ID + " = ?",
                new String[]{String.valueOf(urun.getBarkodNo())});
        db.close();
        return degisenSatir;
    }

    // Gelen barkoda sahip ürünün adetini günceller, artan parametresi true ise adeti arttırır, false ise azaltır
    public boolean urunAdetiGuncelle(String barkod, int adet) {
        SQLiteDatabase db = this.getWritableDatabase();
        try{
            String query = "UPDATE " + TABLO_URUN +
                           " SET " + SUTUN_URUN_KALAN_ADET + " = coalesce(" + SUTUN_URUN_KALAN_ADET + ", 0) + " + adet +
                           " WHERE " + SUTUN_URUN_ID + " = '" + barkod + "'";
            db.execSQL(query);
        } catch (Exception e) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public ArrayList<Urun> butunUrunleriGetir(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Urun> urunler = new ArrayList<>();
        String query = "SELECT * FROM " + TABLO_URUN;

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                Urun urun = new Urun();
                urun.setBarkodNo(c.getString(c.getColumnIndex(SUTUN_URUN_ID)));
                urun.setAd(c.getString(c.getColumnIndex(SUTUN_URUN_AD)));
                urun.setAdet(c.getInt(c.getColumnIndex(SUTUN_URUN_KALAN_ADET)));
                int alisFiyatiInt = c.getInt(c.getColumnIndex(SUTUN_URUN_FIYAT_ALIS));
                int satisFiyatiInt = c.getInt(c.getColumnIndex(SUTUN_URUN_FIYAT_SATIS));
                urun.setAlis(((float)alisFiyatiInt)/100);
                urun.setSatis(((float)satisFiyatiInt)/100);

                urunler.add(urun);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return urunler;
    }

    // Gelen barkodun veritabanında ekli olup olmadığını kontrol eden metot
    public boolean urunTekrariKontrolEt(String barkod){
        String[] sutunlar = {SUTUN_URUN_ID};

        SQLiteDatabase db = this.getReadableDatabase();

        String secim = SUTUN_URUN_ID + " = ?";
        String[] secimOlcutleri = {barkod};

        Cursor cursor = db.query(TABLO_URUN,            // işlem için kullanılacak tablo
                sutunlar,                               // geri dönecek sütunlar
                secim,                                  // WHERE için sütunlar
                secimOlcutleri,                         // WHERE için değerler
                null,
                null,
                null);
        int cursorSayisi = cursor.getCount();
        cursor.close();
        db.close();

        return cursorSayisi > 0;
    }

    // Gelen barkoda göre ürün getiren metot
    public Urun barkodaGoreUrunGetir(String barkod){
        String[] sutunlar = {"*"};

        SQLiteDatabase db = this.getReadableDatabase();

        String secim = SUTUN_URUN_ID + " = ?";
        String[] secimOlcutleri = {barkod};

        Urun urun = new Urun();

        Cursor cursor = db.query(TABLO_URUN,            // işlem için kullanılacak tablo
                sutunlar,                               // geri dönecek sütunlar
                secim,                                  // WHERE için sütunlar
                secimOlcutleri,                         // WHERE için değerler
                null,
                null,
                null);
        if (cursor.moveToFirst() && cursor.getCount() == 1) {
            urun.setBarkodNo(cursor.getString(cursor.getColumnIndex(SUTUN_URUN_ID)));
            urun.setAd(cursor.getString(cursor.getColumnIndex(SUTUN_URUN_AD)));
            urun.setAdet(cursor.getInt(cursor.getColumnIndex(SUTUN_URUN_KALAN_ADET)));
            int alisFiyatiInt = cursor.getInt(cursor.getColumnIndex(SUTUN_URUN_FIYAT_ALIS));
            int satisFiyatiInt = cursor.getInt(cursor.getColumnIndex(SUTUN_URUN_FIYAT_SATIS));
            urun.setAlis(((float)alisFiyatiInt)/100);
            urun.setSatis(((float)satisFiyatiInt)/100);
        }
        cursor.close();
        db.close();
        return urun;
    }

    /**
     *
     * urun_alis tablosuyla ilgili metotlar
     *
     */

    public long urunAlisEkle(UrunAlis urunAlis){
        long degisenSatir = 0;
        return degisenSatir;
    }

    /**
     *
     * urun_satis tablosuyla ilgili metotlar
     *
     */

    public long urunSatisEkle(UrunAlis urunSatis){
        long degisenSatir = 0;
        return degisenSatir;
    }
}
