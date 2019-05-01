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
import com.example.mert.stoktakip.adapters.UrunAdapterIslemGecmisi;
import com.example.mert.stoktakip.models.UrunIslemi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

import java.util.ArrayList;

public class IslemGecmisiFragment extends Fragment {

    ListView liste;
    UrunAdapterIslemGecmisi adapter;
    ArrayList<UrunIslemi> islemler;
    VeritabaniIslemleri vti;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_islemgecmisi, container, false);

        liste = v.findViewById(R.id.liste);

        vti = new VeritabaniIslemleri(getContext());
        islemler = vti.urunIslemleriGetir();
        adapter = new UrunAdapterIslemGecmisi(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
        liste.setAdapter(adapter);

        return v;
    }
}
