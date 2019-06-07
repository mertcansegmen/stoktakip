package com.example.mert.stoktakip.dialogs;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.activities.UrunGuncelleActivity;
import com.example.mert.stoktakip.fragments.StokListesiFragment;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.jeevandeshmukh.glidetoastlib.GlideToast;

import java.util.Locale;

public class UrunBilgileriDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_urun_bilgisi, null);

        dialog.setView(view).setTitle("Ürün Bilgileri");

        TextView urunAdiTxt = view.findViewById(R.id.txt_urun_adi);
        TextView barkodNoTxt = view.findViewById(R.id.txt_barkod_no);
        TextView urunAdetiTxt = view.findViewById(R.id.txt_urun_adeti);
        TextView alisFiyatiTxt = view.findViewById(R.id.txt_alis_fiyati);
        TextView satisFiyatiTxt = view.findViewById(R.id.txt_satis_fiyati);
        Button silBtn = view.findViewById(R.id.btn_sil);
        Button guncelleBtn = view.findViewById(R.id.btn_guncelle);
        Button iptalBtn = view.findViewById(R.id.btn_iptal);

        Bundle degerler = getArguments();
        String urunAdi = degerler.getString("urun_adi");
        String barkodNo = degerler.getString("barkod");
        int urunAdeti = degerler.getInt("adet");
        float alisFiyati = degerler.getFloat("alis_fiyati");
        float satisFiyati = degerler.getFloat("satis_fiyati");

        urunAdiTxt.setText(urunAdi);
        barkodNoTxt.setText(barkodNo);
        urunAdetiTxt.setText(String.valueOf(urunAdeti));
        // Alış ve satış fiyatlarının noktadan sonraki iki basamağı gösterilmesi için (13.20 tl gibi)
        // formatlama işlemi yapılıyor
        alisFiyatiTxt.setText(String.format(Locale.getDefault(), "%.2f", alisFiyati));
        satisFiyatiTxt.setText(String.format(Locale.getDefault(), "%.2f", satisFiyati));

        silBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Ürün Sil")
                        .setMessage("Bu ürünü silmek istediğinizden emin misiniz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                VeritabaniIslemleri vti = new VeritabaniIslemleri(getActivity());
                                vti.urunSil(barkodNo);
                                dismiss();
                                fragmentYenile();
                                new GlideToast.makeToast(getActivity(), "Ürün silindi.", GlideToast.LENGTHTOOLONG,
                                        GlideToast.SUCCESSTOAST).show();
                            }
                        })
                        .setNegativeButton("Hayır", null)
                        .show();
            }
        });
        guncelleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UrunGuncelleActivity.class);
                intent.putExtra("barkod", barkodNo);
                intent.putExtra("urun_adi", urunAdi);
                intent.putExtra("alis_fiyati", String.format(Locale.getDefault(), "%.2f", alisFiyati));
                intent.putExtra("satis_fiyati", String.format(Locale.getDefault(), "%.2f", satisFiyati));
                startActivity(intent);
            }
        });
        iptalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog.create();
    }

    private void fragmentYenile() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof StokListesiFragment) {
            FragmentTransaction fragTransaction = (getActivity()).getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }
    }

}
