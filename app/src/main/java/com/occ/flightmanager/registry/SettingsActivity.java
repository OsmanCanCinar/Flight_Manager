package com.occ.flightmanager.registry;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import com.occ.flightmanager.R;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    ImageButton switch_Tr,switch_eng;
    CoordinatorLayout coordinatorLayout;
    static final String CHANGED = "yes";//can't assign as getResources().getString(R.string.YES); because it's a static key variable.
    int flagVar = 0, count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCurrentLanguage();
        setContentView(R.layout.settings_activity);

        if(savedInstanceState != null)
            flagVar = savedInstanceState.getInt(CHANGED);

        switch_Tr = findViewById(R.id.switchToTurkish);
        switch_eng = findViewById(R.id.switchToEng);
        coordinatorLayout = findViewById(R.id.mr_coordinator);

        switch_Tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewLanguage(getResources().getString(R.string.tr_lang));
                flagVar = 1;
                recreate();
            }
        });

        switch_eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNewLanguage(getResources().getString(R.string.en_lang));
                flagVar = 1;
                recreate();
            }
        });

        if (flagVar == 1)
            showSnackBar();

        Toolbar toolbar = findViewById(R.id.toolbar_that_collapses);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        (actionBar).setDisplayHomeAsUpEnabled(true);
    }

    public void showSnackBar(){
        Snackbar snackBarMain = Snackbar.make(coordinatorLayout,getResources().getString(R.string.language_changed),Snackbar.LENGTH_INDEFINITE)
                .setAction(getResources().getString(R.string.dismiss), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackBarSub= Snackbar.make(coordinatorLayout,getResources().getString(R.string.dismissed),Snackbar.LENGTH_SHORT);
                        snackBarSub.show();
                    }
                }).setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackBarMain.show();
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
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHANGED,flagVar);
    }

    @Override
    public void onBackPressed() {
        count++;
        if(count == 2){
            Toast.makeText(this,getResources().getString(R.string.to_enable),Toast.LENGTH_SHORT).show();
            count =0;
        }
    }
}
