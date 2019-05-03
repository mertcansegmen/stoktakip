package com.example.mert.stoktakip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.adapters.UrunAdapterIslemGecmisi;
import com.example.mert.stoktakip.dialogs.IslemGecmisiFiltreleDialog;
import com.example.mert.stoktakip.models.UrunIslemi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

import java.util.ArrayList;

public class IslemGecmisiFragment extends Fragment {

    ListView liste;
    UrunAdapterIslemGecmisi adapter;
    ArrayList<UrunIslemi> islemler;
    VeritabaniIslemleri vti;
    Spinner spinner;
    ImageButton filtreButon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_islemgecmisi, container, false);

        liste = v.findViewById(R.id.liste);
        spinner = v.findViewById(R.id.spinner);
        filtreButon = v.findViewById(R.id.btn_filtre);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_filtre_sureleri, R.layout.spinner_elemani_filtre_sureleri);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_elemani_filtre_sureleri);
        spinner.setAdapter(spinnerAdapter);

        filtreButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialog = new IslemGecmisiFiltreleDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "İşlem Geçmişi Filtrele");
            }
        });

        vti = new VeritabaniIslemleri(getContext());
        islemler = vti.urunIslemleriGetir();
        adapter = new UrunAdapterIslemGecmisi(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
        liste.setAdapter(adapter);

        return v;
    }
}
