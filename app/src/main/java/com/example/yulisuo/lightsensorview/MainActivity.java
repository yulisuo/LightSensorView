package com.example.yulisuo.lightsensorview;

import android.content.ContentResolver;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LightSensorView";
    private static boolean enable = true;
    private SensorManager sm;
    TextView tvData;
    TextView tvBri;
    SensorView sv;
    Switch sw;
    ContentResolver cr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        regitsterSensor();
    }

    private void regitsterSensor(){
        sm = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor lightSensor = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        sm.registerListener(mListener,lightSensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void unRegisterSensor(){
        sm.unregisterListener(mListener);
    }

    private void init() {
        tvData = (TextView)findViewById(R.id.tv_data);
        tvBri = (TextView)findViewById(R.id.tv_bri);
        sv = (SensorView)findViewById(R.id.sv);
        sw = (Switch)findViewById(R.id.sw_listener);
        sw.setOnCheckedChangeListener(checkedListener);
        cr = this.getContentResolver();
    }

    private void updateTextView(int time,int lux){
        tvData.setText("time:"+time+"\t,lux:"+lux);
    }

    private void updateBriTextView(int bri){
        tvBri.setText("bri:"+bri);
    }

    private int getSystemTime(){
        return (int)(SystemClock.uptimeMillis() / 1000);
    }

    private SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            int lux = Math.round(sensorEvent.values[0]);
            try {
                int bri = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
                updateBriTextView(bri);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "sensor change,lux:" + lux);
            updateTextView(getSystemTime(), lux);
            if(enable){
                updateSensorView(lux);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    private CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            enable = b;
        }
    };

    private void updateSensorView(int lux){
        Log.d(TAG,"updateSensorView:"+lux);
        sv.addDatas(lux);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unRegisterSensor();
    }
}
