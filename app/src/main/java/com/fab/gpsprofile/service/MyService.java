package com.fab.gpsprofile.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.Toast;

import com.fab.gpsprofile.R;
import com.fab.gpsprofile.activity.MainActivity;
import com.fab.gpsprofile.utility.DBHelper;

import java.util.Calendar;

import static android.media.AudioManager.*;

public class MyService extends Service implements LocationListener {

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    AudioManager mode;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    protected LocationManager locationManager;
    private static final int NOTIFY_ME_ID=1337;
    private int count=0;
    private NotificationManager notifyMgr=null;
    AppLocationService appLocationService;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

       String time=hour+":"+minute;
        notifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (mode == null) {
            mode = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        }

        DBHelper dbHelper=new DBHelper(this);
        dbHelper.openToWrite();
        for(int i=0;i<dbHelper.count();i++) {
            if(dbHelper.displayType(i).equals("Location")){

            appLocationService = new AppLocationService(MyService.this);
            Location location = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                Log.d("LAT LNG", location.getLatitude() + " " + location.getLongitude());
                //    Toast.makeText(MyService.this,"Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude(),Toast.LENGTH_LONG).show();


                double lat = location.getLatitude();
                double lon = location.getLongitude();
                String clt = "" + lat;
                String cln = "" + lon;

                clt = clt.toString().trim().substring(0, 5);
                cln = cln.toString().trim().substring(0, 5);
                String slt = dbHelper.displayLatitude(i).toString().trim().substring(0, 5);
                String sln = dbHelper.displayLongitude(i).toString().trim().substring(0, 5);
                String profile = dbHelper.displayProfile(i).trim();
                if (clt.equals(slt) && cln.equals(sln)) {
                    if (profile.equals("Silent"))
                        setSilent();
                    else if (profile.equals("General"))
                        setNormal();
                    else if (profile.equals("Vibrate"))
                        setVibrate();
                    else if (profile.equals("Custom"))
                        setCustom();
                    Notification notifyObj = new Notification(R.drawable.ic_done,
                            "Profile Manager",
                            System.currentTimeMillis());
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                            new Intent(this, MainActivity.class),
                            0);
                    notifyObj.setLatestEventInfo(this, "Profile Manager",
                            dbHelper.displayMessage(i), pendingIntent);
                    notifyObj.defaults |= Notification.DEFAULT_VIBRATE;
                    notifyObj.defaults |= Notification.DEFAULT_SOUND;
                    notifyObj.flags |= Notification.FLAG_AUTO_CANCEL;
                    notifyMgr.notify(NOTIFY_ME_ID, notifyObj);

                    if (dbHelper.displayState(i).equals("Yes")){
//                        Toast.makeText(MyService.this,dbHelper.displayMessage(i),Toast.LENGTH_LONG).show();
                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(dbHelper.displayPhone(i),null,dbHelper.displayMessage(i),null,null);
                        dbHelper.updateState(i,"No");
/*                        if(dbHelper.updateState(i,"No")==1)
                            Toast.makeText(MyService.this,"Updated",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MyService.this,"Update Failed",Toast.LENGTH_LONG).show();
*/                    }

//                    notifyMgr.notify(NOTIFY_ME_ID, notifyObj);
                }

            } else
                Toast.makeText(this, "Please make sure GPS Service is enabled.", Toast.LENGTH_LONG).show();
        }
            else{
                if(dbHelper.displayType(i).equals("Time")){
                   if(dbHelper.displayAddress(i).equals(time)) {
                        String profile = dbHelper.displayProfile(i).trim();
                        if (profile.equals("Silent"))
                            setSilent();
                        else if (profile.equals("General"))
                            setNormal();
                        else if (profile.equals("Vibrate"))
                            setVibrate();
                        else if (profile.equals("Custom"))
                            setCustom();
                        Notification notifyObj = new Notification(R.drawable.ic_done,
                                "Profile Manager",
                                System.currentTimeMillis());
                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                                new Intent(this, MainActivity.class),
                                0);
                        notifyObj.setLatestEventInfo(this, "Profile Manager",
                                dbHelper.displayMessage(i), pendingIntent);
                        notifyObj.defaults |= Notification.DEFAULT_VIBRATE;
                        notifyObj.defaults |= Notification.DEFAULT_SOUND;
                        notifyObj.flags |= Notification.FLAG_AUTO_CANCEL;
                       notifyMgr.notify(NOTIFY_ME_ID, notifyObj);
                        if (dbHelper.displayState(i).equals("Yes")){
//                            Toast.makeText(MyService.this,dbHelper.displayMessage(i),Toast.LENGTH_LONG).show();
                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(dbHelper.displayPhone(i),null,dbHelper.displayMessage(i),null,null);
                            dbHelper.updateState(i,"No");
/*                            if(dbHelper.updateState(i,"No")==1)
                                Toast.makeText(MyService.this,"Updated",Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(MyService.this,"Update Failed",Toast.LENGTH_LONG).show();
*/                        }
                    }
                }
            }
        }

        return START_STICKY;
    }

    private boolean setSilent(){
        mode.setRingerMode(RINGER_MODE_SILENT);

        return false;
    }

    private boolean setNormal(){
        mode.setRingerMode(RINGER_MODE_NORMAL);

        return false;
    }

    private boolean setVibrate(){
        mode.setRingerMode(RINGER_MODE_VIBRATE);

        return false;
    }

    private void setCustom(){
/*
        Setting Alarm Volume.**
        Setting Media Volume.**
        Setting Notification Volume.**
        Setting Ringtone Volume. **
        Setting Silent mode on.**
        Setting Vibrate on.**
        Setting Wi-Fi on.**
        Setting Bluetooth on.**
        Setting up location-based SMS messages.
        Setting Flight mode on.*
*/
        WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);

        if(getSharedPreferences("Custom",MODE_PRIVATE).getString("Ringer","").equals("ON"))
            mode.setStreamVolume(AudioManager.STREAM_RING, mode.getStreamMaxVolume(mode.STREAM_RING), FLAG_SHOW_UI);
        else
            mode.setStreamVolume(AudioManager.STREAM_RING, 0, FLAG_SHOW_UI);

        if(getSharedPreferences("Custom",MODE_PRIVATE).getString("Wifi", "").equals("ON"))
            wifiManager.setWifiEnabled(true);
        else
            wifiManager.setWifiEnabled(false);

        if (getSharedPreferences("Custom",MODE_PRIVATE).getString("Alarm","").equals("ON"))
            mode.setStreamVolume(AudioManager.STREAM_ALARM, mode.getStreamMaxVolume(mode.STREAM_ALARM), FLAG_SHOW_UI);
        else
            mode.setStreamVolume(AudioManager.STREAM_ALARM, 0, FLAG_SHOW_UI);

        if(getSharedPreferences("Custom", MODE_PRIVATE).getString("Airplane", "").equals("ON"))
            airPlanemodeON();
        else
            airPlanemodeOFF();

        if(getSharedPreferences("Custom",MODE_PRIVATE).getString("Notification","").equals("ON"))
            mode.setStreamVolume(AudioManager.STREAM_ALARM, mode.getStreamMaxVolume(mode.STREAM_NOTIFICATION), FLAG_SHOW_UI);
        else
            mode.setStreamVolume(AudioManager.STREAM_ALARM, 0, FLAG_SHOW_UI);

        if(getSharedPreferences("Custom",MODE_PRIVATE).getString("Media","").equals("ON"))
            mode.setStreamVolume(AudioManager.STREAM_MUSIC, mode.getStreamMaxVolume(mode.STREAM_MUSIC), FLAG_SHOW_UI);
        else
            mode.setStreamVolume(AudioManager.STREAM_MUSIC,0,FLAG_SHOW_UI);

        if(getSharedPreferences("Custom",MODE_PRIVATE).getString("Voice","").equals("ON"))
            mode.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mode.getStreamMaxVolume(mode.STREAM_VOICE_CALL), FLAG_SHOW_UI);
        else
            mode.setStreamVolume(AudioManager.STREAM_VOICE_CALL,0,FLAG_SHOW_UI);

        if(getSharedPreferences("Custom",MODE_PRIVATE).getString("Vibrate","").equals("ON"))
            mode.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_ON);
        else
            mode.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);

        if(getSharedPreferences("Custom",MODE_PRIVATE).getString("Bluetooth","").equals("ON"))
            setBluetooth(true);
        else
            setBluetooth(false);
    }

    public void airPlanemodeON() {
        boolean isEnabled = Settings.System.getInt(this.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        if (isEnabled == false) {
            modifyAirplanemode(true);
        }
    }

    public void airPlanemodeOFF() {
        boolean isEnabled = Settings.System.getInt(this.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        if (isEnabled == true) {
            modifyAirplanemode(false);
        }
    }

    public void modifyAirplanemode(boolean mode) {
        Settings.System.putInt(getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, mode ? 1 : 0);// Turning ON/OFF Airplane mode.

        Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);// creating intent and Specifying action for AIRPLANE mode.
        intent.putExtra("state", !mode);// indicate the "state" of airplane mode is changed to ON/OFF
        sendBroadcast(intent);// Broadcasting and Intent

    }

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }

    public MyService(Context context) {
        getLocation();
    }

    public MyService() {
        getLocation();
    }

    public void getGPSLocation(){
        appLocationService = new AppLocationService(MyService.this);
        Location location = appLocationService.getLocation(LocationManager.GPS_PROVIDER);
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                Toast.makeText(MyService.this,"NP Settings Problem",Toast.LENGTH_LONG).show();

            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, MyService.this);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            Log.d("LAT LNG", location.getLatitude()+" "+location.getLongitude());
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                        else
                        Log.d("Location", "Null");
                    }
                }
                else
                    Toast.makeText(MyService.this,"Settings Problem",Toast.LENGTH_LONG).show();

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, MyService.this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
                else
                    Toast.makeText(MyService.this,"Settings Problem",Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
        }
        return location;
    }

    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app.
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(MyService.this);
        }
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyService.this);

        // Setting DialogHelp Title
        alertDialog.setTitle("GPS is settings");

        // Setting DialogHelp Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        float bestAccuracy = -1f;
        if (location.getAccuracy() != 0.0f
                && (location.getAccuracy() < bestAccuracy) || bestAccuracy == -1f) {
            locationManager.removeUpdates(MyService.this);
        }
        bestAccuracy = location.getAccuracy();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public float getAccurecy()
    {
        return location.getAccuracy();
    }

}