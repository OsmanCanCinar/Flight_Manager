package com.occ.flightmanager.registry;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.occ.flightmanager.R;
import java.util.Locale;

public class RegistryHome extends AppCompatActivity {

    int count  = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentLanguage();
        setContentView(R.layout.home_registry);

        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
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
    public void onConfigurationChanged(@NonNull Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        getCurrentLanguage();
    }

    @Override
    public void onBackPressed() {
        count++;
        if(count == 2){
            count = 0;
            Toast.makeText(this,getResources().getString(R.string.to_close),Toast.LENGTH_SHORT).show();
        }
    }
}