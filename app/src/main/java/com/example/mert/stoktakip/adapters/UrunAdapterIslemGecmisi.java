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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class UrunAdapterIslemGecmisi extends ArrayAdapter<UrunIslemi> {

    private Context context;
    private int resource;
    private int lastPosition = -1;

    static class ViewHolder {
        ImageView islemTuruTV;
        TextView urunAdiTV;
        TextView kullanici;
        TextView urunAdeti;
        TextView ayTV;
        TextView gunTV;
        TextView saatTV;
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

        String islemTuru = getItem(position).getIslemTuru();
        String barkod = getItem(position).getBarkodNo();
        String kadi = getItem(position).getKadi();
        int adet = getItem(position).getAdet();
        String urunAdi = (vti.barkodaGoreUrunGetir(barkod)).getAd();
        if(urunAdi == null || urunAdi.equals("")){
            urunAdi = "Silinmiş ürün";
        }
        String tarih = getItem(position).getIslemTarihi();
        String[] tarihElemanlari = parseDate(tarih);
        String ay = tarihElemanlari[0];
        String gun = tarihElemanlari[1];
        String saat = tarihElemanlari[2];

        final View result;
        ViewHolder holder;
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.liste_elemani_islem_gecmisi, parent, false);

            holder = new UrunAdapterIslemGecmisi.ViewHolder();

            holder.islemTuruTV = convertView.findViewById(R.id.img_islem_turu);
            holder.urunAdiTV = convertView.findViewById(R.id.txt_urun_adi);
            holder.urunAdeti = convertView.findViewById(R.id.txt_adet);
            holder.kullanici = convertView.findViewById(R.id.txt_kullanici);
            holder.ayTV = convertView.findViewById(R.id.txt_ay);
            holder.gunTV = convertView.findViewById(R.id.txt_gun);
            holder.saatTV = convertView.findViewById(R.id.txt_saat);

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

        if(islemTuru.equals("in"))
            holder.islemTuruTV.setImageResource(R.drawable.ic_urunal_yesil);
        else
            holder.islemTuruTV.setImageResource(R.drawable.ic_urunsat_kirmizi);
        holder.urunAdiTV.setText(urunAdi);
        holder.kullanici.setText(kadi);
        holder.urunAdeti.setText(String.valueOf(adet));
        holder.ayTV.setText(ay);
        holder.gunTV.setText(gun);
        holder.saatTV.setText(saat);

        return convertView;
    }
    private String[] parseDate(String tarih) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";

        String outputPatternAy = "MMM";
        String outputPatternGun = "dd";
        String outputPatternSaat = "HH:mm";

        String[] sonuc = new String[3];

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormatAy = new SimpleDateFormat(outputPatternAy, Locale.getDefault());
        SimpleDateFormat outputFormatGun = new SimpleDateFormat(outputPatternGun, Locale.getDefault());
        SimpleDateFormat outputFormatSaat = new SimpleDateFormat(outputPatternSaat, Locale.getDefault());


        try {
            Date date = inputFormat.parse(tarih);
            sonuc[0] = outputFormatAy.format(date);
            sonuc[1] = outputFormatGun.format(date);
            sonuc[2] = outputFormatSaat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sonuc;
    }
}
