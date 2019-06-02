package com.example.mert.stoktakip.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.KarCiroBilgisi;
import com.example.mert.stoktakip.models.UrunGetirisi;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class IstatistiklerFragment extends Fragment {

    VeritabaniIslemleri vti;

    BarChart karCiroChart;
    HorizontalBarChart urunGetirisiChart;
    PieChart kullaniciGetirisiChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_istatistikler, container, false);

        karCiroChart = v.findViewById(R.id.kar_ciro_chart);
        urunGetirisiChart = v.findViewById(R.id.urun_getiri_chart);
        kullaniciGetirisiChart = v.findViewById(R.id.kullanici_satisi_chart);

        vti = new VeritabaniIslemleri(getContext());

        karCiroGrafigiCiz(karCiroChart);
        urunGetiriGrafigiCiz(urunGetirisiChart);
        kullaniciGetirisiGrafigiCiz(kullaniciGetirisiChart);

        return v;
    }

    private void kullaniciGetirisiGrafigiCiz(PieChart kullaniciGetirisiChart) {
        List<PieEntry> kullaniciGetirileriListe = vti.kullaniciGetirileriniGetir();

        PieDataSet kullaniciGetirileriDataSet = new PieDataSet(kullaniciGetirileriListe, "  -    Kullanıcının sağladığı Getiri (₺)");
        // Dilim renkleri
        kullaniciGetirileriDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        // Dilimlerin arasındaki boşluğu ayarlar
        kullaniciGetirileriDataSet.setSliceSpace(1f);
        // Dilim seçilince ne kadar dışarı çıkacağını ayarlar
        kullaniciGetirileriDataSet.setSelectionShift(4f);

        PieData data = new PieData(kullaniciGetirileriDataSet);
        // Değerlerin yazı büyüklüğü
        data.setValueTextSize(10f);
        // Değerlerin yazı rengi
        data.setValueTextColor(Color.WHITE);
        kullaniciGetirisiChart.setData(data);

        // Dataset label'ının rengi
        kullaniciGetirisiChart.getLegend().setTextColor(Color.WHITE);
        // Etiketlerin yazı rengi
        kullaniciGetirisiChart.setEntryLabelColor(Color.WHITE);
        // Grafiğin sağ altındaki açıklamayı gizler
        kullaniciGetirisiChart.getDescription().setEnabled(false);
        // Grafiğin iç tarafının boş olmasını engeller
        kullaniciGetirisiChart.setDrawHoleEnabled(false);
        // Dönme animasyonu
        kullaniciGetirisiChart.spin(1000, 0f, 360f, Easing.EaseInOutQuad);
        // Grafiği yeniler
        kullaniciGetirisiChart.invalidate();

    }

    private void karCiroGrafigiCiz(BarChart karCiroChart){

        List<Float> cirolarListe = new ArrayList<>();
        List<Float> karlarListe = new ArrayList<>();
        List<String> zamanlarListe = new ArrayList<>();

        ArrayList<KarCiroBilgisi> karCiroBilgileri = vti.gunlukKarCiroBilgileriniGetir();

        for(int i = 0; i< karCiroBilgileri.size(); i++){
            cirolarListe.add(karCiroBilgileri.get(i).getCiro());
            karlarListe.add(karCiroBilgileri.get(i).getKar());
            zamanlarListe.add(karCiroBilgileri.get(i).getZaman());
        }

        int[] xIndeksleriKarCiro = new int[karCiroBilgileri.size()];
        for(int i = 0; i< karCiroBilgileri.size(); i++){
            xIndeksleriKarCiro[i] = i;
        }
        List<BarEntry> cirolarBarEntries = new ArrayList<>();
        List<BarEntry> karlarBarEntries = new ArrayList<>();

        for(int i = 0; i< karCiroBilgileri.size(); i++){
            cirolarBarEntries.add(new BarEntry(xIndeksleriKarCiro[i], cirolarListe.get(i)));
            karlarBarEntries.add(new BarEntry(xIndeksleriKarCiro[i], karlarListe.get(i)));
        }

        BarDataSet karlarBarDataSet = new BarDataSet(karlarBarEntries, "Günlük Kar (₺)");
        // Bar rengi
        karlarBarDataSet.setColor(getResources().getColor(R.color.karRenk));
        // Barların üstünde çıkan değerlerin yazı boyutu
        karlarBarDataSet.setValueTextSize(8);
        BarDataSet cirolarBarDataSet = new BarDataSet(cirolarBarEntries, "Günlük Ciro (₺)");
        // Bar rengi
        cirolarBarDataSet.setColor(getResources().getColor(R.color.ciroRenk));
        // Barların üstünde çıkan değerlerin yazı boyutu
        cirolarBarDataSet.setValueTextSize(8);

        List<IBarDataSet> karCiroDataSets = new ArrayList<>();
        karCiroDataSets.add(cirolarBarDataSet);
        karCiroDataSets.add(karlarBarDataSet);

        BarData karCiroBarData = new BarData(karCiroDataSets);
        // Barların kalınlığını ayarlar
        karCiroBarData.setBarWidth(0.35f);
        // Barların üstünde çıkan değerlerin rengi
        karCiroBarData.setValueTextColor(Color.WHITE);
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
        xEkseniMultipleBar.setValueFormatter(new IndexAxisValueFormatter(zamanlarListe));
        // X eksenindeki yazıların rengi
        xEkseniMultipleBar.setTextColor(Color.WHITE);

        YAxis yEkseniSolMultipleBar = karCiroChart.getAxisLeft();
        // sol Y ekseninde gözükecek değer sayısını ayarlar
        yEkseniSolMultipleBar.setLabelCount(5, false);
        // sol Y ekseninin göstereceği minimum değeri ayarlar
        yEkseniSolMultipleBar.setAxisMinimum(0f);
        // sol Y eksenindeki yazıların rengi
        yEkseniSolMultipleBar.setTextColor(Color.WHITE);

        YAxis yEkseniSagMultipleBar = karCiroChart.getAxisRight();
        // sağ Y ekseninde gözükecek değer sayısını ayarlar
        yEkseniSagMultipleBar.setLabelCount(5, false);
        // sağ Y ekseninin göstereceği minimum değeri ayarlar
        yEkseniSagMultipleBar.setAxisMinimum(0f);
        // sağ Y ekseni için ızgara çizgilerini gizler çünkü sol Y ekseni zaten gösteriyor
        yEkseniSagMultipleBar.setDrawGridLines(false);
        // sağ Y ekseninin çizgisini gizler
        yEkseniSagMultipleBar.setDrawAxisLine(false);
        // sağ Y eksenindeki yazıların rengi
        yEkseniSagMultipleBar.setTextColor(Color.WHITE);

        // Dataset label'ının rengi
        karCiroChart.getLegend().setTextColor(Color.WHITE);
        // X eksenindeki sadece 7 değerin gözükmesini sağlar
        karCiroChart.setVisibleXRangeMaximum(7);
        // Tablonun en sondaki değerlerin gözükmesini sağlar
        karCiroChart.moveViewToX(xIndeksleriKarCiro.length);
        // Grafiğe çift tıkla yakınlaştırmayı kapatır
        karCiroChart.setDoubleTapToZoomEnabled(false);
        // Y eksenine animasyon uygulayarak grafiği 1000 milisaniyede çizer
        karCiroChart.animateY(1000);
        // PinchZoom aktif edilir. false ayarlanırsa X ve Y ekseni için ayrı ayrı yakınlaştırılır
        karCiroChart.setPinchZoom(true);
        // Grafiğin sağ altındaki açıklamayı siler
        karCiroChart.getDescription().setEnabled(false);
        // Birden fazla bar varsa başlangıç noktasını, barlar arası uzaklığı ve bar grupları arası uzaklığı belirler
        karCiroChart.groupBars(-0.5f, 0.14f, 0.08f);
        // Barlara odaklanma özelliğini kapatır
        karCiroBarData.setHighlightEnabled(false);
        // Grafiği yeniler
        karCiroChart.invalidate();
    }

    private void urunGetiriGrafigiCiz(HorizontalBarChart urunGetirisiChart){
        List<String> urunAdlariListe = new ArrayList<>();
        List<Float> getirilerListe = new ArrayList<>();

        List<UrunGetirisi> urunGetirileri = vti.urunGetirileriniGetir();

        for(int i = 0; i < urunGetirileri.size(); i++){
            urunAdlariListe.add(urunGetirileri.get(i).getUrunAdi());
            getirilerListe.add(urunGetirileri.get(i).getGetiri());
        }

        int[] xIndeksleriUrunGetirileri = new int[urunGetirileri.size()];
        for(int i = 0; i < urunGetirileri.size(); i++){
            xIndeksleriUrunGetirileri[i] = i;
        }
        List<BarEntry> getirilerBarEntries = new ArrayList<>();

        for(int i = 0; i< urunGetirileri.size(); i++){
            getirilerBarEntries.add(new BarEntry(xIndeksleriUrunGetirileri[i], getirilerListe.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(getirilerBarEntries, "Ürün Getirileri (₺)");
        // Bar rengi
        barDataSet.setColor(getResources().getColor(R.color.urunGetirisi));
        // Barların üzerinde değerlerin çıkmasını sağlar
        barDataSet.setDrawValues(true);
        // Dokunmayla noktalara odaklanılmasını engeller
        barDataSet.setHighlightEnabled(false);

        BarData urunGetirisiBarData = new BarData(barDataSet);
        // Bar kalınlığı
        urunGetirisiBarData.setBarWidth(0.5f);
        // Barların üstünde çıkan değerlerin rengi
        urunGetirisiBarData.setValueTextColor(Color.WHITE);
        // Barların üstünde çıkan değerlerin yazı boyutu
        urunGetirisiBarData.setValueTextSize(8);
        urunGetirisiChart.setData(urunGetirisiBarData);

        XAxis xEkseniBar = urunGetirisiChart.getXAxis();
        // X eksenindeki değerlerin aşağıda gözükmesini sağlar
        xEkseniBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        // X ekseni için ızgara çizgilerini gizler
        xEkseniBar.setDrawGridLines(false);
        // Değer formatlayıcıyı ayarlar
        xEkseniBar.setValueFormatter(new IndexAxisValueFormatter(urunAdlariListe));
        // Yakınlaştırılınca x ekseninde fazladan değer çıkmasını engeller
        xEkseniBar.setGranularity(1f);
        // X ekseninin başlangıcında boşluk bırakır
        xEkseniBar.setAxisMinimum(urunGetirisiBarData.getXMin() - 0.5f);
        // X ekseninin sonunda boşluk bırakır
        xEkseniBar.setAxisMaximum(urunGetirisiBarData.getXMax() + 0.5f);
        // X eksenindeki yazıların rengi
        xEkseniBar.setTextColor(Color.WHITE);

        YAxis yEkseniSolBar = urunGetirisiChart.getAxisLeft();
        yEkseniSolBar.setEnabled(false);
        // sol Y ekseninin göstereceği minimum değeri ayarlar
        yEkseniSolBar.setAxisMinimum(0f);

        YAxis yEkseniSagBar = urunGetirisiChart.getAxisRight();
        // sağ Y ekseninde gözükecek değer sayısını ayarlar
        yEkseniSagBar.setLabelCount(5, false);
        // sağ Y ekseninin göstereceği minimum değeri ayarlar
        yEkseniSagBar.setAxisMinimum(0f);
        // sağ Y eksenindeki yazıların rengi
        yEkseniSagBar.setTextColor(Color.WHITE);

        // Dataset label'ının rengi
        urunGetirisiChart.getLegend().setTextColor(Color.WHITE);
        // X eksenindeki sadece 7 değerin gözükmesini sağlar
        urunGetirisiChart.setVisibleXRangeMaximum(7);
        // Tablonun en üstteki değerlerin gözükmesini sağlar
        urunGetirisiChart.moveViewTo(0f, 0f, YAxis.AxisDependency.LEFT);
        // Çift tıklayınca yakınlaştırmayı kapatır
        urunGetirisiChart.setDoubleTapToZoomEnabled(false);
        // X eksenine animasyon uygulayarak grafiği 1000 milisaniyede çizer
        urunGetirisiChart.animateY(1000);
        // PinchZoom aktif edilir. false ayarlanırsa X ve Y ekseni için ayrı ayrı yakınlaştırılır
        urunGetirisiChart.setPinchZoom(true);
        // Grafiğin sağ altındaki açıklamayı gizler
        urunGetirisiChart.getDescription().setEnabled(false);
        // Grafiği yeniler
        urunGetirisiChart.invalidate();
    }
}
