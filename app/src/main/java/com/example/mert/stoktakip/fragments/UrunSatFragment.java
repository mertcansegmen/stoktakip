package com.example.mert.stoktakip.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.UrunAlis;
import com.example.mert.stoktakip.models.UrunSatis;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.example.mert.stoktakip.utils.TouchInterceptorLayout;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.adapters.UrunAdapterUrunAlSat;
import com.example.mert.stoktakip.activities.BarkodOkuyucuActivity;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;
import com.jeevandeshmukh.glidetoastlib.GlideToast;
import com.reginald.editspinner.EditSpinner;

import java.util.ArrayList;

public class UrunSatFragment extends Fragment {
    SearchView search;
    TextView sepetBos;
    ExpandableHeightListView liste;
    TouchInterceptorLayout til;
    ImageButton barkodBtn;
    Button urunSatBtn;
    TextView sepetiBosaltBtn;
    EditText aciklama;
    UrunAdapterUrunAlSat adapter;
    MediaPlayer mp;
    ArrayList<Urun> urunler = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_urunsat, container, false);
        search = v.findViewById(R.id.search_view);
        sepetBos = v.findViewById(R.id.sepetbos);
        liste = v.findViewById(R.id.urun_liste);
        barkodBtn = v.findViewById(R.id.btn_barcode);
        urunSatBtn = v.findViewById(R.id.btn_urunsat);
        sepetiBosaltBtn = v.findViewById(R.id.btn_sepeti_bosalt);
        aciklama = v.findViewById(R.id.aciklama);
        til = v.findViewById(R.id.interceptorLayout);
        mp = MediaPlayer.create(v.getContext(), R.raw.scan_sound);

        barkodBtn.setOnClickListener(e -> barkodOkuyucuAc());
        urunSatBtn.setOnClickListener(e -> urunSat());
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

    private void urunSat() {
        VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());

        // Yeterli sayıya sahip olmayan bir ya da daha fazla ürün bulunursa, bu ürünlerin adı, stoktaki adetleri ve satılmak istenen adet
        // sayıları bu listelerde tutulacak. Bu bilgiler daha sonra kullanıcıya gösterilecek
        ArrayList<String> yetersizStoklarAd = new ArrayList<>();
        ArrayList<Integer> yetersizStoklarAdet = new ArrayList<>();
        ArrayList<Integer> yetersizStoklarStoktakiAdet = new ArrayList<>();
        // Eğer urun sayısı yetmeyen bir ürün bulunursa boolean değer true olacak ve ürünleri güncelleyen döngüye girmeyecek
        boolean yetersizUrunBulundu = false;

        // Stokta yeterli miktar olmayan ürünler olup olmadığı kontrol ediliyor
        for(int i = 0; i < urunler.size(); i++){
            View view = liste.getChildAt(i);
            EditSpinner spinner = view.findViewById(R.id.spinner);
            int satilmakIstenenAdet = Integer.parseInt(spinner.getText().toString());
            int stoktakiAdet = vti.barkodaGoreUrunGetir(urunler.get(i).getBarkodNo()).getAdet();
            if(stoktakiAdet < satilmakIstenenAdet) {
                yetersizStoklarAd.add(urunler.get(i).getAd());
                yetersizStoklarAdet.add(satilmakIstenenAdet);
                yetersizStoklarStoktakiAdet.add(stoktakiAdet);
                yetersizUrunBulundu = true;
            }
        }
        // Eğer yeterli sayıya sahip olmayan bir ürün bulunduysa ürün adları, stoktaki adetleri ve satılmak istenen
        // adetleri AlertDialog ile kullanıcıya gösteriliyor ve ürünlerin veritabanında güncellendiği döngüye girmiyor
        if(yetersizUrunBulundu) {
            String mesaj = "";
            for(int i = 0; i < yetersizStoklarAd.size(); i++){
                mesaj = mesaj.concat(yetersizStoklarAd.get(i) + "\n    Stoktaki ürün sayısı: " + yetersizStoklarStoktakiAdet.get(i) +
                                    "\n    Satılmak istenilen sayı: " + yetersizStoklarAdet.get(i) + "\n");
            }
            new AlertDialog.Builder(getActivity())
                    .setTitle("Yeterli sayıda ürün yok")
                    .setMessage(mesaj)
                    .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return;
        }
        for(int i = 0; i < urunler.size(); i++){
            View view = liste.getChildAt(i);
            EditSpinner spinner = view.findViewById(R.id.spinner);
            int adet = Integer.parseInt(spinner.getText().toString());
            int stoktakiAdet = vti.barkodaGoreUrunGetir(urunler.get(i).getBarkodNo()).getAdet();
            // Ürünün stoktaki adeti satılmak istenen adetten azsa hata ver
            if(!vti.urunAdetiGuncelle(urunler.get(i).getBarkodNo(), adet*(-1))) {
                new GlideToast.makeToast(getActivity(), "Hata.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                return;
            }
            UrunSatis urunSatis = new UrunSatis();
            urunSatis.setBarkodNo(urunler.get(i).getBarkodNo());
            urunSatis.setAdet(adet);
            urunSatis.setSatisFiyati(urunler.get(i).getAlis());
            urunSatis.setAciklama(aciklama.getText().toString());
            if(vti.urunSatisEkle(urunSatis) == -1){
                new GlideToast.makeToast(getActivity(), "Ürün alışı ekleme hatası.", GlideToast.LENGTHTOOLONG, GlideToast.FAILTOAST).show();
                return;
            }
            new GlideToast.makeToast(getActivity(), "Satış başarılı.", GlideToast.LENGTHTOOLONG, GlideToast.SUCCESSTOAST).show();
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
                    urunler.add(urun);
                    sepetiBosaltBtn.setVisibility(View.VISIBLE);
                    sepetBos.setVisibility(View.GONE);
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
