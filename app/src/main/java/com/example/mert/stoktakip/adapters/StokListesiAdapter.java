package com.example.mert.stoktakip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;

import java.util.ArrayList;
import java.util.Locale;

public class StokListesiAdapter extends ArrayAdapter<Urun> {

    private Context context;
    private int resource;
    private int lastPosition = -1;

    private ArrayList<Urun> original;
    private ArrayList<Urun> fitems;
    private Filter filter;

    static class ViewHolder {
        TextView urunAdiTxt;
        TextView barkodNoTxt;
        TextView urunAdetiTxt;
        TextView alisFiyatiTxt;
        TextView satisFiyatiTxt;
    }

    public StokListesiAdapter(@NonNull Context context, int resource, ArrayList<Urun> urunler) {
        super(context, resource, urunler);
        this.context = context;
        this.resource = resource;
        this.original = new ArrayList<>(urunler);
        this.fitems = new ArrayList<>(urunler);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String barkodNo = getItem(position).getBarkodNo();
        String urunAdi = getItem(position).getAd();
        int urunAdeti = getItem(position).getAdet();
        float alisFiyati = getItem(position).getAlis();
        float satisFiyati = getItem(position).getSatis();

        Urun urun = new Urun(barkodNo, urunAdi, urunAdeti, alisFiyati, satisFiyati);

        final View result;
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.liste_elemani_stok_listesi, parent, false);

            holder = new ViewHolder();

            holder.urunAdiTxt = convertView.findViewById(R.id.txt_urun_adi);
            holder.barkodNoTxt = convertView.findViewById(R.id.txt_barkod_no);
            holder.urunAdetiTxt = convertView.findViewById(R.id.txt_urun_adeti);
            holder.alisFiyatiTxt = convertView.findViewById(R.id.txt_alis_fiyati);
            holder.satisFiyatiTxt = convertView.findViewById(R.id.txt_satis_fiyati);

            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.urunAdiTxt.setText(urun.getAd());
        holder.barkodNoTxt.setText(urun.getBarkodNo());
        holder.urunAdetiTxt.setText(String.valueOf(urun.getAdet()));
        // Alış ve satış fiyatlarının noktadan sonraki iki basamağı gösterilmesi için (13.20 tl gibi)
        // formatlama işlemi yapılıyor
        holder.alisFiyatiTxt.setText(String.format(Locale.getDefault(), "%.2f", urun.getAlis()));
        holder.satisFiyatiTxt.setText(String.format(Locale.getDefault(), "%.2f", urun.getSatis()));

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new StokFilter();

        return filter;
    }

    private class StokFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String kelime = constraint.toString().toLowerCase();

            if (kelime.equals("") || kelime.length() == 0) {
                ArrayList<Urun> list = new ArrayList<>(original);
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<Urun> list = new ArrayList<>(original);
                final ArrayList<Urun> nlist = new ArrayList<>();
                int count = list.size();

                for (int i = 0; i < count; i++) {
                    final Urun urun = list.get(i);
                    final String valueAd = urun.getAd().toLowerCase();
                    final String valueBarkod = urun.getBarkodNo().toLowerCase();

                    if (valueAd.contains(kelime) || valueBarkod.contains(kelime)) {
                        nlist.add(urun);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            fitems = (ArrayList<Urun>) results.values;

            clear();
            int count = fitems.size();
            for (int i = 0; i < count; i++) {
                Urun urun = fitems.get(i);
                add(urun);
            }
        }
    }
}
