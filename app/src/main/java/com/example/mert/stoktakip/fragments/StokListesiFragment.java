package com.example.mert.stoktakip.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.activities.UrunEkleActivity;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.adapters.StokListesiAdapter;
import com.example.mert.stoktakip.activities.BarkodOkuyucuActivity;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.example.mert.stoktakip.dialogs.UrunBilgileriDialog;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.util.ArrayList;

public class StokListesiFragment extends Fragment {

    SearchView search;
    ImageButton barkodBtn;
    ListView liste;
    FloatingActionButton urunEkleBtn;

    MediaPlayer mp;
    StokListesiAdapter adapter;
    VeritabaniIslemleri vti;

    ArrayList<Urun> urunler;
    boolean barkodAramasiYapildi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_stok_listesi, container, false);

        search = v.findViewById(R.id.search_view);
        liste = v.findViewById(R.id.liste);
        barkodBtn = v.findViewById(R.id.btn_barkod);
        urunEkleBtn = v.findViewById(R.id.btn_urun_ekle);
        mp = MediaPlayer.create(v.getContext(), R.raw.scan_sound);

        vti = new VeritabaniIslemleri(getContext());
        // Veritabanında bulunan bütün ürünleri çekip urunler listesine ekliyor.
        // Bu 3 satır kod çalışınca bütün ürünler ekrandaki listview'e ekleniyor.
        urunler = vti.butunUrunleriGetir();
        adapter = new StokListesiAdapter(getContext(), R.layout.liste_elemani_stok_listesi, urunler);
        liste.setAdapter(adapter);

        // SearchView'da yazılan değer her değiştiğinde liste filtreleniyor
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

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Urun urun = urunler.get(position);

                Bundle degerler = new Bundle();
                degerler.putString("ad", urun.getAd());
                degerler.putString("barkod", urun.getBarkodNo());
                degerler.putInt("adet", urun.getAdet());
                degerler.putFloat("alis_fiyati", urun.getAlis());
                degerler.putFloat("satis_fiyati", urun.getSatis());

                DialogFragment dialog = new UrunBilgileriDialog();
                dialog.setArguments(degerler);
                dialog.show(getActivity().getSupportFragmentManager(), "Ürün Bilgileri");
            }
        });

        urunEkleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UrunEkleActivity.class);
                startActivity(intent);
            }
        });
        barkodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BarkodOkuyucuActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        return v;
    }

    // Barkod tarayıcı kapanınca okunan barkoda sahip ürünü filtreliyor
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0){
            if(resultCode == CommonStatusCodes.SUCCESS){
                if(data != null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    Urun urun = vti.barkodaGoreUrunGetir(barcode.displayValue);
                    if(!vti.urunTekrariKontrolEt(urun.getBarkodNo())){
                        new GlideToast.makeToast(getActivity(), "Ürün bulunamadı.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                    }
                    else {
                        adapter.getFilter().filter(barcode.displayValue);
                        barkodAramasiYapildi = true;
                        mp.start();
                    }
                }
                else{
                    new GlideToast.makeToast(getActivity(), "Barkod okunmadı.",
                            GlideToast.LENGTHTOOLONG, GlideToast.INFOTOAST).show();
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        urunler.clear();
        urunler = vti.butunUrunleriGetir();
        adapter = new StokListesiAdapter(getContext(), R.layout.liste_elemani_stok_listesi, urunler);
        liste.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
