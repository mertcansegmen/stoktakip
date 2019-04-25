package com.example.mert.stoktakip.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.UrunAlis;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.example.mert.stoktakip.utils.TouchInterceptorLayout;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.adapters.UrunAdapterUrunAlSat;
import com.example.mert.stoktakip.activities.BarkodOkuyucuActivity;
import com.example.mert.stoktakip.utils.UrunListesiDialog;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.util.ArrayList;

import me.himanshusoni.quantityview.QuantityView;

public class UrunAlFragment extends Fragment implements UrunListesiDialog.UrunListesiDialogListener {
    SearchView search;
    TextView sepetBos;
    ExpandableHeightListView liste;
    TouchInterceptorLayout til;
    ImageButton barkodBtn;
    Button urunAlBtn;
    TextView sepetiBosaltBtn;
    EditText aciklama;
    UrunAdapterUrunAlSat adapter;
    MediaPlayer mp;
    ArrayList<Urun> urunler = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_urunal, container, false);
        search = v.findViewById(R.id.search_view);
        sepetBos = v.findViewById(R.id.sepetbos);
        liste = v.findViewById(R.id.urun_liste);
        barkodBtn = v.findViewById(R.id.btn_barcode);
        urunAlBtn = v.findViewById(R.id.btn_urunal);
        sepetiBosaltBtn = v.findViewById(R.id.btn_sepeti_bosalt);
        aciklama = v.findViewById(R.id.aciklama);
        til = v.findViewById(R.id.interceptorLayout);
        mp = MediaPlayer.create(v.getContext(), R.raw.scan_sound);

        barkodBtn.setOnClickListener(e -> barkodOkuyucuAc());
        urunAlBtn.setOnClickListener(e -> urunAl());
        sepetiBosaltBtn.setOnClickListener( e -> sepetiBosalt());
        til.setOnClickListener(e -> urunSec());

        adapter = new UrunAdapterUrunAlSat(getActivity(), R.layout.liste_elemani_urun_alsat, urunler);
        liste.setAdapter(adapter);
        liste.setExpanded(true);

        return v;
    }

    private void sepetiBosalt() {
        sepetiBosaltBtn.setVisibility(View.INVISIBLE);
        urunler.clear();
        adapter.clear();
        liste.setAdapter(adapter);
        sepetBos.setVisibility(View.VISIBLE);
        aciklama.setText("");
    }

    private void urunAl() {
        for(int i = 0; i < urunler.size(); i++){
            VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());
            View view = liste.getChildAt(i);
            QuantityView quantityView = view.findViewById(R.id.quantityView);
            int adet = quantityView.getQuantity();
            if(!vti.urunAdetiGuncelle(urunler.get(i).getBarkodNo(), adet)) {
                new GlideToast.makeToast(getActivity(), "Adet güncelleme hatası.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                return;
            }
            UrunAlis urunAlis = new UrunAlis();
            urunAlis.setBarkodNo(urunler.get(i).getBarkodNo());
            urunAlis.setAdet(adet);
            urunAlis.setAlisFiyati(urunler.get(i).getAlis());
            urunAlis.setAciklama(aciklama.getText().toString());
            if(vti.urunAlisEkle(urunAlis) == -1){
                new GlideToast.makeToast(getActivity(), "Ürün alışı ekleme hatası.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                return;
            }
            new GlideToast.makeToast(getActivity(), "Alım başarılı.", GlideToast.LENGTHTOOLONG, GlideToast.SUCCESSTOAST).show();
        }
        sepetiBosalt();
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
                    // Eğer barkodu okutulan ürün veritabanında yoksa hata ver
                    if(!vti.urunTekrariKontrolEt(urun.getBarkodNo())){
                        new GlideToast.makeToast(getActivity(), "Ürün bulunamadı.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                    }
                    // Eğer seçilen ürün zaten sepette varsa hata ver
                    else if(urunSepetteEkliMi(urun)){
                        new GlideToast.makeToast(getActivity(), "Ürün zaten sepette ekli.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                    }
                    // Eğer ürünün barkodu çekilemediyse hata ver
                    else if(urun.getBarkodNo() == null){
                        new GlideToast.makeToast(getActivity(), "Hata.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                    }
                    else {
                        urunler.add(urun);
                        sepetiBosaltBtn.setVisibility(View.VISIBLE);
                        sepetBos.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        mp.start();
                    }
                }
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private boolean urunSepetteEkliMi(Urun urun) {
        for(int i=0; i<urunler.size(); i++){
            if(urunler.get(i).getBarkodNo().equals(urun.getBarkodNo()))
                return true;
        }
        return false;
    }

    // Ürün arama butonunun click listener'ı
    private void urunSec() {
        DialogFragment dialog = new UrunListesiDialog();
        dialog.setTargetFragment(this, 0);
        dialog.show(getActivity().getSupportFragmentManager(), "Urun Listesi");
    }


    @Override
    public void barkodGetir(String barkod) {
        VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());
        Urun urun = vti.barkodaGoreUrunGetir(barkod);
        // Eğer seçilen ürün zaten sepette varsa hata veriyor
        if(urunSepetteEkliMi(urun)){
            new GlideToast.makeToast(getActivity(), "Ürün zaten sepette ekli.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
            return;
        }
        urunler.add(urun);
        sepetiBosaltBtn.setVisibility(View.VISIBLE);
        sepetBos.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }
}
