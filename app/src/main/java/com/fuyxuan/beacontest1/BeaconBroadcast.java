package com.fuyxuan.beacontest1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Allison on 2016/11/16.
 */

public class BeaconBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Utils.logi("kk", "BeaconBroadcast");
        Intent serviceIntent = new Intent(context, BeaconService.class);
        context.startService(serviceIntent);
    }
}
