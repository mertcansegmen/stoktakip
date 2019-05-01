package com.example.mert.stoktakip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.UrunIslemi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

import java.util.ArrayList;

public class UrunAdapterIslemGecmisi extends ArrayAdapter<UrunIslemi> {

    private Context context;
    private int resource;
    private int lastPosition = -1;

    static class ViewHolder {
        ImageView islemTuru;
        TextView urunAdi;
        TextView barkodNo;
        TextView kullanici;
        TextView urunAdeti;
        TextView urunFiyati;
        TextView islemTarihi;
    }

    public UrunAdapterIslemGecmisi(@NonNull Context context, int resource, ArrayList<UrunIslemi> islemler) {
        super(context, resource, islemler);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());

        String tur = getItem(position).getIslemTuru();
        String barkod = getItem(position).getBarkodNo();
        String kadi = getItem(position).getKadi();
        String ad = vti.barkodaGoreUrunGetir(barkod).getAd();
        int adet = getItem(position).getAdet();
        float fiyat = getItem(position).getUrunFiyati();
        String tarih = getItem(position).getIslemTarihi();

        UrunIslemi islem = new UrunIslemi(tur, barkod, kadi, adet, fiyat, tarih, ad);

        final View result;
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.liste_elemani_islem_gecmisi, parent, false);

            holder = new UrunAdapterIslemGecmisi.ViewHolder();

            holder.islemTuru = convertView.findViewById(R.id.img_islem_turu);
            holder.urunAdi = convertView.findViewById(R.id.urun_adi);
            holder.urunAdeti = convertView.findViewById(R.id.urun_adeti);
            holder.barkodNo = convertView.findViewById(R.id.barkod_no);
            holder.kullanici = convertView.findViewById(R.id.txt_kullanici);
            holder.urunFiyati = convertView.findViewById(R.id.txt_urun_fiyati);
            holder.islemTarihi = convertView.findViewById(R.id.txt_tarih);

            result = convertView;
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        if(islem.getIslemTuru().equals("in"))
            holder.islemTuru.setImageResource(R.drawable.ic_menu_urunal);
        else
            holder.islemTuru.setImageResource(R.drawable.ic_menu_urunsat);
        holder.urunAdi.setText(islem.getUrunAdi());
        holder.barkodNo.setText(islem.getBarkodNo());
        holder.kullanici.setText(islem.getKadi());
        holder.urunAdeti.setText(String.valueOf(islem.getAdet()));
        holder.urunFiyati.setText(String.valueOf(islem.getUrunFiyati()));
        holder.islemTarihi.setText(islem.getIslemTarihi());


        return convertView;
    }
}
