package com.example.mert.stoktakip.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mert.stoktakip.R;
import com.example.mert.stoktakip.models.Urun;
import com.reginald.editspinner.EditSpinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UrunAdapterUrunAlSat extends ArrayAdapter<Urun> {

    private Context context;
    private int resource;


    public UrunAdapterUrunAlSat(@NonNull Context context, int resource, ArrayList<Urun> urun) {
        super(context, resource, urun);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String barkodNo = getItem(position).getBarkodNo();
        String urunAdi = getItem(position).getAd();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);
        String[] sayilar =  convertView.getResources().getStringArray(R.array.sayilar);

        TextView tvAd = convertView.findViewById(R.id.urun_adi);
        TextView tvBarkodNo = convertView.findViewById(R.id.barkod_no);
        tvAd.setText(urunAdi);
        tvBarkodNo.setText(barkodNo);

        ArrayAdapter<String> adapter;
        EditSpinner spinner;

        spinner = convertView.findViewById(R.id.spinner);
        convertView.setTag(R.string.spinner_id, spinner);
        spinner.setEditable(false);
        spinner.setText(sayilar[0]);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, sayilar);
        spinner.setAdapter(adapter);

        // Spinner'ın son elemanına tıkandıysa(Diğer), Spinner'ı düzenlenebilir yapıyor ve hint ekliyor
        // Diğer elemanlardan birisi tıklandıysa düzenlenebilirliği kaldırıyor
        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == sayilar.length-1) {
                    spinner.setEditable(true);
                    spinner.setText("");
                    spinner.setHint("Miktar girin");
                }
                else
                    spinner.setEditable(false);
            }
        });

        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "tık", Toast.LENGTH_SHORT).show();
            }
        });

        //convertView spinner'ın key listener metotunda kullanılamadığı için kopyasını oluşturup kullanılıyor
        View convertViewDuplicate = convertView;

        // Eğer elle spinner'a bir değer girilip enter tuşuna basılırsa hint'i kaldırıyor ve klavyeyi gizliyor
        spinner.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER){
                    spinner.setHint("");
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) spinner.getLayoutParams();
                    lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    spinner.setLayoutParams(lp);
                    klavyeyiGizle(convertViewDuplicate);
                }
                return false;
            }
        });

        return convertView;
    }

    private void klavyeyiGizle(View convertView) {
        InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(convertView.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
