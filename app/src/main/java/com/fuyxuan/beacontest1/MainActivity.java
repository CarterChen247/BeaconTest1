package com.fuyxuan.beacontest1;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private BluetoothAdapter mBluetoothAdapter;
    private LocalBroadcastManager broadcastManager;
    private List<String> beaconList;
    LinearLayoutManager layoutManager;

    private static final long DEFAULT_FOREGROUND_SCAN_PERIOD = 2000L;
    private static final long DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD = 3000L;
    private static final long DEFAULT_BACKGROUND_SCAN_PERIOD = 2000L;
    private static final long DEFAULT_BACKGROUND_BETWEEN_SCAN_PERIOD = 3000L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        layoutManager = new LinearLayoutManager(this);
        beaconList = new ArrayList<>();


        Utils.logd(TAG, "kk MMM MainActivity onCreate");
        startService(new Intent(MainActivity.this, BeaconService.class));
        initBT();
        getBeaconInfo();
    }

    private void initBT() {

        BeaconManager.getInstanceForApplication(this).setForegroundScanPeriod(DEFAULT_FOREGROUND_SCAN_PERIOD);
        BeaconManager.getInstanceForApplication(this).setForegroundBetweenScanPeriod(DEFAULT_FOREGROUND_BETWEEN_SCAN_PERIOD);
        BeaconManager.getInstanceForApplication(this).setBackgroundScanPeriod(DEFAULT_BACKGROUND_SCAN_PERIOD);
        BeaconManager.getInstanceForApplication(this).setBackgroundBetweenScanPeriod(DEFAULT_BACKGROUND_BETWEEN_SCAN_PERIOD);


        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(getApplicationContext(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (mBluetoothAdapter.isEnabled() == false) {
            mBluetoothAdapter.enable();
        }
    }


    private void getBeaconInfo() {

        BroadcastReceiver mBeaconInfoReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.intent.action.UpdateIcon")) {
                    boolean update = intent.getBooleanExtra("update", false);
                    int major = intent.getIntExtra("major", 0);
                    int minor = intent.getIntExtra("minor", 0);
                    double distance = intent.getDoubleExtra("distance", 0);
                    String bluetoothName = intent.getStringExtra("bluetoothName");

                    int beaconCount = intent.getIntExtra("beaconCount", 0);
                    int beaconSize = intent.getIntExtra("beaconSize", 0);
//                    beaconList = new ArrayList<String>();

                    //  if (distance <= 2) {
//                        updateMission1Icon(major, minor);
//                        updateMission2Icon(major, minor);
//                        updateMission3Icon(major, minor);
//                        updateMission4Icon(major, minor);
//                        updateMission5Icon(major, minor);
//                        updateMission6Icon(major, minor);

                    // }

                    if (beaconCount == 1) {
                        Utils.logd("kk", "beaconCount");
                        beaconList.clear();
                    }

                    beaconList.add(bluetoothName + " : " + distance);
                    BeaconAdapter myAdapter = new BeaconAdapter(beaconList);
                    RecyclerView mList = (RecyclerView) findViewById(R.id.list_view);
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    mList.setLayoutManager(layoutManager);
                    mList.setAdapter(myAdapter);


                }
            }
        };
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.UpdateIcon");
        broadcastManager.registerReceiver(mBeaconInfoReceiver, intentFilter);

    }


}
