package com.example.mert.stoktakip.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mert.stoktakip.utils.ZamanFormatlayici;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class VeritabaniIslemleri extends SQLiteOpenHelper {

    // Veritabanı versiyonu
    private static final int VERITABANI_VERSION = 8;

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
    private static final String SUTUN_URUN_ISLEMI_ALIS_FIYATI = "alis_fiyati";
    private static final String SUTUN_URUN_ISLEMI_SATIS_FIYATI = "satis_fiyati";
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
                                                              SUTUN_URUN_ISLEMI_ALIS_FIYATI + " INTEGER, " +
                                                              SUTUN_URUN_ISLEMI_SATIS_FIYATI + " INTEGER, " +
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

    // Yapıcı Metot
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

    // Yeni kullanıcı ekler
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

    // Gelen kullanıcıyı siler
    public void kullaniciSil(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLO_KULLANICI, SUTUN_KULLANICI_KADI + " = ?", new String[]{kullanici.getKadi()});
        db.close();
    }

    // Gelen kullanıcıyı günceller
    public int kullaniciGuncelle(Kullanici kullanici){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_KULLANICI_SIFRE, kullanici.getSifre());

        int degisenSatir = db.update(TABLO_KULLANICI, values, SUTUN_KULLANICI_KADI + " = ?",
                           new String[]{kullanici.getKadi()});
        db.close();
        Log.i("mert", degisenSatir + " ");
        return degisenSatir;
    }

    // Kullanıcı adının var olup olmadığını kontrol eder
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

    // Kullanıcı adı ve şifrenin doğruluğunu kontrol eder
    public boolean girisBilgileriniKontrolEt(Kullanici kullanici) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] sutunlar = {SUTUN_KULLANICI_KADI};
        String  secim = SUTUN_KULLANICI_KADI + " = ?" + " AND " + SUTUN_KULLANICI_SIFRE + " = ?";
        String[] secimOlcutleri = {kullanici.getKadi(), kullanici.getSifre()};

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

    // Yeni ürün ekler
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

    // Gelen ürünü siler
    public void urunSil(String barkod){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLO_URUN, SUTUN_URUN_ID + " = ?", new String[]{String.valueOf(barkod)});
        db.close();
    }

    // Gelen ürünü günceller
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

    // Gelen barkoda sahip ürünün adetini günceller
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

    // Bütün ürünleri getirir
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

    // Gelen barkodun veritabanında ekli olup olmadığını kontrol eder
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

    // Gelen barkoda göre ürün bilgilerini getirir
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

    // Gelen isime göre ürün bilgilerini getirir
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

    // Yeni ürün işlemi ekler
    public long urunIslemiEkle(UrunIslemi urunIslemi){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SUTUN_URUN_ISLEMI_ISLEM_TURU, urunIslemi.getIslemTuru());
        values.put(SUTUN_URUN_ISLEMI_URUN_ID, urunIslemi.getBarkodNo());
        values.put(SUTUN_URUN_ISLEMI_KULLANICI_ID, urunIslemi.getKadi());
        values.put(SUTUN_URUN_ISLEMI_ADET, urunIslemi.getAdet());
        // gelen değer float, kuruş şekline çevrilip integer'a dönüştürülmesi gerekiyor. Veritabanında o şekilde saklanacak
        values.put(SUTUN_URUN_ISLEMI_ALIS_FIYATI, Math.round(urunIslemi.getAlisFiyati()*100));
        values.put(SUTUN_URUN_ISLEMI_SATIS_FIYATI, Math.round(urunIslemi.getSatisFiyati()*100));
        values.put(SUTUN_URUN_ISLEMI_ACIKLAMA, urunIslemi.getAciklama());

        // degisenSatir -1 ise hata oluşmuştur
        long degisenSatir = db.insert(TABLO_URUN_ISLEMI, null, values);
        db.close();
        return degisenSatir;
    }

    // Filtre olmadan bütün işlemleri getirir
    public ArrayList<UrunIslemi> urunIslemiGecmisiFiltrele() {
        // Başlangıç tarihi verilmediği için 1 Ocak 2019 olarak kabul edilir
        String baslangicTarihi = "2019-01-01";
        // Bitiş tarihi verilmediği için o günkü tarih olarak kabul edilir
        String bitisTarihi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        return urunIslemleriniGetir(baslangicTarihi, bitisTarihi, "%");
    }

    // Sadece başlangıç tarihi bilinmesi durumunda, verilen tarihle şuanki tarih arasda yapılan işlemleri getirir
    public ArrayList<UrunIslemi> urunIslemiGecmisiFiltrele(String baslangicTarihi){
        // Bitiş tarihi verilmediği için o günkü tarih olarak kabul edilir
        String bitisTarihi = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime());
        return urunIslemleriniGetir(baslangicTarihi, bitisTarihi, "%");
    }

    // Başlangıç ve bitiş tarihleri arasındaki işlemleri getirir
    public ArrayList<UrunIslemi> urunIslemiGecmisiFiltrele(String baslangicTarihi, String bitisTarihi){
        return urunIslemleriniGetir(baslangicTarihi, bitisTarihi, "%");
    }

    // Başlangıç ve bitiş tarihleri arasında yapılmış sadece alım ya da satım işlemlerini getirir
    public ArrayList<UrunIslemi> urunIslemiGecmisiFiltrele(String baslangicTarihi, String bitisTarihi, String islemTuru){
        return urunIslemleriniGetir(baslangicTarihi, bitisTarihi, islemTuru);
    }

    // Başlangıç tarihi, bitiş tarihi ve işlem türüne göre ürün işlemlerini getirir.
    private ArrayList<UrunIslemi> urunIslemleriniGetir(String baslangicTarihi, String bitisTarihi, String islemTuru){
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<UrunIslemi> islemler = new ArrayList<>();

        baslangicTarihi += " 00:00:00";
        bitisTarihi += " 23:59:59";

        String query = "SELECT " + SUTUN_URUN_ISLEMI_ID + ", " + SUTUN_URUN_ISLEMI_ISLEM_TURU + ", " +
                        SUTUN_URUN_ISLEMI_URUN_ID + ", " + SUTUN_URUN_ISLEMI_KULLANICI_ID + ", " +
                        SUTUN_URUN_ISLEMI_ADET + ", " + SUTUN_URUN_ISLEMI_ALIS_FIYATI + "," +
                        SUTUN_URUN_ISLEMI_SATIS_FIYATI + ", datetime(" +
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
                int alis = c.getInt(c.getColumnIndex(SUTUN_URUN_ISLEMI_ALIS_FIYATI));
                int satis = c.getInt(c.getColumnIndex(SUTUN_URUN_ISLEMI_SATIS_FIYATI));
                String kadi = c.getString(c.getColumnIndex(SUTUN_URUN_ISLEMI_KULLANICI_ID));

                UrunIslemi islem = new UrunIslemi(urunIslemiId, tur, barkod, kadi, adet, ((float)alis)/100, ((float)satis)/100, tarih, urunAdi);
                islemler.add(islem);

            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return islemler;
    }

    // id'si verilen ürün işlemi bilgilerini getirir
    public UrunIslemi urunIslemiGetir(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] sutunlar = {SUTUN_URUN_ISLEMI_ID, SUTUN_URUN_ISLEMI_ISLEM_TURU,
                             SUTUN_URUN_ISLEMI_URUN_ID, SUTUN_URUN_ISLEMI_KULLANICI_ID,
                             SUTUN_URUN_ISLEMI_ADET, SUTUN_URUN_ISLEMI_ALIS_FIYATI,
                             SUTUN_URUN_ISLEMI_SATIS_FIYATI, "datetime(" +
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
            urunIslemi.setAlisFiyati((float)(cursor.getInt(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_ALIS_FIYATI))) / 100);
            urunIslemi.setSatisFiyati((float)(cursor.getInt(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_SATIS_FIYATI))) / 100);
            urunIslemi.setIslemTarihi(cursor.getString(cursor.getColumnIndex("datetime(" +
                    SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ", 'localtime')")));
            urunIslemi.setAciklama(cursor.getString(cursor.getColumnIndex(SUTUN_URUN_ISLEMI_ACIKLAMA)));
        }
        cursor.close();
        db.close();
        return urunIslemi;
    }

    // En az 1 satış yapılan günler için ciro ve kar bilgilerini getirir
    public ArrayList<KarCiroBilgisi> gunlukKarCiroBilgileriniGetir(){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT date(" + SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ", 'localtime'), " +
                       "SUM(" + SUTUN_URUN_ISLEMI_ADET + " * " + SUTUN_URUN_ISLEMI_SATIS_FIYATI + "), " +
                       "SUM(" + SUTUN_URUN_ISLEMI_ADET + " * (" +
                       SUTUN_URUN_ISLEMI_SATIS_FIYATI + " - " + SUTUN_URUN_ISLEMI_ALIS_FIYATI + ")) FROM " +
                       TABLO_URUN_ISLEMI + " WHERE " + SUTUN_URUN_ISLEMI_ISLEM_TURU + " = 'out' " +
                       "GROUP BY date(" + SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ", 'localtime')";

        Cursor c = db.rawQuery(query, null);
        ArrayList<KarCiroBilgisi> karCiroBilgileri = new ArrayList<>();
        ZamanFormatlayici zf = new ZamanFormatlayici();
        if(c.moveToFirst()){
            do{
                int ciro = c.getInt(c.getColumnIndex("SUM(" + SUTUN_URUN_ISLEMI_ADET + " * " +
                        SUTUN_URUN_ISLEMI_SATIS_FIYATI + ")"));
                int kar = c.getInt(c.getColumnIndex("SUM(" + SUTUN_URUN_ISLEMI_ADET + " * (" +
                        SUTUN_URUN_ISLEMI_SATIS_FIYATI + " - " + SUTUN_URUN_ISLEMI_ALIS_FIYATI + "))"));
                String zaman = c.getString(c.getColumnIndex("date(" +
                        SUTUN_URUN_ISLEMI_ISLEM_TARIHI + ", 'localtime')"));
                KarCiroBilgisi karCiroBilgisi = new KarCiroBilgisi((float)ciro/100, (float)kar/100,
                                        zf.zamanFormatla(zaman, "yyyy-MM-dd", "d MMM"));
                karCiroBilgileri.add(karCiroBilgisi);
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return karCiroBilgileri;
    }

    // Her bir ürünün sağladığı toplam getiriyi hesaplar. Ürün ile yapılan her satış işlemi için
    // ürün adeti * ( satış fiyatı - alış fiyatı ) formülüyle bulunan sonuçlar toplanıyor
    public ArrayList<UrunGetirisi> urunGetirileriniGetir(){
        SQLiteDatabase db = this.getReadableDatabase();

        // Eğer daha önceden satış işlemi yapılmış ve sonrasında urun tablosundan silinmiş bir ürün varsa,
        // urun_islemi tablosundaki barkod numarasıyla urun tablosunda bulunmayan bir ürünün
        // adı çekilmeye çalışıldığı için hata alınıyor. Bunu engellemek için önce ürün tablosunda
        // o barkoda uyan bir ürün var mı diye kontrol ediliyor, yoksa ürün adı olarak
        // "Silinmiş Ürün" stringi döndürülüyor.
        String query = "SELECT " +
                       "CASE WHEN "  +
                       "EXISTS(SELECT " + SUTUN_URUN_AD + " FROM " + TABLO_URUN +
                       " WHERE " + SUTUN_URUN_ID + " = " + SUTUN_URUN_ISLEMI_URUN_ID + ")" +
                       " THEN (SELECT " + SUTUN_URUN_AD + " FROM " + TABLO_URUN +
                       " WHERE " + SUTUN_URUN_ID + " = " + SUTUN_URUN_ISLEMI_URUN_ID + ")" +
                       "ELSE 'Silinmiş Ürün' " +
                       "END AS urun_adi, " +
                       "SUM(" + SUTUN_URUN_ISLEMI_ADET + " * (" + SUTUN_URUN_ISLEMI_SATIS_FIYATI + " - " +
                       SUTUN_URUN_ISLEMI_ALIS_FIYATI + ")) AS getiri " +
                       "FROM " + TABLO_URUN_ISLEMI +
                       " WHERE " + SUTUN_URUN_ISLEMI_ISLEM_TURU + " = 'out' " +
                       "GROUP BY " + SUTUN_URUN_ISLEMI_URUN_ID +
                       " ORDER BY getiri ASC";
        Cursor c = db.rawQuery(query, null);
        ArrayList<UrunGetirisi> urunGetirileri = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                String ad = c.getString(c.getColumnIndex("urun_adi"));
                int getiri = c.getInt(c.getColumnIndex("getiri"));
                UrunGetirisi urunGetirisi = new UrunGetirisi(ad, (float) getiri / 100);
                urunGetirileri.add(urunGetirisi);
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return urunGetirileri;
    }

    // Her kullanıcının sağladığı toplam getiriyi hesaplar. Kullanıcının yaptığı her satış işlemi için
    // ürün adeti * ( satış fiyatı - alış fiyatı ) formülüyle bulunan sonuçlar toplanıyor
    // Metot PieChart'ta kullanılacağı için PieEntry listesi olarak değer döndürüyor.
    public List<PieEntry> kullaniciGetirileriniGetir() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + SUTUN_URUN_ISLEMI_KULLANICI_ID +
                       ", SUM(" + SUTUN_URUN_ISLEMI_ADET + " * (" + SUTUN_URUN_ISLEMI_SATIS_FIYATI + " - " +
                       SUTUN_URUN_ISLEMI_ALIS_FIYATI + ")) AS getiri FROM " + TABLO_URUN_ISLEMI +
                       " WHERE " + SUTUN_URUN_ISLEMI_ISLEM_TURU + " = 'out' GROUP BY " + SUTUN_URUN_ISLEMI_KULLANICI_ID;
        Cursor c = db.rawQuery(query, null);
        List<PieEntry> getirilerPieEntry = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                String kadi = c.getString(c.getColumnIndex(SUTUN_URUN_ISLEMI_KULLANICI_ID));
                int getiri = c.getInt(c.getColumnIndex("getiri"));
                PieEntry pieEntry = new PieEntry((float)getiri / 100, kadi);
                getirilerPieEntry.add(pieEntry);
            }while (c.moveToNext());
        }
        c.close();
        db.close();
        return getirilerPieEntry;
    }
}
