package com.example.mert.stoktakip.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.utils.ZamanFormatlayici;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;
import java.util.Date;

public class IslemGecmisiFiltreleDialog extends AppCompatDialogFragment
        implements DatePickerDialog.OnDateSetListener, TarihSecimiDialog.TarihSecimiDialogListener {

    Button baslangicTarihiBtn;
    Button bitisTarihiBtn;
    Button filtreleBtn;
    MaterialSpinner islemTuruSpinner;

    ZamanFormatlayici zf = new ZamanFormatlayici();
    IslemGecmisiFiltreleDialogListener listener;

    String tur;

    public interface IslemGecmisiFiltreleDialogListener {
        void filtreParametreleriniGetir(String baslangicTarihi, String bitisTarihi, String islemTuru);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_islem_gecmisi_filtre, null);

        dialog.setView(view);

        baslangicTarihiBtn = view.findViewById(R.id.btn_baslangic_tarihi);
        bitisTarihiBtn = view.findViewById(R.id.btn_bitis_tarihi);
        islemTuruSpinner = view.findViewById(R.id.spinner_islem_turu);
        filtreleBtn = view.findViewById(R.id.btn_filtrele);

        String bugunString = zf.zamanFormatla(new Date(), "d MMM yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1);
        String dunString = zf.zamanFormatla(calendar, "d MMM yyyy");

        baslangicTarihiBtn.setText(dunString);
        bitisTarihiBtn.setText(bugunString);

        islemTuruSpinner.setItems("Tümü", "Alım", "Satım");

        filtreleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String baslangicTarihi = zf.zamanFormatla(baslangicTarihiBtn.getText().toString(), "d MMM yyyy", "yyyy-MM-dd");
                String bitisTarihi = zf.zamanFormatla(bitisTarihiBtn.getText().toString(), "d MMM yyyy", "yyyy-MM-dd");
                String islemTuru = islemTuruSpinner.getItems().get(islemTuruSpinner.getSelectedIndex()).toString();
                Log.i("mert", baslangicTarihi + " " + bitisTarihi + " " + islemTuru);
                listener.filtreParametreleriniGetir(baslangicTarihi, bitisTarihi, islemTuru);
                dismiss();
            }
        });

        baslangicTarihiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle degerler = new Bundle();
                // Başlangıç tarihi butonuna basılınca açılan tarih seçim dialoguna tur değişkeni
                // olarak "baslangicTarihi" stringi gönderiliyor. Daha sonra dialogdan değer döndürürken
                // bu değişkene bakılarak başlangıç tarihi mi yoksa bitiş tarihi mi olduğu anlaşılacak
                degerler.putString("tur", "baslangicTarihi");
                degerler.putString("tarih", baslangicTarihiBtn.getText().toString());
                DialogFragment dialog = new TarihSecimiDialog();
                dialog.setArguments(degerler);
                dialog.setTargetFragment(IslemGecmisiFiltreleDialog.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "Başlangıç Tarihi Seç");
            }
        });

        bitisTarihiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle degerler = new Bundle();
                // Başlangıç tarihi butonuna basılınca açılan tarih seçim dialoguna tur değişkeni
                // olarak "bitisTarihi" stringi gönderiliyor. Daha sonra dialogdan değer döndürürken
                // bu değişkene bakılarak başlangıç tarihi mi yoksa bitiş tarihi mi olduğu anlaşılacak
                degerler.putString("tur", "bitisTarihi");
                degerler.putString("tarih", bitisTarihiBtn.getText().toString());
                DialogFragment dialog = new TarihSecimiDialog();
                dialog.setArguments(degerler);
                dialog.setTargetFragment(IslemGecmisiFiltreleDialog.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "Bitiş Tarihi Seç");
            }
        });

        return dialog.create();
    }

    // Tarih seçiminin yapıldığı dialogdan dönen tarih değerleri buraya geliyor
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        String tarih = zf.zamanFormatla(calendar, "d MMM yyyy");

        // Eğer başlangıç tarihi seçildiyse başlangıç tarihi butonu değişiyor, başlangıç
        // tarihi seçilmemişse bitiş tarihi seçilmiştir
        if (tur.equals("baslangicTarihi")) {
            baslangicTarihiBtn.setText(tarih);
        } else {
            bitisTarihiBtn.setText(tarih);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (IslemGecmisiFiltreleDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "TarihSecimiDialogListener implement etmek gerekiyor");
        }
    }

    // Tarih seçimi dialogundan türü çekiyor
    @Override
    public void turGetir(String tur) {
        this.tur = tur;
    }
}
