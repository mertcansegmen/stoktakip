package com.example.mert.stoktakip.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import com.example.mert.stoktakip.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class IslemGecmisiFiltreleDialog extends AppCompatDialogFragment implements DatePickerDialog.OnDateSetListener, TarihSecimiDialog.TarihSecimiDialogListener {

    Button baslangicTarihiButon;
    Button bitisTarihiButon;
    String tur;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_islem_gecmisi_filtre, null);

        builder.setView(view);

        baslangicTarihiButon = view.findViewById(R.id.btn_baslangic);
        bitisTarihiButon= view.findViewById(R.id.btn_bitis);
        Spinner spinner = view.findViewById(R.id.spinner);
        Button filtreleButon = view.findViewById(R.id.btn_filtrele);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        Date dun = cal.getTime();
        Date bugun = new Date();
        String dunString = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(dun);
        String bugunString = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(bugun);
        baslangicTarihiButon.setText(dunString);
        bitisTarihiButon.setText(bugunString);

        baslangicTarihiButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle degerler = new Bundle();
                degerler.putString("tur", "baslangicTarihi");
                degerler.putString("tarih", baslangicTarihiButon.getText().toString());
                DialogFragment dialog = new TarihSecimiDialog();
                dialog.setArguments(degerler);
                dialog.setTargetFragment(IslemGecmisiFiltreleDialog.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "Başlangıç Tarihi Seç");
            }
        });

        bitisTarihiButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle degerler = new Bundle();
                degerler.putString("tur", "bitisTarihi");
                degerler.putString("tarih", bitisTarihiButon.getText().toString());
                DialogFragment dialog = new TarihSecimiDialog();
                dialog.setArguments(degerler);
                dialog.setTargetFragment(IslemGecmisiFiltreleDialog.this, 0);
                dialog.show(getActivity().getSupportFragmentManager(), "Bitiş Tarihi Seç");
            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_islem_turleri, R.layout.spinner_elemani_islem_turleri);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_elemani_islem_turleri);
        spinner.setAdapter(spinnerAdapter);

        return builder.create();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String tarih = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        if(tur.equals("baslangicTarihi")){
            baslangicTarihiButon.setText(tarih);
        } else {
            bitisTarihiButon.setText(tarih);
        }
    }

    @Override
    public void turGetir(String tur) {
        this.tur = tur;
    }
}
