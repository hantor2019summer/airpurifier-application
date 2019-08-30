package com.example.ap_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class MainActivity extends AppCompatActivity {
    private TextView fdDensity;
    List<String> density = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fdDensity = findViewById(R.id.FDdensity);
        OkHttpClient client = new OkHttpClient();
        String url = "http://bigpie1367.pythonanywhere.com/api/dusts/";

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();
                    JSONArray jsonarray = null;
                    try {
                        jsonarray = new JSONArray(myResponse);
                        JSONObject jsonobject = jsonarray.getJSONObject(0);
                        final String density = jsonobject.getString("density");
                        MainActivity.this.runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                fdDensity.setText(density);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        Switch sw = (Switch) findViewById(R.id.PowerSwitch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        requestPost("http://bigpie1367.pythonanywhere.com/api/switch/", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        requestPost("http://bigpie1367.pythonanywhere.com/api/switch/", false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void requestPost(String url, Boolean power) throws JSONException {
        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        JSONObject json = new JSONObject();
        json.put("state", power);

        RequestBody body =  RequestBody.create(JSON, String.valueOf(json));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                                    .url(url)
                                    .post(body)
                                    .build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response){
            }
        });
    }

}
