package com.example.mert.stoktakip.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

public class UrunGuncelleActivity extends AppCompatActivity {

    EditText barkodNo;
    EditText urunAdi;
    EditText alisFiyati;
    EditText satisFiyati;
    Button guncelleBtn;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_guncelle);

        barkodNo = findViewById(R.id.txt_barkod);
        urunAdi = findViewById(R.id.txt_urunadi);
        alisFiyati = findViewById(R.id.txt_alisfiyati);
        satisFiyati = findViewById(R.id.txt_satisfiyati);
        guncelleBtn = findViewById(R.id.btn_urunguncelle);
        mp = MediaPlayer.create(this, R.raw.scan_sound);

        barkodNo.setEnabled(false);

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
        String alis = alisFiyati.getText().toString();
        String satis = satisFiyati.getText().toString();

        // Alanlardan herhangi biri boşsa hata ver
        if(ad.equals("") || alis.equals("") || satis.equals("")){
            Toast.makeText(UrunGuncelleActivity.this, "Lütfen bütün alanları doldurun.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        // EditText'ten alınan değer floata çeviriliyor
        float alisFiyatiFloat = Float.parseFloat(alis);
        float satisFiyatiFloat = Float.parseFloat(satis);
        Urun urun = new Urun(barkod, ad, alisFiyatiFloat, satisFiyatiFloat);

        VeritabaniIslemleri vti = new VeritabaniIslemleri(this);

        // Ürün eklenemediyse hata ver
        if(vti.urunGuncelle(urun) != 1){
            Toast.makeText(this, "Ürün eklenemedi.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Ürün eklendiyse giriş alanlarını sil ve ürünün eklendiğini kullanıcıya bildir
        alanlariBosalt();
        Toast.makeText(this, "Ürün güncellendi.",
                Toast.LENGTH_LONG).show();
        finish();
    }

    private void alanlariBosalt() {
        barkodNo.setText(null);
        urunAdi.setText(null);
        alisFiyati.setText(null);
        satisFiyati.setText(null);
    }

}
