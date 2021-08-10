package com.example.assettracker;

import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.assettracker.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ArrayList<String> latitudelist, longitudelist;
    String URL, sessionemail, latitude, longitude;
    RequestQueue requestQueue;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*Initializing SessionManager and Get Email*/
        sessionManager = new SessionManager(getApplicationContext());
        sessionemail = sessionManager.getEmail();

        /*Initialize the Request Queue*/
        requestQueue = Volley.newRequestQueue(this);



        /*URL Where I get the tags from*/
        URL = "http://10.0.0.14/LoginRegister/markers.php";

        /*Method to fetch Data from the URL*/
        try {
           getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*To avoid the Map function to load before the getData*/
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Iterator<String> lat = latitudelist.iterator();
        Iterator<String> lng = longitudelist.iterator();

        // Add a marker in Sydney and move the camera
        while(lat.hasNext() && lng.hasNext()) {
            latitude = lat.next();
            longitude = lng.next();

            LatLng markers = new LatLng(Double.parseDouble(String.valueOf(latitude)), Double.parseDouble(String.valueOf(longitude)));
            mMap.addMarker(new MarkerOptions().position(markers).title("Last Seen"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markers, 15));

        }
    }

    /* GETS THE DATA FOR THE MAPS MARKERS */
    public void getData() throws JSONException {
        /*Create ArrayList to store markers to later send it to the Maps*/
        latitudelist = new ArrayList<>();
        longitudelist = new ArrayList<>();

        /*To get the JSON Data from the Query*/
        StringRequest strRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String latitude = jsonObject.optString("Latitude");
                                String longitude = jsonObject.optString("Longitude");
                                latitudelist.add(latitude);
                                longitudelist.add(longitude);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "ERROR FETCHING THE DATA: LATITUDE, LONGITUDE", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                /*To add parameters & values for the request*/
                Map<String, String> params = new HashMap<>();
                params.put("Email", sessionemail);
                return params;
            }
        };

        requestQueue.add(strRequest);
    }
}