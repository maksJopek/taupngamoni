package com.jopek.taupngamoni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    public static final String TAG = "maks";
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedClosableObjects()
//                .penaltyLog()
//                .build());
        StrictMode.enableDefaults();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

    }
    HomeFragment homeFragment = new HomeFragment();
    ChartFragment chartFragment = new ChartFragment();

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;
            case R.id.chart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, chartFragment).commit();
                return true;
        }
        return false;
    }

    public static String round(double num, int nPlaces) {
        DecimalFormat df = new DecimalFormat("#." + String.join("", Collections.nCopies(nPlaces, "#")));
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(num);
    }
}
