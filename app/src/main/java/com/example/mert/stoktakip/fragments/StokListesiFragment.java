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
import com.example.mert.stoktakip.activities.UrunEkleActivity;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.adapters.UrunAdapterStokListesi;
import com.example.mert.stoktakip.activities.BarkodOkuyucuActivity;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StokListesiFragment extends Fragment {

    SearchView search;
    ImageButton barkodBtn;
    FloatingActionButton fab;
    ListView liste;

    MediaPlayer mp;

    ArrayList<Urun> urunler;
    UrunAdapterStokListesi adapter;

    VeritabaniIslemleri vti;

    boolean barkodAramasiYapildi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stoklistesi, container, false);
        search = v.findViewById(R.id.search_view);
        liste = v.findViewById(R.id.liste);
        barkodBtn = v.findViewById(R.id.btn_barcode);
        fab = v.findViewById(R.id.btn_ekle);
        mp = MediaPlayer.create(v.getContext(), R.raw.scan_sound);

        vti = new VeritabaniIslemleri(getContext());
        urunler = vti.butunUrunleriGetir();
        adapter = new UrunAdapterStokListesi(getContext(), R.layout.liste_elemani_stok_listesi, urunler);
        liste.setAdapter(adapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        fab.setOnClickListener(e -> yeniStokKaydiEkle());
        barkodBtn.setOnClickListener(e -> BarkodOkuyucuAc());
        return v;
    }

    // Stok kaydı ekle butonunun click listener'ı
    private void yeniStokKaydiEkle() {
        Intent intent = new Intent(getActivity(), UrunEkleActivity.class);
        startActivity(intent);
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
                    adapter.getFilter().filter(barcode.displayValue);
                    barkodAramasiYapildi = true;
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
