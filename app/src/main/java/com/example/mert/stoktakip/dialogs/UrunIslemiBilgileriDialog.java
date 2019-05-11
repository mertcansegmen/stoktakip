package com.example.mert.stoktakip.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.UrunIslemi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UrunIslemiBilgileriDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_urun_islemi_bilgisi, null);

        dialog.setView(view).setTitle("Ürün İşlemi Bilgileri");

        TextView urunAdiTxt = view.findViewById(R.id.txt_urun_adi);
        TextView barkodNoTxt = view.findViewById(R.id.txt_barkod_no);
        TextView islemTuruTxt = view.findViewById(R.id.txt_islem_turu);
        TextView urunAdetiTxt = view.findViewById(R.id.txt_urun_adeti);
        TextView urunFiyatiTxt = view.findViewById(R.id.txt_urun_fiyati);
        TextView kullaniciTxt = view.findViewById(R.id.txt_kullanici);
        TextView aciklamaTxt = view.findViewById(R.id.txt_aciklama);
        TextView gunTxt = view.findViewById(R.id.txt_gun);
        TextView ayTxt = view.findViewById(R.id.txt_ay);
        TextView saatTxt = view.findViewById(R.id.txt_saat);
        Button iptalBtn = view.findViewById(R.id.btn_iptal);

        Bundle degerler = getArguments();
        assert degerler != null;
        int urunIslemiId = degerler.getInt("urun_islemi_id");

        VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());

        UrunIslemi urunIslemi = vti.urunIslemiGetir(urunIslemiId);

        String urunAdi = (vti.barkodaGoreUrunGetir(urunIslemi.getBarkodNo())).getAd();
        if(urunAdi == null || urunAdi.equals(""))
            urunAdi = "Silinmiş Ürün";
        urunAdiTxt.setText(urunAdi);
        barkodNoTxt.setText(urunIslemi.getBarkodNo());
        if(urunIslemi.getIslemTuru().equals("in"))
            islemTuruTxt.setText("Alım");
        else
            islemTuruTxt.setText("Satım");
        urunAdetiTxt.setText(String.valueOf(urunIslemi.getAdet()));
        urunFiyatiTxt.setText(String.valueOf(urunIslemi.getUrunFiyati()));
        kullaniciTxt.setText(urunIslemi.getKadi());
        if(!urunIslemi.getAciklama().equals(""))
            aciklamaTxt.setText(urunIslemi.getAciklama());
        gunTxt.setText(tarihFormatiDegistir(urunIslemi.getIslemTarihi())[1]);
        ayTxt.setText(tarihFormatiDegistir(urunIslemi.getIslemTarihi())[0]);
        saatTxt.setText(tarihFormatiDegistir(urunIslemi.getIslemTarihi())[2]);

        iptalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return dialog.create();
    }

    private String[] tarihFormatiDegistir(String tarih) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";

        String outputPatternAy = "MMM";
        String outputPatternGun = "dd";
        String outputPatternSaat = "HH:mm";

        String[] parcalanmisTarihler = new String[3];

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.getDefault());
        SimpleDateFormat outputFormatAy = new SimpleDateFormat(outputPatternAy, Locale.getDefault());
        SimpleDateFormat outputFormatGun = new SimpleDateFormat(outputPatternGun, Locale.getDefault());
        SimpleDateFormat outputFormatSaat = new SimpleDateFormat(outputPatternSaat, Locale.getDefault());

        try {
            Date date = inputFormat.parse(tarih);
            parcalanmisTarihler[0] = outputFormatAy.format(date);
            parcalanmisTarihler[1] = outputFormatGun.format(date);
            parcalanmisTarihler[2] = outputFormatSaat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parcalanmisTarihler;
    }
}
