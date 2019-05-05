package com.example.mert.stoktakip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.adapters.UrunAdapterIslemGecmisi;
import com.example.mert.stoktakip.dialogs.IslemGecmisiFiltreleDialog;
import com.example.mert.stoktakip.models.UrunIslemi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IslemGecmisiFragment extends Fragment implements IslemGecmisiFiltreleDialog.IslemGecmisiFiltreleDialogListener {

    ListView liste;
    UrunAdapterIslemGecmisi adapter;
    ArrayList<UrunIslemi> islemler = new ArrayList<>();
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
                dialog.setTargetFragment(IslemGecmisiFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "İşlem Geçmişi Filtrele");
            }
        });
        vti = new VeritabaniIslemleri(getContext());
        islemler = vti.urunIslemleriGetir();
        adapter = new UrunAdapterIslemGecmisi(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
        liste.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                switch(position){
                    case 0:
                        islemGecmisiGetir(1, cal);
                        break;
                    case 1:
                        islemGecmisiGetir(7, cal);
                        break;
                    case 2:
                        islemGecmisiGetir(30, cal);
                        break;
                    case 3:
                        islemGecmisiGetir(90, cal);
                        break;
                    case 4:
                        islemler.clear();
                        islemler = vti.urunIslemleriGetir();
                        adapter = new UrunAdapterIslemGecmisi(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
                        liste.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return v;
    }

    private void islemGecmisiGetir(int gunFiltre, Calendar cal){
        cal.add(Calendar.DATE, (-1)*gunFiltre);
        islemler.clear();
        islemler = vti.urunIslemleriGetir(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime()));
        adapter = new UrunAdapterIslemGecmisi(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
        liste.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void filtreParametreleriniGetir(String baslangicTarihi, String bitisTarihi, String islemTuru) {
        islemler.clear();
        if(islemTuru.equals("Tümü"))
            islemler = vti.urunIslemleriGetir(baslangicTarihi, bitisTarihi);
        else if(islemTuru.equals("Alım"))
            islemler = vti.urunIslemleriGetir(baslangicTarihi, bitisTarihi, "in");
        else
            islemler = vti.urunIslemleriGetir(baslangicTarihi, bitisTarihi, "out");
        adapter = new UrunAdapterIslemGecmisi(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
        liste.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
