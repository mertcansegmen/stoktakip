package com.example.mert.stoktakip.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TarihSecimiDialog extends DialogFragment {

    TarihSecimiDialogListener listener;

    public interface TarihSecimiDialogListener{
        void turGetir(String tur);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        String tur = bundle.getString("tur");
        String tarih = bundle.getString("tarih");

        Date date = new Date();

        try {
            date = new SimpleDateFormat("d MMM yyyy", Locale.getDefault()).parse(tarih);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c = Calendar.getInstance();

        c.setTime(date);
        int yil = c.get(Calendar.YEAR);
        int ay = c.get(Calendar.MONTH);
        int gun = c.get(Calendar.DAY_OF_MONTH);

        listener.turGetir(tur);

        DatePickerDialog dialog = new DatePickerDialog(getTargetFragment().getContext(),
                (DatePickerDialog.OnDateSetListener) getTargetFragment(), yil, ay, gun);
        dialog.getDatePicker().setMaxDate((new Date()).getTime());
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (TarihSecimiDialogListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "TarihSecimiDialogListener implement etmek gerekiyor");
        }
    }
}
