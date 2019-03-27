package com.fab.gpsprofile.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.fab.gpsprofile.R;
import com.fab.gpsprofile.utility.DBHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
    //    static final LatLng KIEL = new LatLng(53.551, 9.993);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        String address = getIntent().getStringExtra("address");
        double lat = getIntent().getDoubleExtra("lat", 0.0);
        double lon = getIntent().getDoubleExtra("lon", 0.0);
//        final LatLng HAMBURG = new LatLng(53.558, 9.927);
        Marker hamburg = map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
                .title(address));

        // Move the camera instantly to hamburg with a zoom of 15.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15));

        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);
        addMarkers();

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {
                Toast.makeText(getApplicationContext(), point.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMarkers() {
        if (map != null) {

            DBHelper dbhelper = new DBHelper(getApplicationContext());
            dbhelper.openToWrite();

            for(int i=0;i<dbhelper.count();i++) {
                // marker with custom color
                LatLng BROOKLYN_BRIDGE = new LatLng(Double.parseDouble(dbhelper.displayLatitude(i)), Double.parseDouble(dbhelper.displayLongitude(i)));
                map.addMarker(new MarkerOptions()
                        .position(BROOKLYN_BRIDGE)
                        .title(dbhelper.displayMessage(i))
                        .icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        BROOKLYN_BRIDGE, 13));
            }
        }
    }
}