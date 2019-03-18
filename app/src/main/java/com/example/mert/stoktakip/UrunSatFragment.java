package com.example.mert.stoktakip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class UrunSatFragment extends Fragment {
    private SearchView search;
    private ExpandableHeightListView liste;
    private TouchInterceptorLayout til;
    private ImageButton barkodBtn;

    //Örnek ürünler, tamamlandığında veritabanından çekilecek
    Urun[] urunler = new Urun[]{ new Urun("978020137962", "Biscolata Starz"),
            new Urun("978020137962", "Eti Karam"),
            new Urun("123456432564", "Canpare")};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_urunsat, container, false);
        search = v.findViewById(R.id.search_view);
        liste = v.findViewById(R.id.urun_liste);
        barkodBtn = v.findViewById(R.id.btn_barcode);
        til = v.findViewById(R.id.interceptorLayout);

        barkodBtn.setOnClickListener(e -> BarkodOkuyucuAc());
        til.setOnClickListener(e -> urunSec());

        //Geçici kodlar
        UrunAdapterUrunAlSat adapter = new UrunAdapterUrunAlSat(getActivity(), R.layout.liste_elemani_urun_alsat, urunler);
        liste.setAdapter(adapter);
        liste.setExpanded(true);


        return v;
    }

    private void BarkodOkuyucuAc() {
        Intent intent = new Intent(getActivity(), BarkodOkuyucuActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Toast.makeText(getActivity(), "Barkod No: " + barcode.displayValue, Toast.LENGTH_LONG).show();
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

    private void urunSec() {
        Toast.makeText(getActivity(), "Ürünler açılıyor", Toast.LENGTH_LONG).show();
    }

}
