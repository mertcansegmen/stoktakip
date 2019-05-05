package com.example.mert.stoktakip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;

import java.util.ArrayList;

import me.himanshusoni.quantityview.QuantityView;

public class UrunAdapterUrunAlSat extends ArrayAdapter<Urun> {

    private Context context;
    private int resource;


    public UrunAdapterUrunAlSat(@NonNull Context context, int resource, ArrayList<Urun> urun) {
        super(context, resource, urun);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String barkodNo = getItem(position).getBarkodNo();
        String urunAdi = getItem(position).getAd();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView tvAd = convertView.findViewById(R.id.txt_urun_adi);
        TextView tvBarkodNo = convertView.findViewById(R.id.barkod_no);
        QuantityView quantityView = convertView.findViewById(R.id.quantityView);
        tvAd.setText(urunAdi);
        tvBarkodNo.setText(barkodNo);

        return convertView;
    }

    private void klavyeyiGizle(View convertView) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(convertView.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
