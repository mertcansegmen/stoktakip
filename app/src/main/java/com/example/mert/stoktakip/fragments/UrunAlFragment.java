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
import com.example.mert.stoktakip.models.UrunIslemi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.example.mert.stoktakip.utils.TouchInterceptorLayout;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.adapters.UrunAlSatAdapter;
import com.example.mert.stoktakip.activities.BarkodOkuyucuActivity;
import com.example.mert.stoktakip.dialogs.UrunListesiDialog;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.util.ArrayList;

import me.himanshusoni.quantityview.QuantityView;

public class UrunAlFragment extends Fragment implements UrunListesiDialog.UrunListesiDialogListener {
    SearchView search;
    TouchInterceptorLayout til;
    ImageButton barkodBtn;
    TextView sepetiBosaltBtn;
    TextView sepetBosTxt;
    ExpandableHeightListView liste;
    Button urunAlBtn;
    EditText aciklamaTxt;

    UrunAlSatAdapter adapter;
    MediaPlayer mp;

    ArrayList<Urun> urunler = new ArrayList<>();
    String kadi;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_urun_al, container, false);

        search = v.findViewById(R.id.search_view);
        sepetBosTxt = v.findViewById(R.id.txt_bos_sepet);
        liste = v.findViewById(R.id.liste);
        barkodBtn = v.findViewById(R.id.btn_barkod);
        urunAlBtn = v.findViewById(R.id.btn_urun_al);
        sepetiBosaltBtn = v.findViewById(R.id.btn_sepeti_bosalt);
        aciklamaTxt = v.findViewById(R.id.txt_aciklama);
        til = v.findViewById(R.id.interceptorLayout);
        mp = MediaPlayer.create(v.getContext(), R.raw.scan_sound);

        Bundle bundle = this.getArguments();
        kadi = bundle.getString("kadi");

        adapter = new UrunAlSatAdapter(getActivity(), R.layout.liste_elemani_urun_al_sat, urunler);
        liste.setAdapter(adapter);
        liste.setExpanded(true);

        urunAlBtn.setOnClickListener(e -> urunAl());
        sepetiBosaltBtn.setOnClickListener( e -> sepetiBosalt());
        barkodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BarkodOkuyucuActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        til.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = new UrunListesiDialog();
                dialog.setTargetFragment(UrunAlFragment.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "Urun Listesi");
            }
        });

        return v;
    }

    private void urunAl() {
        for(int i = 0; i < urunler.size(); i++){
            VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());
            View view = liste.getChildAt(i);
            QuantityView quantityView = view.findViewById(R.id.quantity_view);
            int adet = quantityView.getQuantity();
            if(!vti.urunAdetiGuncelle(urunler.get(i).getBarkodNo(), adet)) {
                new GlideToast.makeToast(getActivity(), "Adet güncelleme hatası.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                return;
            }
            UrunIslemi urunIslemi = new UrunIslemi();
            urunIslemi.setIslemTuru("in");
            urunIslemi.setBarkodNo(urunler.get(i).getBarkodNo());
            urunIslemi.setKadi(kadi);
            urunIslemi.setAdet(adet);
            urunIslemi.setUrunFiyati(urunler.get(i).getAlis());
            urunIslemi.setAciklama(aciklamaTxt.getText().toString());
            if(vti.urunIslemiEkle(urunIslemi) == -1){
                new GlideToast.makeToast(getActivity(), "Ürün alımı ekleme hatası.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                return;
            }
            new GlideToast.makeToast(getActivity(), "Alım başarılı.", GlideToast.LENGTHTOOLONG, GlideToast.SUCCESSTOAST).show();
        }
        sepetiBosalt();
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
                        sepetBosTxt.setVisibility(View.GONE);
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
        sepetBosTxt.setVisibility(View.GONE);
        adapter.notifyDataSetChanged();
    }

    private void sepetiBosalt() {
        sepetiBosaltBtn.setVisibility(View.INVISIBLE);
        urunler.clear();
        adapter.clear();
        liste.setAdapter(adapter);
        sepetBosTxt.setVisibility(View.VISIBLE);
        aciklamaTxt.setText("");
    }
}
