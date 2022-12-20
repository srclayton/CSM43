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

public class NewCreditCard extends AppCompatActivity {
    private static final String TAG = "NewCreditCardActivity";
    private TextView surname, cardHolderName, cardNumber, expirationDate, cvcCode;
    private ArrayList parameters = new ArrayList();
    protected JSONObject response;
    protected transient User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_credit_card);
        user = new User(
                (int) getIntent().getSerializableExtra("userId"),
                (Boolean) getIntent().getSerializableExtra("SU"),
                (String) getIntent().getSerializableExtra("name"),
                (String) getIntent().getSerializableExtra("email"),
                (int) getIntent().getSerializableExtra("team"),
                (String) getIntent().getSerializableExtra("registerDate")
        );
        surname = findViewById(R.id.newCardSurname);
        cardHolderName = findViewById(R.id.newCardCardHolderName);
        cardNumber = findViewById(R.id.newCardCardNumber);
        expirationDate = findViewById(R.id.newCardExpirationDate);
        cvcCode = findViewById(R.id.newCardCvcCode);

        Button btnNewCard = findViewById(R.id.btnCreditCard);
        btnNewCard.setOnClickListener(v -> {
            if(surname.getText().toString().length() > 1 || cardHolderName.getText().toString().length() > 1 || cardNumber.getText().toString().length() > 1 || expirationDate.getText().toString().length() > 1 || cvcCode.getText().toString().length() > 1){
                parameters.add("creditCard");
                parameters.add(String.valueOf(user.getUserId()));
                parameters.add(surname.getText().toString());
                parameters.add(cardHolderName.getText().toString());
                parameters.add(cardNumber.getText().toString());
                parameters.add(expirationDate.getText().toString());
                parameters.add(cvcCode.getText().toString());


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
            builder.setMessage("Cartão inserida com sucesso");
        else
            builder.setMessage("Erro ao cadastrar cartão");

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