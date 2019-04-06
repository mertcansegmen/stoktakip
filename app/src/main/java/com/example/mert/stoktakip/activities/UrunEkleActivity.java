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
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class UrunEkleActivity extends AppCompatActivity {

    EditText barkodNo;
    EditText urunAdi;
    EditText alisFiyati;
    EditText satisFiyati;
    ImageButton barkodBtn;
    Button ekleBtn;
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urun_ekle);

        barkodNo = findViewById(R.id.txt_barkod);
        urunAdi = findViewById(R.id.txt_urunadi);
        alisFiyati = findViewById(R.id.txt_alisfiyati);
        satisFiyati = findViewById(R.id.txt_satisfiyati);
        barkodBtn = findViewById(R.id.btn_barcode);
        ekleBtn = findViewById(R.id.btn_ekle);
        mp = MediaPlayer.create(this, R.raw.scan_sound);

        barkodBtn.setOnClickListener(e -> barkodOkuyucuAc());
    }

    // Barkod okuyucu aç butonunun click listener'ı
    private void barkodOkuyucuAc() {
        Intent intent = new Intent(UrunEkleActivity.this, BarkodOkuyucuActivity.class);
        startActivityForResult(intent, 0);
    }

    // Barkod tarayıcı kapanınca şimdilik gelen değeri ekrana bastırıyor
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(this, "Barkod No: " + barcode.displayValue, Toast.LENGTH_LONG).show();
                    mp.start();
                }
                else{
                    Toast.makeText(this, "Barkod okuma başarısız oldu.", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
