package com.example.mert.stoktakip.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.fragments.StokListesiFragment;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

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
        ekleBtn = findViewById(R.id.btn_urunekle);
        mp = MediaPlayer.create(this, R.raw.scan_sound);

        barkodBtn.setOnClickListener(e -> barkodOkuyucuAc());
        ekleBtn.setOnClickListener(e -> urunEkle());
    }

    private void urunEkle() {
        String barkod = barkodNo.getText().toString();
        String ad = urunAdi.getText().toString();
        String alis = alisFiyati.getText().toString();
        String satis = satisFiyati.getText().toString();

        // Alanlardan herhangi biri boşsa hata ver
        if(barkod.equals("") || ad.equals("") || alis.equals("") || satis.equals("")){
            new GlideToast.makeToast(UrunEkleActivity.this, "Lütfen bütün alanları doldurun.",
                    GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            return;
        }

        // EditText'ten alınan değer floata çeviriliyor
        float alisFiyatiFloat = Float.parseFloat(alis);
        float satisFiyatiFloat = Float.parseFloat(satis);
        Urun urun = new Urun(barkod, ad, 0, alisFiyatiFloat, satisFiyatiFloat);

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

    private void alanlariBosalt() {
        barkodNo.setText(null);
        urunAdi.setText(null);
        alisFiyati.setText(null);
        satisFiyati.setText(null);
        barkodNo.setEnabled(true);
    }

    // Barkod okuyucu aç butonunun click listener'ı
    private void barkodOkuyucuAc() {
        Intent intent = new Intent(UrunEkleActivity.this, BarkodOkuyucuActivity.class);
        startActivityForResult(intent, 0);
    }

    // Barkod tarayıcı kapanınca gelen değeri barkodno edittext'ine geçiriyor ve düzenlemeyi kapatıyor
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    mp.start();
                    barkodNo.setText(barcode.displayValue);
                    barkodNo.setEnabled(false);
                    urunAdi.requestFocus();
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
}
