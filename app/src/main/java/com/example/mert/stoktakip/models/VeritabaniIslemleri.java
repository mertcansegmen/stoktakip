package com.example.mert.stoktakip.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
    public static final String URUN_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_URUN + "(" + SUTUN_URUN_ID + " TEXT, " + SUTUN_URUN_AD + " TEXT, " +
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

        if(cursorSayisi > 0){
            return true;
        }
        return false;
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

        if (cursorSayisi > 0) {
            return true;
        }
        return false;
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
        values.put(SUTUN_URUN_AD, urun.getAdet());
        // gelen değerler float, kuruş şekline çevrilip integer'a dönüştürülmesi gerekiyor. Veritabanında o şekilde saklanacak
        values.put(SUTUN_URUN_FIYAT_ALIS, Math.round(urun.getAlis()*100));
        values.put(SUTUN_URUN_FIYAT_SATIS, Math.round(urun.getSatis()*100));

        // degisenSatir -1 ise hata oluşmuştur
        long degisenSatir = db.insert(TABLO_URUN, null, values);
        db.close();
        return degisenSatir;
    }

    public void urunSil(Urun urun){

    }

    public int urunGuncelle(Urun urun){
        int degisenSatir = 0;
        return degisenSatir;
    }

    public Urun[] butunUrunleriGetir(){
        return new Urun[1];
    }

    public Urun barkodaGoreUrunGetir(String barkod){
        return new Urun();
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
