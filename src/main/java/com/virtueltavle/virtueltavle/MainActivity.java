package com.virtueltavle.virtueltavle;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.RemoteException;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


public class MainActivity extends ActionBarActivity implements BeaconConsumer, View.OnClickListener, SensorEventListener {

    private BeaconManager beaconManager;
    SensorManager sensorManager;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("MainActivity", "onCreate");

        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);

//        beaconManager.getBeaconParsers().add(new BeaconParser().
//                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

//        if(MyApp.beaconId != null && MyApp.beaconId.length() >0)
//        {
//            Toast.makeText(this, MyApp.beaconId, Toast.LENGTH_LONG).show();
//        }

        if (savedInstanceState == null) {
            RoomsList_Frag fragment = new RoomsList_Frag();
            getFragmentManager().beginTransaction()
                    .add(R.id.fragmentContent, fragment) // tom container i layout
                    .commit();
        }

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.i("MainActivity", "Sensor instantiated");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onResume() {
        super.onResume();
        MyApp.setMainActivity(this);
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApp.setMainActivity(null);
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
        sensorManager.unregisterListener(this); // Stop med at modtage sensordata
    }

    public void onSensorChanged(SensorEvent e) {
        if (e.sensor.getType() ==Sensor.TYPE_ACCELEROMETER) {
            double g= SensorManager.GRAVITY_EARTH; // normal tyngdeaccelerationen
            double sum=Math.abs(e.values[0])+Math.abs(e.values[1])+Math.abs(e.values[2]);
            if (sum>3*g)
            {
                Log.i("MainActivity", "Shake detected");
                //Enter room alert
            }
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() == 0)return;

                Iterator<Beacon> iterator = beacons.iterator();

                if (beacons.size() > 0) {
                    Beacon beacon = iterator.next();
                    Log.d("MainActivity","The 1. beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
                }
                if (beacons.size() > 1) {
                    Beacon beacon = iterator.next();
                    Log.d("MainActivity","The 2. beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    private void makeToast(String txt)
    {
        Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
    }

    private void makeUiToast(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
                makeToast(line);
            }
        });
    }
}
