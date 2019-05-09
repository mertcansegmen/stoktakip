package com.example.mert.stoktakip.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mert.stoktakip.R;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import me.himanshusoni.quantityview.QuantityView;

public class AyarlarFragment extends Fragment {

    QuantityView quantityView;
    Button kaydetBtn;

    int varsayilanEsik = 10;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ayarlar, container, false);

        quantityView = v.findViewById(R.id.quantity_view);
        kaydetBtn = v.findViewById(R.id.btn_kaydet);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();

        quantityView.setQuantity(preferences.getInt("esik", varsayilanEsik));

        kaydetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int esik = quantityView.getQuantity();
                if (esik != preferences.getInt("esik", varsayilanEsik)){
                    editor.putInt("esik", esik);
                    editor.apply();
                }
                new GlideToast.makeToast(getActivity(), "Ayarlar güncellendi.",
                        GlideToast.LENGTHTOOLONG, GlideToast.SUCCESSTOAST).show();
            }
        });

        return v;
    }
}
