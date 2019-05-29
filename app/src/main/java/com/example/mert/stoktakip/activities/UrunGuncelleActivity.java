package com.example.mert.stoktakip.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.example.mert.stoktakip.utils.TouchInterceptorLayout;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class UrunGuncelleActivity extends AppCompatActivity {

    EditText barkodNoTxt;
    EditText urunAdiTxt;
    CurrencyEditText alisFiyatiTxt;
    CurrencyEditText satisFiyatiTxt;
    Button urunGuncelleBtn;
    TouchInterceptorLayout til;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_guncelle);

        barkodNoTxt = findViewById(R.id.txt_barkod);
        urunAdiTxt = findViewById(R.id.txt_urun_adi);
        alisFiyatiTxt = findViewById(R.id.txt_alis_fiyati);
        satisFiyatiTxt = findViewById(R.id.txt_satis_fiyati);
        urunGuncelleBtn = findViewById(R.id.btn_urun_guncelle);
        til = findViewById(R.id.interceptorLayout);
        mp = MediaPlayer.create(this, R.raw.scan_sound);

        // Barkod numarası güncellenemeyeceği için TextView devre dışı bırakılıyor
        barkodNoTxt.setEnabled(false);

        Intent intent = getIntent();
        barkodNoTxt.setText(intent.getStringExtra("barkod"));
        urunAdiTxt.setText(intent.getStringExtra("urun_adi"));
        alisFiyatiTxt.setText(intent.getStringExtra("alis_fiyati"));
        satisFiyatiTxt.setText(intent.getStringExtra("satis_fiyati"));

        urunGuncelleBtn.setOnClickListener(e -> urunGuncelle());
        // Barkod güncellenmek için tıklanırsa kullanıcıya hata veriyor
        til.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GlideToast.makeToast(UrunGuncelleActivity.this, "Ürünün barkod numarası değiştirilemez.",
                        GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            }
        });
    }

    private void urunGuncelle() {
        String barkod = barkodNoTxt.getText().toString();
        String ad = urunAdiTxt.getText().toString();
        // Alış ve satış fiyatları kuruş şeklinde long değer olarak geliyor. Floata çevrilmesi gerekiyor
        float alis = alisFiyatiTxt.getRawValue() / (float)100;
        float satis = satisFiyatiTxt.getRawValue() / (float)100;

        // Alanlardan herhangi biri boşsa hata ver
        if(ad.equals("")){
            new GlideToast.makeToast(UrunGuncelleActivity.this, "Lütfen bütün alanları doldurun.",
                    GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            return;
        }

        Urun urun = new Urun(barkod, ad, alis, satis);

        VeritabaniIslemleri vti = new VeritabaniIslemleri(this);

        // Ürün eklenemediyse hata ver
        if(vti.urunGuncelle(urun) != 1){
            new GlideToast.makeToast(UrunGuncelleActivity.this, "Ürün güncellenemedi.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        // Ürün eklendiyse giriş alanlarını sil ve ürünün eklendiğini kullanıcıya bildir
        alanlariBosalt();
        new GlideToast.makeToast(UrunGuncelleActivity.this, "Ürün güncellendi.",
                GlideToast.LENGTHTOOLONG, GlideToast.SUCCESSTOAST).show();
        // UrunGuncelleActivity'yi kapatmadan önce GlideToast'ın ekranda kalma süresi kadar beklenilip çıkılmazsa hata veriyor
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, GlideToast.LENGTHTOOLONG);
    }

    private void alanlariBosalt() {
        barkodNoTxt.setText(null);
        urunAdiTxt.setText(null);
        alisFiyatiTxt.setValue(0);
        satisFiyatiTxt.setValue(0);
    }
}
