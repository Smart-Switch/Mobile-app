package com.example.alperen.akilliev;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

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



public class MainActivity extends AppCompatActivity {

    Button giris,kayit;
    EditText kuladig,sifreg;
    TextView sifreunut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        giris = (Button) findViewById(R.id.giris);
        kayit = (Button) findViewById(R.id.kayit);
        sifreunut = (TextView) findViewById(R.id.sifreunut);
        kuladig = (EditText) findViewById(R.id.kuladig);
        sifreg = (EditText) findViewById(R.id.sifreg);

        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, KayitOl.class));

            }
        });
        sifreunut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, SifreUnut.class));

            }
        });

        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> myMap = new HashMap<String, String>();
                myMap.put("username", kuladig.getText().toString());
                myMap.put("password", sifreg.getText().toString());
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(myMap);

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                Request request = new Request.Builder()
                        .addHeader("Accept", "application/json")
                        .url("http://188.166.40.162/auth")
                        .post(body)
                        .build();

                OkHttpClient okHttpClient = new OkHttpClient();

                okHttpClient.newCall(request).enqueue(new Callback() {
                                                          @Override
                                                          public void onFailure(Call call, IOException e) {
                                                          }

                                                          @Override
                                                          public void onResponse(Call call, Response response) throws IOException {

                                                              try {
                                                                  if (response.code() == 200) {
                                                                      JSONObject Jobject = new JSONObject(response.body().string());
                                                                      String accessToken = Jobject.getString("access_token");
                                                                          Intent wi = new Intent(getApplicationContext(), Wi.class);
                                                                          wi.putExtra("accessToken", accessToken.toString());
                                                                          startActivity(wi);

                                                                  } else if (response.code() == 401) {
                                                                      JSONObject Jobjectt = new JSONObject(response.body().string());
                                                                      final String error = Jobjectt.getString("error");
                                                                      final String description = Jobjectt.getString("description");
                                                                      MainActivity.this.runOnUiThread(new Runnable() {
                                                                          @Override
                                                                          public void run() {
                                                                              //Handle UI here
                                                                              Toast.makeText(getApplicationContext(), description +","+ error , Toast.LENGTH_LONG).show();
                                                                          }
                                                                      });
                                                                  }

                                                              } catch (JSONException e) {
                                                                  e.printStackTrace();
                                                              }


                                                          }


                                                      }
                );


            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
