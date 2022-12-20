package com.example.cofredesenhas;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NewCredential extends AppCompatActivity {
    private static final String TAG = "NewCreditCardActivity";
    private TextView username, credentialName, password, note;
    private ArrayList parameters = new ArrayList();
    protected JSONObject response;
    protected transient User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_credential);
        user = new User(
                (int) getIntent().getSerializableExtra("userId"),
                (Boolean) getIntent().getSerializableExtra("SU"),
                (String) getIntent().getSerializableExtra("name"),
                (String) getIntent().getSerializableExtra("email"),
                (int) getIntent().getSerializableExtra("team"),
                (String) getIntent().getSerializableExtra("registerDate")
        );
        username = findViewById(R.id.newCredentialEmail);
        credentialName = findViewById(R.id.newCredentialName);
        password = findViewById(R.id.newCredentialPassword);
        note = findViewById(R.id.newCredentialNote);
        Button btnNewCredential = findViewById(R.id.btnCredential);
        btnNewCredential.setOnClickListener(v -> {
            if(username.getText().toString().length() > 1 || credentialName.getText().toString().length() > 1 || password.getText().toString().length() > 1 || note.getText().toString().length() > 1){
                parameters.add("credential");
                parameters.add(String.valueOf(user.getUserId()));
                parameters.add(username.getText().toString());
                parameters.add(credentialName.getText().toString());
                parameters.add(password.getText().toString());
                parameters.add(note.getText().toString());
                parameters.add(String.valueOf(-1));



                MyUrlRequestCallback apiResponse = new MyUrlRequestCallback("POST", "insertItem", parameters);
                try{
                    response = new JSONObject(apiResponse.execute().get());
                    Log.d(TAG,"response" + response);
                    showDialog(response.getBoolean("success"));


                } catch (ExecutionException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                    showDialog(false);
                }
            }
        });
    }
    protected void showDialog(Boolean success) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(success)
            builder.setMessage("Credencial inserida com sucesso");
        else
            builder.setMessage("Erro ao cadastrar Credencial");

        builder.setTitle("Alerta");
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setPositiveButton("Ok", (dialog, id) -> {
            // Selecionado OK
            Intent secondView;

            if(success){
                secondView=new Intent(getApplicationContext(), MainActivity.class);
                secondView.putExtra("User", user);
            }
            else
                secondView=new Intent(getApplicationContext(), NewFolderActivity.class);

            startActivity(secondView);            });

        AlertDialog dialog = builder.create();

        dialog.show();
    }
}