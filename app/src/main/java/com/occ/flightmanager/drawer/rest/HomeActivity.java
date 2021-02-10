package com.occ.flightmanager.drawer.rest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.occ.flightmanager.R;
import com.occ.flightmanager.drawer.flights.FragmentSearchFlight;
import com.occ.flightmanager.drawer.togo.FragmentToGo;
import com.occ.flightmanager.registry.RegistryHome;
import java.util.Locale;
import static com.occ.flightmanager.R.id.drawer_toolbar;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawer_itself;
    ShareActionProvider shareActionProvider;
    NavigationView navView;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentLanguage();
        setContentView(R.layout.activity_home);

        final Toolbar toolbar = findViewById(drawer_toolbar);
        setSupportActionBar(toolbar);

        drawer_itself = findViewById(R.id.drawer_itself);
        navView = findViewById(R.id.drawer_view);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.direct_to_places:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                                new FragmentToGo()).commit();
                        toolbar.setTitle(getResources().getString(R.string.menu_places));
                        break;

                    case R.id.direct_to_search:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                                new FragmentSearchFlight()).commit();
                        toolbar.setTitle(getResources().getString(R.string.menu_search));
                        break;

                    case R.id.direct_to_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                                new FragmentHome()).commit();
                        toolbar.setTitle(getResources().getString(R.string.menu_home));
                        break;
                }
                drawer_itself.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(HomeActivity.this,
                drawer_itself, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);

        drawer_itself.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, new FragmentHome()).commit();
            navView.setCheckedItem(R.id.homeFragment);
        }
    }

    @Override
    public void onBackPressed() {
        count++;
        if(drawer_itself.isDrawerOpen(GravityCompat.START)){
            count--;
            drawer_itself.closeDrawer(GravityCompat.START);
        }
        else if(count == 1){
            Toast.makeText(this,getResources().getString(R.string.one_time),Toast.LENGTH_SHORT).show();

        }
        else if(count == 3) {
            count = 0;
            Toast.makeText(this,getResources().getString(R.string.to_logout),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home,menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareActionIntent(getResources().getString(R.string.plain_text));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            Intent intent = new Intent(this, RegistryHome.class);
            startActivity(intent);
            Toast.makeText(this,getResources().getString(R.string.logged_out),Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareActionIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(getResources().getString(R.string.share_type));
        intent.putExtra(Intent.EXTRA_TEXT,text);
        shareActionProvider.setShareIntent(intent);
    }

    public void setNewLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration newConfiguration = new Configuration();
        newConfiguration.locale = locale;
        getBaseContext().getResources().updateConfiguration(newConfiguration,getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.shared_language_key),MODE_PRIVATE).edit();
        editor.putString(getResources().getString(R.string.selected_language),language);
        editor.apply();
    }

    public void getCurrentLanguage(){
        SharedPreferences sharedPrefs = getSharedPreferences(getResources().getString(R.string.shared_language_key), Activity.MODE_PRIVATE);
        String selected_language = sharedPrefs.getString(getResources().getString(R.string.selected_language),getResources().getString(R.string.empty));
        setNewLanguage(selected_language);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getCurrentLanguage();
    }
}
