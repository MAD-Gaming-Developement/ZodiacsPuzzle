package dev.andeng.matchingpicture;


import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;


import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {

    private boolean firstRun = false;
    private static final String PREFS_NAME = "FirstRun";
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        pref = getPreferences(MODE_PRIVATE);

        pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        firstRun = pref.getBoolean("firstRun", true);


        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, MainActivity.class));
            }
        });


        findViewById(R.id.Policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Menu.this, Policy.class));
            }
        });

    }
}



