package com.example.mert.stoktakip.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.adapters.UrunAdapterStokListesi;
import com.example.mert.stoktakip.models.Urun;
import com.example.mert.stoktakip.models.VeritabaniIslemleri;

import java.util.ArrayList;

public class UrunListesiDialog extends AppCompatDialogFragment {

    UrunListesiDialogListener listener;
    String barkodNo;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_urun_listesi, null);

        builder.setView(view);

        ListView liste = view.findViewById(R.id.liste);
        SearchView search = view.findViewById(R.id.search_view);
        VeritabaniIslemleri vti = new VeritabaniIslemleri(getContext());
        ArrayList<Urun> urunler = vti.butunUrunleriGetir();

        UrunAdapterStokListesi adapter = new UrunAdapterStokListesi(getContext(), R.layout.liste_elemani_stok_listesi, urunler);
        liste.setAdapter(adapter);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Urun urun = urunler.get(position);
                barkodNo = urun.getBarkodNo();
                listener.barkodGetir(barkodNo);
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (UrunListesiDialogListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "UrunListesiDialogListener implement etmek gerekiyor");
        }
    }

    public interface UrunListesiDialogListener{
        void barkodGetir(String barkod);
    }

}
