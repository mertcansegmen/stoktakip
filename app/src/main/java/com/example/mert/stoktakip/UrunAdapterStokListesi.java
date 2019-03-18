package com.example.mert.stoktakip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UrunAdapterStokListesi extends ArrayAdapter<Urun> {

    private Context context;
    private int resource;
    private int lastPosition = -1;

    static class ViewHolder {
        TextView urunAdi;
        TextView barkodNo;
        TextView adet;
        TextView alisSatis;
    }

    public UrunAdapterStokListesi(@NonNull Context context, int resource, Urun[] urun) {
        super(context, resource, urun);
        this.context = context;
        this.resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String barkodNo = getItem(position).getBarkodNo();
        String urunAdi = getItem(position).getAd();
        int adet = getItem(position).getAdet();
        float alis = getItem(position).getAlis();
        float satis = getItem(position).getSatis();

        Urun urun = new Urun(barkodNo, urunAdi, adet, alis, satis);

        final View result;

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.liste_elemani_stok_listesi, parent, false);

            holder = new ViewHolder();
            holder.urunAdi = (TextView) convertView.findViewById(R.id.urun_adi);
            holder.barkodNo = (TextView) convertView.findViewById(R.id.barkod_no);
            holder.adet = (TextView) convertView.findViewById(R.id.urun_adeti);
            holder.alisSatis = (TextView) convertView.findViewById(R.id.alis_satis_fiyati);

            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
            result = convertView;
        }



        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.urunAdi.setText(urun.getAd());
        holder.barkodNo.setText(urun.getBarkodNo());
        holder.adet.setText(String.valueOf(urun.getAdet()));
        holder.alisSatis.setText(urun.getAlisSatis());

        return convertView;

    }
}
