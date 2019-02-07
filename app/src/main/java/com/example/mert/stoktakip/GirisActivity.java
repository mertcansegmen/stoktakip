package com.example.mert.stoktakip;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
    }

    private void uyeOl(){
        Intent intent = new Intent(GirisActivity.this, UyeOlActivity.class);
        startActivity(intent);
    }
}
