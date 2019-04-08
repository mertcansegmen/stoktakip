package com.example.mert.stoktakip.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mert.stoktakip.models.Kullanici;
import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

public class GirisActivity extends AppCompatActivity {

    private EditText kadiTxt;
    private EditText sifreTxt;
    private Button girisYapBtn;
    private TextView uyeOlBtn;
    private CheckBox hatirla;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        kadiTxt = findViewById(R.id.txt_kadi);
        sifreTxt = findViewById(R.id.txt_sifre);
        girisYapBtn = findViewById(R.id.btn_giris);
        uyeOlBtn = findViewById(R.id.btn_uyeol);
        hatirla = findViewById(R.id.checkbox_hatirla);

        sharedPreferencesKontrol();

        girisYapBtn.setOnClickListener(e -> girisYap());
        uyeOlBtn.setOnClickListener(e -> uyeOl());
        hatirla.setOnClickListener(e -> sifreHatirla());
    }

    // Beni hatırla checkbox'ının click listener'ı
    private void sifreHatirla() {
        if(hatirla.isChecked()){
            editor.putString("checkbox", "True");
            editor.commit();

            String kadi = kadiTxt.getText().toString();
            editor.putString("kadi", kadi);
            editor.commit();

            String sifre = sifreTxt.getText().toString();
            editor.putString("sifre", sifre);
            editor.commit();
        }
        else{
            editor.putString("checkbox", "False");
            editor.commit();

            editor.putString("kadi", "");
            editor.commit();

            editor.putString("sifre", "");
            editor.commit();
        }
    }

    // Giriş yap butonunun click listener'ı
    private void girisYap() {
        Kullanici kullanici = new Kullanici();
        kullanici.setKadi(kadiTxt.getText().toString());
        kullanici.setSifre(sifreTxt.getText().toString());

        VeritabaniIslemleri vy = new VeritabaniIslemleri(GirisActivity.this);

        // Alanlardan herhangi biri boşsa hata ver
        if(kullanici.getKadi().equals("") || kullanici.getSifre().equals("")){
            Toast.makeText(GirisActivity.this, "Lütfen bütün alanları doldurun.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Eğer kullanıcı adı ya da şifre yanlışsa hata ver
        if(!vy.girisBilgileriniKontrolEt(kullanici.getKadi(), kullanici.getSifre())){
            Toast.makeText(GirisActivity.this, "Kullanıcı adı ya da şifre yanlış.",
                    Toast.LENGTH_LONG).show();
            sifreTxt.setText(null);
            return;
        }
        // Giriş yapıldıysa anasayfaya yönlendir
        alanlariBosalt();
        Intent intent = new Intent(GirisActivity.this, AnasayfaActivity.class);
        intent.putExtra("kadi", kullanici.getKadi());
        startActivity(intent);
        finish();
    }

    // Üye ol butonunun click listener'ı
    private void uyeOl(){
        Intent intent = new Intent(GirisActivity.this, UyeOlActivity.class);
        startActivity(intent);
    }

    private void alanlariBosalt(){
        sifreTxt.setText(null);
        kadiTxt.setText(null);
    }

    // Uygulama ilk çalıştığında bu metot çalışıyor, eğer shared preferences'da kayıtlı kullanıcı adı, şifre veya
    // checkbox durumu varsa onları kontrol edip gerekli alanlara çekiyor
    private void sharedPreferencesKontrol(){
        String checkBox = preferences.getString("checkbox", "False");
        String kadi = preferences.getString("kadi", "");
        String sifre = preferences.getString("sifre", "");

        kadiTxt.setText(kadi);
        sifreTxt.setText(sifre);

        if(checkBox.equals("True"))
            hatirla.setChecked(true);
        else
            hatirla.setChecked(false);
    }
}
