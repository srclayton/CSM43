package com.example.cofredesenhas;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class NewFolderActivity extends AppCompatActivity {


    private static final String TAG = "NewFolderActivity";
    private TextView name;
    private ArrayList parameters = new ArrayList();
    protected JSONObject response;
    protected transient User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_folder);
        //int userId = (int) getIntent().getSerializableExtra("userId");
        //int teamId = (int) getIntent().getSerializableExtra("teamId");
        user = new User(
                (int) getIntent().getSerializableExtra("userId"),
                (Boolean) getIntent().getSerializableExtra("SU"),
                (String) getIntent().getSerializableExtra("name"),
                (String) getIntent().getSerializableExtra("email"),
                (int) getIntent().getSerializableExtra("team"),
                (String) getIntent().getSerializableExtra("registerDate")
        );
        int userId = user.getUserId();
        int teamId = user.getTeam();
        name = findViewById(R.id.txtNewFolderName);
        Button btnNewFolder = findViewById(R.id.btnFolder);

        btnNewFolder.setOnClickListener(v -> {
            Log.d(TAG, name.getText().toString() + userId + teamId);
            if(name.getText().toString().length() > 1){
                parameters.add("folder");
                parameters.add(name.getText().toString());
                parameters.add(String.valueOf(userId));
                parameters.add(String.valueOf(teamId));
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
            builder.setMessage("Pasta inserida com sucesso");
        else
            builder.setMessage("Erro ao cadastrar pasta");

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