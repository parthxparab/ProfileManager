package com.fab.gpsprofile.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.fab.gpsprofile.R;
import com.fab.gpsprofile.service.AppLocationService;
import com.fab.gpsprofile.service.MyService;
import com.fab.gpsprofile.utility.DBHelper;

public class Fragment1 extends Fragment  {

    public static Fragment1 newInstance() {
        return new Fragment1();
    }
    AutoCompleteTextView atvPlaces;
    PlacesTask placesTask;
    ParserTask parserTask;
    ImageView imgView;
    private Switch switchType;
    private CheckBox sendSMS,curLoc;
    private LinearLayout layout;
    private TimePicker time;
    private View mViewFragment2;
    private static final String TAG = "CardListActivity";
    private EditText editMsg,editPhone;
    private RadioGroup radioGroup;
    private RadioButton sound,vibration,silent,custom,radioProfileButton;
    private String msg,profile,current_time,lat,lon;
    String type="Location";
    String setYes="No";
    String phone="";
    DBHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mViewFragment2 = inflater.inflate(R.layout.fragment_1, container, false);

        switchType =(Switch)mViewFragment2.findViewById(R.id.txtType);
        layout= (LinearLayout)mViewFragment2.findViewById(R.id.type);
        time=(TimePicker)mViewFragment2.findViewById(R.id.timePicker);
        sendSMS=(CheckBox)mViewFragment2.findViewById(R.id.sendSMS);
        curLoc=(CheckBox)mViewFragment2.findViewById(R.id.curLoc);
        editMsg=(EditText)mViewFragment2.findViewById(R.id.editText);
        editPhone=(EditText)mViewFragment2.findViewById(R.id.editPhone);
        imgView= (ImageView) mViewFragment2.findViewById(R.id.imageView);

        switchType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    layout.setVisibility(View.GONE);
                    time.setVisibility(View.VISIBLE);
                    curLoc.setVisibility(View.GONE);
                    type="Time";
                }
                else{
                    layout.setVisibility(View.VISIBLE);
                    time.setVisibility(View.GONE);
                    curLoc.setVisibility(View.VISIBLE);
                    type="Location";
                }

            }
        });

        curLoc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    layout.setVisibility(View.GONE);
                    time.setEnabled(false);                }
                else{
                    layout.setVisibility(View.VISIBLE);
                    time.setEnabled(true);
                }

            }
        });

        sendSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    setYes="Yes";
                    editPhone.setVisibility(View.VISIBLE);
                }
                else{
                    setYes="No";
                    editPhone.setVisibility(View.GONE);
                }
            }
        });

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Geocoder coder = new Geocoder(getActivity());
                List<Address> address;

                try {
                    address = coder.getFromLocationName(atvPlaces.getText().toString(), 5);
                    Address location = address.get(0);

                    Intent intent=new Intent(getActivity(),MapActivity.class);
                    intent.putExtra("address", atvPlaces.getText().toString());
                    intent.putExtra("lat",location.getLatitude());
                    intent.putExtra("lon", location.getLongitude());
                    startActivity(intent);
                }
                catch(Exception e){}
            }
        });

        radioGroup = (RadioGroup)mViewFragment2. findViewById(R.id.myRadioGroup);


        sound = (RadioButton)mViewFragment2. findViewById(R.id.sound);
        vibration = (RadioButton)mViewFragment2. findViewById(R.id.vibration);
        silent = (RadioButton)mViewFragment2. findViewById(R.id.silent);
        custom = (RadioButton)mViewFragment2. findViewById(R.id.custom);

        time.setIs24HourView(true);

        final DBHelper dbHelper=new DBHelper(getActivity());
        dbHelper.openToWrite();
        Button btn=(Button)mViewFragment2.findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioProfileButton = (RadioButton) mViewFragment2.findViewById(selectedId);
                profile = radioProfileButton.getText().toString();
                msg = editMsg.getText().toString();
                phone=editPhone.getText().toString();


                if(phone.equals(""))
                    phone="-";
                if(msg.equals(""))
                    editMsg.setError("Message Required");
                else if(phone.equals("-") && setYes.equals("Yes"))
                    editPhone.setError("Phone number Required");
                else if (curLoc.isChecked()){
                    AppLocationService appLocationService = new AppLocationService(getActivity());
                    Location location = appLocationService.getLocation(LocationManager.NETWORK_PROVIDER);

                    if (location != null) {
                        Log.d("LAT LNG", location.getLatitude() + " " + location.getLongitude());
                        //    Toast.makeText(MyService.this,"Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude(),Toast.LENGTH_LONG).show();


                        double lat = location.getLatitude();
                        double lon = location.getLongitude();

                        try {
                            if (dbHelper.insert(getCurrentLocation(lat, lon), msg, profile, Double.toString(lat), Double.toString(lon), type, setYes, phone)) {
                                Toast.makeText(getActivity(), "Event Added", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                                getActivity().finish();
                            }
                        }
                        catch (IOException e){

                        }
                    }

                }
                else if (layout.getVisibility() == View.VISIBLE) {
                    Geocoder coder = new Geocoder(getActivity());
                    List<Address> address;

                    try {
                        address = coder.getFromLocationName(atvPlaces.getText().toString(), 5);
                        Address location = address.get(0);

                        lat = Double.toString(location.getLatitude());
                        lon = Double.toString(location.getLongitude());
//                        Toast.makeText(getActivity(),lat,Toast.LENGTH_LONG).show();
                        if(lat.equals("") && lon.equals(""))
                            Toast.makeText(getActivity(),"Make sure you have proper internet connection or your location is known.",Toast.LENGTH_LONG).show();
                        else if (dbHelper.insert(atvPlaces.getText().toString(), msg, profile, lat, lon, type, setYes,phone)) {
                            Toast.makeText(getActivity(), "Event Added", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(),"Make sure you have proper internet connection or your location is known.",Toast.LENGTH_LONG).show();
                    }
                } else {
                    current_time = time.getCurrentHour() + ":" + time.getCurrentMinute();

                    if (dbHelper.insert(current_time, msg, profile, "-", "-", type, setYes,phone)) {

                        Calendar cur_cal = new GregorianCalendar();
                        cur_cal.setTimeInMillis(System.currentTimeMillis());//set the current time and date for this calendar

                        Calendar cal = new GregorianCalendar();
                        cal.set(Calendar.HOUR_OF_DAY, time.getCurrentHour());
                        cal.set(Calendar.MINUTE, time.getCurrentMinute());
                        Intent intent = new Intent(getActivity(), MyService.class);
                        PendingIntent pintent = PendingIntent.getService(getActivity(), 0, intent, 0);
                        AlarmManager alarm = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
                        alarm.set(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),pintent);
//
//                        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);



                        Toast.makeText(getActivity(), "Event Added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    }
                }
            }
        });


        atvPlaces = (AutoCompleteTextView)mViewFragment2. findViewById(R.id.autoCompleteTextView);
        atvPlaces.setThreshold(2);
        atvPlaces.enoughToFilter();

            atvPlaces.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                placesTask = new PlacesTask();
                placesTask.execute(s.toString());
            }
        });

      return mViewFragment2;
    }

    public String getCurrentLocation(double lt, double ln) throws IOException{

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        addresses = geocoder.getFromLocation(lt, ln, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();

        return address+", "+city+", "+state+", "+country;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBtx9Xx-jWx-RW2OUsqRWyFLXzpALdpEJM";

            String input="";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input+"&"+types+"&"+sensor+"&"+key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;

            try{
                // Fetching the data from we service
                data = downloadUrl(url);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try{
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            }catch(Exception e){
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[] { "description"};
            int[] to = new int[] { android.R.id.text1 };

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), result, android.R.layout.activity_list_item, from, to);

            // Setting the adapter
            atvPlaces.setAdapter(adapter);
        }
    }

}
