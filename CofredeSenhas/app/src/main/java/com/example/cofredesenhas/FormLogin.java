package com.example.cofredesenhas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FormLogin extends AppCompatActivity {

    Button btnSigIn, btnSigUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        btnSigUp=findViewById(R.id.btnSigUpView);
        btnSigIn=findViewById(R.id.btnSigIn);

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
                Intent secondView=new Intent(getApplicationContext(), MainActivity.class);

                startActivity(secondView);
            }
        });

    }
}