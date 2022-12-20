package com.example.cofredesenhas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class FormLoginActivity extends AppCompatActivity {

    protected static final String TAG = "FormLogin";
    protected Button btnSigIn, btnSigUp, btnLogin;
    protected TextView username, password;
    protected ArrayList<String> parameters = new ArrayList<>();
    protected JSONObject response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Iniciada!");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        btnSigUp=findViewById(R.id.btnSigUpView);
        btnSigIn=findViewById(R.id.btnSignIn);
        btnLogin=findViewById(R.id.btnBiometric);

        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS)
            btnLogin.setVisibility(View.VISIBLE);

        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(FormLoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"logado");
                username = findViewById(R.id.txtFormEmail);
                parameters.clear();
                parameters.add(username.getText().toString());
                MyUrlRequestCallback apiResponse = new MyUrlRequestCallback("GET", "fingerprintLogin", parameters);
                try {
                    JSONArray result2 = new JSONArray(apiResponse.execute().get());
                    response = new JSONObject();
                    response = result2.getJSONObject(0);
                    if(result2.getJSONObject(1).getBoolean("success")){
                        User user = new User(response.getInt("_id"),
                                response.getBoolean("su"),
                                response.getString("name"),
                                response.getString("email"),
                                response.getInt("team"),
                                response.getString("registerDate"));
                        Log.d(TAG, "RESPONSE: " +response.toString());
                        Intent secondView = new Intent(getApplicationContext(), MainActivity.class);
                        secondView.putExtra("User", user);
                        startActivity(secondView);
                    }else
                        showDialog(response.getString("message"));

                }catch (ExecutionException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Login").setDescription("Use a biometria para realizar o login").setNegativeButtonText("Cancel").build();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        btnSigUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent secondView=new Intent(getApplicationContext(), RegistrationFormActivity.class);

                startActivity(secondView);
            }
        });
        btnSigIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                username = findViewById(R.id.txtFormEmail);
                password = findViewById(R.id.txtFormPassword);
                parameters.add(username.getText().toString());
                parameters.add(password.getText().toString());
                MyUrlRequestCallback apiResponse = new MyUrlRequestCallback("GET", "password_Authentication", parameters);
                try {
                    JSONArray result = new JSONArray(apiResponse.execute().get());
                    response = new JSONObject();
                    response = result.getJSONObject(0);
                    if(result.getJSONObject(1).getBoolean("success")){
                        User user = new User(response.getInt("_id"),
                                response.getBoolean("su"),
                                response.getString("name"),
                                response.getString("email"),
                                response.getInt("team"),
                                response.getString("registerDate"));
                        Log.d(TAG, "RESPONSE: " +response.toString());
                        Intent secondView = new Intent(getApplicationContext(), MainActivity.class);
                        secondView.putExtra("User", user);
                        startActivity(secondView);
                    }else
                        showDialog(response.getString("message"));

                }catch (ExecutionException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
                //Intent secondView=new Intent(getApplicationContext(), MainActivity.class);

                //startActivity(secondView);
            }
        });

    }
    protected void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(message.equals("usernotfound")){
            builder.setMessage("Usuário não existe!");
        }else{
            builder.setMessage("Usuário ou senha incorreto(a)!");
        }
        builder.setTitle("Alerta");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Ok", (dialog, id) -> {
            // Selecionado OK
            Intent secondView;
            secondView=new Intent(getApplicationContext(), FormLoginActivity.class);
            startActivity(secondView);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}