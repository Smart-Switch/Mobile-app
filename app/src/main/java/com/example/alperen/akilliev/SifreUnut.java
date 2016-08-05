package com.example.alperen.akilliev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SifreUnut extends ActionBarActivity {

    EditText kuladigg,mailgg;
    Button onayla;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sifreunut);

        mailgg = (EditText) findViewById(R.id.mailgg);
        onayla = (Button) findViewById(R.id.onayla);

        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> myMap = new HashMap<String, String>();
                myMap.put("email", mailgg.getText().toString());
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(myMap);

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                Request request = new Request.Builder()
                        .addHeader("Accept", "application/json")
                        .url("http://188.166.40.162/passreset")
                        .post(body)
                        .build();

                OkHttpClient okHttpClient = new OkHttpClient();

                okHttpClient.newCall(request).enqueue(new Callback() {
                                                          @Override
                                                          public void onFailure(Call call, IOException e) {

                                                          }

                                                          @Override
                                                          public void onResponse(Call call, Response response) throws IOException {

                                                              if(response.code() == 200){
                                                                  Toast.makeText(SifreUnut.this,"Maile gönderildi.",Toast.LENGTH_LONG).show();
                                                                  startActivity(new Intent(SifreUnut.this,MainActivity.class));
                                                              }else{
                                                                  Toast.makeText(SifreUnut.this,"Mail bulunamadı.",Toast.LENGTH_LONG).show();

                                                              }


                                                          }}
                );


            }
        });


    }
}
