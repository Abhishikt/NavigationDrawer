package com.resultstrack.navigationdrawer1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ProximityActivity extends AppCompatActivity implements SensorEventListener{

    TextView proximityTextView;
    SensorManager sensorManager;
    Sensor proximitySensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        proximitySensor =sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        proximityTextView = (TextView) findViewById(R.id.txtProximityView);

        sensorManager.registerListener(this,proximitySensor,SensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        proximityTextView.setText(String.valueOf(event.values[0]));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
}
