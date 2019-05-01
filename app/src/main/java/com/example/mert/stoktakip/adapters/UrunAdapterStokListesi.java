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

public class UrunAdapterStokListesi extends ArrayAdapter<Urun> {

    private Context context;
    private int resource;
    private int lastPosition = -1;

    private ArrayList<Urun> original;
    private ArrayList<Urun> fitems;
    private Filter filter;

    static class ViewHolder {
        TextView urunAdi;
        TextView barkodNo;
        TextView adet;
        TextView alisSatis;
    }

    public UrunAdapterStokListesi(@NonNull Context context, int resource, ArrayList<Urun> urunler) {
        super(context, resource, urunler);
        this.context = context;
        this.resource = resource;
        this.original = new ArrayList<Urun>(urunler);
        this.fitems = new ArrayList<Urun>(urunler);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String barkodNo = getItem(position).getBarkodNo();
        String urunAdi = getItem(position).getAd();
        int adet = getItem(position).getAdet();
        float alis = getItem(position).getAlis();
        float satis = getItem(position).getSatis();
        Urun urun = new Urun(barkodNo, urunAdi, adet, alis, satis);

        final View result;

        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.liste_elemani_stok_listesi, parent, false);

            holder = new ViewHolder();
            holder.urunAdi = convertView.findViewById(R.id.urun_adi);
            holder.barkodNo = convertView.findViewById(R.id.barkod_no);
            holder.adet = convertView.findViewById(R.id.urun_adeti);
            holder.alisSatis = convertView.findViewById(R.id.txt_alis_satis_fiyati);

            result = convertView;
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(context,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.urunAdi.setText(urun.getAd());
        holder.barkodNo.setText(urun.getBarkodNo());
        holder.adet.setText(String.valueOf(urun.getAdet()));
        holder.alisSatis.setText(urun.getAlisSatis());

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter()
    {
        if (filter == null)
            filter = new StokFilter();

        return filter;
    }

    private class StokFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            FilterResults results = new FilterResults();
            String kelime = constraint.toString().toLowerCase();

            if (kelime.equals("") || kelime.length() == 0)
            {
                ArrayList<Urun> list = new ArrayList<Urun>(original);
                results.values = list;
                results.count = list.size();
            }
            else
            {
                final ArrayList<Urun> list = new ArrayList<Urun>(original);
                final ArrayList<Urun> nlist = new ArrayList<Urun>();
                int count = list.size();

                for (int i=0; i<count; i++)
                {
                    final Urun urun = list.get(i);
                    final String valueAd = urun.getAd().toLowerCase();
                    final String valueBarkod = urun.getBarkodNo().toLowerCase();

                    if (valueAd.contains(kelime) || valueBarkod.contains(kelime))
                    {
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
            fitems = (ArrayList<Urun>)results.values;

            clear();
            int count = fitems.size();
            for (int i=0; i<count; i++)
            {
                Urun urun = (Urun)fitems.get(i);
                add(urun);
            }
        }

    }

}
