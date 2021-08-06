package com.example.assettracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ArrayList<String> markerslist;
    String URL, sessionemail;
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

        /*URL Where I get the tags from*/
        URL = "http://10.0.0.14/LoginRegister/markers.php";

        /*Method to fetch Data from the URL*/
        try {
            getData();
        } catch (JSONException e) {
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    /* GETS THE DATA FOR THE MAPS MARKERS */
    public void getData() throws JSONException {
        /*Create ArrayList to store vehicle_id to later get vehicle data from DB Query*/


        /*To get the JSON Data from the Query*/
        StringRequest strRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String vin = jsonObject.optString("VIN");
                                String make = jsonObject.optString("Make");
                                String model = jsonObject.optString("Model");
                                String color = jsonObject.optString("Color");
                                alertslist.add(vin);
                                makelist.add(make);
                                modellist.add(model);
                                colorlist.add(color);
                                alertAdapter = new ArrayAdapter<>(DisableAlert.this, android.R.layout.simple_spinner_item, alertslist);
                                alertAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                vinspinner.setAdapter(alertAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                /*To add parameters & values for the request*/
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email", sessionemail);
                return params;
            }
        };

        requestQueue.add(strRequest);



        vinspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vinChoice = parent.getItemAtPosition(position).toString();

                if (alertslist.contains(vinChoice)) {
                    int choice = alertslist.indexOf(vinChoice);
                    makeindex = makelist.get(choice);
                    modelindex = modellist.get(choice);
                    colorindex = colorlist.get(choice);
                } else {
                    Toast.makeText(getApplicationContext(), "The tag does not exist", Toast.LENGTH_SHORT).show();
                }

                Make.setText(makeindex);
                Model.setText(modelindex);
                Color.setText(colorindex);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(), "No Data to retrieve", Toast.LENGTH_SHORT).show();
            }
        });
}