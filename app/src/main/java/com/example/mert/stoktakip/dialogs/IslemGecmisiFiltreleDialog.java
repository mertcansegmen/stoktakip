package com.example.mert.stoktakip.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.mert.stoktakip.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class IslemGecmisiFiltreleDialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_islem_gecmisi_filtre, null);

        builder.setView(view);

        Button baslangicTarihiButon = view.findViewById(R.id.btn_baslangic);
        Button bitisTarihiButon = view.findViewById(R.id.btn_bitis);
        Spinner spinner = view.findViewById(R.id.spinner);
        Button filtreleButon = view.findViewById(R.id.btn_filtrele);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, -1);
        Date birOncekiGun = cal.getTime();
        baslangicTarihiButon.setText(new SimpleDateFormat("yyyy-MM-dd").format(birOncekiGun) + "  ");
        bitisTarihiButon.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "  ");

        baslangicTarihiButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bitisTarihiButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.spinner_islem_turleri, R.layout.spinner_elemani_islem_turleri);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_elemani_islem_turleri);
        spinner.setAdapter(spinnerAdapter);

        return builder.create();
    }
}
