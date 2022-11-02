package com.example.cofredesenhas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class FormLogin extends AppCompatActivity {

    protected static final String TAG = "FormLogin";
    protected Button btnSigIn, btnSigUp;
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

        btnSigUp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent secondView=new Intent(getApplicationContext(), RegistrationForm.class);

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
            secondView=new Intent(getApplicationContext(), FormLogin.class);
            startActivity(secondView);
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}