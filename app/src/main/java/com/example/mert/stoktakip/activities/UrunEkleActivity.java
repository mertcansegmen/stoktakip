package com.example.mert.stoktakip.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class UrunEkleActivity extends AppCompatActivity {

    EditText barkodNoTxt;
    EditText urunAdiTxt;
    CurrencyEditText alisFiyatiTxt;
    CurrencyEditText satisFiyatiTxt;
    ImageButton barkodOkuyucuImgBtn;
    Button urunEkleBtn;

    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ekle);

        barkodNoTxt = findViewById(R.id.txt_barkod);
        urunAdiTxt = findViewById(R.id.txt_urun_adi);
        alisFiyatiTxt = findViewById(R.id.txt_alis_fiyati);
        satisFiyatiTxt = findViewById(R.id.txt_satis_fiyati);
        barkodOkuyucuImgBtn = findViewById(R.id.btn_barkod);
        urunEkleBtn = findViewById(R.id.btn_urun_ekle);
        mp = MediaPlayer.create(this, R.raw.scan_sound);

        urunEkleBtn.setOnClickListener(e -> urunEkle());
        barkodOkuyucuImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UrunEkleActivity.this, BarkodOkuyucuActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    private void urunEkle() {
        String barkod = barkodNoTxt.getText().toString();
        String ad = urunAdiTxt.getText().toString();
        // Alış ve satış fiyatları kuruş şeklinde long değer olarak geliyor. Floata çevrilmesi gerekiyor
        float alis = alisFiyatiTxt.getRawValue() / (float)100;
        float satis = satisFiyatiTxt.getRawValue() / (float)100;

        // Alanlardan herhangi biri boşsa hata ver
        if(barkod.equals("") || ad.equals("")){
            new GlideToast.makeToast(UrunEkleActivity.this, "Lütfen bütün alanları doldurun.",
                    GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            return;
        }

        Urun urun = new Urun(barkod, ad, 0, alis, satis);

        VeritabaniIslemleri vti = new VeritabaniIslemleri(this);

        // Eğer ürün barkodu veritabanında bulunuyorsa hata ver
        if(vti.urunTekrariKontrolEt(urun.getBarkodNo())){
            new GlideToast.makeToast(UrunEkleActivity.this, "Ürün zaten ekli.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        // Ürün eklenemediyse hata ver
        if(vti.urunEkle(urun) == -1){
            new GlideToast.makeToast(UrunEkleActivity.this, "Ürün eklenemedi.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        // Ürün eklendiyse giriş alanlarını sil ve ürünün eklendiğini kullanıcıya bildir
        alanlariBosalt();
        new GlideToast.makeToast(UrunEkleActivity.this, "Ürün eklendi.",
                GlideToast.LENGTHTOOLONG, GlideToast.SUCCESSTOAST).show();
    }

    // Barkod tarayıcı kapanınca gelen değeri barkodno edittext'ine geçiriyor ve düzenlemeyi kapatıyor
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    mp.start();
                    barkodNoTxt.setText(barcode.displayValue);
                    barkodNoTxt.setEnabled(false);
                    urunAdiTxt.requestFocus();
                }
                else{
                    new GlideToast.makeToast(UrunEkleActivity.this, "Barkod eklenemedi.",
                            GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void alanlariBosalt() {
        barkodNoTxt.setText(null);
        urunAdiTxt.setText(null);
        alisFiyatiTxt.setValue(0);
        satisFiyatiTxt.setValue(0);
        barkodNoTxt.setEnabled(true);
    }
}
