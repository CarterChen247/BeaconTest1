package com.fuyxuan.beacontest1;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * Created by Allison on 2016/11/16.
 */

public class BeaconService extends Service implements BeaconConsumer {

    int i = 0;
    private final static String TAG = BeaconService.class.getSimpleName();
    private BeaconManager beaconManager;
    private int beaconCount = 0;
    long timeCount = 0;
    boolean isTimeCount = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        beaconManager = BeaconManager.getInstanceForApplication(this);
        Utils.logd("kk", "Service onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Utils.logd("kk", "Service onStartCommand");
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.bind(this);
        BeaconParser bp0 = new BeaconParser();
        bp0.setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24");
        beaconManager.getBeaconParsers().add(bp0);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    //EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                    Beacon firstBeacon = beacons.iterator().next();
                    // Utils.logd(TAG," first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                    beaconCount = 0;
                    Message m = new Message();
                    m.obj = beacons;
                    beaconHandler.sendMessage(m);
                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {
        }
    }

    Handler beaconHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            @SuppressWarnings("unchecked")
            final Collection<Beacon> beacons = (Collection<Beacon>) msg.obj;
//              Utils.logi(TAG, "beacon size = " + beacons.size());
//            if (beacons.size() > 0) {
            for (Beacon beacon : beacons) {
                beaconCount++;
//                    Utils.logi(TAG, "beacon.getBluetoothName() = " + beacon.getBluetoothName()+"  >>>beaconCount:"+beaconCount+"   beacons.size():"+beacons.size());
//                    Utils.logi(TAG, "major = " + beacon.getId2().toInt());
//                    Utils.logi(TAG, "minor = " + beacon.getId3().toInt());
//                    Utils.logi(TAG, "bluetoothAddress = " + beacon.getId1().toString());
//                    Utils.logi(TAG, "bluetoothAddress = " + beacon.getBluetoothAddress());
//                    Utils.logi(TAG, "txPower = " +  beacon.getTxPower());
//                    Utils.logi(TAG, "rssi = " + beacon.getRssi());
//                    Utils.logi(TAG, "distance = " + beacon.getDistance());

                sendBeaconInfo(beacon.getId2().toInt(), beacon.getId3().toInt(), beacon.getDistance(), beacon.getBluetoothName(), beaconCount, beacons.size());


            }
//            }
        }
    };


    private void sendBeaconInfo(int major, int minor, double distance, String bluetoothName, int sendbeaconCount, int beaconSize) {
        // Utils.logd(TAG,"kk"+" sendBeaconInfo??????????????"+sendbeaconCount);

        Intent intent = new Intent("android.intent.action.UpdateIcon");
        intent.putExtra("update", true);
        intent.putExtra("major", major);
        intent.putExtra("minor", minor);
        intent.putExtra("distance", distance);
        intent.putExtra("bluetoothName", bluetoothName);
        intent.putExtra("beaconCount", sendbeaconCount);
        intent.putExtra("beaconSize", beaconSize);


        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }

}
