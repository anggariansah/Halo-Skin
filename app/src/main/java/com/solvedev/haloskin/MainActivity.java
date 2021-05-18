package com.solvedev.haloskin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.solvedev.haloskin.activity.LoginActivity;
import com.solvedev.haloskin.activity.MainMenuActivity;
import com.solvedev.haloskin.utils.UserPreferences;

public class MainActivity extends AppCompatActivity {

    private UserPreferences preference;
    TextView tvVersion;

    String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvVersion = findViewById(R.id.tv_version);
        preference = new UserPreferences(this);

        versionName = BuildConfig.VERSION_NAME;
        tvVersion.setText("HaloSkin "+versionName);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkSession();
            }
        }, 3000);
    }

    private void checkSession () {

        if (preference.isUserLoggedIn()) {
            startActivity(new Intent(MainActivity.this, MainMenuActivity.class));
            finish();
        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

    }
}
