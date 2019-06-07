package com.example.mert.stoktakip.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mert.stoktakip.models.Kullanici;
import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

public class GirisYapActivity extends AppCompatActivity {

    EditText kadiTxt;
    EditText sifreTxt;
    Button girisYapBtn;
    TextView uyeOlBtn;
    CheckBox bilgileriHatirlaCheckBox;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);

        kadiTxt = findViewById(R.id.txt_kadi);
        sifreTxt = findViewById(R.id.txt_sifre);
        girisYapBtn = findViewById(R.id.btn_giris_yap);
        uyeOlBtn = findViewById(R.id.btn_uye_ol);
        bilgileriHatirlaCheckBox = findViewById(R.id.checkbox_giris_bilgisi_hatirla);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        sharedPreferencesKontrol();

        girisYapBtn.setOnClickListener(e -> girisYap());
        bilgileriHatirlaCheckBox.setOnClickListener(e -> bilgileriHatirla());
        uyeOlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GirisYapActivity.this, UyeOlActivity.class);
                startActivity(intent);
            }
        });
    }

    // Giriş yap butonunun click listener'ı
    private void girisYap() {
        String kadi = kadiTxt.getText().toString().toLowerCase().trim();
        String sifre = sifreTxt.getText().toString();

        Kullanici kullanici = new Kullanici();
        kullanici.setKadi(kadi);
        kullanici.setSifre(sifre);

        VeritabaniIslemleri vti = new VeritabaniIslemleri(GirisYapActivity.this);

        // Alanlardan herhangi biri boşsa hata ver
        if (kullanici.getKadi().equals("") || kullanici.getSifre().equals("")) {
            new GlideToast.makeToast(GirisYapActivity.this, "Lütfen bütün alanları doldurun.",
                    GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
            return;
        }
        // Eğer kullanıcı adı ya da şifre yanlışsa hata ver
        if (!vti.girisBilgileriniKontrolEt(kullanici)) {
            new GlideToast.makeToast(GirisYapActivity.this, "Kullanıcı adı ya da şifre yanlış.",
                    GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            sifreTxt.setText(null);
            return;
        }
        // Giriş yapıldıysa anasayfaya yönlendir
        alanlariBosalt();
        Intent intent = new Intent(GirisYapActivity.this, AnasayfaActivity.class);
        intent.putExtra("kadi", kullanici.getKadi());
        startActivity(intent);
        finish();
    }

    // Beni hatırla checkbox'ının click listener'ı
    private void bilgileriHatirla() {
        if (bilgileriHatirlaCheckBox.isChecked()) {
            editor.putString("checkbox", "True");
            editor.commit();

            String kadi = kadiTxt.getText().toString();
            editor.putString("kadi", kadi);
            editor.commit();

            String sifre = sifreTxt.getText().toString();
            editor.putString("sifre", sifre);
            editor.commit();
        } else {
            editor.putString("checkbox", "False");
            editor.commit();

            editor.putString("kadi", "");
            editor.commit();

            editor.putString("sifre", "");
            editor.commit();
        }
    }

    // Uygulama ilk çalıştığında bu metot çalışıyor, eğer shared preferences'da kayıtlı kullanıcı adı, şifre veya
    // checkbox durumu varsa onları kontrol edip gerekli alanlara çekiyor
    private void sharedPreferencesKontrol() {
        String checkBox = preferences.getString("checkbox", "False");
        String kadi = preferences.getString("kadi", "");
        String sifre = preferences.getString("sifre", "");

        kadiTxt.setText(kadi);
        sifreTxt.setText(sifre);

        if (checkBox.equals("True"))
            bilgileriHatirlaCheckBox.setChecked(true);
        else
            bilgileriHatirlaCheckBox.setChecked(false);
    }

    private void alanlariBosalt() {
        sifreTxt.setText(null);
        kadiTxt.setText(null);
    }
}
