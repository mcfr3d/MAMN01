package com.example.myinteractiveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Accelerometer extends AppCompatActivity implements SensorEventListener {

    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float mLastX, mLastY, mLastZ;
    LinearLayout accel_layout;
    /**
     * Called when the activity is first created.
     */

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        mInitialized = false;
        accel_layout = (LinearLayout) findViewById(R.id.activity_accelerometer);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        TextView tvX = (TextView) findViewById(R.id.x_axis);
        TextView tvY = (TextView) findViewById(R.id.y_axis);
        TextView tvZ = (TextView) findViewById(R.id.z_axis);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");
            mInitialized = true;
        } else {
            if(Math.abs(mLastX - x)>1)
                tvX.setText(Double.toString((double)Math.round(x * 1000d) / 1000d
                ));
            if(Math.abs(mLastY - y)>1)
                tvY.setText(Double.toString((double)Math.round(y * 1000d) / 1000d
                ));
            if(Math.abs(mLastZ - z)>1)
                tvZ.setText(Double.toString((double)Math.round(z * 1000d) / 1000d
                ));
        }
        accel_layout.setBackgroundColor(colorGenerator(x,y,z));
        accel_layout.invalidate();

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
// can be safely ignored for this demo
    }

    public int colorGenerator(float x, float y, float z){

        int newX=Math.abs(Math.round(x)*10)%255;
        int newY=Math.abs(Math.round(y)*10)%255;
        int newZ=Math.abs(Math.round(z)*10)%255;
        int color = Color.argb(255, newX, newY,newZ);
        return color;
    }
}