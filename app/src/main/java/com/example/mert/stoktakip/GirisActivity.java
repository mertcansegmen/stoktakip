package com.example.mert.stoktakip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class GirisActivity extends AppCompatActivity {

    EditText kadiTxt;
    EditText sifreTxt;
    Button girisYapBtn;
    Button uyeOlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        kadiTxt = findViewById(R.id.txt_kadi);
        sifreTxt = findViewById(R.id.txt_sifre);
        girisYapBtn = findViewById(R.id.btn_giris);
        uyeOlBtn = findViewById(R.id.btn_uyeol);

        girisYapBtn.setOnClickListener(e -> girisYap());
        uyeOlBtn.setOnClickListener(e -> uyeOl());
    }

    private void girisYap() {
        Kullanici kullanici = new Kullanici();
        kullanici.setKadi(kadiTxt.getText().toString());
        kullanici.setSifre(sifreTxt.getText().toString());

        VeritabaniYonetici vy = new VeritabaniYonetici(GirisActivity.this);

        // Alanlardan herhangi biri boşsa hata ver.
        if(kullanici.getKadi().equals("") || kullanici.getSifre().equals("")){
            Toast.makeText(GirisActivity.this, "Lütfen bütün alanları doldurun.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Eğer kullanıcı adı ya da şifre yanlışsa hata ver.
        if(!vy.kullaniciKontrolEt(kullanici.getKadi(), kullanici.getSifre())){
            Toast.makeText(GirisActivity.this, "Kullanıcı adı ya da şifre yanlış.",
                    Toast.LENGTH_LONG).show();
            sifreTxt.setText(null);
            return;
        }
        // Giriş yapıldıysa anasayfaya yönlendir.
        alanlariBosalt();
        Intent intent = new Intent(GirisActivity.this, AnasayfaActivity.class);
        intent.putExtra("kadi", kullanici.getKadi());
        startActivity(intent);
    }

    private void uyeOl(){
        Intent intent = new Intent(GirisActivity.this, UyeOlActivity.class);
        startActivity(intent);
    }

    private void alanlariBosalt(){
        sifreTxt.setText(null);
        kadiTxt.setText(null);
    }
}
