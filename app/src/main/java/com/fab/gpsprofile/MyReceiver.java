package com.fab.gpsprofile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.fab.gpsprofile.service.MyService;


/**
 * Created by Patel on 06-09-2015.
 */
public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, MyService.class);
        context.startService(myIntent);

    }
}
