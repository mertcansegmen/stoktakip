package com.example.mert.stoktakip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;

import java.util.ArrayList;
import java.util.Random;

import me.himanshusoni.quantityview.QuantityView;

public class UrunAlSatAdapter extends ArrayAdapter<Urun> {

    private Context context;
    private int resource;

    public UrunAlSatAdapter(@NonNull Context context, int resource, ArrayList<Urun> urun) {
        super(context, resource, urun);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView urunAdiTxt = convertView.findViewById(R.id.txt_urun_adi);
        TextView barkodNoTxt = convertView.findViewById(R.id.txt_barkod_no);

        QuantityView quantityView = convertView.findViewById(R.id.quantity_view);

        quantityView.setOnQuantityChangeListener(new QuantityView.OnQuantityChangeListener() {
            @Override
            public void onQuantityChanged(int oldQuantity, int newQuantity, boolean programmatically) {
                Random rnd = new Random();
                quantityView.setQuantity(oldQuantity+rnd.nextInt(30) + 20);
            }

            @Override
            public void onLimitReached() {

            }
        });


        String barkodNo = getItem(position).getBarkodNo();
        String urunAdi = getItem(position).getAd();

        urunAdiTxt.setText(urunAdi);
        barkodNoTxt.setText(barkodNo);

        return convertView;
    }
}
