package com.example.cofredesenhas;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import org.intellij.lang.annotations.JdkConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MainActivity";
    protected static final String[] TITTLE = {"Pastas", "Nenhuma Pasta", "Lixeira"};
    protected static final String[] TYPE = {"Cartão", "Credencial"};
    protected ArrayList<String> parameters = new ArrayList<>();
    protected JSONArray response;
    protected User user;
    Button btnNewFolder,btnNewCreditCard,btnNewCredential,btnExportJson;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Iniciada!");
        user = (User) getIntent().getSerializableExtra("User");
        createObj(user);
        initializeActivity(user);
        btnNewFolder=findViewById(R.id.btnNewFolder);
        btnNewCreditCard=findViewById(R.id.btnNewCreditCard);
        btnNewCredential=findViewById(R.id.btnNewCredential);
        btnExportJson=findViewById(R.id.btnExportJson);

        btnNewFolder.setOnClickListener(v -> {
            Intent secondView = new Intent(getApplicationContext(), NewFolderActivity.class);
            secondView.putExtra("userId", user.getUserId());
            secondView.putExtra("su", user.getSu());
            secondView.putExtra("name", user.getName());
            secondView.putExtra("email", user.getEmail());
            secondView.putExtra("team", user.getTeam());
            secondView.putExtra("registerDate", user.getRegisterDate());
            //secondView.putExtra("user", user); n funfa ;(
            startActivity(secondView);
        });
        btnNewCreditCard.setOnClickListener(v -> {
            Intent secondView = new Intent(getApplicationContext(), NewCreditCard.class);
            secondView.putExtra("userId", user.getUserId());
            secondView.putExtra("su", user.getSu());
            secondView.putExtra("name", user.getName());
            secondView.putExtra("email", user.getEmail());
            secondView.putExtra("team", user.getTeam());
            secondView.putExtra("registerDate", user.getRegisterDate());
            //secondView.putExtra("user", user); n funfa ;(
            startActivity(secondView);
        });
        btnNewCredential.setOnClickListener(v -> {
            Intent secondView = new Intent(getApplicationContext(), NewCredential.class);
            secondView.putExtra("userId", user.getUserId());
            secondView.putExtra("su", user.getSu());
            secondView.putExtra("name", user.getName());
            secondView.putExtra("email", user.getEmail());
            secondView.putExtra("team", user.getTeam());
            secondView.putExtra("registerDate", user.getRegisterDate());
            //secondView.putExtra("user", user); n funfa ;(
            startActivity(secondView);
        });
    }
    protected void initializeActivity(User user){
        for (String tittle : TITTLE) {
            //============================
            //Adição dos titulas na tela:
            //Tipos, Pastas, All, Lixeira.
            //============================
            View linearLayout = findViewById(R.id.mainLinearLayout);
            TextView valueET = new TextView(this);
            valueET.setText("\n"+tittle);
            valueET.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f);
            valueET.setTextColor(getResources().getColor(R.color.black));
            valueET.setPaintFlags(valueET.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            valueET.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            ((LinearLayout) linearLayout).addView(valueET);
            /*if (tittle.equals("Tipos")) {
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
            } else*/ if (tittle.equals("Pastas")) {
                for (int i = 1; i < user.getFolder().size(); i++) {
                    //============================
                    //Adição das pastas na tela:
                    //Pastas do usuário.
                    //===========================
                    TextView valueTV = new TextView(this);
                    valueTV.setText("\n"+user.getFolder().get(i).getFolderName());
                    valueTV.setId(user.getFolder().get(i).getId());
                    valueTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                    valueTV.setTextColor(getResources().getColor(R.color.black));
                    valueTV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                    ((LinearLayout) linearLayout).addView(valueTV);
                    for(Credential credential : user.getFolder().get(i).getCredential()){
                        if(credential.getActive() &&  credential.getIdFolder() ==  user.getFolder().get(i).getId()){
                            Button valueV = new Button(this);
                            valueV.setText("       "+credential.getCredentialName());
                            valueV.setId(credential.getId());
                            valueV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            ((LinearLayout) linearLayout).addView(valueV);
                            valueV.setOnClickListener(v ->{
                                parameters.clear();
                                parameters.add("Credential");
                                parameters.add(credential.getCredentialName());
                                parameters.add(credential.getUsername());
                                parameters.add(credential.getPassword());
                                parameters.add(credential.getNote());
                                parameters.add(String.valueOf(credential.getId()));
                                parameters.add(String.valueOf(credential.getActive()));
                                parameters.add(String.valueOf(credential.getIdFolder()));
                                showDialog(parameters, "delete");
                            });
                        }
                    }
                    for(CreditCard creditCard : user.getFolder().get(i).getCreditCard()) {
                        if (creditCard.getActive() && creditCard.getIdFolder() == user.getFolder().get(i).getId()) {
                            Button valueV = new Button(this);
                            valueV.setText("       "+creditCard.getSurname());
                            valueV.setId(creditCard.getId());
                            valueV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                            ((LinearLayout) linearLayout).addView(valueV);
                            valueV.setOnClickListener(v ->{
                                parameters.clear();
                                parameters.add("CreditCard");
                                parameters.add(creditCard.getSurname());
                                parameters.add(creditCard.getCardholerName());
                                parameters.add(String.valueOf(creditCard.getCardNumber()));
                                parameters.add(creditCard.getExpirationDate());
                                parameters.add(creditCard.getCvcCode());
                                parameters.add(String.valueOf(creditCard.getId()));
                                parameters.add(String.valueOf(creditCard.getActive()));
                                parameters.add(String.valueOf(creditCard.getIdFolder()));
                                showDialog(parameters, "delete");
                            });
                        }
                    }
                }
            }else if (tittle.equals("Nenhuma Pasta")) {
                //============================
                //Adição dos itens na tela:
                //itens sem pasta.
                //============================

                for(Credential credential : user.getFolder().get(0).getCredential()){
                    if(credential.getActive()){
                        Button valueV = new Button(this);
                        valueV.setText("       "+credential.getCredentialName());
                        valueV.setId(credential.getId());
                        valueV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueV);
                        valueV.setOnClickListener(v ->{
                            parameters.clear();
                            parameters.add("Credential");
                            parameters.add(credential.getCredentialName());
                            parameters.add(credential.getUsername());
                            parameters.add(credential.getPassword());
                            parameters.add(credential.getNote());
                            parameters.add(String.valueOf(credential.getId()));
                            parameters.add(String.valueOf(credential.getActive()));
                            parameters.add(String.valueOf(credential.getIdFolder()));
                            showDialog(parameters, "delete");
                        });
                    }
                }
                for(CreditCard creditCard : user.getFolder().get(0).getCreditCard()){
                    if(creditCard.getActive()){
                        Button valueV = new Button(this);
                        valueV.setText(creditCard.getSurname());
                        valueV.setId(creditCard.getId());
                        valueV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueV);
                        valueV.setOnClickListener(v ->{
                            parameters.clear();
                            parameters.add("CreditCard");
                            parameters.add(creditCard.getSurname());
                            parameters.add(creditCard.getCardholerName());
                            parameters.add(String.valueOf(creditCard.getCardNumber()));
                            parameters.add(creditCard.getExpirationDate());
                            parameters.add(creditCard.getCvcCode());
                            parameters.add(String.valueOf(creditCard.getId()));
                            parameters.add(String.valueOf(creditCard.getActive()));
                            parameters.add(String.valueOf(creditCard.getIdFolder()));
                            showDialog(parameters, "delete");
                        });
                    }
                }
            }else if(tittle.equals("Lixeira")){
                for(Credential credential : user.getFolder().get(0).getCredential()){
                    if(!credential.getActive()){
                        Button valueV = new Button(this);
                        valueV.setText("       "+credential.getCredentialName());
                        valueV.setId(credential.getId());
                        valueV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueV);
                        valueV.setOnClickListener(v ->{
                            parameters.clear();
                            parameters.add("Credential");
                            parameters.add(credential.getCredentialName());
                            parameters.add(credential.getUsername());
                            parameters.add(credential.getPassword());
                            parameters.add(credential.getNote());
                            parameters.add(String.valueOf(credential.getId()));
                            parameters.add(String.valueOf(credential.getActive()));
                            parameters.add(String.valueOf(credential.getIdFolder()));
                            showDialog(parameters, "restore");
                        });
                    }
                }
                for(CreditCard creditCard : user.getFolder().get(0).getCreditCard()){
                    if(!creditCard.getActive()){
                        Button valueV = new Button(this);
                        valueV.setText(creditCard.getSurname());
                        valueV.setId(creditCard.getId());
                        valueV.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                        ((LinearLayout) linearLayout).addView(valueV);
                        valueV.setOnClickListener(v ->{
                            parameters.clear();
                            parameters.add("CreditCard");
                            parameters.add(creditCard.getSurname());
                            parameters.add(creditCard.getCardholerName());
                            parameters.add(String.valueOf(creditCard.getCardNumber()));
                            parameters.add(creditCard.getExpirationDate());
                            parameters.add(creditCard.getCvcCode());
                            parameters.add(String.valueOf(creditCard.getId()));
                            parameters.add(String.valueOf(creditCard.getActive()));
                            parameters.add(String.valueOf(creditCard.getIdFolder()));
                            showDialog(parameters, "restore");
                        });
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
                        response.getJSONObject(i).getInt("idFolder"),
                        response.getJSONObject(i).getString("type"),
                        response.getJSONObject(i).getBoolean("active")
                );
                if(creditCard.getIdFolder() == -1 || !creditCard.getActive())
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
                Log.d(TAG, response.getJSONObject(i).getString("credentialName"));
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
                if(credential.getIdFolder() == -1 || !credential.getActive())
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
    protected void showDialog(ArrayList parameters, String method) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayList item = new ArrayList<>();
        item.add(String.valueOf(parameters.get(0)));
        if(parameters.get(0).equals("Credential")){
            builder.setMessage(
                    "Nome: "+ parameters.get(1)+
                            "\nEmail:" + parameters.get(2)+
                            "\nSenha: "+ parameters.get(3)+
                            "\nObs: " +parameters.get(4)
            );
            item.add(String.valueOf(parameters.get(5)));
        }else if(parameters.get(0).equals("CreditCard")){
            builder.setMessage(
                    "Apelido: "+ parameters.get(1)+
                            "\nNome no cartão:" + parameters.get(2)+
                            "\nNumero do cartão: "+ parameters.get(3)+
                            "\nData de validade: " +parameters.get(4)+
                            "\nCVC: " +parameters.get(5)
            );
            item.add(String.valueOf(parameters.get(6)));
        }

        builder.setTitle("Detalhes");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton("Ok", (dialog, id) -> {
            // Selecionado OK
            dialog.cancel();
        });
        if(method.equals("delete")){
            builder.setNegativeButton("Deletar", (dialog, id) ->{
                MyUrlRequestCallback apiResponse = new MyUrlRequestCallback("POST", "removeItem", item);
                try{
                    JSONObject responseRequest = new JSONObject(apiResponse.execute().get());

                } catch (ExecutionException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.cancel();

            });
        }
        else{
            builder.setNegativeButton("Restaurar", (dialog, id) ->{
                MyUrlRequestCallback apiResponse = new MyUrlRequestCallback("POST", "restoreItem", item);
                try{
                    Log.d(TAG, String.valueOf(item));
                    JSONObject responseRequest = new JSONObject(apiResponse.execute().get());
                    Log.d(TAG,"response" + responseRequest);
                } catch (ExecutionException | JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
                dialog.cancel();

            });
        }


        AlertDialog dialog = builder.create();
        dialog.show();
    }
}