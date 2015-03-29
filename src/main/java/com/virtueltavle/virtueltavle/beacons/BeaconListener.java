package com.virtueltavle.virtueltavle.beacons;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.hardware.SensorManager;
import android.os.RemoteException;
import android.util.Log;

import com.virtueltavle.virtueltavle.Models.Employee;
import com.virtueltavle.virtueltavle.Models.VirtualBoard;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jeanette on 26-Mar-15.
 */
public class BeaconListener {

    private ArrayList<BeaconRoom> knownBeaconRooms ;

    public BeaconListener()
    {
        knownBeaconRooms = new ArrayList<BeaconRoom>();
        knownBeaconRooms.add(new BeaconRoom("f7826da6-4fa2-4e98-8024-bc5b71e0893e","48289","41039","60787","18607"));

    }
    public class BeaconRoom
    {
        public BeaconRoom( String beaconUID,String in2, String in3, String out2, String out3)        {
            this.beaconUID = beaconUID;
            this.in_id2 = in2;
            this.in_id3 = in3;
            this.out_id2 = out2;
            this.out_id3 = out3;
        }

        public boolean areInRoom = false;
        public Time enteredRoomAt;
        public String beaconUID ;
        public String in_id2;
        public String in_id3;
        public String out_id2;
        public String out_id3;

        public int isThisRoom(String id1,String id2, String id3)
        {
            if(!beaconUID.equals(id1)) return 0;

            // 1 is inside beacon
            if(in_id2.equals(id2) && in_id3.equals(id3)) return 1;

            // 2 is outside beacon
            if(out_id2.equals(id2) && out_id3.equals(id3)) return 2;

            return 0;
        }
    }

    private transient ArrayList<Runnable> observers = new ArrayList<Runnable>();

    public void addObserver(Runnable observer)
    {
        observers.add(observer);
    }

    public void removeObserver(Runnable observer)
    {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Runnable r : observers) r.run();
    }



    private BeaconRoom GetBeaconRoom()
    {
        return null;
    }

    public void rangeChanged(Collection<Beacon> beacons, Region region) {
        if (beacons.size() == 0) return;

        for(int i =0; i<beacons.size(); i++)
        {


        }

        Iterator<Beacon> iterator = beacons.iterator();

        if (beacons.size() > 0) {
            Beacon beacon = iterator.next();
            Log.d("MainActivity", "The 1. beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
        }
        if (beacons.size() > 1) {
            Beacon beacon = iterator.next();
            Log.d("MainActivity", "The 2. beacon " + beacon.toString() + " is about " + beacon.getDistance() + " meters away.");
        }
    }



}
