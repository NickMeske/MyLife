package com.justfortom.mylife;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.List;

/**
 * Created by nmesk on 6/11/2016.
 */
public class Starter extends AppCompatActivity {

    public void StartListening() {
        IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        try {
            this.registerReceiver(mReceiver, filter1);
            this.registerReceiver(mReceiver, filter2);
            this.registerReceiver(mReceiver, filter3);
        } catch (Exception ex) {
            String exString = ex.toString();
            String ex2 = ex.getMessage();
        }

    }

    public void StopListening() {
        this.unregisterReceiver(mReceiver);
    }

    //The BroadcastReceiver that listens for bluetooth broadcasts
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


            PackageManager packageManager = getPackageManager();
            Intent openInten = new Intent(Intent.CATEGORY_APP_MUSIC);

            List activities = packageManager.queryIntentActivities(openInten,
                    PackageManager.MATCH_ALL);
            boolean isIntentSafe = activities.size() > 0;

            if (device.getName().toLowerCase().contains("ihome")) {

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    //Device found
                } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    //Device is now connected
                    CallDevice();
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                    //Done searching

                } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                    //Device is about to disconnect
                } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    //Device has disconnected
                }
            }
        }
    };

    private void CallDevice() {

        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        final List<ResolveInfo> pkgAppsList = this.getPackageManager().queryIntentActivities(mainIntent, 0);

        for (ResolveInfo info : pkgAppsList) {
            System.out.println(info.activityInfo.name);

            if (info.activityInfo.name.contains("spotify")) {
                ComponentName name = new ComponentName(info.activityInfo.packageName,
                        info.activityInfo.name);
                Intent i = new Intent(Intent.ACTION_MAIN);

                i.addCategory(Intent.CATEGORY_LAUNCHER);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                i.setComponent(name);

                startActivity(i);

            }
        }
    }
}
