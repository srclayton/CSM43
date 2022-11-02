package com.example.cofredesenhas;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.utils.ViewSpline;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MainActivity";
    protected static final String[] TITTLE = {"Tipos", "Pastas", "Nenhuma Pasta", "Lixeira"};
    protected static final String[] TYPE = {"Cartão", "Credencial"};
    protected ArrayList<String> parameters = new ArrayList<>();
    protected JSONArray response;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Iniciada!");
        User user = (User) getIntent().getSerializableExtra("User");
        createObj(user);
        for (String tittle : TITTLE) {
            //============================
            //Adição dos titulas na tela:
            //Tipos, Pastas, All, Lixeira.
            //============================
            View linearLayout = findViewById(R.id.mainLinearLayout);
            EditText valueET = new EditText(this);
            valueET.setText(tittle);
            valueET.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((LinearLayout) linearLayout).addView(valueET);
            if (tittle.equals("Tipos")) {
                for (String type : TYPE) {
                    //============================
                    //Adição dos tipos na tela:
                    //Cartão e Credencial.
                    //============================
                    TextView valueTV = new TextView(this);
                    valueTV.setText(type);
                    valueTV.setId(0);
                    valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) linearLayout).addView(valueTV);
                }
            } else if (tittle.equals("Pastas")) {
                for (int i = 0; i < user.getFolder().size(); i++) {
                    //============================
                    //Adição das pastas na tela:
                    //Pastas do usuário.
                    //===========================
                    TextView valueTV = new TextView(this);
                    valueTV.setText(user.getFolder().get(i).getFolderName());
                    valueTV.setId(user.getFolder().get(i).getId());
                    valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) linearLayout).addView(valueTV);
                }
            }else if (tittle.equals("Nenhuma Pasta")) {
                //============================
                //Adição dos itens na tela:
                //itens sem pasta.
                //============================

                for(Credential credential : user.getFolder().get(0).getCredential()){
                    if(credential.getActive()){
                        TextView valueTV = new TextView(this);
                        valueTV.setText(credential.getCredentialName());
                        valueTV.setId(credential.getId());
                        valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueTV);
                    }
                }
                for(CreditCard creditCard : user.getFolder().get(0).getCreditCard()){
                    if(creditCard.getActive()){
                        TextView valueTV = new TextView(this);
                        valueTV.setText(creditCard.getSurname());
                        valueTV.setId(creditCard.getId());
                        valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueTV);
                    }
                }
            }else if(tittle.equals("Lixeira")){
                for(Credential credential : user.getFolder().get(0).getCredential()){
                    if(!credential.getActive()){
                        TextView valueTV = new TextView(this);
                        valueTV.setText(credential.getCredentialName());
                        valueTV.setId(credential.getId());
                        valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueTV);
                    }
                }
                for(CreditCard creditCard : user.getFolder().get(0).getCreditCard()){
                    if(!creditCard.getActive()){
                        TextView valueTV = new TextView(this);
                        valueTV.setText(creditCard.getSurname());
                        valueTV.setId(creditCard.getId());
                        valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueTV);
                    }
                }
            }

        }
    }

    protected void createObj(User user) {
        parameters.add(String.valueOf(user.getUserId()));
        parameters.add("Folder");
        Folder emptyFolder = new Folder(
                -1,
                "emptyFolder",
                user.getUserId(),
                user.getTeam(),
                "null"
        );
        user.setFolder(emptyFolder);
        parameters.clear();
        parameters.add(String.valueOf(user.getUserId()));
        parameters.add("Folder");
        MyUrlRequestCallback folderResponse = new MyUrlRequestCallback("GET", "getItem", parameters);
        try {
            JSONArray response = new JSONArray(folderResponse.execute().get());
            for (int i = 0; i < response.length() - 1; i++) {
                Folder folder = new Folder(
                        response.getJSONObject(i).getInt("_id"),
                        response.getJSONObject(i).getString("folderName"),
                        response.getJSONObject(i).getInt("idUser"),
                        response.getJSONObject(i).getInt("idTeam"),
                        response.getJSONObject(i).getString("registerDate")
                );
                user.setFolder(folder);
            }
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        parameters.clear();
        parameters.add(String.valueOf(user.getUserId()));
        parameters.add("CreditCard");
        MyUrlRequestCallback creditCardResponse = new MyUrlRequestCallback("GET", "getItem", parameters);
        try {
            JSONArray response = new JSONArray(creditCardResponse.execute().get());
            for (int i = 0; i < response.length() - 1; i++) {
                CreditCard creditCard = new CreditCard(
                        response.getJSONObject(i).getInt("_id"),
                        response.getJSONObject(i).getInt("idUser"),
                        response.getJSONObject(i).getString("surname"),
                        response.getJSONObject(i).getString("cardholderName"),
                        response.getJSONObject(i).getLong("cardNumber"),
                        response.getJSONObject(i).getString("expirationDate"),
                        response.getJSONObject(i).getString("cvcCode"),
                        response.getJSONObject(i).getString("company"),
                        response.getJSONObject(i).getInt("idFolder"),
                        response.getJSONObject(i).getString("note"),
                        response.getJSONObject(i).getString("type"),
                        response.getJSONObject(i).getBoolean("active")
                );
                if(creditCard.getIdFolder() == -1)
                    user.getFolder().get(0).setCreditCard(creditCard);
                else
                    for(Folder folder : user.getFolder()){
                        if(creditCard.getIdFolder() == folder.getId())
                            folder.setCreditCard(creditCard);
                    }
            }
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        parameters.clear();
        parameters.add(String.valueOf(user.getUserId()));
        parameters.add("Credential");
        MyUrlRequestCallback credentialResponse = new MyUrlRequestCallback("GET", "getItem", parameters);
        try {
            JSONArray response = new JSONArray(credentialResponse.execute().get());
            for (int i = 0; i < response.length() - 1; i++) {
                //todo aqui meso
                Credential credential = new Credential(
                        response.getJSONObject(i).getInt("_id"),
                        response.getJSONObject(i).getString("credentialName"),
                        response.getJSONObject(i).getString("username"),
                        response.getJSONObject(i).getString("password"),
                        response.getJSONObject(i).getInt("idFolder"),
                        response.getJSONObject(i).getString("note"),
                        response.getJSONObject(i).getInt("idUser"),
                        response.getJSONObject(i).getString("type"),
                        response.getJSONObject(i).getBoolean("active")
                );
                if(credential.getIdFolder() == -1)
                    user.getFolder().get(0).setCredential(credential);
                else
                    for(Folder folder : user.getFolder()){
                        if(credential.getIdFolder() == folder.getId())
                            folder.setCredential(credential);
                    }
            }
        } catch (JSONException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}