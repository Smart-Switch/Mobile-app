package com.example.alperen.akilliev;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Wi extends ActionBarActivity {

    TextView logout,kulbilgileri;
    ListView listView;
    Button listele;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wi);

        Bundle extras = getIntent().getExtras();
        final String accessToken = extras.getString("accessToken");

        logout = (TextView) findViewById(R.id.logout);
        listView = (ListView) findViewById(R.id.listView);
        listele = (Button) findViewById(R.id.listele);

        logout.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Çıkış başarıyla yapıldı", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        listele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> myMap = new HashMap<String, String>();
                Gson gson = new GsonBuilder().create();
                String json = gson.toJson(myMap);

                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                Request request = new Request.Builder()
                        .addHeader("Authorization", "JWT " + accessToken)
                        .url("http://188.166.40.162/dev")
                        .get()
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
                                                                      final List<Integer> darray = new ArrayList<>();
                                                                      final List<Integer> darray2 = new ArrayList<>();
                                                                      final JSONObject jsonObject = new JSONObject(response.body().string());
                                                                      final JSONArray jsonArray = jsonObject.getJSONArray("data");
                                                                      for (int i = 0; i < jsonArray.length(); i++) {
                                                                          JSONObject json = jsonArray.getJSONObject(i);
                                                                          final String name = json.getString("name");
                                                                          final String created_at = json.getString("created_at");
                                                                          final int status = json.getInt("status");
                                                                          final String updated_at = json.getString("updated_at");
                                                                          final int kod = json.getInt("id");
                                                                          final String message = jsonObject.getString("message");
                                                                          darray.add(kod);
                                                                          darray2.add(status);

                                                                          Wi.this.runOnUiThread(new Runnable() {
                                                                              @Override
                                                                              public void run() {
                                                                                  //Handle UI here
                                                                                  if(adapter.getCount() == 0){
                                                                                  adapter.add("Name              :" + name + "\n" + "Updated_at   :" + created_at);}


                                                                                  listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                                      @Override
                                                                                      public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {

                                                                                          String durum = null;
                                                                                          if (darray2.get(position) == 0) {
                                                                                              durum = "Kapalı";
                                                                                          } else if (darray2.get(position) == 1) {
                                                                                              durum = "Açık";
                                                                                          }

                                                                                          AlertDialog.Builder diyalogOlusturucu =
                                                                                                  new AlertDialog.Builder(Wi.this);

                                                                                          diyalogOlusturucu.setMessage("Cihaz Durumu :" + durum)
                                                                                                  .setCancelable(false)
                                                                                                  .setNeutralButton("Sil", new DialogInterface.OnClickListener() {
                                                                                                      @Override
                                                                                                      public void onClick(DialogInterface dialog, int which) {

                                                                                                          Map<String, String> myMap = new HashMap<String, String>();
                                                                                                          Gson gson = new GsonBuilder().create();
                                                                                                          String json = gson.toJson(myMap);

                                                                                                          RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                                                                                                          Request request = new Request.Builder()
                                                                                                                  .addHeader("Authorization", "JWT " + accessToken)
                                                                                                                  .url("http://188.166.40.162/dev/" + darray.get(position))
                                                                                                                  .delete()
                                                                                                                  .build();

                                                                                                          OkHttpClient okHttpClient = new OkHttpClient();

                                                                                                          okHttpClient.newCall(request).enqueue(new Callback() {
                                                                                                              @Override
                                                                                                              public void onFailure(Call call, IOException e) {

                                                                                                              }

                                                                                                              @Override
                                                                                                              public void onResponse(Call call, Response response) throws IOException {
                                                                                                              }
                                                                                                          });

                                                                                                      }
                                                                                                  })
                                                                                                  .setPositiveButton("Aç", new DialogInterface.OnClickListener() {
                                                                                                      @Override
                                                                                                      public void onClick(DialogInterface dialog, int which) {
                                                                                                          Map<String, String> myMap = new HashMap<String, String>();
                                                                                                          myMap.put("status", "1");
                                                                                                          Gson gson = new GsonBuilder().create();
                                                                                                          String json = gson.toJson(myMap);

                                                                                                          RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                                                                                                          Request request = new Request.Builder()
                                                                                                                  .addHeader("Authorization", "JWT " + accessToken)
                                                                                                                  .url("http://188.166.40.162/dev/" + darray.get(position))
                                                                                                                  .put(body)
                                                                                                                  .build();

                                                                                                          OkHttpClient okHttpClient = new OkHttpClient();
                                                                                                          okHttpClient.newCall(request).enqueue(new Callback() {
                                                                                                              @Override
                                                                                                              public void onFailure(Call call, IOException e) {

                                                                                                              }

                                                                                                              @Override
                                                                                                              public void onResponse(Call call, Response response) throws IOException {
                                                                                                                  Map<String, String> myMap = new HashMap<String, String>();
                                                                                                                  Gson gson = new GsonBuilder().create();
                                                                                                                  String json = gson.toJson(myMap);

                                                                                                                  RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                                                                                                                  Request request = new Request.Builder()
                                                                                                                          .addHeader("Authorization", "JWT " + accessToken)
                                                                                                                          .url("http://188.166.40.162/dev")
                                                                                                                          .get()
                                                                                                                          .build();

                                                                                                                  OkHttpClient okHttpClient = new OkHttpClient();
                                                                                                                  okHttpClient.newCall(request).enqueue(new Callback() {
                                                                                                                      @Override
                                                                                                                      public void onFailure(Call call, IOException e) {

                                                                                                                      }

                                                                                                                      @Override
                                                                                                                      public void onResponse(Call call, Response response) throws IOException {

                                                                                                                      }
                                                                                                                  });


                                                                                                              }
                                                                                                          });

                                                                                                      }
                                                                                                  })
                                                                                                  .setNegativeButton("Kapat", new DialogInterface.OnClickListener() {
                                                                                                      @Override
                                                                                                      public void onClick(DialogInterface dialog, int which) {
                                                                                                          Map<String, String> myMap = new HashMap<String, String>();
                                                                                                          myMap.put("status", "0");
                                                                                                          Gson gson = new GsonBuilder().create();
                                                                                                          String json = gson.toJson(myMap);
                                                                                                          RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                                                                                                          Request request = new Request.Builder()
                                                                                                                  .addHeader("Authorization", "JWT " + accessToken)
                                                                                                                  .url("http://188.166.40.162/dev/" + darray.get(position))
                                                                                                                  .put(body)
                                                                                                                  .build();

                                                                                                          OkHttpClient okHttpClient = new OkHttpClient();

                                                                                                          okHttpClient.newCall(request).enqueue(new Callback() {
                                                                                                              @Override
                                                                                                              public void onFailure(Call call, IOException e) {

                                                                                                              }

                                                                                                              @Override
                                                                                                              public void onResponse(Call call, Response response) throws IOException {


                                                                                                              }
                                                                                                          });

                                                                                                      }
                                                                                                  });
                                                                                          diyalogOlusturucu.create().show();
                                                                                      }
                                                                                  });
                                                                              }
                                                                          });
                                                                      }
                                                                  } else {
                                                                      Wi.this.runOnUiThread(new Runnable() {
                                                                          @Override
                                                                          public void run() {
                                                                              //Handle UI here
                                                                              Toast.makeText(getApplicationContext(), "Bad Request ", Toast.LENGTH_LONG).show();
                                                                          }
                                                                      });
                                                                  }


                                                              } catch (JSONException e) {
                                                                  e.printStackTrace();
                                                              }


                                                          }
                                                      }
                );

                Toast.makeText(getApplicationContext(),"Cihaz Sayısı: "+adapter.getCount(),Toast.LENGTH_LONG).show();

            }
        });


    }
}
