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
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by nmesk on 6/18/2016.
 */
public class BluetoothHelper extends AppCompatActivity {
    private BluetoothAdapter btAdapter;

    public BluetoothHelper() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    public List<String> GetDevices() {
        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();

        List<String> s = new ArrayList<String>();
        for (BluetoothDevice bt : pairedDevices)
            s.add(bt.getName());

        return s;
    }


//    private void CallDevice() {
//
//        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
//        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//        final List<ResolveInfo> pkgAppsList = this.getPackageManager().queryIntentActivities(mainIntent, 0);
//
//        for (ResolveInfo info : pkgAppsList) {
//            System.out.println(info.activityInfo.name);
//
//            if (info.activityInfo.name.contains("spotify")) {
//                ComponentName name = new ComponentName(info.activityInfo.packageName,
//                        info.activityInfo.name);
//                Intent i = new Intent(Intent.ACTION_MAIN);
//
//                i.addCategory(Intent.CATEGORY_LAUNCHER);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//                        Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                i.setComponent(name);
//
//                startActivity(i);
//
//            }
//        }
//    }
}
