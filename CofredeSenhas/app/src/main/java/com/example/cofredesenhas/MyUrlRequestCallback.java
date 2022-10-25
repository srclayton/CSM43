package com.example.cofredesenhas;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class MyUrlRequestCallback extends AsyncTask<Void, Void, String> {
    protected static final String TAG = "MyUrlRequestCallback";
    protected static final String URL = "http://10.0.2.2:5000/";
    protected String method;
    protected String route;
    protected String urlParameters;
    protected String response;
    protected ArrayList<String> parameters;

    public MyUrlRequestCallback(String method, String route, ArrayList<String>  parameters){
        this.method = method;
        this.route = route;
        this.parameters = parameters;
        this.urlParameters = null;
        this.response = null;
    }
    @Override
    protected String doInBackground(Void... voids){
        if(route.equals("password_Authentication"))
            urlParameters = "?email="+parameters.get(0) + "&password=" +parameters.get(1);
        else if(route.equals("registrationForm"))
            urlParameters = "?name="+parameters.get(0)+"&email="+parameters.get(1)+"&password="+parameters.get(2)+"&team="+parameters.get(3)+"&su="+parameters.get(4);
        if(parameters == null)
            urlParameters = "";
        urlParameters = route + urlParameters;
        try {
            URL url = new URL(URL  + urlParameters);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(this.method);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(30000);
            if(this.method.equals("GET")){
                StringBuilder responseApi = new StringBuilder();
                connection.connect();
                Scanner scanner = new Scanner(url.openStream());
                while(scanner.hasNext()){
                    responseApi.append(scanner.next());
                }
                response = responseApi.toString();
            }else{
                StringBuffer responseApi;
                // For POST only - START
                OutputStream os = connection.getOutputStream();
                os.write(parameters.get(0).getBytes());
                os.flush();
                os.close();
                // For POST only - END

                int responseCode = connection.getResponseCode();
                Log.d(TAG, "POST Response Code :: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    String inputLine;
                    responseApi = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        responseApi.append(inputLine);
                    }
                    in.close();
                    response = responseApi.toString();
                    // print result
                } else {
                    Log.d(TAG, "POST request not worked");
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }
        return response;
    }
}
