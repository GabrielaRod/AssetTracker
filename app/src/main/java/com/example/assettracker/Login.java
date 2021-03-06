package com.example.assettracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {

    TextInputEditText textInputEditTextEmail, textInputEditTextPassword;
    Button buttonLogin;
    TextView signUpText;
    ProgressBar progressBar;
    SessionManager sessionManager;
    String token, serverURL;
    static final String CHANNEL_ID = "assettracker_id";
    static final String CHANNEL_NAME = "assettracker name";
    static final String CHANNEL_DESC = "assettracker desc";



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



        /**********************FIREBASE****************************/
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()){
                            token = task.getResult().getToken();
                        } else {
                            Toast.makeText(Login.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        //Creating notification channel for devices on and above Android O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        /*********************************************************/


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
                if(!email.equals("") && !password.equals("") && !token.equals("")){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "Email";
                            field[1] = "Password";
                            field[2] = "Token";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = email;
                            data[1] = password;
                            data[2] = token;
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