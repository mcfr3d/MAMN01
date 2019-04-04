package com.example.myinteractiveapp;

import androidx.appcompat.app.AppCompatActivity;

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
        // Do something in response to button
    }


    /** Called when the user taps the accelermoeter button */
    public void openAccelerometer(View view) {
        // Do something in response to button
    }
}
