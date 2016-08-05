package com.example.alperen.akilliev;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class KayitOl extends ActionBarActivity {

    EditText sifregiris,yenidensifregiris,mailgiris,kullaniciadigiris;
    Button kayittamamla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kayitol);

        sifregiris = (EditText)findViewById(R.id.sifregiris);
        yenidensifregiris = (EditText)findViewById(R.id.yenidensifregiris);
        mailgiris = (EditText)findViewById(R.id.mailgiris);
        kullaniciadigiris = (EditText)findViewById(R.id.kullaniciadigiris);
        kayittamamla = (Button)findViewById(R.id.kayittamamla);


        kayittamamla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> myMap = new HashMap<String, String>();
                myMap.put("username", kullaniciadigiris.getText().toString());
                myMap.put("password", sifregiris.getText().toString());
                myMap.put("email", mailgiris.getText().toString());
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(myMap);

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                Request request = new Request.Builder()
                        .addHeader("Accept", "application/json")
                        .url("http://188.166.40.162/register")
                        .post(body)
                        .build();

                OkHttpClient okHttpClient = new OkHttpClient();

                okHttpClient.newCall(request).enqueue(new Callback() {
                                                          @Override
                                                          public void onFailure(Call call, IOException e) {

                                                          }

                                                          @Override
                                                          public void onResponse(Call call, Response response) throws IOException {

                                                              if( 6<=kullaniciadigiris.length()&& 6<=sifregiris.length()){
                                                              try {
                                                                  if(response.code() == 201){
                                                                      JSONObject Jobject = new JSONObject(response.body().string());
                                                                      final String message = Jobject.getString("message");
                                                                      startActivity(new Intent(KayitOl.this,MainActivity.class));
                                                                      KayitOl.this.runOnUiThread(new Runnable() {
                                                                          @Override
                                                                          public void run() {
                                                                              //Handle UI here
                                                                              Toast.makeText(getApplicationContext(), message ,Toast.LENGTH_LONG).show();
                                                                          }
                                                                      });
                                                                  }else{
                                                                      JSONObject Jobject1 = new JSONObject(response.body().string());
                                                                      final String message1 = Jobject1.getString("message");
                                                                      startActivity(new Intent(KayitOl.this,KayitOl.class));
                                                                      KayitOl.this.runOnUiThread(new Runnable() {
                                                                          @Override
                                                                          public void run() {
                                                                              //Handle UI here
                                                                              Toast.makeText(getApplicationContext(), message1, Toast.LENGTH_LONG).show();
                                                                          }
                                                                      });
                                                                  }
                                                              } catch (JSONException e) {
                                                                  e.printStackTrace();
                                                              }}else{
                                                                  KayitOl.this.runOnUiThread(new Runnable() {
                                                                      @Override
                                                                      public void run() {
                                                                          //Handle UI here
                                                                          Toast.makeText(getApplicationContext(),"Boşluklar doldurulmadı, kullanıcı adı ve şifre en az 6 hane olmalı." ,Toast.LENGTH_LONG).show();
                                                                          startActivity(new Intent(KayitOl.this,KayitOl.class));
                                                                      }
                                                                  });
                                                              }


                                                      }
                                                      }
                );


            }
        });
    }
}

