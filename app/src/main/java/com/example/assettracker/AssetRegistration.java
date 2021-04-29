package com.example.assettracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AssetRegistration extends AppCompatActivity {

        TextInputEditText textInputEditTextVin, textInputEditTextMake, textInputEditTextModel, textInputEditTextYear, textInputEditTextTag;
        Button buttonSubmit;
        Button buttonCancel;
        ProgressBar progressBar;
        Spinner type, color;
        String typechoice, colorchoice;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sign_up);

            /*To set the variables for the input data*/
            textInputEditTextVin = findViewById(R.id.vin);
            textInputEditTextMake = findViewById(R.id.make);
            textInputEditTextModel = findViewById(R.id.model);
            textInputEditTextYear = findViewById(R.id.year);
            textInputEditTextTag = findViewById(R.id.tag);

            /*Spinners (Drop Down Menu's)*/
            type = findViewById(R.id.typespinner);
            color = findViewById(R.id.colortype);

            /*type*/
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type.setAdapter(adapter);

            //type.setOnItemSelectedListener(this);
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

            /*color*/
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.color, android.R.layout.simple_spinner_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            color.setAdapter(adapter2);

            //color.setOnItemSelectedListener(this);
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

            buttonSubmit = findViewById(R.id.submit);
            buttonCancel = findViewById(R.id.cancel);
            progressBar = findViewById(R.id.progress);


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
                    String vin, make, model, year, tagid, type, color;
                    vin = String.valueOf(textInputEditTextVin.getText());
                    make = String.valueOf(textInputEditTextMake.getText());
                    model = String.valueOf(textInputEditTextModel.getText());
                    year = String.valueOf(textInputEditTextYear.getText());
                    tagid = String.valueOf(textInputEditTextTag.getText());
                    type = typechoice;
                    color = colorchoice;


                    /*Error handling, if the fields are empty it will show Toast*/
                    if(!vin.equals("") && !make.equals("") && !model.equals("") && !year.equals("") && !color.equals("") && !type.equals("") && !tagid.equals("")){

                        progressBar.setVisibility(View.VISIBLE);
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Starting Write and Read data with URL
                                //Creating array for parameters
                                String[] field = new String[7];
                                field[0] = "VIN";
                                field[1] = "Make";
                                field[2] = "Model";
                                field[3] = "Year";
                                field[4] = "Color";
                                field[5] = "Type";
                                field[6] = "Tag";


                                //Creating array for data
                                String[] data = new String[7];
                                data[0] = vin;
                                data[1] = make;
                                data[2] = model;
                                data[3] = year;
                                data[4] = type;
                                data[5] = color;
                                data[6] = tagid;

                                Log.v("log", data[4]);

                                PutData putData = new PutData("http://10.0.0.14/LoginRegister/asset.php", "POST", field, data); //If using a mobil device it will be visible if they are connected to the same network
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        if(result.equals("Asset Registration Successful")){
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
        /*
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            typechoice = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), typechoice, Toast.LENGTH_SHORT).show();

            colorchoice = parent.getItemAtPosition(position).toString();
            Toast.makeText(getApplicationContext(), colorchoice, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Toast.makeText(getApplicationContext(), "This field is required", Toast.LENGTH_SHORT).show();
        }*/

}