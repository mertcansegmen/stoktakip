package com.example.mert.stoktakip.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.KarCiroBilgisi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class IstatistiklerFragment extends Fragment {

    List<Float> cirolarListe = new ArrayList<>();
    List<Float> karlarListe = new ArrayList<>();
    List<String> zamanlarListe = new ArrayList<>();

    BarChart karCiroChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_istatistikler, container, false);

        karCiroChart = v.findViewById(R.id.kar_ciro_chart);

        VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());
        ArrayList<KarCiroBilgisi> karCiroBilgileri = vti.gunlukKarCiroBilgileriniGetir();

        ValueFormatter formatterMultipleBar = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if(zamanlarListe.size() > (int) value)
                    return zamanlarListe.get((int) value);
                else return "";
            }
        };

        for(int i = 0; i< karCiroBilgileri.size(); i++){
            cirolarListe.add(karCiroBilgileri.get(i).getCiro());
            karlarListe.add(karCiroBilgileri.get(i).getKar());
            zamanlarListe.add(karCiroBilgileri.get(i).getZaman());
        }

        int[] xIndeksleri = new int[karCiroBilgileri.size()];
        for(int i = 0; i< karCiroBilgileri.size(); i++){
            xIndeksleri[i] = i;
        }
        List<BarEntry> cirolarBarEntries = new ArrayList<>();
        List<BarEntry> karlarBarEntries = new ArrayList<>();

        for(int i = 0; i< karCiroBilgileri.size(); i++){
            cirolarBarEntries.add(new BarEntry(xIndeksleri[i], cirolarListe.get(i)));
            karlarBarEntries.add(new BarEntry(xIndeksleri[i], karlarListe.get(i)));
        }

        BarDataSet karlarBarDataSet = new BarDataSet(karlarBarEntries, "Günlük Kar (₺)");
        // Bar rengi
        karlarBarDataSet.setColor(getResources().getColor(R.color.karRenk));
        BarDataSet cirolarBarDataSet = new BarDataSet(cirolarBarEntries, "Günlük Ciro (₺)");
        // Bar rengi
        cirolarBarDataSet.setColor(getResources().getColor(R.color.ciroRenk));

        List<IBarDataSet> karCiroDataSets = new ArrayList<>();
        karCiroDataSets.add(cirolarBarDataSet);
        karCiroDataSets.add(karlarBarDataSet);

        BarData karCiroBarData = new BarData(karCiroDataSets);
        // Barların kalınlığını ayarlar
        karCiroBarData.setBarWidth(0.35f);
        karCiroChart.setData(karCiroBarData);

        XAxis xEkseniMultipleBar = karCiroChart.getXAxis();
        // X eksenindeki değerlerin aşağıda gözükmesini sağlar
        xEkseniMultipleBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        // X ekseni için ızgara çizgilerini gizler
        xEkseniMultipleBar.setDrawGridLines(false);
        // Yakınlaştırılınca x ekseninde fazladan değer çıkmasını engeller
        xEkseniMultipleBar.setGranularity(1f);
        // X ekseninin başlangıcında boşluk bırakır
        xEkseniMultipleBar.setAxisMinimum(karCiroBarData.getXMin() - 0.5f);
        // X ekseninin sonunda boşluk bırakır
        xEkseniMultipleBar.setAxisMaximum(karCiroBarData.getXMax() + 0.5f);
        // Değer formatlayıcıyı ayarlar
        xEkseniMultipleBar.setValueFormatter(formatterMultipleBar);


        YAxis yEkseniSolMultipleBar = karCiroChart.getAxisLeft();
        // sol Y ekseninde gözükecek değer sayısını ayarlar
        yEkseniSolMultipleBar.setLabelCount(5, false);
        // sol Y ekseninin göstereceği minimum değeri ayarlar
        yEkseniSolMultipleBar.setAxisMinimum(0f);

        YAxis yEkseniSagMultipleBar = karCiroChart.getAxisRight();
        // sağ Y ekseninde gözükecek değer sayısını ayarlar
        yEkseniSagMultipleBar.setLabelCount(5, false);
        // sağ Y ekseninin göstereceği minimum değeri ayarlar
        yEkseniSagMultipleBar.setAxisMinimum(0f);
        // sağ Y ekseni için ızgara çizgilerini gizler çünkü sol Y ekseni zaten gösteriyor
        yEkseniSagMultipleBar.setDrawGridLines(false);
        // sağ Y ekseninin çizgisini gizler
        yEkseniSagMultipleBar.setDrawAxisLine(false);

        // X eksenindeki sadece 7 değerin gözükmesini sağlar
        karCiroChart.setVisibleXRangeMaximum(7);
        // Tablonun en sondaki değerlerin gözükmesini sağlar
        karCiroChart.moveViewToX(xIndeksleri.length*1f);
        // Grafiğe çift tıkla yakınlaştırmayı kapatır
        karCiroChart.setDoubleTapToZoomEnabled(false);
        // X eksenine animasyon uygulayarak grafiği 750 milisaniyede çizer
        karCiroChart.animateX(750);
        // PinchZoom aktif edilir. false ayarlanırsa X ve Y ekseni için ayrı ayrı yakınlaştırılır
        karCiroChart.setPinchZoom(true);
        // Grafiğin sağ altındaki açıklamayı siler
        karCiroChart.getDescription().setEnabled(false);
        // Birden fazla bar varsa başlangıç noktasını, barlar arası uzaklığı ve bar grupları arası uzaklığı belirler
        karCiroChart.groupBars(-0.5f, 0.14f, 0.08f);
        karCiroBarData.setHighlightEnabled(false);
        // Grafiği yeniler
        karCiroChart.invalidate();
        return v;
    }
}
