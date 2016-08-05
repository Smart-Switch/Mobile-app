package com.example.alperen.akilliev;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Blu extends ActionBarActivity {

    ToggleButton bluackapa;
    TextView bludurum,logoutt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blu);

        bluackapa = (ToggleButton)findViewById(R.id.bluackapa);
        bludurum = (TextView) findViewById(R.id.bludurum);
        logoutt = (TextView) findViewById(R.id.logoutt);

        logoutt.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Blu.this,MainActivity.class ));
                Toast.makeText(getApplicationContext(),"Başarıyla çıkış yapıldı",Toast.LENGTH_SHORT).show();
            }
        });


        final BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        bluackapa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(adapter==null){
                        bludurum.setText("Bluetooth aygıtı bulunamadı");
                    }else{
                        if(!adapter.isEnabled()) {
                            Intent bluetoothBaslat = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(bluetoothBaslat, 1);
                            Intent gorunurYap = new Intent (adapter.ACTION_REQUEST_DISCOVERABLE);
                            startActivityForResult(gorunurYap,1);
                            bludurum.setText("Bluetooth aygıtı açık");

                        }else {
                            adapter.disable();
                        }
                    }

                }else{
                    if (adapter.isEnabled()){}else{
                        adapter.disable();
                        bludurum.setText("Bluetooth kapandı.");
                    }
                }
            }
        });


    }
}
