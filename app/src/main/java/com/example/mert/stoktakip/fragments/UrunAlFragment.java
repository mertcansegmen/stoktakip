package com.example.mert.stoktakip.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.utils.TouchInterceptorLayout;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.adapters.UrunAdapterUrunAlSat;
import com.example.mert.stoktakip.activities.BarkodOkuyucuActivity;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class UrunAlFragment extends Fragment {

    private SearchView search;
    private ExpandableHeightListView liste;
    private TouchInterceptorLayout til;
    private ImageButton barkodBtn;
    MediaPlayer mp;

    //Örnek ürünler, tamamlandığında veritabanından çekilecek
    Urun[] urunler = new Urun[]{ new Urun("978020137962", "Biscolata Starz"),
            new Urun("978020137962", "Kekstra"),
            new Urun("123456432564", "Biskrem"),
            new Urun("897613576652", "Laviva"),
            new Urun("745023898341", "Canga")};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_urunal, container, false);
        search = v.findViewById(R.id.search_view);
        liste = v.findViewById(R.id.urun_liste);
        barkodBtn = v.findViewById(R.id.btn_barcode);
        til = v.findViewById(R.id.interceptorLayout);
        mp = MediaPlayer.create(v.getContext(), R.raw.scan_sound);

        barkodBtn.setOnClickListener(e -> BarkodOkuyucuAc());
        til.setOnClickListener(e -> urunSec());

        //Geçici kodlar
        UrunAdapterUrunAlSat adapter = new UrunAdapterUrunAlSat(getActivity(), R.layout.liste_elemani_urun_alsat, urunler);
        liste.setAdapter(adapter);
        liste.setExpanded(true);


        return v;
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

    // Ürün arama butonunun click listener'ı
    private void urunSec() {
        Toast.makeText(getActivity(), "Ürünler açılıyor", Toast.LENGTH_LONG).show();
    }

}
