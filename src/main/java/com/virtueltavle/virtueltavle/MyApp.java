package com.virtueltavle.virtueltavle;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;
import org.altbeacon.beacon.Region;

/**
 * Created by Jeanette on 12-Mar-15.
 */
public class MyApp extends Application implements BootstrapNotifier {
    // Globale data (kunne godt være gemt i en klassevariabel andetsteds)
    public static SharedPreferences prefs;
    public static Programdata data;
    public static MyApp instans;
    public static DataAccess dataAccess;

    public static String beaconId;

    private static final String TAG = "VirtuelTavle.MyApp";
    private RegionBootstrap regionBootstrap;
    private BackgroundPowerSaver backgroundPowerSaver;
    private boolean haveDetectedBeaconsSinceBoot = false;
    private static MainActivity mainActivity = null;

    /**
     * Håndtag til forgrundstråden
     */
    public static Handler forgrundstråd = new Handler();

//    public static Programdata getData() {
//        return data;
//    }

    @Override
    public void onCreate() {
        Log.d("MyApp", "onCreate() called");
        super.onCreate();
        instans = this;
        Region region = new Region("com.example.myapp.boostrapRegion", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        StartBeacon();

        // Initialisering der kræver en Context
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Programdata der skal være indlæst ved opstart
        try {
            dataAccess = new DataAccess();
            data = (Programdata) Serialisering.hent(getFilesDir() + "/programdata.ser");
            Log.d("data", "" + data);
            System.out.println("programdata indlæst fra fil");

        } catch (Exception ex) {
            data = new Programdata(); // fil fandtes ikke eller data var inkompatible
            System.out.println("programdata oprettet fra ny: " + ex);
        }

        if(data.observatører == null)
        {
            data.observatører = new ArrayList<Runnable>();
        }

        try {
            data.observatører.add(new Runnable() {
                @Override
                public void run() {
                    MyApp.gemData();
                }
            });
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

    }

    private void StartBeacon() {
        BeaconManager beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"));

        Log.d(TAG, "setting up background monitoring for beacons and power saving");

        // wake up the app when a beacon is seen
        Region region = new Region("backgroundRegion", null, null, null);
        regionBootstrap = new RegionBootstrap(this, region);

        backgroundPowerSaver = new BackgroundPowerSaver(this);
    }

    public static void gemData() {
        try {  // nu er aktiviteten ikke synlig, så er det tid til at gemme data!
            Serialisering.gem(data, instans.getFilesDir() + "/programdata.ser");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void didDetermineStateForRegion(int arg0, Region arg1) {
        // Don't care
    }

    @Override
    public void didEnterRegion(Region arg0) {
        // In this example, this class sends a notification to the user whenever a Beacon
        // matching a Region (defined above) are first seen.
        Log.d(TAG, "did enter region.");
        if (!haveDetectedBeaconsSinceBoot) {
            Log.d(TAG, "auto launching MainActivity");

            // The very first time since boot that we detect an beacon, we launch the
            // MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // Important:  make sure to add android:launchMode="singleInstance" in the manifest
            // to keep multiple copies of this activity from getting created if the user has
            // already manually launched the app.
            this.startActivity(intent);
            haveDetectedBeaconsSinceBoot = true;
        } else {
            if (mainActivity != null) {
                // If the Monitoring Activity is visible, we log info about the beacons we have
                // seen on its display
                //mainActivity.logToDisplay("I see a beacon again" );
            } else {
                // If we have already seen beacons before, but the monitoring activity is not in
                // the foreground, we send a notification to the user on subsequent detections.
                Log.d(TAG, "Sending notification.");
                //sendNotification();
            }
        }
    }

    @Override
    public void didExitRegion(Region arg0) {
        // Don't care
    }

    public static void setMainActivity(MainActivity activity) {
        mainActivity = activity;
    }
}
