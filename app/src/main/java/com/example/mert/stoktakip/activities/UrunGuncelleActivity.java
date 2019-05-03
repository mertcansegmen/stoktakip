package com.example.mert.stoktakip.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;
import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.example.mert.stoktakip.utils.TouchInterceptorLayout;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class UrunGuncelleActivity extends AppCompatActivity {

    EditText barkodNo;
    EditText urunAdi;
    CurrencyEditText alisFiyati;
    CurrencyEditText satisFiyati;
    Button guncelleBtn;
    MediaPlayer mp;
    TouchInterceptorLayout til;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_guncelle);

        barkodNo = findViewById(R.id.txt_barkod);
        urunAdi = findViewById(R.id.txt_urunadi);
        alisFiyati = findViewById(R.id.txt_alisfiyati);
        satisFiyati = findViewById(R.id.txt_satisfiyati);
        guncelleBtn = findViewById(R.id.btn_urunguncelle);
        til = findViewById(R.id.interceptorLayout);
        mp = MediaPlayer.create(this, R.raw.scan_sound);

        // Barkod numarası güncellenemeyeceği için TextView devre dışı bırakılıyor, tıklanırsa kullanıcıya hata veriyor
        barkodNo.setEnabled(false);
        til.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GlideToast.makeToast(UrunGuncelleActivity.this, "Ürünün barkod numarası değiştirilemez.",
                                        GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            }
        });

        Intent intent = getIntent();
        barkodNo.setText(intent.getStringExtra("barkod"));
        urunAdi.setText(intent.getStringExtra("ad"));
        alisFiyati.setText(intent.getStringExtra("alis"));
        satisFiyati.setText(intent.getStringExtra("satis"));

        guncelleBtn.setOnClickListener(e -> urunGuncelle());
    }

    private void urunGuncelle() {
        String barkod = barkodNo.getText().toString();
        String ad = urunAdi.getText().toString();
        // Alış ve satış fiyatları kuruş şeklinde long değer olarak geliyor. Floata çevrilmesi gerekiyor
        float alis = alisFiyati.getRawValue() / (float)100;
        float satis = satisFiyati.getRawValue() / (float)100;

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
        barkodNo.setText(null);
        urunAdi.setText(null);
        alisFiyati.setValue(0);
        satisFiyati.setValue(0);
    }

}
