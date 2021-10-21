package com.example.assettracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    Button buttonLogin;
    TextView signUpText;
    ProgressBar progressBar;
    SessionManager sessionManager;
    String serverURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextEmail = findViewById(R.id.emaillogin);
        textInputEditTextPassword = findViewById(R.id.passwordlogin);

        buttonLogin = findViewById(R.id.buttonLogin);
        signUpText = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);

        /*Global variable to hold the server URL + Initializing SessionManager*/
        sessionManager = new SessionManager(getApplicationContext());
        serverURL = sessionManager.ServerURL;


        /*On click listener for the sign up button*/
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });


        /*On click listener for the Login button in case the user already has an account*/
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email, password;
                /*Getting the data from the input variables*/
                email = String.valueOf(textInputEditTextEmail.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                /*Error handling, if the fields are empty it will show Toast*/
                if(!email.equals("") && !password.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "Email";
                            field[1] = "Password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = email;
                            data[1] = password;
                            PutData putData = new PutData(serverURL+"login.php", "POST", field, data); //If using a mobil device it will be visible if they are connected to the same network
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    if(result.equals("Login Success")){
                                        sessionManager.setLogin(true); //User is logged in
                                        sessionManager.setEmail(email); //Store email
                                        Intent intent = new Intent(getApplicationContext(), MainActivity2.class); //If success it will take you to MainActivity
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
}