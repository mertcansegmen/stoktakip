package com.example.mert.stoktakip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mert.stoktakip.models.Urun;

import java.util.ArrayList;

public class UrunAdapterIslemGecmisi extends ArrayAdapter<Urun> {

    private Context context;
    private int resource;
    private int lastPosition = -1;

    static class ViewHolder {
        ImageView islemTuru;
        TextView urunAdi;
        TextView barkodNo;
        TextView adet;
        TextView alisFiyati;
        TextView alisTarihi;
        TextView kullanici;
    }

    public UrunAdapterIslemGecmisi(@NonNull Context context, int resource, ArrayList<Urun> urunler) {
        super(context, resource, urunler);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
