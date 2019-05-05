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

public class UrunBilgileriDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_urunbilgisi, null);

        builder.setView(view).setTitle("Ürün Bilgileri");

        TextView adTV = view.findViewById(R.id.txt_urun_adi);
        TextView barkodTV = view.findViewById(R.id.barkod_no);
        TextView adetTV = view.findViewById(R.id.urun_adeti);
        TextView alisSatisTV = view.findViewById(R.id.txt_alis_satis_fiyati);
        Button sil = view.findViewById(R.id.btn_sil);
        Button guncelle = view.findViewById(R.id.btn_guncelle);
        Button iptal = view.findViewById(R.id.btn_iptal);

        Bundle degerler = getArguments();
        String ad = degerler.getString("ad");
        String barkod = degerler.getString("barkod");
        int adet = degerler.getInt("adet");
        String alisSatis = degerler.getString("alissatis");

        adTV.setText(ad);
        barkodTV.setText(barkod);
        adetTV.setText(String.valueOf(adet));
        alisSatisTV.setText(alisSatis);

        sil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Ürün Sil")
                        .setMessage("Bu ürünü silmek istediğinizden emin misiniz?")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                VeritabaniIslemleri vti = new VeritabaniIslemleri(getActivity());
                                vti.urunSil(barkod);
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

        guncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UrunGuncelleActivity.class);
                intent.putExtra("barkod", barkod);
                intent.putExtra("ad", ad);
                intent.putExtra("alis", alisSatis.substring(0, alisSatis.indexOf("/")));
                intent.putExtra("satis", alisSatis.substring(alisSatis.indexOf("/") + 1));
                startActivity(intent);
            }
        });

        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void fragmentYenile() {
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof StokListesiFragment) {
            FragmentTransaction fragTransaction =   (getActivity()).getSupportFragmentManager().beginTransaction();
            fragTransaction.detach(currentFragment);
            fragTransaction.attach(currentFragment);
            fragTransaction.commit();
        }
    }

}
