package com.example.cofredesenhas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    protected static final String TAG = "MainActivity";
    protected String response = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG,"Iniciada!");

        MyUrlRequestCallback apiResponse = new MyUrlRequestCallback("GET", "test", null);
        try {
            response = apiResponse.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d(TAG,"response" + response);
    }
}