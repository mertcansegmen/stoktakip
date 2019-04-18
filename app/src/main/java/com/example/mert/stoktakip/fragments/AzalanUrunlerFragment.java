package com.example.mert.stoktakip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.adapters.UrunAdapterStokListesi;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

import java.util.ArrayList;
import java.util.List;

public class AzalanUrunlerFragment extends Fragment {

    ListView liste;
    UrunAdapterStokListesi adapter;
    ArrayList<Urun> urunler;
    VeritabaniIslemleri vti;
    int esik;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_azalanurunler, container, false);

        liste = v.findViewById(R.id.list_urun);

        vti = new VeritabaniIslemleri(getContext());
        urunler = vti.azalanUrunGetir(10);
        adapter = new UrunAdapterStokListesi(getContext(),R.layout.liste_elemani_stok_listesi, urunler);
        liste.setAdapter(adapter);

        return v;
    }
}
