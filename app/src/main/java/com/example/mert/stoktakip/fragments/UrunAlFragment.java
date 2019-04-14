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
import com.example.mert.stoktakip.activities.UrunEkleActivity;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.example.mert.stoktakip.utils.TouchInterceptorLayout;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.adapters.UrunAdapterUrunAlSat;
import com.example.mert.stoktakip.activities.BarkodOkuyucuActivity;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.util.ArrayList;

public class UrunAlFragment extends Fragment {
    private SearchView search;
    private ExpandableHeightListView liste;
    private TouchInterceptorLayout til;
    private ImageButton barkodBtn;
    UrunAdapterUrunAlSat adapter;
    MediaPlayer mp;
    ArrayList<Urun> urunler = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_urunal, container, false);
        search = v.findViewById(R.id.search_view);
        liste = v.findViewById(R.id.urun_liste);
        barkodBtn = v.findViewById(R.id.btn_barcode);
        til = v.findViewById(R.id.interceptorLayout);
        mp = MediaPlayer.create(v.getContext(), R.raw.scan_sound);

        barkodBtn.setOnClickListener(e -> barkodOkuyucuAc());
        til.setOnClickListener(e -> urunSec());

        adapter = new UrunAdapterUrunAlSat(getActivity(), R.layout.liste_elemani_urun_alsat, urunler);
        liste.setAdapter(adapter);
        liste.setExpanded(true);

        return v;
    }

    // Barkod okuyucu aç butonunun click listener'ı
    private void barkodOkuyucuAc() {
        Intent intent = new Intent(getActivity(), BarkodOkuyucuActivity.class);
        startActivityForResult(intent, 0);
    }

    // Barkod tarayıcı kapanınca gelen barkoda sahip ürünü sepete ekliyor
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());
                    Urun urun = vti.barkodaGoreUrunGetir(barcode.displayValue);
                    new GlideToast.makeToast(getActivity(), "Ürün sepete eklendi: " + urun.getAd(),
                            GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
                    urunler.add(urun);
                    adapter.notifyDataSetChanged();
                    mp.start();
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
