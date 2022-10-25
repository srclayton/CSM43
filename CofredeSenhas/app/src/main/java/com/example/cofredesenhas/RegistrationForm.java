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

public class RegistrationForm extends AppCompatActivity {
    protected static final String TAG = "RegistrationForm";
    protected TextView name, email, password;
    protected ArrayList<String> parameters = new ArrayList<>();
    protected JSONObject response;
    Button btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG,"Iniciada!");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_form);
        btnSignUp=findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(v -> {
            if(addParameters()){
                MyUrlRequestCallback apiResponse = new MyUrlRequestCallback("POST", "registrationForm",parameters);
                try {
                    response = new JSONObject(apiResponse.execute().get());
                    showDialog(response.getBoolean("success"), "onClickRegister");

                    Log.d(TAG,"response" + response);
                } catch (ExecutionException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }else
                showDialog(false,"addParameters");



        });


    }

    protected Boolean addParameters(){
        name=findViewById(R.id.txtName);
        email=findViewById(R.id.txtEmail);
        password=findViewById(R.id.txtPassword);
        if(name.getText().toString().length() < 1 ||
                email.getText().toString().length() < 1 ||
                password.getText().toString().length() < 1 )
            return false;




        parameters.add(name.getText().toString());
        parameters.add(email.getText().toString());
        parameters.add(password.getText().toString());
        parameters.add(null);
        parameters.add("1");


        return true;
    }

    protected void showDialog(Boolean success, String content) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        Log.d(TAG, String.valueOf(success));
        if(content.equals("addParameters"))
            builder.setMessage("Todos os campos são obrigatórios");
        else if(success){
            builder.setMessage("Usuário cadastrado com sucesso!");
        }else{
            builder.setMessage("este Email já esta cadastrado.");
        }
        builder.setTitle("Alerta");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Ok", (dialog, id) -> {
            // Selecionado OK
            Intent secondView;
            if(success)
                secondView=new Intent(getApplicationContext(), FormLogin.class);
            else
                secondView=new Intent(getApplicationContext(), RegistrationForm.class);

            startActivity(secondView);            });
        AlertDialog dialog = builder.create();

        dialog.show();
    }
}
