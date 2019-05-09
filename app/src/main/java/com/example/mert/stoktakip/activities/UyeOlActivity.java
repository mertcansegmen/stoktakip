package com.example.mert.stoktakip.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mert.stoktakip.models.Kullanici;
import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class UyeOlActivity extends AppCompatActivity {

    EditText kadiTxt;
    EditText sifreTxt;
    EditText sifreTekrarTxt;
    Button uyeOlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_ol);

        uyeOlBtn = findViewById(R.id.btn_uye_ol);
        kadiTxt = findViewById(R.id.txt_kadi);
        sifreTxt = findViewById(R.id.txt_sifre);
        sifreTekrarTxt = findViewById(R.id.txt_sifre_tekrar);

        uyeOlBtn.setOnClickListener(e -> uyeOl());
    }

    // Uye ol butonunun click listenerı
    private void uyeOl() {
        String kadi = kadiTxt.getText().toString().toLowerCase().trim();
        String sifre = sifreTxt.getText().toString();
        String sifreTekrar = sifreTekrarTxt.getText().toString();

        Kullanici kullanici = new Kullanici();
        kullanici.setKadi(kadi);
        kullanici.setSifre(sifre);

        VeritabaniIslemleri vti = new VeritabaniIslemleri(UyeOlActivity.this);

        // Alanlardan herhangi biri boşsa hata ver
        if(kadi.equals("") || sifre.equals("") || sifreTekrar.equals("")){
            new GlideToast.makeToast(UyeOlActivity.this, "Lütfen bütün alanları doldurun.",
                    GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            return;
        }
        // Şifre ile şifre tekrarı aynı değilse hata ver
        else if(!sifre.equals(sifreTekrar)){
            new GlideToast.makeToast(UyeOlActivity.this, "Girdiğiniz şifreler birbiriyle uyuşmuyor.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        // Kullanıcı adı daha önceden alınmışsa hata ver
        else if(vti.kullaniciAdiniKontrolEt(kadi)){
            new GlideToast.makeToast(UyeOlActivity.this, "Böyle bir kullanıcı zaten mevcut.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        // Kullanıcıyı ekle. Bir hata oluştuysa kullanıcıya bildir
        if(vti.kullaniciEkle(kullanici) == -1){
            new GlideToast.makeToast(UyeOlActivity.this, "Kayıt olurken bir hata oluştu.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        // Hiç bir hata oluşmadıysa kullanıcıya bildir, dolu alanları sil ve anasayfaya geç
        new GlideToast.makeToast(UyeOlActivity.this, "Kaydınız başarıyla tamamlandı!",
                GlideToast.LENGTHTOOLONG, GlideToast.SUCCESSTOAST).show();
        alanlariBosalt();
        // Direk anasayfaya geçilirse GlideToast kapanıyor, o yüzden anasayfaya geçmeden önce GlideToast'un gözükme süresi kadar bekleniyor.
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
        sifreTekrarTxt.setText(null);
    }
}
