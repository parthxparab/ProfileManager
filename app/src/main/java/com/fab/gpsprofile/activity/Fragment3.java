package com.fab.gpsprofile.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.fab.gpsprofile.R;
import com.fab.gpsprofile.adapter.CardArrayAdapter;
import com.fab.gpsprofile.utility.Card;
import com.fab.gpsprofile.utility.DBHelper;

public class Fragment3 extends Fragment {
    public static Fragment3 newInstance() {
        return new Fragment3();
    }
    private View mViewFragment3;
    private SharedPreferences preferences;
    private Switch ring,alarm,media,vibrate,voice,notification,wifi,bluetooth,flight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewFragment3 = inflater.inflate(R.layout.fragment_3, container, false);

        preferences=getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE);
        ring=(Switch)mViewFragment3.findViewById(R.id.switch1);
        alarm=(Switch)mViewFragment3.findViewById(R.id.switch2);
        media=(Switch)mViewFragment3.findViewById(R.id.switch3);
        vibrate=(Switch)mViewFragment3.findViewById(R.id.switch4);
        voice=(Switch)mViewFragment3.findViewById(R.id.switch5);
        notification=(Switch)mViewFragment3.findViewById(R.id.switch6);
        wifi=(Switch)mViewFragment3.findViewById(R.id.switch7);
        bluetooth=(Switch)mViewFragment3.findViewById(R.id.switch8);
        flight=(Switch)mViewFragment3.findViewById(R.id.switch9);

        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Ringer","").equals("ON"))
            ring.setChecked(true);
        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Alarm","").equals("ON"))
            alarm.setChecked(true);
        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Media","").equals("ON"))
            media.setChecked(true);
        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Vibrate","").equals("ON"))
            vibrate.setChecked(true);
        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Voice","").equals("ON"))
            voice.setChecked(true);

        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Notification","").equals("ON"))
            notification.setChecked(true);
        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Wifi","").equals("ON"))
            wifi.setChecked(true);
        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Bluetooth","").equals("ON"))
            bluetooth.setChecked(true);
        if(getActivity().getSharedPreferences("Custom", Context.MODE_PRIVATE).getString("Flight","").equals("ON"))
            flight.setChecked(true);

        ring.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Ringer","ON").commit();
                }else{
                    preferences.edit().putString("Ringer","OF").commit();
                }
            }
        });
        alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Alarm","ON").commit();
                }else{
                    preferences.edit().putString("Alarm","OFF").commit();
                }
            }
        });
        media.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Media","ON").commit();
                }else{
                    preferences.edit().putString("Media","OFF").commit();
                }
            }
        });
        voice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Voice","ON").commit();
                }else{
                    preferences.edit().putString("Voice","OFF").commit();
                }
            }
        });
        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Vibrate","ON").commit();
                }else{
                    preferences.edit().putString("Vibrate", "OFF").commit();
                }
            }
        });
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Notification","ON").commit();
                }else{
                    preferences.edit().putString("Notification", "OFF").commit();
                }
            }
        });
        flight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Flight","ON").commit();
                }else{
                    preferences.edit().putString("Flight", "OFF").commit();
                }
            }
        });
        bluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Bluetooth","ON").commit();
                }else{
                    preferences.edit().putString("Bluetooth", "OFF").commit();
                }
            }
        });
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    preferences.edit().putString("Wifi","ON").commit();
                }else{
                    preferences.edit().putString("Wifi", "OFF").commit();
                }
            }
        });

      return mViewFragment3;
    }
}
