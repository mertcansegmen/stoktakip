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
import com.example.mert.stoktakip.utils.ZamanFormatlayici;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class IslemGecmisiAdapter extends ArrayAdapter<UrunIslemi> {

    private Context context;
    private int resource;
    private int lastPosition = -1;

    static class ViewHolder {
        ImageView islemTuruTxt;
        TextView urunAdiTxt;
        TextView kadiTxt;
        TextView urunAdetiTxt;
        TextView ayTxt;
        TextView gunTxt;
        TextView saatTxt;
    }

    public IslemGecmisiAdapter(@NonNull Context context, int resource, ArrayList<UrunIslemi> islemler) {
        super(context, resource, islemler);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());
        ZamanFormatlayici zf = new ZamanFormatlayici();

        String islemTuru = getItem(position).getIslemTuru();
        String barkod = getItem(position).getBarkodNo();
        String kadi = getItem(position).getKadi();
        int urunAdeti = getItem(position).getAdet();
        String urunAdi = (vti.barkodaGoreUrunGetir(barkod)).getAd();
        // Eğer veritabanında barkod no'dan ürün adı bulunmadıysa ürün silinmiş demektir
        if (urunAdi == null || urunAdi.equals("")) {
            urunAdi = "Silinmiş ürün";
        }
        // Veritabanından tarih "yyyy-MM-dd HH:mm:ss" formatında geliyor
        // ZamanFormatlayici classını kullanarak tarih; ay, gün, saat ve dakika olarak parçalanıyor
        String islemTarihi = getItem(position).getIslemTarihi();
        String ay = zf.zamanFormatla(islemTarihi, "yyyy-MM-dd HH:mm:ss", "MMM");
        String gun = zf.zamanFormatla(islemTarihi, "yyyy-MM-dd HH:mm:ss", "dd");
        String saat = zf.zamanFormatla(islemTarihi, "yyyy-MM-dd HH:mm:ss", "HH:mm");

        final View result;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.liste_elemani_islem_gecmisi, parent, false);

            holder = new IslemGecmisiAdapter.ViewHolder();

            holder.islemTuruTxt = convertView.findViewById(R.id.img_islem_turu);
            holder.urunAdiTxt = convertView.findViewById(R.id.txt_urun_adi);
            holder.urunAdetiTxt = convertView.findViewById(R.id.txt_adet);
            holder.kadiTxt = convertView.findViewById(R.id.txt_kullanici);
            holder.ayTxt = convertView.findViewById(R.id.txt_ay);
            holder.gunTxt = convertView.findViewById(R.id.txt_gun);
            holder.saatTxt = convertView.findViewById(R.id.txt_saat);

            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        if (islemTuru.equals("in"))
            holder.islemTuruTxt.setImageResource(R.drawable.ic_urunal_yesil);
        else
            holder.islemTuruTxt.setImageResource(R.drawable.ic_urunsat_kirmizi);
        holder.urunAdiTxt.setText(urunAdi);
        holder.kadiTxt.setText(kadi);
        holder.urunAdetiTxt.setText(String.valueOf(urunAdeti));
        holder.ayTxt.setText(ay);
        holder.gunTxt.setText(gun);
        holder.saatTxt.setText(saat);

        return convertView;
    }
}
