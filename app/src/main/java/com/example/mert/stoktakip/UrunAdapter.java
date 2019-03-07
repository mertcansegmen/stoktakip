package com.example.mert.stoktakip;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UrunAdapter extends ArrayAdapter<Urun> {

    private Context context;
    private int resource;

    public UrunAdapter(@NonNull Context context, int resource, Urun[] urun) {
        super(context, resource, urun);
        context = context;
        resource = resource;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String barkodNo = getItem(position).getBarkodNo();
        String urunAdi = getItem(position).getAd();
        int adet = getItem(position).getAdet();
        float alis = getItem(position).getAlis();
        float satis = getItem(position).getSatis();
        String alisSatis = alis + "/" + satis;
        Urun urun = new Urun(barkodNo, urunAdi, adet, alis, satis);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.liste_elemani, parent, false);

        TextView urunAdiTV = convertView.findViewById(R.id.urun_adi);
        TextView barkodNoTV = convertView.findViewById(R.id.barkod_no);
        TextView adetTV = convertView.findViewById(R.id.urun_adeti);
        TextView alisSatisTV = convertView.findViewById(R.id.alis_satis_fiyati);

        urunAdiTV.setText(urunAdi);
        barkodNoTV.setText(barkodNo);
        adetTV.setText(adet+"");
        alisSatisTV.setText(alisSatis);

        return convertView;

    }
}
