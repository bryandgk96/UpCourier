package com.example.examplebroadcastreceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    static MainActivity instance;
    public static final int MY_PERMISSION_REQUEST_LOCATION = 99;
    private LocationBroadcastReceiver locationBroadcastReceiver;
    TextView txtLocation;
    Button btnStart, btnStop;


    public static MainActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationBroadcastReceiver = new LocationBroadcastReceiver();


        instance = this;
        txtLocation = (TextView) findViewById(R.id.txtLocation);

        runTimePermissions();
//        btnStart = (Button) findViewById(R.id.btnStart);
//        btnStop = (Button) findViewById(R.id.btnStop);


//        if(!runTimePermissions())
//            enableButtons();


    }

//        private void enableButtons() {
//            btnStart.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent i = new Intent(getApplicationContext(), LocationBroadcastReceiver.class);
//                    startService(i);
//                }
//            });
//
//            btnStop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent j = new Intent(getApplicationContext(), LocationBroadcastReceiver.class);
//                    stopService(j);
//                }
//            });

//        }

        private boolean runTimePermissions() {
            /*if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ){
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
                },100);
                return true;
            }
            return false;*/
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, MY_PERMISSION_REQUEST_LOCATION );

            return true;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            /*if(requestCode==100) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    enableButtons();
                else
                    runTimePermissions();
            }*/

            switch (requestCode){
                case MY_PERMISSION_REQUEST_LOCATION:
                    if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            initGPS();
                        }
                    }else{
                        Toast.makeText(instance, "without permission", Toast.LENGTH_SHORT).show();
                    }

            }
        }

    private void initGPS() {
        Intent i = new Intent(this, LocationBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
                0,
                i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0 , 0, pendingIntent);
    }

    public void updateTextView(final String value){
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    txtLocation.setText(value);
                }
            });
        }

    @Override
    protected void onResume() {
        super.onResume();
        if(locationBroadcastReceiver !=null){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(LocationManager.KEY_LOCATION_CHANGED);
            registerReceiver(locationBroadcastReceiver,intentFilter);
        }else{
            Toast.makeText(instance, "locationBroadcastReceiver NULL", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(locationBroadcastReceiver);
    }
}
