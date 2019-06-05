package com.example.mert.stoktakip.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Kullanici;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.example.mert.stoktakip.utils.TouchInterceptorLayout;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class KullaniciGuncelleActivity extends AppCompatActivity {

    TouchInterceptorLayout til;
    EditText kadiTxt;
    EditText sifreTxt;
    EditText yeniSifreTxt;
    EditText yeniSifreTekrarTxt;
    Button guncelleBtn;

    String kadi;

    VeritabaniIslemleri vti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_guncelle);

        til = findViewById(R.id.interceptorLayout);
        kadiTxt = findViewById(R.id.txt_kadi);
        sifreTxt = findViewById(R.id.txt_sifre);
        yeniSifreTxt = findViewById(R.id.txt_yeni_sifre);
        yeniSifreTekrarTxt = findViewById(R.id.txt_yeni_sifre_tekrari);
        guncelleBtn = findViewById(R.id.btn_kullanici_guncelle);

        vti = new VeritabaniIslemleri(KullaniciGuncelleActivity.this);

        Intent intent = getIntent();
        kadi = intent.getStringExtra("kadi");

        kadiTxt.setEnabled(false);
        kadiTxt.setText(kadi);

        til.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GlideToast.makeToast(KullaniciGuncelleActivity.this, "Kullanıcı adı değiştirilemez.",
                        GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            }
        });
        guncelleBtn.setOnClickListener(e -> kullaniciGuncelle());
    }

    private void kullaniciGuncelle() {
        Kullanici kullanici = new Kullanici(kadi, sifreTxt.getText().toString());
        Kullanici yeniKullanici = new Kullanici(kadi, yeniSifreTxt.getText().toString());
        // Eğer herhangi bir alan boş bırakıldıysa hata ver
        if(sifreTxt.getText().toString().equals("") || yeniSifreTxt.getText().toString().equals("") || yeniSifreTekrarTxt.getText().toString().equals("")){
            new GlideToast.makeToast(KullaniciGuncelleActivity.this, "Lütfen bütün alanları doldurun.",
                    GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            return;
        }
        // Eğer kullanıcı şifresi yanlışsa hata ver
        if(!vti.girisBilgileriniKontrolEt(kullanici)){
            new GlideToast.makeToast(KullaniciGuncelleActivity.this, "Girdiğiniz şifre yanlış.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            sifreTxt.setText(null);
            return;
        }
        // Güncellenecek şifre, şifre tekrarıyla uyuşmuyorsa hata ver
        if(!yeniSifreTxt.getText().toString().equals(yeniSifreTekrarTxt.getText().toString())){
            new GlideToast.makeToast(KullaniciGuncelleActivity.this, "Girdiğiniz şifreler birbiriyle uyuşmuyor.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        // Güncelleme sonrasında veritabanında değişen satır sayısı 1den azsa hata ver
        if(vti.kullaniciGuncelle(yeniKullanici) < 1){
            new GlideToast.makeToast(KullaniciGuncelleActivity.this, "Hata oluştu.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        // Hata oluşmadıysa şifrenin güncellendiğini kullanıcıya bildir
        new GlideToast.makeToast(KullaniciGuncelleActivity.this, "Bilgileriniz güncellendi.",
                GlideToast.LENGTHTOOLONG, GlideToast.SUCCESSTOAST).show();
        alanlariBosalt();
        // GlideToast'ın ekrandan gitmesini bekleyip activity'yi sonlandır
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, GlideToast.LENGTHTOOLONG);

    }

    private void alanlariBosalt(){
        kadiTxt.setText(null);
        sifreTxt.setText(null);
        yeniSifreTxt.setText(null);
        yeniSifreTekrarTxt.setText(null);
    }
}
