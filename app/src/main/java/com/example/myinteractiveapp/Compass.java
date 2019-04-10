package com.example.myinteractiveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Compass extends AppCompatActivity implements SensorEventListener {
    ImageView compass_img;
    TextView txt_compass;
    RelativeLayout compass_layout;
    MediaPlayer mp;
    boolean mAllowShakeNsound;
    long startTime;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img = (ImageView) findViewById(R.id.img_compass);
        txt_compass = (TextView) findViewById(R.id.txt_azimuth);
        compass_layout = (RelativeLayout) findViewById(R.id.activity_compass);
        startTime = SystemClock.elapsedRealtime();


        start();
    }

    public void start() {
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR) == null) {
            if ((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)) {
                noSensorsAlert();
            } else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        } else {
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop() {
        if (haveSensor && haveSensor2) {
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        } else {
            if (haveSensor)
                mSensorManager.unregisterListener(this, mRotationV);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rMat, event.values);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {
            SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
            SensorManager.getOrientation(rMat, orientation);
            mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
        }

        mAzimuth = Math.round(mAzimuth);
        compass_img.setRotation(-mAzimuth);

        String where = "NW";

        if (mAzimuth == 345 || mAzimuth == 15) {

        }
        if (mAzimuth >= 345 || mAzimuth <= 15) {
            where = "N";
            compass_layout.setBackgroundColor(Color.GREEN);
            compass_layout.invalidate();
            if (hasTwoSecondsPassed()) {
                shakeItBaby();
                playSound();
            }

        }
        if (mAzimuth < 345 && mAzimuth > 280) {
            where = "NW";
            compass_layout.setBackgroundColor(0x708090);
            compass_layout.invalidate();
        }
        if (mAzimuth <= 280 && mAzimuth > 260)
            where = "W";
        if (mAzimuth <= 260 && mAzimuth > 190)
            where = "SW";
        if (mAzimuth <= 190 && mAzimuth > 170)
            where = "S";
        if (mAzimuth <= 170 && mAzimuth > 100)
            where = "SE";
        if (mAzimuth <= 100 && mAzimuth > 80)
            where = "E";
        if (mAzimuth <= 80 && mAzimuth > 15) {
            where = "NE";
            compass_layout.setBackgroundColor(0x708090);
            compass_layout.invalidate();
        }


        txt_compass.setText(mAzimuth + "° " + where);
    }

    @Override
    protected void onPause() {
        mp.stop();
        mAllowShakeNsound = false;
        super.onPause();
        stop();
    }

    @Override
    protected void onResume() {
        mAllowShakeNsound = true;
        super.onResume();
        start();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void shakeItBaby() {
        if (mAllowShakeNsound) {
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(50);
            }
        }
    }

    /**
     * Play the sound using android.media.MediaPlayer
     */
    public void playSound() {
        stopPlaying();
        if (mAllowShakeNsound) {
            mp = MediaPlayer.create(getApplicationContext(), R.raw.bell);
            mp.start();
        }
    }

    public boolean hasTwoSecondsPassed() {
        long elapsedMilliSeconds = SystemClock.elapsedRealtime() - startTime;
        double elapsedSeconds = elapsedMilliSeconds / 1000.0;
        if (elapsedSeconds > 2) {
            startTime = SystemClock.elapsedRealtime();
            return true;
        } else {
            return false;
        }
    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;

    }
}
