package com.example.mert.stoktakip.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.adapters.UrunAdapterStokListesi;
import com.example.mert.stoktakip.activities.BarkodOkuyucuActivity;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class StokListesiFragment extends Fragment {

    SearchView search;
    ImageButton barkodBtn;
    FloatingActionButton fab;
    ListView liste;
    MediaPlayer mp;
    Urun[] urunler = new Urun[]{
            new Urun("978020137962", "Biscolata Starz", 230, 1.25f, 1.85f),
            new Urun("908328372838", "Biskrem", 12, 1.25f, 1.50f),
            new Urun("128793473412", "Laviva", 66, 0.75f, 1.25f),
            new Urun("329842347903", "Canga", 150, 0.75f, 1.50f),
            new Urun("128937213732", "Eti Cin", 8, 1.05f, 1.55f),
            new Urun("120398210384", "Popkek", 440, 0.55f, 1.05f),
            new Urun("901298374893", "Eti Karam", 84, 1.05f, 1.50f),
            new Urun("836478348301", "Negro", 46, 1.10f, 1.75f),
            new Urun("342378654356", "Benimo", 19, 1.15f, 1.75f),
            new Urun("823862983444", "Albeni", 408, 0.75f, 1.25f),
            new Urun("038349081824", "Caramio", 112, 1.15f, 1.85f),

            new Urun("978020137962", "Biscolata Starz", 230, 1.25f, 1.85f),
            new Urun("908328372838", "Biskrem", 12, 1.25f, 1.50f),
            new Urun("128793473412", "Laviva", 66, 0.75f, 1.25f),
            new Urun("329842347903", "Canga", 150, 0.75f, 1.50f),
            new Urun("128937213732", "Eti Cin", 8, 1.05f, 1.55f),
            new Urun("120398210384", "Popkek", 440, 0.55f, 1.05f),
            new Urun("901298374893", "Eti Karam", 84, 1.05f, 1.50f),
            new Urun("836478348301", "Negro", 46, 1.10f, 1.75f),
            new Urun("342378654356", "Benimo", 19, 1.15f, 1.75f),
            new Urun("823862983444", "Albeni", 408, 0.75f, 1.25f),
            new Urun("038349081824", "Caramio", 112, 1.15f, 1.85f),

            new Urun("978020137962", "Biscolata Starz", 230, 1.25f, 1.85f),
            new Urun("908328372838", "Biskrem", 12, 1.25f, 1.50f),
            new Urun("128793473412", "Laviva", 66, 0.75f, 1.25f),
            new Urun("329842347903", "Canga", 150, 0.75f, 1.50f),
            new Urun("128937213732", "Eti Cin", 8, 1.05f, 1.55f),
            new Urun("120398210384", "Popkek", 440, 0.55f, 1.05f),
            new Urun("901298374893", "Eti Karam", 84, 1.05f, 1.50f),
            new Urun("836478348301", "Negro", 46, 1.10f, 1.75f),
            new Urun("342378654356", "Benimo", 19, 1.15f, 1.75f),
            new Urun("823862983444", "Albeni", 408, 0.75f, 1.25f),
            new Urun("038349081824", "Caramio", 112, 1.15f, 1.85f),

            new Urun("978020137962", "Biscolata Starz", 230, 1.25f, 1.85f),
            new Urun("908328372838", "Biskrem", 12, 1.25f, 1.50f),
            new Urun("128793473412", "Laviva", 66, 0.75f, 1.25f),
            new Urun("329842347903", "Canga", 150, 0.75f, 1.50f),
            new Urun("128937213732", "Eti Cin", 8, 1.05f, 1.55f),
            new Urun("120398210384", "Popkek", 440, 0.55f, 1.05f),
            new Urun("901298374893", "Eti Karam", 84, 1.05f, 1.50f),
            new Urun("836478348301", "Negro", 46, 1.10f, 1.75f),
            new Urun("342378654356", "Benimo", 19, 1.15f, 1.75f),
            new Urun("823862983444", "Albeni", 408, 0.75f, 1.25f),
            new Urun("038349081824", "Caramio", 112, 1.15f, 1.85f)
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stoklistesi, container, false);
        search = v.findViewById(R.id.search_view);
        liste = v.findViewById(R.id.liste);
        barkodBtn = v.findViewById(R.id.btn_barcode);
        fab = v.findViewById(R.id.btn_ekle);
        mp = MediaPlayer.create(v.getContext(), R.raw.scan_sound);

        //Geçici kodlar
        UrunAdapterStokListesi adapter = new UrunAdapterStokListesi(getActivity(), R.layout.liste_elemani_stok_listesi, urunler);
        liste.setAdapter(adapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        fab.setOnClickListener(e -> yeniStokKaydiEkle());
        barkodBtn.setOnClickListener(e -> BarkodOkuyucuAc());
        return v;
    }

    // Stok kaydı ekle butonunun click listener'ı
    private void yeniStokKaydiEkle() {
        //Activity mi fragment mı ?

        //Intent intent = new Intent(getActivity(), YeniStokKaydiActivity.class);
        //startActivity(intent);
    }

    // Barkod okuyucu aç butonunun click listener'ı
    private void BarkodOkuyucuAc() {
        Intent intent = new Intent(getActivity(), BarkodOkuyucuActivity.class);
        startActivityForResult(intent, 0);
    }


    // Barkod tarayıcı kapanınca şimdilik gelen değeri ekrana bastırıyor
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(getActivity(), "Barkod No: " + barcode.displayValue, Toast.LENGTH_LONG).show();
                    mp.start();
                }
                else{
                    Toast.makeText(getActivity(), "Barkod okuma başarısız oldu.", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
