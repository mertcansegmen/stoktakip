package com.example.mert.stoktakip.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.adapters.StokListesiAdapter;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

import java.util.ArrayList;

public class AzalanUrunlerFragment extends Fragment {

    ListView liste;

    StokListesiAdapter adapter;
    VeritabaniIslemleri vti;

    ArrayList<Urun> urunler;
    int varsayilanEsik = 10;
    int esik;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_azalan_urunler, container, false);

        liste = v.findViewById(R.id.liste);

        preferencesKontrol();

        vti = new VeritabaniIslemleri(getContext());
        urunler = vti.azalanUrunGetir(esik);
        adapter = new StokListesiAdapter(getContext(), R.layout.liste_elemani_stok_listesi, urunler);
        liste.setAdapter(adapter);

        return v;
    }

    public void preferencesKontrol() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        esik = pref.getInt("esik", varsayilanEsik);
    }
}
