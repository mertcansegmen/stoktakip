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
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.adapters.IslemGecmisiAdapter;
import com.example.mert.stoktakip.dialogs.IslemGecmisiFiltreleDialog;
import com.example.mert.stoktakip.dialogs.UrunIslemiBilgileriDialog;
import com.example.mert.stoktakip.models.UrunIslemi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IslemGecmisiFragment extends Fragment implements IslemGecmisiFiltreleDialog.IslemGecmisiFiltreleDialogListener {

    MaterialSpinner spinner;
    ImageButton filtreleBtn;
    ListView liste;

    IslemGecmisiAdapter adapter;
    VeritabaniIslemleri vti;

    ArrayList<UrunIslemi> islemler = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_islem_gecmisi, container, false);

        liste = v.findViewById(R.id.liste);
        spinner = v.findViewById(R.id.spinner_filtre_araligi);
        filtreleBtn = v.findViewById(R.id.btn_filtre);

        vti = new VeritabaniIslemleri(getContext());
        adapter = new IslemGecmisiAdapter(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
        liste.setAdapter(adapter);

        spinner.setItems("Son 1 Gün", "Son 1 Hafta", "Son 1 Ay", "Son 3 Ay", "Bütün Kayıtlar");
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        islemGecmisiGetir(1, cal);

        filtreleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new IslemGecmisiFiltreleDialog();
                dialog.setTargetFragment(IslemGecmisiFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "İşlem Geçmişi Filtrele");
            }
        });

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
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
                        islemler = vti.urunIslemiGecmisiFiltrele();
                        adapter = new IslemGecmisiAdapter(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
                        liste.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        });

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int urunIslemiId = islemler.get(position).getId();
                Bundle degerler = new Bundle();
                degerler.putInt("urun_islemi_id", urunIslemiId);

                DialogFragment dialog = new UrunIslemiBilgileriDialog();
                dialog.setArguments(degerler);
                dialog.show(getActivity().getSupportFragmentManager(), "Ürün İşlemi Bilgileri");
            }
        });
        return v;
    }

    // Gelen Calender objesini gelen gün kadar geriye götürür, sonra bugün ile o Calender
    // objesinin tuttuğu tarih arasında yapılmış işlem geçmişini getirir
    private void islemGecmisiGetir(int gunFiltre, Calendar cal){
        cal.add(Calendar.DATE, (-1)*gunFiltre);
        islemler.clear();
        islemler = vti.urunIslemiGecmisiFiltrele(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime()));
        adapter = new IslemGecmisiAdapter(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
        liste.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    // IslemGecmisiFiltreleDialog'dan gelen değerleri çekip bu değerlere göre listeyi filtreler
    @Override
    public void filtreParametreleriniGetir(String baslangicTarihi, String bitisTarihi, String islemTuru) {
        islemler.clear();
        if(islemTuru.equals("Tümü"))
            islemler = vti.urunIslemiGecmisiFiltrele(baslangicTarihi, bitisTarihi);
        else if(islemTuru.equals("Alım"))
            islemler = vti.urunIslemiGecmisiFiltrele(baslangicTarihi, bitisTarihi, "in");
        else
            islemler = vti.urunIslemiGecmisiFiltrele(baslangicTarihi, bitisTarihi, "out");
        adapter = new IslemGecmisiAdapter(getContext(), R.layout.liste_elemani_islem_gecmisi, islemler);
        liste.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
