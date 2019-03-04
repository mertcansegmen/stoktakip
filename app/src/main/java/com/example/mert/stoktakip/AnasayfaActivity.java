package com.example.mert.stoktakip;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AnasayfaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setTitle(R.string.nav_stoklistesi_title);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StokListesiFragment()).commit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.anasayfa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_stoklistesi) {
            getSupportActionBar().setTitle(R.string.nav_stoklistesi_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StokListesiFragment()).commit();
        } else if (id == R.id.nav_urunsat) {
            getSupportActionBar().setTitle(R.string.nav_urunsat_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UrunSatFragment()).commit();
        } else if (id == R.id.nav_urunal) {
            getSupportActionBar().setTitle(R.string.nav_urunal_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UrunAlFragment()).commit();
        } else if (id == R.id.nav_azalanurunler) {
            getSupportActionBar().setTitle(R.string.nav_azalanurunler_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AzalanUrunlerFragment()).commit();
        } else if (id == R.id.nav_islemgecmisi) {
            getSupportActionBar().setTitle(R.string.nav_islemgecmisi_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IslemGecmisiFragment()).commit();
        } else if (id == R.id.nav_istatistikler) {
            getSupportActionBar().setTitle(R.string.nav_istatistikler_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new IstatistiklerFragment()).commit();
        } else if (id == R.id.nav_veritabani) {
            getSupportActionBar().setTitle(R.string.nav_veritabani_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VeritabaniFragment()).commit();
        } else if (id == R.id.nav_ayarlar) {
            getSupportActionBar().setTitle(R.string.nav_ayarlar_title);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AyarlarFragment()).commit();
        } else if (id == R.id.nav_uygulamayipuanla) {
            Toast.makeText(this, "Uygulamanın Google Play sayfasına yönlendiriliyorsunuz...", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
