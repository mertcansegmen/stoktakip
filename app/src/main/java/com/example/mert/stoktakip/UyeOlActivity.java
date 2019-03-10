package com.example.mert.stoktakip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UyeOlActivity extends AppCompatActivity {

    private Button uyeOlBtn;
    private EditText kadiTxt;
    private EditText sifreTxt;
    private EditText sifreTekrarTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uye_ol);

        uyeOlBtn = findViewById(R.id.btn_uyeol);
        kadiTxt = findViewById(R.id.txt_kadi);
        sifreTxt = findViewById(R.id.txt_sifre);
        sifreTekrarTxt = findViewById(R.id.txt_sifretekrar);

        uyeOlBtn.setOnClickListener(e -> uyeOl());
    }

    private void uyeOl() {
        String kadi = kadiTxt.getText().toString();
        String sifre = sifreTxt.getText().toString();
        String sifreTekrar = sifreTekrarTxt.getText().toString();

        Kullanici kullanici = new Kullanici();
        kullanici.setKadi(kadi);
        kullanici.setSifre(sifre);

        VeritabaniYonetici vy = new VeritabaniYonetici(UyeOlActivity.this);

        // Alanlardan herhangi biri boşsa hata ver.
        if(kadi.equals("") || sifre.equals("") || sifreTekrar.equals("")){
            Toast.makeText(UyeOlActivity.this, "Lütfen bütün alanları doldurun.",
                            Toast.LENGTH_LONG).show();
            return;
        }
        // Şifre ile şifre tekrarı aynı değilse hata ver.
        else if(!sifre.equals(sifreTekrar)){
            Toast.makeText(UyeOlActivity.this, "Girdiğiniz şifreler birbiriyle uyuşmuyor.",
                            Toast.LENGTH_LONG).show();
            return;
        }
        // Kullanıcı adı daha önceden alınmışsa hata ver.
        else if(vy.kullaniciKontrolEt(kadi)){
            Toast.makeText(UyeOlActivity.this, "Böyle bir kullanıcı zaten mevcut.",
                            Toast.LENGTH_LONG).show();
            return;
        }
        // Kullanıcıyı ekle. Bir hata oluştuysa kullanıcıya bildir.
        if(vy.kullaniciEkle(kullanici) == -1){
            Toast.makeText(UyeOlActivity.this, "Kayıt olurken bir hata oluştu.",
                            Toast.LENGTH_LONG).show();
            return;
        }
        // Hiç bir hata oluşmadıysa kullanıcıya bildir ve dolu alanları sil.
        Toast.makeText(UyeOlActivity.this, "Kaydınız başarıyla tamamlandı!",
                Toast.LENGTH_LONG).show();
        alanlariBosalt();
        Intent intent = new Intent(this, GirisActivity.class);
        startActivity(intent);

    }

    private void alanlariBosalt(){
        kadiTxt.setText(null);
        sifreTxt.setText(null);
        sifreTekrarTxt.setText(null);
    }
}
