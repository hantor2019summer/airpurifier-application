package com.example.ap_test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private TextView ufdDensity;
    private ImageView rotate;
    private LinearLayout background;
    private ImageView emotion;
    private TextView state;
    private double FD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fdDensity = findViewById(R.id.FDdensity);
        ufdDensity = findViewById(R.id.UFDdensity);
        rotate = findViewById(R.id.RotateButton);
        background = findViewById(R.id.VerticalLayout);
        emotion = findViewById(R.id.CurrentStatusImage);
        state = findViewById(R.id.CurrentStatusText);
        final OkHttpClient client = new OkHttpClient();
        String url = "http://bigpie1367.pythonanywhere.com/api/dusts/";

        final Request request = new Request.Builder()
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
                        final String fine_density = jsonobject.getString("fine_density");
                        final String ultra_fine_density = jsonobject.getString("ultra_fine_density");
                        MainActivity.this.runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                fdDensity.setText(fine_density);
                                ufdDensity.setText(ultra_fine_density);
                                FD = Double.parseDouble(fine_density);
                                if (0 <= FD && FD <= 30) {
                                    background.setBackgroundColor(0xFF2196F3);
                                    emotion.setImageResource(R.drawable.good);
                                    state.setText("현재 상태 : 좋음");
                                }
                                else if (30 < FD && FD <= 80) {
                                    background.setBackgroundColor(0xFF4CAF50);
                                    emotion.setImageResource(R.drawable.normal);
                                    state.setText("현재 상태 : 보통");
                                }
                                else if (80 < FD && FD <= 150) {
                                    background.setBackgroundColor(0xFFFFC107);
                                    emotion.setImageResource(R.drawable.bad);
                                    state.setText("현재 상태 : 나쁨");
                                }
                                else if (FD > 150) {
                                    background.setBackgroundColor(0xFFF44336);
                                    emotion.setImageResource(R.drawable.very_bad);
                                    state.setText("현재 상태 : 매우 나쁨");
                                }
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

        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
                rotate.startAnimation(anim);
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
                                final String fine_density = jsonobject.getString("fine_density");
                                final String ultra_fine_density = jsonobject.getString("ultra_fine_density");
                                MainActivity.this.runOnUiThread(new Runnable(){
                                    @Override
                                    public void run() {
                                        fdDensity.setText(fine_density);
                                        ufdDensity.setText(ultra_fine_density);
                                        FD = Double.parseDouble(fine_density);
                                        if (0 <= FD && FD <= 30) {
                                            background.setBackgroundColor(0xFF2196F3);
                                            emotion.setImageResource(R.drawable.good);
                                            state.setText("현재 상태 : 좋음");
                                        }
                                        else if (30 < FD && FD <= 80) {
                                            background.setBackgroundColor(0xFF4CAF50);
                                            emotion.setImageResource(R.drawable.normal);
                                            state.setText("현재 상태 : 보통");
                                        }
                                        else if (80 < FD && FD <= 150) {
                                            background.setBackgroundColor(0xFFFFC107);
                                            emotion.setImageResource(R.drawable.bad);
                                            state.setText("현재 상태 : 나쁨");
                                        }
                                        else if (FD > 150) {
                                            background.setBackgroundColor(0xFFF44336);
                                            emotion.setImageResource(R.drawable.very_bad);
                                            state.setText("현재 상태 : 매우 나쁨");
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

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
