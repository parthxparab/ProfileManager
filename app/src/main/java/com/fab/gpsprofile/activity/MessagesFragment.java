package com.fab.gpsprofile.activity;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fab.gpsprofile.R;
import com.fab.gpsprofile.service.AppLocationService;


public class MessagesFragment extends Fragment {


    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        AppLocationService appLocationService;
        appLocationService = new AppLocationService(getActivity());

        Location nwLocation = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);

        if (nwLocation != null) {
            double latitude = nwLocation.getLatitude();
            double longitude = nwLocation.getLongitude();
            Toast.makeText(
                    getActivity(),
                    "Mobile Location (NW): \nLatitude: " + latitude
                            + "\nLongitude: " + longitude,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(),"Something went wrong",Toast.LENGTH_LONG).show();
        }

        return inflater.inflate(R.layout.fragment_messages, container, false);
    }
}
