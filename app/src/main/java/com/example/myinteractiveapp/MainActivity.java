package com.example.myinteractiveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /** Called when the user taps the compass button */
    public void openCompass(View view) {
        Intent intent = new Intent(this, Compass.class);
        startActivity(intent);

        // Do something in response to button
    }


    /** Called when the user taps the accelermoeter button */
    public void openAccelerometer(View view) {
        Intent intent = new Intent(this, Accelerometer.class);
        startActivity(intent);

        // Do something in response to button
    }
}
