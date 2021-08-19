package com.example.assettracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AssetRegistration extends AppCompatActivity {

        TextInputEditText textInputEditTextVin, textInputEditTextMake, textInputEditTextModel, textInputEditTextYear, textInputEditTextTag;
        Button buttonSubmit;
        Button buttonCancel;
        ProgressBar progressBar;
        Spinner color, type, brand;
        String typechoice, colorchoice, brandchoice, sessionemail;
        SessionManager sessionManager;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_asset_registration);

            /*To set the variables for the input data*/
            textInputEditTextVin = findViewById(R.id.vin);
            textInputEditTextModel = findViewById(R.id.model);
            textInputEditTextYear = findViewById(R.id.year);
            textInputEditTextTag = findViewById(R.id.tag);

            /*Spinners (Drop Down Menu's)*/
            color = findViewById(R.id.color_spinner);
            type = findViewById(R.id.type_spinner);
            brand = findViewById(R.id.brand_spinner);

            /*Initializing SessionManager and Get Email*/
            sessionManager = new SessionManager(getApplicationContext());
            sessionemail = sessionManager.getEmail();

            /*color*/
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.color, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            color.setAdapter(adapter2);

            color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
                @Override
                public void onItemSelected (AdapterView<?>adapterView, View view, int i, long l) {
                    colorchoice = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), colorchoice, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected (AdapterView<?>adapterView) {
                    Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
                }
            });

            /*type*/
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(adapter);

            type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
                @Override
                public void onItemSelected (AdapterView<?>adapterView, View view, int i, long l) {
                    typechoice = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), typechoice, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected (AdapterView<?>adapterView) {
                    Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
                }
            });

            /*brand*/
            ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.brand, android.R.layout.simple_spinner_item);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            brand.setAdapter(adapter3);

            brand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener () {
                @Override
                public void onItemSelected (AdapterView<?>adapterView, View view, int i, long l) {
                    brandchoice = adapterView.getItemAtPosition(i).toString();
                    Toast.makeText(getApplicationContext(), brandchoice, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onNothingSelected (AdapterView<?>adapterView) {
                    Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
                }
            });

            buttonSubmit = findViewById(R.id.submit);
            buttonCancel = findViewById(R.id.cancel);
            progressBar = findViewById(R.id.progressasset);


            /*On click listener for the Cancel button in case the user wants to cancel registration*/
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
                    startActivity(intent);
                    finish();
                }
            });

            /*On click listener for the submit button*/
            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /*Getting the data from the input variables*/
                    String vin, make, model, year, tagid, cartype, carcolor, email;
                    vin = String.valueOf(textInputEditTextVin.getText());
                    make = String.valueOf(brandchoice);
                    model = String.valueOf(textInputEditTextModel.getText());
                    year = String.valueOf(textInputEditTextYear.getText());
                    tagid = String.valueOf(textInputEditTextTag.getText());
                    cartype = String.valueOf(typechoice);
                    carcolor = String.valueOf(colorchoice);
                    email = sessionemail;


                    /*Error handling, if the fields are empty it will show Toast*/
                    if(!vin.equals("") && !make.equals("") && !model.equals("") && !year.equals("") && !carcolor.equals("") && !cartype.equals("") && !tagid.equals("")){

                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[8];
                                field[0] = "VIN";
                                field[1] = "Make";
                                field[2] = "Model";
                                field[3] = "Year";
                                field[4] = "Color";
                                field[5] = "Type";
                                field[6] = "Tag";
                                field[7] = "Email";


                                //Creating array for data
                                String[] data = new String[8];
                                data[0] = vin;
                                data[1] = make;
                                data[2] = model;
                                data[3] = year;
                                data[4] = carcolor;
                                data[5] = cartype;
                                data[6] = tagid;
                                data[7] = email;

                                PutData putData = new PutData("http://10.0.0.14/LoginRegister/asset.php", "POST", field, data); //If using a mobil device it will be visible if they are connected to the same network
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        if(result.equals("Registration Successful")){
                                            Intent intent = new Intent(getApplicationContext(), MainActivity2.class); //If success it will take you to MainActivity2
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }
                                //End Write and Read data with URL
                            }

                        });
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            startActivity(intent);
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }

    }

}