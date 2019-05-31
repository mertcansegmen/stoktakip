package com.example.mert.stoktakip.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ZamanFormatlayici {

    public String zamanFormatla(String zaman, String girisBicimi, String cikisBicimi){
        String formatliZaman = "";

        SimpleDateFormat girisFormati = new SimpleDateFormat(girisBicimi, Locale.getDefault());
        SimpleDateFormat cikisFormati = new SimpleDateFormat(cikisBicimi, Locale.getDefault());
        try {
            Date date = girisFormati.parse(zaman);
            formatliZaman = cikisFormati.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatliZaman;
    }

}
