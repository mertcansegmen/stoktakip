package com.example.mert.stoktakip.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.example.mert.stoktakip.utils.ZamanFormatlayici;

public class TarihSecimiDialog extends DialogFragment {

    TarihSecimiDialogListener listener;

    // Dialog kapanınca tür bilgisini geri gönderir
    public interface TarihSecimiDialogListener {
        void turGetir(String tur);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        String tur = bundle.getString("tur");
        String tarih = bundle.getString("tarih");

        ZamanFormatlayici zf = new ZamanFormatlayici();

        int yil = Integer.parseInt(zf.zamanFormatla(tarih, "d MMM yyyy", "yyyy"));
        int ay = Integer.parseInt(zf.zamanFormatla(tarih, "d MMM yyyy", "M"));
        int gun = Integer.parseInt(zf.zamanFormatla(tarih, "d MMM yyyy", "d"));

        listener.turGetir(tur);

        DatePickerDialog dialog = new DatePickerDialog(getTargetFragment().getContext(),
                (DatePickerDialog.OnDateSetListener) getTargetFragment(), yil, ay - 1, gun);
        //dialog.getDatePicker().setMaxDate((new Date()).getTime());
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (TarihSecimiDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "TarihSecimiDialogListener implement etmek gerekiyor");
        }
    }
}
