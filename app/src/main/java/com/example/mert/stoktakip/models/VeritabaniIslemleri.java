package com.example.mert.stoktakip.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class VeritabaniIslemleri extends SQLiteOpenHelper {

    // Veritabanı versiyonu
    private static final int VERITABANI_VERSION = 7;

    // Veritabanı ismi
    private static final String VERITABANI_ADI = "StokTakip.db";

    // Tablo isimleri
    private static final String TABLO_KULLANICI = "kullanici";
    private static final String TABLO_URUN = "urun";
    private static final String TABLO_URUN_ISLEMI = "urun_islemi";

    // kullanici tablosu sütun isimleri
    private static final String SUTUN_KULLANICI_KADI = "kadi";
    private static final String SUTUN_KULLANICI_SIFRE = "sifre";

    // urun tablosu sütun isimleri
    private static final String SUTUN_URUN_ID = "barkod_no";
    private static final String SUTUN_URUN_AD = "ad";
    private static final String SUTUN_URUN_KALAN_ADET = "kalan_adet";
    private static final String SUTUN_URUN_FIYAT_ALIS = "alis_fiyati";
    private static final String SUTUN_URUN_FIYAT_SATIS = "satis_fiyati";

    // urun_islemi sütun isimleri
    private static final String SUTUN_URUN_ISLEMI_ID = "id";
    private static final String SUTUN_URUN_ISLEMI_ISLEM_TURU = "islem_turu";
    private static final String SUTUN_URUN_ISLEMI_URUN_ID = "urun_id";
    private static final String SUTUN_URUN_ISLEMI_KULLANICI_ID = "kullanici_id";
    private static final String SUTUN_URUN_ISLEMI_ADET = "adet";
    private static final String SUTUN_URUN_ISLEMI_URUN_FIYATI = "urun_fiyati";
    private static final String SUTUN_URUN_ISLEMI_ISLEM_TARIHI = "islem_tarihi";
    private static final String SUTUN_URUN_ISLEMI_ACIKLAMA = "aciklama";

    // Tablo oluşturma sorguları

    // kullanici tablosunun oluşturma sorgusu
    private static final String KULLANICI_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_KULLANICI + "(" +
                                                            SUTUN_KULLANICI_KADI + " TEXT PRIMARY KEY NOT NULL, " +
                                                            SUTUN_KULLANICI_SIFRE + " TEXT NOT NULL" + ")";

    // urun tablosunun oluşturma sorgusu
    private static final String URUN_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_URUN + "(" +
                                                       SUTUN_URUN_ID + " TEXT PRIMARY KEY NOT NULL, " +
                                                       SUTUN_URUN_AD + " TEXT NOT NULL, " +
                                                       SUTUN_URUN_KALAN_ADET + " INTEGER DEFAULT 0, " +
                                                       SUTUN_URUN_FIYAT_ALIS + " INTEGER, " +
                                                       SUTUN_URUN_FIYAT_SATIS + " INTEGER" + ")";

    private static final String URUN_ISLEMI_TABLOSU_OLUSTUR = "CREATE TABLE " + TABLO_URUN_ISLEMI + "(" +
                                                              SUTUN_URUN_ISLEMI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                              SUTUN_URUN_ISLEMI_ISLEM_TURU + " TEXT NOT NULL, " +
                                                              SUTUN_URUN_ISLEMI_URUN_ID + " TEXT NOT NULL, " +
                                                              SUTUN_URUN_ISLEMI_KULLANICI_ID + " TEXT NOT NULL, " +
                                                              SUTUN_URUN_ISLEMI_ADET + " INTEGER NOT NULL, " +
                                                              SUTUN_URUN_ISLEMI_URUN_FIYATI + " INTEGER, " +
                                                              SUTUN_URUN_ISLEMI_ISLEM_TARIHI + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                                                              SUTUN_URUN_ISLEMI_ACIKLAMA + " TEXT, " +
                                                              "FOREIGN KEY(" + SUTUN_URUN_ISLEMI_URUN_ID + ") REFERENCES " + TABLO_URUN + "(" + SUTUN_URUN_ID + ")," +
                                                              "FOREIGN KEY(" + SUTUN_URUN_ISLEMI_KULLANICI_ID + ") REFERENCES " + TABLO_KULLANICI + "(" + SUTUN_KULLANICI_KADI + "))";

    // kullanici tablosunun silme sorgusu
    private static final String KULLANICI_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_KULLANICI;

    // urun tablosunun silme sorgusu
    private static final String URUN_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_URUN;

    // urun_islemi tablosunun silme sorgusu
    private static final String URUN_ISLEMI_TABLOSU_SIL = "DROP TABLE IF EXISTS " + TABLO_URUN_ISLEMI;

    //Yapıcı Metot
    public VeritabaniIslemleri(Context context) {
        super(context, VERITABANI_ADI, null, VERITABANI_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(KULLANICI_TABLOSU_OLUSTUR);
        db.execSQL(URUN_TABLOSU_OLUSTUR);
        db.execSQL(URUN_ISLEMI_TABLOSU_OLUSTUR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(KULLANICI_TABLOSU_SIL);
        db.execSQL(URUN_TABLOSU_SIL);
        db.execSQL(URUN_ISLEMI_TABLOSU_SIL);
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
        long hataKontrolu = db.insert(TABLO_KULLANICI, null, values);
        db.close();
        return hataKontrolu;
    }

    public void kullaniciSil(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLO_KULLANICI, SUTUN_KULLANICI_KADI + " = ?", new String[]{kullanici.getKadi()});
        db.close();
    }

    public int kullaniciGuncelle(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_KULLANICI_KADI, kullanici.getKadi());
        values.put(SUTUN_KULLANICI_SIFRE, kullanici.getSifre());

        int degisenSatir = db.update(TABLO_KULLANICI, values, SUTUN_KULLANICI_KADI + " = ?",
                           new String[]{kullanici.getKadi()});
        db.close();
        return degisenSatir;
    }

    // Kullanıcı adının var olup olmadığını kontrol eden metot
    public boolean kullaniciAdiniKontrolEt(String kadi){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] sutunlar = {SUTUN_KULLANICI_KADI};
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
        SQLiteDatabase db = this.getReadableDatabase();

        String[] sutunlar = {SUTUN_KULLANICI_KADI};
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
        values.put(SUTUN_URUN_FIYAT_ALIS, Math.round(urun.getAlis()*100));
        values.put(SUTUN_URUN_FIYAT_SATIS, Math.round(urun.getSatis()*100));

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
        SQLiteDatabase db = this.getReadableDatabase();

        String[] sutunlar = {SUTUN_URUN_ID};
        String secim = SUTUN_URUN_ID + " = ?";
        String[] secimOlcutleri = {barkod};

        int cursorSayisi = 0;

        if(barkod != null) {
            Cursor cursor = db.query(TABLO_URUN,            // işlem için kullanılacak tablo
                    sutunlar,                               // geri dönecek sütunlar
                    secim,                                  // WHERE için sütunlar
                    secimOlcutleri,                         // WHERE için değerler
                    null,
                    null,
                    null);

            cursorSayisi = cursor.getCount();
            cursor.close();
            db.close();
        }

        return cursorSayisi > 0;
    }

    // Gelen barkoda göre ürün getiren metot
    public Urun barkodaGoreUrunGetir(String barkod){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] sutunlar = {"*"};
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

    // Gelen isime göre ürün getiren metot
    public Urun isimeGoreUrunGetir(String isim){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] sutunlar = {"*"};
        String secim = SUTUN_URUN_AD + " = ?";
        String[] secimOlcutleri = {isim};

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

    // Kalan adeti azalan ürün eşiğinin altında olan ürünleri getiren metot
    public ArrayList<Urun> azalanUrunGetir(int esik){
        SQLiteDatabase db = this.getReadableDatabase();
        
        ArrayList<Urun> urunler = new ArrayList<>();
        String query = "SELECT * FROM " + TABLO_URUN + " WHERE " + SUTUN_URUN_KALAN_ADET + " <= " + esik;

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

    /**
     *
     * urun_islemi tablosuyla ilgili metotlar
     *
     */

    public long urunIslemiEkle(UrunIslemi urunIslemi){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_URUN_ISLEMI_ISLEM_TURU, urunIslemi.getIslemTuru());
        values.put(SUTUN_URUN_ISLEMI_URUN_ID, urunIslemi.getBarkodNo());
        values.put(SUTUN_URUN_ISLEMI_KULLANICI_ID, urunIslemi.getKadi());
        values.put(SUTUN_URUN_ISLEMI_ADET, urunIslemi.getAdet());
        // gelen değer float, kuruş şekline çevrilip integer'a dönüştürülmesi gerekiyor. Veritabanında o şekilde saklanacak
        values.put(SUTUN_URUN_ISLEMI_URUN_FIYATI, Math.round(urunIslemi.getUrunFiyati()*100));
        values.put(SUTUN_URUN_ISLEMI_ACIKLAMA, urunIslemi.getAciklama());

        // degisenSatir -1 ise hata oluşmuştur
        long degisenSatir = db.insert(TABLO_URUN_ISLEMI, null, values);
        db.close();
        return degisenSatir;
    }

    public ArrayList<UrunIslemi> urunIslemiGecmisiFiltrele() {
        String baslangicTarihi = "2019-01-01";
        String bitisTarihi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        return urunIslemleriniGetir(baslangicTarihi, bitisTarihi, "%");
    }

    public ArrayList<UrunIslemi> urunIslemiGecmisiFiltrele(String baslangicTarihi){
        String bitisTarihi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        return urunIslemleriniGetir(baslangicTarihi, bitisTarihi, "%");
    }

    public ArrayList<UrunIslemi> urunIslemiGecmisiFiltrele(String baslangicTarihi, String bitisTarihi){
        return urunIslemleriniGetir(baslangicTarihi, bitisTarihi, "%");
    }

    public ArrayList<UrunIslemi> urunIslemiGecmisiFiltrele(String baslangicTarihi, String bitisTarihi, String islemTuru){
        return urunIslemleriniGetir(baslangicTarihi, bitisTarihi, islemTuru);
    }

    private ArrayList<UrunIslemi> urunIslemleriniGetir(String baslangicTarihi, String bitisTarihi, String islemTuru){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<UrunIslemi> islemler = new ArrayList<>();

        baslangicTarihi += " 00:00:00";
        bitisTarihi += " 23:59:59";

        String query = "SELECT " + SUTUN_URUN_ISLEMI_ID + ", " + SUTUN_URUN_ISLEMI_ISLEM_TURU + ", " +
                        SUTUN_URUN_ISLEMI_URUN_ID + ", " + SUTUN_URUN_ISLEMI_KULLANICI_ID + ", " +
                        SUTUN_URUN_ISLEMI_ADET + ", " + SUTUN_URUN_ISLEMI_URUN_FIYATI+", datetime(" +
                        SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ", 'localtime'), " + SUTUN_URUN_ISLEMI_ACIKLAMA +
                        " FROM " + TABLO_URUN_ISLEMI +
                        " WHERE " + SUTUN_URUN_ISLEMI_ISLEM_TARIHI + " BETWEEN '" +
                        baslangicTarihi + "' AND '" + bitisTarihi + "' AND " +
                        SUTUN_URUN_ISLEMI_ISLEM_TURU + " LIKE '" + islemTuru +
                        "' ORDER BY datetime(" + SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ") DESC" ;

        Log.d("mert", query);

        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()){
            do{
                int urunIslemiId = c.getInt(c.getColumnIndex(SUTUN_URUN_ISLEMI_ID));
                String tur = c.getString(c.getColumnIndex(SUTUN_URUN_ISLEMI_ISLEM_TURU));
                String barkod = c.getString(c.getColumnIndex(SUTUN_URUN_ISLEMI_URUN_ID));
                String urunAdi = barkodaGoreUrunGetir(barkod).getAd();
                String tarih = c.getString(c.getColumnIndex("datetime(" + SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ", 'localtime')"));
                int adet = c.getInt(c.getColumnIndex(SUTUN_URUN_ISLEMI_ADET));
                int fiyat = c.getInt(c.getColumnIndex(SUTUN_URUN_ISLEMI_URUN_FIYATI));
                String kadi = c.getString(c.getColumnIndex(SUTUN_URUN_ISLEMI_KULLANICI_ID));

                UrunIslemi islem = new UrunIslemi(urunIslemiId, tur, barkod, kadi, adet, ((float)fiyat)/100, tarih, urunAdi);
                islemler.add(islem);

            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return islemler;
    }

    public UrunIslemi urunIslemiGetir(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] sutunlar = {SUTUN_URUN_ISLEMI_ID, SUTUN_URUN_ISLEMI_ISLEM_TURU,
                             SUTUN_URUN_ISLEMI_URUN_ID, SUTUN_URUN_ISLEMI_KULLANICI_ID,
                             SUTUN_URUN_ISLEMI_ADET, SUTUN_URUN_ISLEMI_URUN_FIYATI, "datetime(" +
                             SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ", 'localtime')",SUTUN_URUN_ISLEMI_ACIKLAMA};
        String secim = SUTUN_URUN_ISLEMI_ID + " = ?";
        String[] secimOlcutleri = {String.valueOf(id)};

        Cursor cursor = db.query(TABLO_URUN_ISLEMI,
                sutunlar,
                secim,
                secimOlcutleri,
                null,
                null,
                null);

        UrunIslemi urunIslemi = new UrunIslemi();

        if(cursor.moveToFirst() && cursor.getCount() == 1){
            urunIslemi.setId(cursor.getInt(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_ID)));
            urunIslemi.setIslemTuru(cursor.getString(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_ISLEM_TURU)));
            urunIslemi.setBarkodNo(cursor.getString(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_URUN_ID)));
            urunIslemi.setKadi(cursor.getString(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_KULLANICI_ID)));
            urunIslemi.setAdet(cursor.getInt(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_ADET)));
            urunIslemi.setUrunFiyati((float)(cursor.getInt(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_URUN_FIYATI))) / 100);
            urunIslemi.setIslemTarihi(cursor.getString(cursor.getColumnIndex("datetime(" +
                    SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ", 'localtime')")));
            urunIslemi.setAciklama(cursor.getString(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_ACIKLAMA)));
        }
        cursor.close();
        db.close();
        return urunIslemi;
    }
}
