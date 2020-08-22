package com.example.samarth.ecampus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    TextView txtLogo,txtSlogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        txtLogo = (TextView) findViewById(R.id.SplashLogo);
        txtSlogan = (TextView) findViewById(R.id.SplashSlogan);

        Typeface myCustomFontLogo = Typeface.createFromAsset(getAssets(),"fonts/Lobster_1.3.otf");
        Typeface myCustomFontSlogan = Typeface.createFromAsset(getAssets(),"fonts/Allura-Regular.otf");

        txtLogo.setTypeface(myCustomFontLogo);
        txtSlogan.setTypeface(myCustomFontSlogan);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
