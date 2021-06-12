package com.example.assettracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Alert extends AppCompatActivity {

    Button buttonSubmit, buttonCancel;
    Spinner tagSpinner;
    ArrayList<String> tagList;
    ArrayAdapter<String> tagidAdapter;
    RequestQueue requestQueue;
    String URL, sessionemail, tagChoice;
    SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        /*Initialize Spinner (Drop Down Menu's)*/
        tagSpinner = findViewById(R.id.tags_spinner);

        /*Initialize the Request Queue*/
        requestQueue = Volley.newRequestQueue(this);

        /*Initializing SessionManager and Get Email*/
        sessionManager = new SessionManager(getApplicationContext());
        sessionemail = sessionManager.getEmail();

        /*URL Where I get the tags from*/
        URL = "http://10.0.0.14/LoginRegister/gettag.php";


        /*Initialize the Arraylist*/
        tagList = new ArrayList<>();


        /*Method to fetch Data from the URL*/
        getData();

        /*Initialize buttons*/
        buttonSubmit = findViewById(R.id.submit);
        buttonCancel = findViewById(R.id.cancel);


        /*On click listener for the Cancel button in case the user wants to cancel and go back the main activity*/
        buttonCancel.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
            finish();
        });

        /*On click listener for the submit button*/
        buttonSubmit.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
            finish();

        });
    }

    /* GETS THE DATA FOR THE SPINNER */
    public void getData(){
        /*To add parameters & values for the request*/
        Map<String, String> params = new HashMap<>();
        params.put("Email", sessionemail);

        JSONObject parameters = new JSONObject(params);

        /*Create ArrayList to store vehicle_id to later get vehicle data from DB Query*/
        ArrayList<String> vehicleid = new ArrayList<>();

        /*Initialize Json Object Request*/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, response -> {
            try {
                //Parsing the fetched Json String to JSON Object
                JSONArray jsonArray = response.getJSONArray("tags");
                Log.d(String.valueOf(jsonArray), "JSON DATA");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String tag = jsonObject.optString("Tag");
                    String vehicle = jsonObject.optString("Vehicle_Id");
                    tagList.add(tag);
                    vehicleid.add(vehicle);
                    tagidAdapter = new ArrayAdapter<>(Alert.this, android.R.layout.simple_spinner_item, tagList);
                    tagidAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    tagSpinner.setAdapter(tagidAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                }
            }, Throwable::getStackTrace);

        /*Adding request to the queue*/
        requestQueue.add(jsonObjectRequest);
        tagSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tagChoice = parent.getItemAtPosition(position).toString();
                String idvehicle = "";

                if(tagList.contains(tagChoice)){
                    int choice = tagList.indexOf(tagChoice);
                    idvehicle = vehicleid.get(choice);
                }
                else {
                    Toast.makeText(getApplicationContext(), "The tag does not exist", Toast.LENGTH_SHORT).show();
                }

                String url = "http://10.0.0.14/LoginRegister/getvehicleinfo.php?VehicleId=" + idvehicle;

                /*Initialize Json Object Request*/
                /*TODO: Finish the request to bring the vehicle information*/
                JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.POST, url, null, response -> {
                    try {
                        //Parsing the fetched Json String to JSON Object
                        JSONArray jsonArray = response.getJSONArray("tags");
                        Log.d(String.valueOf(jsonArray), "JSON DATA");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String tag = jsonObject.optString("Tag");
                            String vehicle = jsonObject.optString("Vehicle_Id");
                            tagList.add(tag);
                            vehicleid.add(vehicle);
                            tagidAdapter = new ArrayAdapter<>(Alert.this, android.R.layout.simple_spinner_item, tagList);
                            tagidAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            tagSpinner.setAdapter(tagidAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::getStackTrace);

            }
        });
    }

}
