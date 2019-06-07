package com.example.mert.stoktakip.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * {@code ZamanFormatlayici} sınıfı {@code String} şeklinde gelen bir tarih ya da saatin istenilen
 * şekilde formatlanmasını sağlar.
 */

public class ZamanFormatlayici {

    /**
     * @param zaman       Formatlanması istenilen zaman
     * @param girisBicimi Formatlanması istenilen zamanın formatlanmadan önceki biçimi. Örneğin
     *                    {@code zaman} parametresi {@code "2019-03-21 13:09:45"} gibi bir değer
     *                    ise {@code girisBicimi} {@code "yyyy-MM-dd HH:mm:ss} şeklinde girilmesi
     *                    gerekiyor.
     * @param cikisBicimi Formatlanması istenilen zamanın formatlandıktan sonraki biçimi. Örneğin
     *                    sadece saat ve dakikanın alınması isteniyorsa {@code "HH:mm"} şeklinde
     *                    girilmesi gerekiyor.
     * @return Formatlanmış zaman
     */
    public String zamanFormatla(String zaman, String girisBicimi, String cikisBicimi) {
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

    /**
     * @param zaman       İçinde formatlanması istenilen zamanı tutan {@code Calendar} nesnesi
     * @param cikisBicimi Formatlanması istenilen zamanın formatlandıktan sonraki biçimi. Örneğin
     *                    sadece saat ve dakikanın alınması isteniyorsa {@code "HH:mm"} şeklinde
     *                    girilmesi gerekiyor.
     * @return Formatlanmış zaman
     */
    public String zamanFormatla(Calendar zaman, String cikisBicimi) {
        return new SimpleDateFormat(cikisBicimi, Locale.getDefault()).format(zaman.getTime());
    }

    /**
     * @param zaman       İçinde formatlanması istenilen zamanı tutan {@code Date} nesnesi
     * @param cikisBicimi Formatlanması istenilen zamanın formatlandıktan sonraki biçimi. Örneğin
     *                    sadece saat ve dakikanın alınması isteniyorsa {@code "HH:mm"} şeklinde
     *                    girilmesi gerekiyor.
     * @return Formatlanmış zaman
     */
    public String zamanFormatla(Date zaman, String cikisBicimi) {
        SimpleDateFormat cikisFormati = new SimpleDateFormat(cikisBicimi, Locale.getDefault());
        return cikisFormati.format(zaman);
    }

}
